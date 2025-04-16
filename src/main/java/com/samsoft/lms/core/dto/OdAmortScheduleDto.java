package com.samsoft.lms.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OdAmortScheduleDto {
	private String previousInstallmentDate;
	private Double openingBalance;
	private Integer installmentNo;
	private String installmentDate;
	private Float interestRate;
	private Double interestAmount;
	private Double installmentAmount;
	private Double principalAmount;
	private Double closingBalance;
	private Double bpiAmount;
	private String tranType;
	private Double interestAccrued;
	private Integer sortSeq;
	
}
