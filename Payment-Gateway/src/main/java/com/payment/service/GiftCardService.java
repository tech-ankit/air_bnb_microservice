package com.payment.service;

import com.payment.entity.GiftCard;
import com.payment.entity.Transaction;
import com.payment.payload.GiftCardPurchaseDto;
import com.payment.payload.GiftCardRequestDto;

import java.util.List;

public interface GiftCardService {

    GiftCard buyGiftCard(GiftCardRequestDto requestDto);
    Boolean purchaseGiftCard(GiftCardPurchaseDto giftCardPurchaseDto);
    String updateGiftCard(String giftCode , String orderId , String receipt,String paymentId);
}
