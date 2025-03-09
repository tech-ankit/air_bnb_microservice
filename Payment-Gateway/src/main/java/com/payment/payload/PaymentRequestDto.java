package com.payment.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    public Double amount;
    public Long bookingNumber;
    public String userId;
}
