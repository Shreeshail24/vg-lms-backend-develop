package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundReqDto {

	private Integer reqId;
	private String mastAgrId;
	private String dtRequestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String refundReason;
	private String refundRemark;
	private String userId;
	private String allocatedUserId;
	private String dtInstrument;
	private String paymentType;
	private String paymentMode;
	private String instrumentType;
	private String bankAccountNo;
	private String accountType;
	private String instrumentNo;
	private String bankCode;
	private String branchCode;
	private Double refundAmount;
	private String utrNo;
	private String ifscCode;
	private String activityCode;
	private String instrumentLocation;
}
