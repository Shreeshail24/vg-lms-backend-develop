package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleDetailsDto {

	private String npaGroupSeq;
	private Integer sortSeq;
	private String dueCategory;
	private String payHead;
	private Integer dueDtlId;
	private Integer installmentNo;
	private Double dueAmount;
	private Date dueDate;
	private Double totalDue;
	private String loanId;
	private Integer tranDtlId;

}
