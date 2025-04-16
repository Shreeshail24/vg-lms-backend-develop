package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OdAmortDto {
	
	private String dtLimitSanctioned;
	private String dtLimitExpired;
	private Double sanctionedAmount;
	private String dtDrawdown;
	private Double drawDownAmount;
	private Integer interestPaymentCycleDay;
	private Integer principalPaymentCycleDay;
	private Float interestRate;
	private String interestBasis;
	private String interestRepaymentFrequency;
	private String emiRounding;
	private Double bpiAmount;
	private String dropLineODYN;
	private Float dropLinePerc;
	private String dropLineMode;
	private Double dropLineAmount;
	private String dropLineFreq;
	private String dtInterestStart;
	private String dtPrinStart;

}
