package com.booking.payload;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.booking.entity.Room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PdfDetailsDto {
	
    private Long bookingId;
    
    private Integer gst;
	
	private String guestName;
	
	private String propertyName;
	
	private LocalDate checkInDate;
	
	private LocalDate checkoutDate;
	
	private String propertyAddress;
	
	private Long nightCount;
	
	private BigDecimal totalAmount;
	
	private Room room;
	
	private Long roomId;
	
	private String userId;
	
	private Integer guestCount;
	
	private Integer roomCount;

}
