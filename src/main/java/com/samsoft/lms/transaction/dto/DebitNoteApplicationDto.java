package com.samsoft.lms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DebitNoteApplicationDto {

	private String mastAgrId;
	private String loanId;
	private String tranDate;
	private String source;
	private String sourceId;
	private String tranHead;
	private Double amount;
	private String reason;
	private String remark;
	private Integer installmentNo;
	private String userId;
	
}
