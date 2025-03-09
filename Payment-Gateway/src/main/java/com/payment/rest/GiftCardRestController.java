package com.payment.rest;

import com.payment.payload.GiftCardRequestDto;
import com.payment.payload.GiftCardResponseDto;
import com.payment.payload.PaymentRequestDto;
import com.payment.service.PaymentService;
import com.payment.service.impl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/gift-card")
public class GiftCardRestController {

    @Autowired
    private PaymentService paymentService;

    //@RequestBody GiftCardRequestDto giftCardRequestDto,
    @PostMapping
    public GiftCardResponseDto showGiftCardPaymentPage(@RequestBody GiftCardRequestDto giftCardRequestDto, Model model) {
        String orderResponse = paymentService.buyGiftCard(giftCardRequestDto);
        return GiftCardResponseDto.builder()
                .amount(giftCardRequestDto.getBalance())
                .bookingNumber(giftCardRequestDto.getGiftCode())
                .user_id(giftCardRequestDto.getUser_id())
                .orderResponse(orderResponse)
                .transaction_type("gift_card")
                .build();
    }

    @PostMapping("/verify")
    public Map<String,String> verifyPayment(@RequestBody Map<String, String> paymentData) {
        String res = paymentService.verifyPayment(paymentData);
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("success", "true");
        return hashMap;
    }
}
