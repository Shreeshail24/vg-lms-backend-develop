package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllAgreementDetailsDto {

	private String mastAgrId;
	private String originationApplnNo;
	private String customerId;
	private String customerName;
	private String mobile;
	private String custCategory;
	private Integer custInternalId;
	private String boardingDate;
}
