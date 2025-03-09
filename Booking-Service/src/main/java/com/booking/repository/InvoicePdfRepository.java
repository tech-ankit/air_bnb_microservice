package com.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.entity.InvoicePdf;

public interface InvoicePdfRepository extends JpaRepository<InvoicePdf, Long> {

	List<InvoicePdf> findByUserId(String userId);

}
