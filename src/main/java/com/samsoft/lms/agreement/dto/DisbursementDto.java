package com.samsoft.lms.agreement.dto;

import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursementDto {

	private String mastAgrId;
	private Integer disbSrNo;	
	private String dtDisbDate;
	private String dtInstallmentStartDate;
	private String dtPrinStart;
	private Double disbAmount;
	private String finalDisbYn;
	private String ifscCode;
	private String accountNo;
	private String paymentMode;
	private String utrNo;
}
