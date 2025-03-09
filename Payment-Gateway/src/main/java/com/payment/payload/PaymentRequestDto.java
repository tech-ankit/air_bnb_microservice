package com.payment.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    public Double amount;
    public String bookingNumber;
    public String userId;
}
