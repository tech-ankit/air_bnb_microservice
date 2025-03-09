package com.booking.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.entity.InvoicePdf;
import com.booking.service.InvoicePdfService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/invoice")
@AllArgsConstructor
public class InvoicePdfRestController {
	
	private final InvoicePdfService invoicePdfService;
	
	@GetMapping
	public List<InvoicePdf> getAllInvoicePdfByUser(@RequestParam String userId){
		return invoicePdfService.getAllInvoicePdfsByUser(userId);
	}
	
	@DeleteMapping(value = "/{invoiceId}")
	public String deleteInvoice(@PathVariable Long invoiceId) {
		return invoicePdfService.deleteInvoicePdf(invoiceId);
	}

}
