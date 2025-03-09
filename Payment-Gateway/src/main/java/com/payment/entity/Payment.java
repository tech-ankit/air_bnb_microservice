package com.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Long bookingNumber;
    private String orderId;
    private String paymentId;
    private String paymentStatus;
    private String receipt;
}
