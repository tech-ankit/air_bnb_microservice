package com.payment.rest;

import com.payment.payload.PaymentRequestDto;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/v1/payment")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public String showPaymentPage(@RequestBody PaymentRequestDto paymentDto , Model model) {
        try {
            String orderResponse = paymentService.bookingPayment(paymentDto);
            model.addAttribute("orderResponse", orderResponse);
            model.addAttribute("bookingNumber",paymentDto.getBookingNumber());
            model.addAttribute("amount", paymentDto.getAmount());
            model.addAttribute("transaction_type", "online_payment");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create order: " + e.getMessage());
        }
        return "payment";
    }

    @PostMapping("/verify")
    @ResponseBody
    public String verifyPayment(@RequestBody Map<String, String> paymentData) {
        return paymentService.verifyPayment(paymentData);
    }


    @GetMapping("/success")
    public String paymentSuccess() {
        return "success"; // Redirect to success page
    }
}
