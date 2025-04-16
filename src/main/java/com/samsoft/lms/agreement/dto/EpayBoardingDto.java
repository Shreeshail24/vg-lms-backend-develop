package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpayBoardingDto {

	//private String mastAgrId;
	//private String mandateStatus;
	private String instrumentType;
	private String dtFromDate;
	private String dtToDate;
	private String bankName;
	private String bankBranchName;
	private String accountType;
	private String accountNo;
	private String accountHolderName;
	private Double maxCeilAmount;
	private Double installmentAmount;
	private Double maxInstallmentAmount;
	private String mandateRefNo;
	private String depositBank;
	private String mandateType;
	private String frequency;
	private String ifscCode;
	private String utrNo;
	//private String iBan;
	private String active;
}
