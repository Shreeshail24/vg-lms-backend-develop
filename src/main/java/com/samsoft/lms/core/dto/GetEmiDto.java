package com.samsoft.lms.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetEmiDto {
	private Double amount;
	private Float interestRate;
	private Integer tenor;
	private String repaymentFrequency;
	private String interestBasis;
	private String amortizationMethod;
}
