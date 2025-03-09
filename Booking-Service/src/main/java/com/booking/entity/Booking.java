package com.booking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	
	@Column(nullable = false , length = 200)
	private String guestName;
	
	@Column(nullable = false , length = 15)
	private LocalDate checkInDate;
	
	@Column(nullable = false , length = 15)
	private LocalDate checkoutDate;
	
	@Column(nullable = false, length = 100)
	private Long roomId;
	
	@Column(nullable = false , length = 300)
	private String propertyId;
	
	@Column(nullable = false, length = 20)
    private Integer guestCount;
	
	@Column(nullable = false , length = 20)
	private Integer roomCount;

	@Column(nullable = false , length = 20)
	private String bookingStatus;
	
	@Column(nullable = false)
	private Long totalNights;
	
	@Column(nullable = false, length = 400)
	private String userId;
	
	@Column(nullable = false , length = 50)
	private BigDecimal totalBill;

	private String paymentId;

	private String orderId;
	
	@CreationTimestamp
	@Column(updatable = false , length = 15)
	private LocalDateTime bookingDateTime;

	@Column(nullable = false)
	private LocalDateTime expiry;
	
	@UpdateTimestamp
	@Column(insertable = false , length = 15)
	private LocalDateTime updateDateTime;

}
