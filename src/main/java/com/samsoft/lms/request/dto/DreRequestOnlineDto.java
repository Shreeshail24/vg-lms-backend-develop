package com.samsoft.lms.request.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DreRequestOnlineDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5659109283960532522L;
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
	private String utrNo;
	private String ifscCode;
	private String bankCode;
	private String bankBranchCode;
	@NotEmpty
	private List<DreAllocationDto> dreAllocation;
}
