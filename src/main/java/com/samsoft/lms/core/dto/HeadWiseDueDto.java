package com.samsoft.lms.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeadWiseDueDto {

	private String mastAgrId;
	private String dueCategory;
	private String dueHead;
	private Double dueAmount;
}
