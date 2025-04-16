package com.samsoft.lms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundApplicationDto {

	private String mastAgrId;
	private String dtTranDate;
	private String source;
	private String sourceId;
	private Double refundAmount;
	private String refundReason;
	private String refundRemark;
	private String userId;
	private String dtInstrument;
	private String instrumentType;
	private String accountNo;
	private String accountType;
	private String instrumentNo;
	private String bankCode;
	private String branchCode;
	private String utrNo;
	private String ifscCode;
	private String paymentRefNo;
	
	
}
