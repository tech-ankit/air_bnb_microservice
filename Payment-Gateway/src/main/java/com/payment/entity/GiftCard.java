package com.payment.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.GET;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class GiftCard {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String giftCardId;

    private String orderId;

    private String giftCode;

    private String passcode;

    private Double balance;

    private String paymentId;

    private String receipt;

    private String giftCardIssuerName;

    private String giftCardStatus;

    private LocalDate expiry;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate issueDate;

}
