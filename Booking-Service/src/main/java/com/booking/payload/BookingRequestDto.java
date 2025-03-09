package com.booking.payload;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingRequestDto {

	private Long bookingId;
	
	private String guestName;
	
	private String propertyName;
	
	private LocalDate checkInDate;
	
	private LocalDate checkoutDate;
	
	private Long roomId;
	
	private String userId;
	
	private Integer guestCount;
	
	private Integer roomCount;
}
