package com.samsoft.lms.newux.dto.response;

import java.util.List;

import com.samsoft.lms.request.dto.DreAllocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawDownDisbUpdateReqDetailsResponseDto {

	private String collectedBy;
	private String collectionAgency;
	private String depositRefNo;
	private List<DreAllocationDto> dreAllocation;
	private String flowType;
	private Double instrumentAmount;
	private String instrumentDate;	
	private String instrumentType;
	private String masterAgreementId;
	private String paymentMode;
	private String paymentType;
	private String provisionalReceiptFlag;
	private String reason;
	private String remark;
	private String requestDate;	
	private String requestStatus;	
	private String userId;
	private String customerId;
}
