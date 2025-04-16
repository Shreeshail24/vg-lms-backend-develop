package com.samsoft.lms.agreement.dto;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBoardingOdDto {
	//private String mastAgrId;
	private String prodCode;
	private String prodType;
	private String interestBasis;
	private String emiBasis;
	private String bpiTreatmentFlag;
	private String interestType;
	private String interestAccrualFrequ;
	private String amortizationMethod;
	private String amortizationType;
	private String compoundingFreq;
	private String emiRounding;
	private Float lastRowEMIThreshold;
	private Float penalInterestRate;
	private String penalInterestBasis;
	private String penalAccountingBasis;
	private Integer graceDays;
	private Integer reschLockinPeriod;
	private Integer minTenor;
	private String maxTenor;
	private Double minInstallmentAmount;
	private Double maxInstallmentAmount;
	private Float minInterestRate;
	private Float maxInterestRate;
	private String dropLineODYN;
	private Float dropLinePerc;
	private String dropMode;
	private Double dropLineAmount;
	private Integer dropLineCycleDay;
	private String dropLIneFreq;
}
