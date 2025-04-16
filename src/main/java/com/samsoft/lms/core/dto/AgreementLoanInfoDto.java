package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementLoanInfoDto {

	private String loanId;
	private Double loanAmount;
	private Integer totalTenor;
	private Integer balanceTenor;
	private String dtDisbursement;
	private String dtTenorStartDate;
	private String dtTenorEndDate;
	private Double unbilledPrincipal = 0.0;
	private Integer cycleDay;
	private Float interestRate = 0.0f;
	private String interestType;
	private String indexCode;
	private Float indexRate = 0.0f;
	private Float offsetRate = 0.0f;
	private Float spreadRate = 0.0f;
	private Double currentDroppedLimit = 0.0;


}
