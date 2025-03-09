package com.payment.repo;

import com.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,String> {
    Optional<Payment> findByBookingNumberAndOrderIdAndReceipt(String bookingNumber, String orderId, String receipt);
}
