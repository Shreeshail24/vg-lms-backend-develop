package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RuleDto {

	private Integer npaGroupSeq;
	private Integer nSortSeq;
	private String sDueCategory;
	private String sPayHead;
	private Integer nDueDtlId;
	private Integer nInstallmentNo;
	private Double nDueAmount;
	private Date dDueDate;
	private Double totalDue;
	
	
}
