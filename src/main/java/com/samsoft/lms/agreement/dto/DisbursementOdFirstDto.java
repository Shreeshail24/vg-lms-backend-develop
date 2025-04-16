package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursementOdFirstDto {

	private String dtDisbDate;
	//@JsonIgnore
	private String dtPrinStart;
	private Double disbAmount;
	private String ifscCode;
	private String accountNo;
	private String paymentMode;
	private String utrNo;
	
}
