package com.booking.listner;

import com.booking.entity.Booking;
import com.booking.entity.Room;
import com.booking.feign.PropertyFeign;
import com.booking.feign.UserFeign;
import com.booking.payload.MessageDto;
import com.booking.payload.PdfDetailsDto;
import com.booking.payload.User;
import com.booking.repository.BookingRepository;
import com.booking.service.InvoicePdfService;
import com.booking.service.KafkaMessageProducer;
import com.booking.util.PDFUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class PdfListner {

    @Autowired
    private PDFUtil pdfUtil;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyFeign propertyFeign;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private UserFeign  userFeign;

    @Autowired
    private InvoicePdfService invoicePdfService;

    @KafkaListener(topics = "pdf-topic", containerFactory = "pdfFactory", groupId = "pdf-group")
    public void generatePdf(String bookingNumber){
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Optional<Booking> opBooking =  bookingRepository.findById(Long.valueOf(bookingNumber));
        if(opBooking.isPresent()){
            System.out.println(bookingNumber);
            Booking booking = opBooking.get();
            CompletableFuture<Room> roomFuture = CompletableFuture.supplyAsync(()->{
                ResponseEntity<Room> roomResponse = propertyFeign.invokeGetRoomById(booking.getRoomId());
                return roomResponse.getBody();
            },executorService);

            CompletableFuture<User> userFuture =  CompletableFuture.supplyAsync(()->{
                ResponseEntity<User> userResponse = userFeign.invokeGetUserById(booking.getUserId());
                return userResponse.getBody();
            },executorService);

            CompletableFuture.allOf(roomFuture,userFuture).join();
            Room room = null;
            User user = null;

            try {
                room = roomFuture.get();
                user = userFuture.get();
            }catch(InterruptedException e){
                log.info(e.getMessage());
            }catch (Exception e){
                log.info(e.getMessage());
            }finally{
                executorService.shutdown();
            }

            if(room != null && user != null){
                PdfDetailsDto dto = getPropertyDetailsDto(room, booking);
                String url = pdfUtil.pdfInvoiceGenerator(dto);
                invoicePdfService.saveInvoicePdf(url,booking.getUserId());
                MessageDto messageDto = getMessageDto(booking,url,user,room);
                kafkaMessageProducer.sendMessage(messageDto);
            }else {
                log.error("Notification not send..");
            }
        }
    }

    private MessageDto getMessageDto(Booking booking, String url, User user, Room room) {
        return  MessageDto.builder()
                .number(user.getMobile())
                .checkOutDate(booking.getCheckoutDate())
                .mediaUrl(url)
                .guestName(booking.getGuestName())
                .propertyName(room.getProperty().getPropertyName())
                .checkInDate(booking.getCheckInDate())
                .build();
    }

    private PdfDetailsDto getPropertyDetailsDto(Room room, Booking booking) {
        return PdfDetailsDto.builder()
                .bookingId(booking.getBookingId())
                .guestName(booking.getGuestName())
                .checkInDate(booking.getCheckInDate())
                .checkoutDate(booking.getCheckoutDate())
                .roomId(booking.getRoomId())
                .nightCount(booking.getTotalNights())
                .totalAmount(booking.getTotalBill())
                .guestCount(booking.getGuestCount())
                .userId(booking.getUserId())
                .roomCount(booking.getRoomCount())
                .room(room)
                .gst(room.getProperty().getTaxInPercentage())
                .propertyName(room.getProperty().getPropertyName())
                .propertyAddress(room.getProperty().getPropertyAddress())
                .build();
    }
}
