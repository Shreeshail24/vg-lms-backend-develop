package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargesWaiverDto {

	private String mastAgrId;
	private String requestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String allocatedUserId;
	List<ChargesWaiverParamListDto> waiverParamList;
	private Integer reqId;
	private String activityCode;
	private String loanId;
}
