package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrepaymentDuesList {

	private String dueCategory;
	private String dueHead;
	private double dueAmount;
	private double taxAmount;
	private String dueDate;
	private int installmentNo;
	
}
