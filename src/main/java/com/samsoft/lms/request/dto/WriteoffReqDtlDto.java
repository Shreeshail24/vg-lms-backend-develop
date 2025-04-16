package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WriteoffReqDtlDto {

	private String loanId;
	private String tranType;
	private String tranHead;
	private double dueAmount;
	private double taxAmount;
	private double totalAmount;
}
