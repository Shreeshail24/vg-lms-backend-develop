package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitNoteReqDto {

	private Integer reqId;
	private String mastAgrId;
	private String loanId;
	private String dtRequestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String allocatedUserId;
	private String tranHead;
	private Double drNoteAmount;
	private String drNoteReason;
	private String drNoteRemark;
	private Integer installmentNo;
}
