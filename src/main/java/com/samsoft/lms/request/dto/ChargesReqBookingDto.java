package com.samsoft.lms.request.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(value=JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargesReqBookingDto {

	private Integer reqId;
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
	
	private String loanId;

	private String tranHead;
	
	private double chargeAmount;
	
	private String chargeBookReason;
	
	private String chargeBookRemark;
	
	private int installmentNo;
	
	private double totalAmount;
	
}
