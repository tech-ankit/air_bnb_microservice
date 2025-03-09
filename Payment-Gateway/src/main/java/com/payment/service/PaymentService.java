package com.payment.service;

import com.payment.payload.GiftCardRequestDto;
import com.payment.payload.PaymentRequestDto;

import java.util.Map;

public interface PaymentService {
    String bookingPayment(PaymentRequestDto paymentDto);
    String verifyPayment(Map<String, String> paymentData);
    String buyGiftCard(GiftCardRequestDto giftCardRequestDto);
}
