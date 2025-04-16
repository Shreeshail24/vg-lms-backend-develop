package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecallGetDto {

	private String mastAgrId;
	private String loanId;
	private String portfolioCode;
	private double outstandingAmount;
	private double bpiAmount;
	private double totalOutstanding;

}
