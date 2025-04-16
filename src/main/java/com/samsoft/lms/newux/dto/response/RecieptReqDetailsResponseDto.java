package com.samsoft.lms.newux.dto.response;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.samsoft.lms.request.dto.DreAllocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecieptReqDetailsResponseDto {
	
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
	private String allocatedUserId;
	private String customerName;
	
	private String clearingLocation;
	private String ifscCode;
	private String issuingBank;
	private String accountType;
	private String accountNo;
	private Date dtReceipt;
	private String depositBank;
	
	private String bankBranchCode;
	private String bankCode;
	private String CardHolderName;
	private String cardType;
	private String instrumentNo;
	private String mICRCode;
	private String procLoc;
	private Double tdsAmount;
	private String uPIVPA;
	private String uTRNo;
    private String instrumentLocation;
}
