package com.booking.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class InvoicePdf {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long invoiceId;
	
	@Column(nullable = false , length = 2000)
	private String userId;
	
	@Column(nullable = false , length = 6000, unique = true)
	private String invoicePdfUrl;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime pdfCreationDateAndTime;

}
