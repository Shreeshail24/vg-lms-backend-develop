package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLimitBoardingDto {

	//private String mastAgrId;
	private Double limitSanctionAmount;
	private String dtLimitSanctioned;
	private String dtLimitExpired;
	private String purpose;
	private Double ltv;
}
