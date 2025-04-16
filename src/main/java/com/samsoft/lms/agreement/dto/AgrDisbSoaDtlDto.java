package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrDisbSoaDtlDto {

	private String dtDueDate;
	private Double tranAmount;
	private String tranCategory;
	private String tranHead;
	private String tranSide;
	private String dtlRemark;
}
