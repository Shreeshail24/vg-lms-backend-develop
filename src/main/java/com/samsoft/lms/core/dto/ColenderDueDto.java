package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColenderDueDto {

	private String mastAgrId;
	private String loanId;
	private double dueAmount;
	private int installmentNo;
	private String dueCategory;
	private String dueHead;
	private Date dtDueDate;
	private int tranDtlId;
	private String userId;

}
