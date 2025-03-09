package com.booking.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class BookingResponseDto {

    private Long bookingNumber;
    private String propertyId;
    private Long roomId;
    private String userId;
    private BigDecimal totalAmount;
}
