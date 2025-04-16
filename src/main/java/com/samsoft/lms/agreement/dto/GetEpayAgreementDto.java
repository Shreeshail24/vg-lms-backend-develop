package com.samsoft.lms.agreement.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetEpayAgreementDto {

	private Integer epayId;	
	private String mandateStatus;
	private String customerId;
	private String instrumentType;
	private String dtFromDate;
	private String dtToDate;
	private String bankCode;
	private String bankBranchCode;
	private String accountType;
	private String accountNo;
	private Double maxCeilAmount;
	private Double installmentAmount;
	private Double maxInstallmentAmount;
	private String mandateRefNo;
	private String depositBank;
	private String mandateType;
	private String frequency;
	private String ifscCode;
	private String utrNo;
	private String iBan;
	private String active;
	private String accountHolderName;
	private String virtualId;
}
