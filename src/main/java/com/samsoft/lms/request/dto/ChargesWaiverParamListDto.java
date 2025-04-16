package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargesWaiverParamListDto {

	private String loanId;
	private Integer chargeTranId;
	private String tranHead;
	private Double waiverAmount;
	private String waiverReason;
	private String waiverRemark;
	private Integer installmentNo;
	
}
