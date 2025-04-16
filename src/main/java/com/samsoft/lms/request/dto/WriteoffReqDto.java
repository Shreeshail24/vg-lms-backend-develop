package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WriteoffReqDto {

	private String mastAgrId;
	private String dtRequest;
	private String flowType;
	private String reqStatus;
	private String reason;
	private String remark;
	private String userId;
	private String loanId;
	private double writeoffAmount;
	private String writeoffReason;
	private String writeoffRemark;
	private List<WriteoffReqDtlDto> writeoffDtl;

}
