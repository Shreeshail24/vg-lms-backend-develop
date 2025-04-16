package com.samsoft.lms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesBookingDto {
		
	private String mastAgrId;		
	private String loanId;
	private String trandDate;
	private String source;
	private String sourceId;
	private String tranHead;
	private Double chargeAmount;
	private String reason;
	private String remark;
	private int installmentNo;
	private String userId;
	
		
}
