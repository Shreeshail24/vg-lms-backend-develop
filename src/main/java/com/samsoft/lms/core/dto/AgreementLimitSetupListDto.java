package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementLimitSetupListDto {
	private Integer sLimitId;
	private Double utilizedLimit = 0.0;
	private String dtLimitSanctioned;
	private String dtLimitExpired;
	private Double limitSanctionAmount;
	private String purpose;
	private String masterAgreement;
	private String loanId;
	private Double drawingPower = 0.0;
	private Double availableLimitAmount;
	private Double allPendingRequestsAmount;
}
