package com.samsoft.lms.newux.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargesWaiverParamListResponseDto {

	private String loanId;
	private Integer chargeTranId;
	private String tranHead;
	private Double waiverAmount;
	private String waiverReason;
	private String waiverRemark;
	private Integer installmentNo;
	private String reqDate;
	
}
