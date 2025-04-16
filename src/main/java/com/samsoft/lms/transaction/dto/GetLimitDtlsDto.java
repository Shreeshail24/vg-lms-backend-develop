package com.samsoft.lms.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetLimitDtlsDto {

	private Integer limitTranId;
	private Double tranAmount;
	private String tranType;
	private String purpose;
	private Integer refTranId;
	private Double limitSanctionAmount;
	private Double utilizedLimit = 0.0;
	private Double availableLimit;
	private Double currentDroppedLimit = 0.0;
	private String mastAgrId;
}
