package com.samsoft.lms.request.dto;

import com.samsoft.lms.transaction.dto.ForclosureDueDetails;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettlementReceivableListDto {
	
	private String tranHead;
	private String tranType;
	private Double dueAmount;
	private Double taxAmount=0d;
	private double totalAmount;
	private double paymentAmount;
	private double deficitAmount;

}
