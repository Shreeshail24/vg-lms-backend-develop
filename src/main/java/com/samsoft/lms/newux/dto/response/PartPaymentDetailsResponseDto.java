package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PartPaymentDetailsResponseDto {

    private String masterAgreementId;
	private String prepayAmount;
	private String paymentFor;
	private String referenceNo;
	private String changeFactor;
	private String reason;
	private String remark;
	private String instrumentType;
	private String receiptDate;
	private String userId;
	private Integer reqID; 
	private String paymode;  
	private double unbilledPrincipal;
	private String tranType;
	private String loanID;
	private String tranDate;
	private String userDate;
	private double instrumentAmount;
	private String uTRN;
	private String refNo;
	private String payMode;
	private String payType;
	private String accountType;
	private String accountNo;
	private String InstrumentLocation;
	private String ifscCode;
	private String bankCode;
	private String branchCode;
	private Double discountAmount;
	private String reqStatus;
	private String allocatedUserId;
	private String instrumentNo;
}
