package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDisbursementOdFirstDto {

	private String repayFreq;
	//private Float interestRate = 0.0f;
	private String userId;
	private String loanType="DRAWDOWN";
}
