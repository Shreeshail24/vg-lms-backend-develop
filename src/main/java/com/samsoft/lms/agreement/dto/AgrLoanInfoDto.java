package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrLoanInfoDto {

	private Double loanAmount = 0.0;
	private Integer totalTenor;
	private Integer balanceTenor;
	private String tenorUnit;
	private String dtDisburseDate;
	private String dtTenorStartDate;
	private String dtTenorEndDate;
	private Double unbilledPrincipal = 0.0;
	private Integer cycleDay;
	private Float interestRate = 0.0f;
	private String interestType;
	private Float indexRate = 0.0f;
	private Float offsetRate = 0.0f;
	private Float spreadRate = 0.0f;
}
