package com.payment.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiftCardPurchaseDto {

    private Double amount;
    private String userId;
    private String giftCode;
    private String passcode;
}
