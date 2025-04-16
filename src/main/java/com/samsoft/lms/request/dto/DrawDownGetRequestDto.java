package com.samsoft.lms.request.dto;

import com.samsoft.lms.core.dto.AgreementLimitSetupListDto;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawDownGetRequestDto {

	private AgreementLimitSetupListDto agreementLimitList;
	private CustomerBasicInfoDto customerBasicInfo;
	private DrawDownResponseDto downResponseDto;

}
