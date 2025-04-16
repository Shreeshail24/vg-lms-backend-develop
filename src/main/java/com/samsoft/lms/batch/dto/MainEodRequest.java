package com.samsoft.lms.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainEodRequest {

	private String orgId;
	
	private String businessDate;
	
	private String dateFormat;
}
