package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecallReqDto {

	private String mastAgrId;
	private String dtRequest;
	private String flowType;
	private String reqStatus;
	private String reason;
	private String remark;
	private String userId;
	private List<RecallReqLoanReceivableListDto> loanReceivables;
	private String recallStatus;
	private String recallRemark;

}
