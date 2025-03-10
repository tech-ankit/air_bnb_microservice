package com.booking.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageDto {

    private String number;
    private String guestName;
    private String propertyName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String mediaUrl;
}
