package com.samsoft.lms.request.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForclosureChequeRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5659109283960532522L;
	@NotEmpty	
	private String masterAgreementId;
	@NotEmpty	
	private String requestDate;
	@NotEmpty	
	private String flowType;
	@NotEmpty	
	private String requestStatus;
	@NotEmpty	
	private String reason;
	@NotEmpty	
	private String remark;
	@NotEmpty	
	private String userId;
	
	private String allocatedUserId;
	private String instrumentDate;	
	private String paymentType;
	private String paymentMode;
	private String instrumentType;
	private String bankAccountNo;
	private String accountType;
	private String instrumentNo;
	private String bankCode;
	private String brachCode;
	private String micrCode;
	private String clearingLocation;
	private Double instrumentAmount;
	private String depositBank;
	private String utrNo;
	private String ifscCode;
	private String depositRefNo;
	private String cardHolderName;
	private String issuingBank;
	private String upiVpa;
	private String processingLocation;
	private String cardType;
	private String collectionAgency;
	private String collectionAgent;
	private String provisionalReceiptFlag;
	private String loanId;
	private Integer reqId;
	private String instrumentLocation;
	
}
