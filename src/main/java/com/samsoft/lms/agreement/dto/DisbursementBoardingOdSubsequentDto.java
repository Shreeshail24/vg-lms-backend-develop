package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisbursementBoardingOdSubsequentDto {
	
	private String mastAgrId;
	private LoanDisbursementOdSubsequentDto loanDtl;
	private DisbursementOdSubsequentDto disbursementDtl;

}
