package com.booking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.booking.constant.BookingConstant;
import com.booking.payload.*;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.booking.entity.Booking;
import com.booking.entity.Property;
import com.booking.entity.Room;
import com.booking.feign.NotificationFeign;
import com.booking.feign.PropertyFeign;
import com.booking.feign.UserFeign;
import com.booking.repository.BookingRepository;
import com.booking.service.BookingService;
import com.booking.service.InvoicePdfService;
import com.booking.util.PDFUtil;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final PropertyFeign propertyFeign;
	private final UserFeign userFeign;
	private final RedissonClient redissonClient;
	private final InvoicePdfService invoicePdfService;
	private final PDFUtil pdfUtil;
	private final NotificationFeign notificationFeign;
	private ExecutorService executorService = Executors.newFixedThreadPool(10);

	@Override
	public BookingResponseDto createBooking(BookingRequestDto bookingDto, String userId) {
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
					if (responseRoom != null && userDto != null) {
						Room room = responseRoom.getBody();
						User user = userDto.getBody();
						user.setUserId(userId);
						if (room != null && user != null) {
							List<Booking> existingBookings = bookingRepository.getBookedRooms(
									room.getId(), bookingDto.getCheckInDate(), bookingDto.getCheckoutDate());

							if (existingBookings == null || existingBookings.isEmpty()) {
								if (room.getNoOfRooms() >= bookingDto.getRoomCount()) {
									return  bookRoom(bookingDto, room, user);
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
						return null;
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
	private BookingResponseDto bookRoom(BookingRequestDto bookingDto, Room room, User user) {
	    ExecutorService executorService = Executors.newFixedThreadPool(4);
	    Booking newBooking = new Booking();
		newBooking.setBookingStatus(BookingConstant.PENDING);
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
		newBooking.setExpiry(LocalDateTime.now().plusMinutes(10));
	    BillingDetailsDto bill = generateBill(bookingDto, calculateTotalNigth(bookingDto), room);
	    newBooking.setTotalBill(bill.getTotalWithTax());
	    try {
	        Booking saveBooking = bookingRepository.save(newBooking);
	        
	        Long bookingId = saveBooking.getBookingId();
	        return BookingResponseDto.builder()
					.bookingNumber(saveBooking.getBookingId())
					.propertyId(saveBooking.getPropertyId())
					.roomId(saveBooking.getRoomId())
					.totalAmount(saveBooking.getTotalBill())
					.userId(saveBooking.getUserId())
					.build();

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
		BookingResponseDto savedBookingId = createBooking(updatedBookingDto,userId);
		if(savedBookingId != null) {
			return "Booking updated with Booking Id : "+savedBookingId.getBookingNumber();
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

	@Override
	public String updateBookingStatus(BookingUpdateDto updateDto) {
		Optional<Booking> opBooking = bookingRepository.findById(updateDto.getBookingNumber());
		if (opBooking.isPresent()){
			Booking booking = opBooking.get();
			booking.setBookingStatus(updateDto.getBookingStatus());
			booking.setPaymentId(updateDto.getPaymentId());
			booking.setOrderId(updateDto.getOrderId());
			Booking bookingSaved = bookingRepository.save(booking);
			if (bookingSaved.getBookingStatus().equals(updateDto.getBookingStatus())){
				return "Successfully Updated";
			}
		}
		return null;
	}

	@Override
	public String deleteBookingPaymentFailed(BookingUpdateDto bookingUpdateDto) {
		Optional<Booking> opBooking = bookingRepository.findById(bookingUpdateDto.getBookingNumber());
		if (opBooking.isPresent() && "FAILED".equals(bookingUpdateDto.getBookingStatus())){
			Booking booking = opBooking.get();
			bookingRepository.delete(booking);
			opBooking = bookingRepository.findById(bookingUpdateDto.getBookingNumber());
			if (opBooking.isEmpty()){
				return "Booking Failed Status Is Updated";
			}
		}
		return "Booking Not Avaiable With Id:"+bookingUpdateDto.getBookingNumber();
	}

	@Scheduled(fixedRate = 30000)
	public void deletePendingBooking() {
		LocalDateTime now = LocalDateTime.now();
		log.info("Running scheduled task: Checking for expired pending bookings");
		List<Booking> bookingsList = bookingRepository.findExpiredPendingBookings(now);

		executorService.submit(()->{
			bookingsList.forEach(booking -> {
				bookingRepository.delete(booking);
				log.info("Deleted expired booking with ID: {}", booking.getBookingId());
			});
		});
	}

}
