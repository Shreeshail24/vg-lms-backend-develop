package com.samsoft.lms.newux.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SrWorklistByActivityCodeAndStatusResponseDto {
	
	private Integer reqId;
	private String activityCode;
	private Date dtLastUpdated;
	private Date dtRequest;
	private Date dtUserDate;
	private String flowType;	
	private String reason;
	private String remark;
	private String reqStatus;
	private String userId;
	private String mastAgrId;
	private String customerName;
	private String mobile;
	private String customerId;
}