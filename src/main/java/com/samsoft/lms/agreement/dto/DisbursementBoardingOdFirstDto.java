package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisbursementBoardingOdFirstDto {
	private String mastAgrId;
	private LoanDisbursementOdFirstDto loanDtl;
	private DisbursementOdFirstDto disbursementDtl;
}
