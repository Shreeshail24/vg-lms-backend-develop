package com.samsoft.lms.agreement.dto;

import javax.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlabWiseInterestBoardingDto {

	private int tenorFrom;
	private int tenorTo;
	private float interestRate;
	private String tenorUnit;
}
