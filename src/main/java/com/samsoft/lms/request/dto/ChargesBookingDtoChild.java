package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesBookingDtoChild {

	private String loanId;
	private String tranCategory;
	private String tranHead;
	private double chargeAmount;
	private String chargeBookReason;
	private String chargeBookRemark;
	private int installmentNo;
	
	
}
