package com.samsoft.lms.newux.dto.response.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestIncomeResDto {

	private String mastAgrId;

	private String customerId;

	private String portfolioCode;

	private String productCode;

	private Double interestDueAmount;

	private Double interestPaidAmount;
	
	private Double accruedInterestAmount;
}
