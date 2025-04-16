package com.samsoft.lms.agreement.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class AllOdAgreementMainDto {

	private Integer totalRows;
	List<AllOdAgreementDetailsDto> allAgreementDto;
}
