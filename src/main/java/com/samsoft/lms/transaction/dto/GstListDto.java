package com.samsoft.lms.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GstListDto {

	private String taxCode;
	private String taxDescription;
	private Double taxAmount;
	private Float taxPercentage;
}
