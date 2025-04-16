package com.samsoft.lms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrMasterAgreementDto {

	private String mastAgrId;
	private String customerId;
	private String customerType="Individual";
	private String portfolioCode;
	private Integer totalTenor;
	private Integer balanceTenor;
	private String tenorUnit="MONTHLY";
	private String homeBranch;
	private String servBranch;
	private String gstExempted;
	private String agreementStatus;
	private Double outstandingAmount;
	private Double nextInstallmentAmount = 0.0;
	private String dtNextInstallment;

}
