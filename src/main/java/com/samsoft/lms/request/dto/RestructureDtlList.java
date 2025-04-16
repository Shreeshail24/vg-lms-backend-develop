package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestructureDtlList {

	private String tranType;
	private String tranHead;
	private double dueAmount;
	private double taxAmount;
	private double totalOsAmount;
	private double waiverAmount;
	private double capitalizaAmount;
	private double balanceAmount;
}
