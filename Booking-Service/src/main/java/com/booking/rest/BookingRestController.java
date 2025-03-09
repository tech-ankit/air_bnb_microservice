package com.booking.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.entity.Booking;
import com.booking.payload.BookingRequestDto;
import com.booking.service.BookingService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/booking")
@AllArgsConstructor
public class BookingRestController {
	
	private final BookingService bookingService;
	
	@PostMapping(value = "/book/{userId}")
	public ResponseEntity<String> createBooking(@RequestBody BookingRequestDto bookingDto,@PathVariable String userId) {
		Long bookingId =  bookingService.createBooking(bookingDto, userId);
		if(bookingId == null) {
			return new ResponseEntity<>("Room Not Available",HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>("Room is Booked With Booking Id :"+bookingId,HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/{userId}")
	public List<Booking> getAllBookingsForUser(@PathVariable String userId){
		return bookingService.getAllBookingsForUser(userId);
	}
	
	@GetMapping(value = "/property/{propertyId}")
	public List<Booking> getAllBookingForProperty(@PathVariable String propertyId){
		return bookingService.getAllBookingForProperty(propertyId);
	}

}
