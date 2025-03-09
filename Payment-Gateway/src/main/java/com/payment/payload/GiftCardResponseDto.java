package com.payment.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GiftCardResponseDto {

    private String orderResponse;
    private String bookingNumber;
    private Double amount;
    private String user_id;
    private String transaction_type;
}
