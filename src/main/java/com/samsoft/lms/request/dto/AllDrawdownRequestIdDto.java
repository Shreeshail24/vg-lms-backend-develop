package com.samsoft.lms.request.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllDrawdownRequestIdDto {

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
	private String useridRequest;
	private String userIdDecision;
	private Character status;
	private String customerId;
	private String customerName; 
}
