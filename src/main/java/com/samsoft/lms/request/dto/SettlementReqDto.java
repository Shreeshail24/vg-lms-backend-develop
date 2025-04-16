package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettlementReqDto {

	private String mastAgrId;
	private String dtRequest;
	private String flowType;
	private String reqStatus;
	private String reason;
	private String remark;
	private String userId;
	private String loanId;
	private double settlementAmount;
	private double receivableAmount;
	private double deficitAmount;
	private double excessAmount;
	private String settlementReason;
	private String settlementMode;
	private String settlementRemark;
	private List<SettlementDtlDto> settlementDtl;
	private List<SettlementPaymentScheduleListDto> paymentSchedule;

}
