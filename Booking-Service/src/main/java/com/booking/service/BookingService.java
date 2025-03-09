package com.booking.service;

import java.util.List;

import com.booking.entity.Booking;
import com.booking.payload.BookingRequestDto;
import com.booking.payload.BookingResponseDto;
import com.booking.payload.BookingUpdateDto;
import org.springframework.scheduling.annotation.Scheduled;

public interface BookingService {
	
	BookingResponseDto createBooking(BookingRequestDto bookingDto, String userId);
	String cancelBooking(Long bookingId);
	String updateBooking(BookingRequestDto updatedBookingDto,Long bookingId,String userId);
	List<Booking> getAllBookingsForUser(String userId);
	List<Booking> getAllBookingForProperty(String propertyId);
	String updateBookingStatus(BookingUpdateDto updateDto);
	String deleteBookingPaymentFailed(BookingUpdateDto bookingUpdateDto);
}
