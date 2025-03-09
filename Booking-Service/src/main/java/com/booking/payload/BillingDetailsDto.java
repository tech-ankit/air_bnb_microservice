package com.booking.payload;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BillingDetailsDto {
	
	private BigDecimal totalAmt;
	private BigDecimal totalWithTax;
	private BigDecimal taxCharges;

}
