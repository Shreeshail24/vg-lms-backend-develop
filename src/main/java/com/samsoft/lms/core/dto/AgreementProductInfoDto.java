package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementProductInfoDto {

	private Integer prodDtlId;
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
	private Float penalInterestRate = 0.0f;
	private String penalInterestBasis;
	private String penalAccountingBasis;
	private Integer graceDays;
	private Integer reschLockinPeriod;
	private Integer prepayAfterInstNo;
	private Integer prepayBeforeInstNo;
	private Integer minInstallmentGapBetPrepay;
	private Double minPrepayAmount = 0.0;
	private Integer forecloseAfterInstNo;
	private Integer forecloseBeforeInstaNo;
	private Integer minTenor;
	private String maxTenor;
	private Double minInstallmentAmount = 0.0d;
	private Double maxInstallmentAmount = 0.0d;
	private Float minInterestRate;
	private Float maxInterestRate;
	private Character dropLineODYN;
	private Float dropLinePerc;
	private String dropMode;
	private Double dropLineAmount = 0.0d;
	private String dropLIneFreq;
	private String mastAgrId;

}
