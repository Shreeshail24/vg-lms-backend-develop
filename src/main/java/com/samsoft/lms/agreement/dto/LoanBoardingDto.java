package com.samsoft.lms.agreement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanBoardingDto {
	private String mastAgrId;
	private LoanDisbursementDto loanDtl;
	private DisbursementDto disbursementDtl;
	private List<RepayVariationDto> repaymentVariationDtl;
}
