package com.booking.service.impl;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.booking.entity.Booking;
import com.booking.entity.InvoicePdf;
import com.booking.entity.Property;
import com.booking.entity.Room;
import com.booking.feign.NotificationFeign;
import com.booking.feign.PropertyFeign;
import com.booking.feign.UserFeign;
import com.booking.payload.BillingDetailsDto;
import com.booking.payload.BookingRequestDto;
import com.booking.payload.MessageDto;
import com.booking.payload.PdfDetailsDto;
import com.booking.payload.User;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingService;
import com.booking.service.InvoicePdfService;
import com.booking.util.PDFUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final PropertyFeign propertyFeign;
	private final UserFeign userFeign;
	private final RedissonClient redissonClient;
	private final InvoicePdfService invoicePdfService;
	private final PDFUtil pdfUtil;
	private final NotificationFeign notificationFeign;

	@Override
	public Long createBooking(BookingRequestDto bookingDto, String userId) {
		int MAX_RETRIES = 3;
		String lockId = "ROOM-LOCK" + bookingDto.getRoomId() + bookingDto.getCheckInDate()
				+ bookingDto.getCheckoutDate();
		log.info("Lock is Created For this Key: {}", lockId);
		RLock lock = redissonClient.getLock(lockId);
		int retries = 0;

		while (retries < MAX_RETRIES) {
			try {
				if (lock.tryLock(10, 10, TimeUnit.SECONDS)) {
					ResponseEntity<Room> responseRoom = propertyFeign.invokeGetRoomById(bookingDto.getRoomId());
					ResponseEntity<User> userDto = userFeign.invokeGetUserById(userId);
					log.info("Thread name: {}", Thread.currentThread().getName());
					if (responseRoom != null && userDto != null) {
						Room room = responseRoom.getBody();
						User user = userDto.getBody();
						user.setUserId(userId);
						if (room != null && user != null) {
							List<Booking> existingBookings = bookingRepository.findByRoomAndCheckInDateAndCheckoutDate(
									room.getId(), bookingDto.getCheckInDate(), bookingDto.getCheckoutDate());

							if (existingBookings == null || existingBookings.isEmpty()) {
								if (room.getNoOfRooms() >= bookingDto.getRoomCount()) {
									return bookRoom(bookingDto, room, user);
								} else {
									return null;
								}
							}

							Integer availableRooms = getAvailableRoomCount(existingBookings,bookingDto.getBookingId(), room.getNoOfRooms());
							if (availableRooms >= bookingDto.getRoomCount()) {
								return bookRoom(bookingDto, room, user);
							} else {
								return null;
							}
						} else {
							return null;
						}
					} else {
						return null; // Invalid response from Feign clients
					}
				} else {
					retries++;
					Thread.sleep(5000);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				retries++;
			} catch (Exception e) {
				return null;
			} finally {
				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
			}
		}
		return null;
	}

	@Transactional
	private Long bookRoom(BookingRequestDto bookingDto, Room room, User user) {
	    ExecutorService executorService = Executors.newFixedThreadPool(4);
	    Booking newBooking = new Booking();
	    
	    if(bookingDto.getBookingId() != null) {
	        newBooking.setBookingId(bookingDto.getBookingId());
	    }
	    newBooking.setCheckInDate(bookingDto.getCheckInDate());
	    newBooking.setCheckoutDate(bookingDto.getCheckoutDate());
	    newBooking.setRoomId(room.getId());
	    newBooking.setPropertyId(room.getProperty().getPropertyId());
	    newBooking.setUserId(user.getUserId());
	    newBooking.setTotalNights(calculateTotalNigth(bookingDto));
	    newBooking.setGuestCount(bookingDto.getGuestCount());
	    newBooking.setRoomCount(bookingDto.getRoomCount());
	    newBooking.setGuestName(user.getName());
	    BillingDetailsDto bill = generateBill(bookingDto, calculateTotalNigth(bookingDto), room);
	    newBooking.setTotalBill(bill.getTotalWithTax());
	    try {
	        Booking saveBooking = bookingRepository.save(newBooking);
	        
	        Long bookingId = saveBooking.getBookingId();
	        
	        PdfDetailsDto pdfDetailsDto = createPdfDetailsDto(saveBooking, room, bill);
	        String url = pdfUtil.pdfInvoiceGenerator(pdfDetailsDto);
	        
	        CompletableFuture.runAsync(()->{
	        	invoicePdfService.saveInvoicePdf(url, user.getUserId());
	        },executorService);
	        
	        CompletableFuture<MessageDto> messageDtoCreationFuture = CompletableFuture.supplyAsync(()->{
	        	log.info("GENRATE-DTO-MSG");
                MessageDto messageDto = new MessageDto();
                messageDto.setMediaUrl(url);
                messageDto.setCheckOutDate(bookingDto.getCheckoutDate());
                messageDto.setCheckInDate(bookingDto.getCheckInDate());
                messageDto.setPropertyName(room.getProperty().getPropertyName());
                messageDto.setGuestName(user.getName());
                messageDto.setNumber(user.getMobile());
                return messageDto;
            },executorService);
	        
	        messageDtoCreationFuture.join();
	        
	        CompletableFuture<Boolean> smsSendFuture = messageDtoCreationFuture.thenApplyAsync(dto->{
	        	log.info("SMS-MSG");
	        	return notificationFeign.invokeSendSms(dto);
	        },executorService);
	        
	        CompletableFuture<Boolean> whatsappSendFuture = messageDtoCreationFuture.thenApplyAsync(dto->{
	        	log.info("WHATSAPP-MSG");
	        	return notificationFeign.invokeSendWhatsappWithMedia(dto);
	        },executorService);
	        
	        Boolean isSmsSend = smsSendFuture.get();
	        Boolean isWhatsappSend = whatsappSendFuture.get();
	        if(isSmsSend || isWhatsappSend) {
	        	executorService.shutdownNow();
	        }else {
	        	executorService.isShutdown();
	        }
	        return bookingId;

	    } catch (Exception e) {
	        log.error("Error during booking process", e);
	        return null;
	    } finally {
	        executorService.shutdown();
	    }
	}

	

	private PdfDetailsDto createPdfDetailsDto(Booking booking,Room room, BillingDetailsDto billingDetails) {
		return PdfDetailsDto.builder()
				.bookingId(booking.getBookingId())
				.checkInDate(booking.getCheckInDate())
				.checkoutDate(booking.getCheckoutDate())
				.gst(room.getProperty().getTaxInPercentage())
				.guestName(booking.getGuestName())
				.propertyName(room.getProperty().getPropertyName())
				.propertyAddress(room.getProperty().getPropertyAddress())
				.nightCount(booking.getTotalNights())
				.totalAmount(booking.getTotalBill())
				.room(room)
				.roomId(room.getId())
				.userId(booking.getUserId())
				.guestCount(booking.getGuestCount())
				.roomCount(booking.getRoomCount())
				.build();
	}

	private BillingDetailsDto generateBill(BookingRequestDto bookingDto,Long nights,Room room) {
		BigDecimal roomCount = BigDecimal.valueOf(bookingDto.getRoomCount());
		BigDecimal price = BigDecimal.valueOf(room.getPricePerNight());
		BigDecimal totalNight = BigDecimal.valueOf(nights);
		BigDecimal tax = BigDecimal.valueOf(room.getProperty().getTaxInPercentage());
		BigDecimal totalAmt = price.multiply(roomCount).multiply(totalNight);
		BigDecimal taxAmt = totalAmt.multiply(tax).divide(BigDecimal.valueOf(100));
		BigDecimal totalBill = totalAmt.add(taxAmt);
		return BillingDetailsDto.builder()
				.totalAmt(totalAmt)
				.taxCharges(taxAmt)
				.totalWithTax(totalBill)
				.build();
	}

	private Integer getAvailableRoomCount(List<Booking> bookings,Long bookingId, Integer roomCapacity) {
		int totalBookedRoomCount = 0;
		if(bookingId == null) {
			totalBookedRoomCount = bookings.stream().mapToInt(Booking::getRoomCount).sum();
		}else {
			totalBookedRoomCount = bookings.stream().filter(b-> !b.getBookingId().equals(bookingId)).mapToInt(Booking::getRoomCount).sum();
		}
		return Math.abs(roomCapacity - totalBookedRoomCount);
	}

	private Long calculateTotalNigth(BookingRequestDto dto) {
		return ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckoutDate());
	}

	@Override
	public String cancelBooking(Long bookingId) {
		Optional<Booking> opBooking = bookingRepository.findById(bookingId);
		if(opBooking.isPresent()) {
			bookingRepository.delete(opBooking.get());
			opBooking = bookingRepository.findById(bookingId);
			if(opBooking.isEmpty()) {
				return "Booking Cancelled With Booking Id: "+bookingId;
			}
		}
		return "Booking cancellation not Proceed Please Try Again.";
	}

	@Override
	public String updateBooking(BookingRequestDto updatedBookingDto, Long bookingId,String userId) {
		updatedBookingDto.setBookingId(bookingId);
		Long savedBookingId = createBooking(updatedBookingDto,userId);
		if(savedBookingId != null) {
			return "Booking updated with Booking Id : "+savedBookingId;
		}else {
			return "Booking Updation Not Proceed";
		}
	}

	@Override
	public List<Booking> getAllBookingsForUser(String userId) {
		ResponseEntity<User> byId = userFeign.invokeGetUserById(userId);
		if (byId != null) {
			User user = byId.getBody();
			List<Booking> bookings =  bookingRepository.findByUserId(userId);
			return bookings;
		}
		return null;
	}

	@Override
	public List<Booking> getAllBookingForProperty(String propertyId) {
		Optional<Property> opProperty = propertyFeign.invokeGetPropertyById(propertyId);
		if (opProperty.isPresent()) {
			List<Booking> bookings = bookingRepository.findByProperty(opProperty.get().getPropertyId());
			return bookings;
		}
		return null;
	}

}
