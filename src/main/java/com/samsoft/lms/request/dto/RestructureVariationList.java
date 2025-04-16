package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestructureVariationList {

	private int fromInstallmentNo;
	private int noOfInstallment;
	private String variation;
	private String option;
	private double value;
	private String adjustmentFactor;
}
