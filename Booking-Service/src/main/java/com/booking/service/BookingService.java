package com.booking.service;

import java.util.List;

import com.booking.entity.Booking;
import com.booking.payload.BookingRequestDto;
import com.booking.payload.User;

public interface BookingService {
	
	Long createBooking(BookingRequestDto bookingDto,String userId);
	String cancelBooking(Long bookingId);
	String updateBooking(BookingRequestDto updatedBookingDto,Long bookingId,String userId);
	List<Booking> getAllBookingsForUser(String userId);
	List<Booking> getAllBookingForProperty(String propertyId);
	
}
