package com.booking.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.booking.entity.InvoicePdf;
import com.booking.repository.InvoicePdfRepository;
import com.booking.service.AmazonService;
import com.booking.service.InvoicePdfService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoicePdfServiceImpl implements InvoicePdfService{
	
	private final InvoicePdfRepository invoicePdfRepository;
	private final AmazonService amazonService;
	
	@Override
	public InvoicePdf saveInvoicePdf(String url,String userId) {
		InvoicePdf pdf = new InvoicePdf();
		pdf.setInvoicePdfUrl(url);
		pdf.setUserId(userId);
		return invoicePdfRepository.save(pdf);
	}

	@Override
	public String deleteInvoicePdf(Long id) {
		Optional<InvoicePdf> byId = invoicePdfRepository.findById(id);
		if(byId.isPresent()) {
			String url = byId.get().getInvoicePdfUrl();
			String key = getKeyFromUrl(url);
			Boolean deletePdf = amazonService.deletePdf(key);
			if(deletePdf) {
				invoicePdfRepository.deleteById(id);
				Optional<InvoicePdf> opInvoice = invoicePdfRepository.findById(id);
				if(opInvoice.isEmpty()) {
					return "Pdf Delete With Id: "+id;
				}
			}
		}
		return "Delete Not Possible Due To Some Reasone";
	}

	@Override
	public List<InvoicePdf> getAllInvoicePdfsByUser(String userId) {
		List<InvoicePdf> pdfs =  invoicePdfRepository.findByUserId(userId);
		return pdfs;
	}
	
	private String getKeyFromUrl(String url) {
		int lastIndex = url.lastIndexOf("/");
		return url.substring(lastIndex+1);
	}

}
