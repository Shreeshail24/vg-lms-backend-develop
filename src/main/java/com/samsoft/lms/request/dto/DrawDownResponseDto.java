package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawDownResponseDto {

	private Integer requestId;
	private String mastAgrId;
	private Double limitSanctionAmount;
	private Double utilizedLimit;
	private Double availableLimit;
	private Double totalDues;
	private Double totalOverDues;
	private Double requestedAmount;
	private Double approvedAmount;
	private Integer rejectReasonCode;
	private String remarksRequest;
	private String remarksApproval;
	private String requestedDateTime;
	private String decisionDateTime;
	private String userIdRequest;
	private String userIdDecision;
	private Character status;
	private String dtPrinStart;

	/*
	 * private String disbifscCode; private String disbaccountNo; private String
	 * repayFreq;
	 */
	public DrawDownResponseDto(Integer requestId, String mastAgrId, Double limitSanctionAmount, Double utilizedLimit,
			Double availableLimit, Double totalDues, Double totalOverDues, Double requestedAmount,
			Double approvedAmount, Integer rejectReasonCode, String remarksRequest, String remarksApproval,
			String requestedDateTime, String decisionDateTime, String userIdRequest, String userIdDecision,
			Character status) {
		super();
		this.requestId = requestId;
		this.mastAgrId = mastAgrId;
		this.limitSanctionAmount = limitSanctionAmount;
		this.utilizedLimit = utilizedLimit;
		this.availableLimit = availableLimit;
		this.totalDues = totalDues;
		this.totalOverDues = totalOverDues;
		this.requestedAmount = requestedAmount;
		this.approvedAmount = approvedAmount;
		this.rejectReasonCode = rejectReasonCode;
		this.remarksRequest = remarksRequest;
		this.remarksApproval = remarksApproval;
		this.requestedDateTime = requestedDateTime;
		this.decisionDateTime = decisionDateTime;
		this.userIdRequest = userIdRequest;
		this.userIdDecision = userIdDecision;
		this.status = status;
	}

}
