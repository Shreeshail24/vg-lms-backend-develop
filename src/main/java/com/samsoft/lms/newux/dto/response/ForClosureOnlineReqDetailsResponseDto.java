package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForClosureOnlineReqDetailsResponseDto {

	private Integer reqId;
	private String masterAgreementId;
	private String requestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String loanId;
	private String collectionAgency;
	private String collectionAgent;
	private String instrumentDate;
	private String receiptDate;
	private String paymentType;
	private String paymentMode;
	private String instrumentType;
	private String ifscCode;
	private String bankCode;
	private String bankBranchCode;
	private Double instrumentAmount;
	private String utrNo;
	private String customerId;
	private String customerName;
	private String accountType;
	private String accountNo;
	private String instrumentLocation;
	private String allocatedUserId;
	private String instrumentNo;
}
