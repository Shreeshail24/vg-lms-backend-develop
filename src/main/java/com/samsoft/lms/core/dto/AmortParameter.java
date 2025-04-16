package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AmortParameter {

	private String dtDisbursement;
	private String dtInstallmentStart;
	private double loanAmount;
	private float interestRate;
	private int tenor;
	private String tenorUnit;
	private String emiBasis;
	private String bpiHandling; //Y= ADD_TO_FIRST,  N=COLLECT_SEPARATE
	private String interestBasis;
	private String repaymentFrequency;
	private String amortizationType;
	private String amortizationMethod;
	private String emiRounding; //ROUND, ROUND_UP, ROUND_DOWN 
	private float lastRowThresholdPercentage;
	private int noOfAdvanceEmi;
	private double emiAmount=0;
}
