package com.booking.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingUpdateDto {

    private String bookingStatus;
    private String paymentId;
    private String orderId;
    private Long bookingNumber;
}
