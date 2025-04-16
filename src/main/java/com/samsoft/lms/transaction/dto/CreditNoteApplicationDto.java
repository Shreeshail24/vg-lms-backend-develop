package com.samsoft.lms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreditNoteApplicationDto {

	private String mastAgrId;
	private String loanId;
	private String tranDate;
	private String source;
	private String sourceId;
	private String tranHead;
	private Double crNoteAmount;
	private String crNoteReasonCode ;
	private String crNoteRemark;
	private Integer installmentNo;
	private String userId;
//	private String instrumentNo;
	
}
