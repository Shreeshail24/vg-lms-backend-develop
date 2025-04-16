package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettlementDtlDto {

	private String tranType;
	private String tranHead;
	private double dueAmount;
	private double taxAmount;
	private double totalAmount;
	private double paymentAmount;
	private double deficitAmount;
	
}
