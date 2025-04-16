package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargesWaiverApplicationDto {

	private String mastAgrId;
	private String loanId;
	private String tranDate;
	private String source;
	private String sourceId;
	private String tranHead;
	private Integer chargeBookTranId;
	private Double chargeWaiveAmount;
	private String chargeWaiveReason;
	private String chargeWavaieRemark;
	private Integer installmentNo;
	private String userId;
	
}
