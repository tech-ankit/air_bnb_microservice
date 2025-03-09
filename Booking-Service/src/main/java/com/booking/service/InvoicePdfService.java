package com.booking.service;

import java.util.List;

import com.booking.entity.InvoicePdf;

public interface InvoicePdfService {
	
	InvoicePdf saveInvoicePdf(String url,String userId);
	String deleteInvoicePdf(Long id);
	List<InvoicePdf> getAllInvoicePdfsByUser(String userId);
}
