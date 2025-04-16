package com.samsoft.lms.core.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InterestParameterDto {

	private double principalAmount;
	
	private float interestRate;
	
	private String interestBasis;
	
	private String dtInterestStart;
	
	private String dtInterestEnd;
	
	private String dateFormat;
}
