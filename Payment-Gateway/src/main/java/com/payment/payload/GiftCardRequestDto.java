package com.payment.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GiftCardRequestDto {

    private Double balance;
    private String giftCardIssuerName;
    private String passcode;
    private String orderId;
    private String giftCode;
    private String receipt;
    private String user_id;
}
