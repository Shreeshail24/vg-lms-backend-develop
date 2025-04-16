package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementRepaymentRecordsDto {
	private Integer repaySchId;
	private String masterAgrId;
	private Integer installmentNo;
	private String dtInstallment;
	private Double openingPrincipal;
	private Double principalAmount;
	private Double interestAmount;
	private Double bpiAmount;
	private Double installmentAmount;
	private String loanId;
	private String dtPaymentDate;
	private int invoiceId;
	private String invoiceNo;
	private Double utilisedLimit;
	private Double unusedLimit;
}
