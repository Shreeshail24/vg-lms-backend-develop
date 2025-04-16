package com.samsoft.lms.agreement.dto;

import java.util.Date;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursementOdSubsequentDto {

	private String dtDisbDate;
	private Double disbAmount;
	private String ifscCode;
	private String accountNo;
	private String paymentMode;
	private String utrNo;
	
}
