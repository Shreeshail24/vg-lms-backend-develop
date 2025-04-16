package com.samsoft.lms.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class AmortVariationListDto {

	private Integer fromInstNo;
	private Integer noOfInstallment;
	private String variationType;
	private String variationOption;
	private Double variationValue;
	private String adjustmentFactor;

}
