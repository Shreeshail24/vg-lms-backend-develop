package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingForApprovalDto {
	
	private Integer reqId;
	private String dtRequestDate;
	private String requestStatus;
	private String remark;
	private String paymentMode;
	private String requestAmount;
	private String activityCode;
	private String userId;
	private Double amount;
	private String mastAgrId;
	private Boolean isAdminAllocated;
	

}
