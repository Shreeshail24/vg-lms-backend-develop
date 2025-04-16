package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AgreementAmortListDto {

	private Integer repaySchId;
	private String masterAgrId;
	private Integer installmentNo;
	private String dtInstallment;
	private Double openingPrincipal;
	private Double principalAmount;
	private Double interestAmount;
	private Double bpiAmount;
	private Double installmentAmount;
	private Double closingPrincipal;
	private Float interestRate;
	private String interestBasis;
	private String loanId;
	private String dtPaymentDate;
	private Double tdsAmount;
	
}
