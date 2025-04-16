package com.samsoft.lms.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstScheduleList {

	private int installmentNo;
	private String installmentDate;
	private double openingAmount;
	private double principalAmount;
	private double interestAmount;
	private double bpiAmount;
	private double installmentAmount;
	private double closingBalance;
	private float interestRate;
	private String interestBasis;
	private String interestAccrualFrequency;
}
