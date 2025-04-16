package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartPrepaymentDto {

	private String mastAgrId;
	private String dtRequest;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String allocatedUserId;
	private String loanId;
	private Double allocatedAmount;
	private String collectionAgency;
	private String collectionAgent;
	private String dtInstrument;
	private String dtReceipt;
	private String paymentMode;
	private String paymentType;
	private Double discountAmount;
	private String prepayReason;
	private String changeFactor;
	private Double prepayAmount;
	private List<PrepaymentDuesList> prepayDueList;
	private Double netReceivable;
	private Double excessAmount;	
	private String instrumentType;
	private String ifscCode;
	private String bankCode;
	private String branchCode;
	private Double instrumentAmount;
	private String utrNo;
	private String depositRefNo;
	private Integer reqId;
	private String accountType;
	private String accountNo;
	private String instrumentLocation;
	private String instrumentNo;
	
	
}
