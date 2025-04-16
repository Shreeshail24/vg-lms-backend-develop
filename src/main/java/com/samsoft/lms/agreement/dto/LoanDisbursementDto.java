package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDisbursementDto {

	//private String mastAgrId;
	private String loanId;
	private String customerId;
	private Integer cycleDay;
	private String repayFreq;
	private String assetClass;
	private Integer dpd = 0;
	private Integer tenor;
	private String tenorUnit;
	private Float interestRate = 0.0f;
	private String interestType;
	private String indexCode;
	private Float indexRate = 0.0f;
	private Float offsetRate = 0.0f;
	private Float spreadRate = 0.0f;
	private Integer penalCycleDay;
	private String dtTenorStartDate;
	private String dtTenorEndDate;
	private String finalDisbursement;
	private String preEmiyn;
	private String emiOnSanctionedAmount;
	private String loanType="LOAN";
	private Double loanAmount = 0.0;
	private String userId;
	private Integer noOfAdvEmi;
}
