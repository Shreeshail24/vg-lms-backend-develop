package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GlGenerateDto {

	private String customerId;
	private String fromDate;
	private String toDate;
	private String regenerateYn;
	private String userId;
}
