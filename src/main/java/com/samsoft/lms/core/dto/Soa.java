package com.samsoft.lms.core.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Soa {

	private String mastAgrId;
	private String dtDisbDate;
	private String portfolioCode;
	private String productCode;
	private Float interestRate;
	private String interestType;
	private String indexCode;
	private Float indexRate;
	private Float offsetRate;
	private Float spreadRate;
	private String repayFreq;
	private Double loanAmount;
	private Double unbilledPrincipal;
	private Double limitSanctionAmount;
	private Double availableLimit;
	private Double utilizedLimit;
	private String dtLimitExpired;
	private String agreementStatus;
	private String loanAdditionalStatus;
	private String dropLineODYN;
	private Float dropLinePerc;
	private String dropMode;
	private Double dropLineAmount;
	private Double currentDues;
	private Double excessAmount;
	private Integer totalRows;
	List<SoaTransactionDetails> tranDtl;
}
