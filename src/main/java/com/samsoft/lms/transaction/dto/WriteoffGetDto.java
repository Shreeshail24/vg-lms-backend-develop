package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WriteoffGetDto {

	private String loanId;
	private String dueCategory;
	private String dueHead;
	private Double dueAmount;
	private double taxAmount;
	private double totalAmount;
}
