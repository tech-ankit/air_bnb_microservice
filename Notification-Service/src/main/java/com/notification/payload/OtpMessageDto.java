package com.notification.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpMessageDto {
    private String number;
    private String message;
}
