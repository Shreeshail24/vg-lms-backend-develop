package com.samsoft.lms.core.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AmortVariationDto {

	private String dtDisbursement;
	private String dtInstallmentStart;
	private Double loanAmount;
	private Float interestRate;
	private Integer tenor;
	private String tenorUnit;
	private String emiBasis;
	private String bpiHandling;
	private String interestBasis;
	private String repaymentFreq;
	private String amortizationType;
	private String amortizationMethod;
	private String emiRounding;
	private Float lastRowThreshold;
	private Integer noOfAdvEmi;
	List<AmortVariationListDto> variationList;

}
