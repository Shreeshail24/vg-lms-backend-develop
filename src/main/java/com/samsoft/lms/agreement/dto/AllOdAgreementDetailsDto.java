package com.samsoft.lms.agreement.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllOdAgreementDetailsDto {

	private String mastAgrId;
	private String originationApplnNo;
	private String customerId;
	private String customerName;
	private String mobile;
	private String custCategory;
	private Integer custInternalId;
	private String boardingDate;
	private Double utilizedLimit=0.0;
	private Date dtLimitSanctioned;
	private Date dtLimitExpired;
	private Double limitSanctionAmount;
	private Double availableLimit;
}
