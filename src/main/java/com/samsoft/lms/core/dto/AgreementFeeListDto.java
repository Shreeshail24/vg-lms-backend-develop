package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementFeeListDto {

	private Integer feeParamId;
	private String feeCode;
	private String feeEvent;
	private Integer minTenor;
	private Integer maxTenor;
	private Double minAmount;
	private Double maxAmount;
	private Double minFee;	
	private Double maxFee;
	private String feeType;
	private Float feePercentage;
	private Double feeAmount;
	private String taxApplicatbleYN;
	private String mastAgrId;
}
