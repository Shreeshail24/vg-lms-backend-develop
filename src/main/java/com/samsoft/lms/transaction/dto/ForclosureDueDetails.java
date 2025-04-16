package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForclosureDueDetails {

	private String dueHead;
	private String dueCategory;
	private Double dueAmount;
	private String bookYn;
	private Double taxAmount=0d;
	private String dueDate;
	private Integer installmentNo=0;
}
