package com.samsoft.lms.request.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForclosureOnlineRequestDto {

	private static final long serialVersionUID = -5659109283960532522L;
	private String masterAgreementId;
	private String requestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String allocateduserId;
	private String loanId;
	private String collectionAgency;
	private String collectionAgent;
	private String instrumentDate;
	private String receiptDate;
	private String paymentType;
	private String paymentMode;
	private String instrumentType;
	private String ifscCode;
	private String bankCode;
	private String bankBranchCode;
	private Double instrumentAmount;
	private String utrNo;

}
