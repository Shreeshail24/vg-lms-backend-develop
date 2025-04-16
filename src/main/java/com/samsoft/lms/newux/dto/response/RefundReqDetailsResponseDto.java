package com.samsoft.lms.newux.dto.response;

import com.samsoft.lms.request.dto.RefundReqDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundReqDetailsResponseDto {

	private String accountType;
	private String bankAccountNo;
	private String bankCode;
	private String branchCode;
	private String dtInstrument;
	private String dtRequestDate;
	private String flowType;
	private String ifscCode;
	private Double instrumentAmount;
	private String instrumentNo;
	private String instrumentType;
	private String mastAgrId;
	private String paymentMode;
	private String paymentType;
	private String reason;
	private Double refundAmount;
	private String refundReason;
	private String refundRemark;
	private String remark;
	private String requestStatus;
	private String userId;
	private String utrNo;
	private String customerId;
	private String customerName;
	private String instrumentLocation;
	private String allocatedUserId;
}