package com.samsoft.lms.request.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DreReqCashDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5659109283960532522L;
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
	private String instrumentDate;	
	private Double instrumentAmount;
	private Double tdsAmount;
	private String instrumentType;
	private String paymentType;
	private String paymentFor;
	private String paymentMode;
	private String depositRefNo;
	private String collectionAgency;
	private String collectedBy;
	private String provisionalReceiptFlag;
	private String instrumentLocation;
	@NotEmpty
	private List<DreAllocationDto> dreAllocation;
}
