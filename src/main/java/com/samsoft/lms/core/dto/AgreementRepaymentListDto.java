package com.samsoft.lms.core.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementRepaymentListDto {
	private List<AgreementRepaymentRecordsDto> agreementRepaymentRecords;
}
