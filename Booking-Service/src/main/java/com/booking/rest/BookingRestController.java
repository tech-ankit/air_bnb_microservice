package com.booking.rest;

import java.util.List;

import com.booking.payload.BookingResponseDto;
import com.booking.payload.BookingUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingDto,@PathVariable String userId) {
		BookingResponseDto bookingResDto =  bookingService.createBooking(bookingDto, userId);
		if(bookingDto == null) {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>(bookingResDto,HttpStatus.OK);
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

	@PostMapping(value = "/update/status")
	public String updateBookingStatus(@RequestBody BookingUpdateDto updateDto){
		return bookingService.updateBookingStatus(updateDto);
	}

	@PostMapping(value = "/delete")
	public String deleteBookingPaymentFailed(@RequestBody BookingUpdateDto bookingUpdateDto){
		return bookingService.deleteBookingPaymentFailed(bookingUpdateDto);
	}

}
