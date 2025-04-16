package com.samsoft.lms.agreement.dto;

import java.util.ArrayList;
import java.util.List;

import com.samsoft.lms.core.dto.AmortVariationListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursementBoardingDto {
	private String mastAgrId;
	private LoanDisbursementDto loanDtl = new LoanDisbursementDto();
	private DisbursementDto disbursementDtl = new DisbursementDto();
	private List<AmortVariationListDto> repaymentVariationDtl;
	private List<AgrDisbSoaDtlDto> soaDtls = new ArrayList<AgrDisbSoaDtlDto>();
}
