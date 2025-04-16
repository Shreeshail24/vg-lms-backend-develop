package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RepayVariationDto {

	private Integer fromInstallmentNo;
	private Integer noOfInstallment;
	private String variationType;
	private String variationOption;
	private Double variationValue;
	private String adjustmentFactor;
}
