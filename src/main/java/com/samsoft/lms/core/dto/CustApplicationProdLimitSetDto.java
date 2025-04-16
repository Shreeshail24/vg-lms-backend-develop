package com.samsoft.lms.core.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustApplicationProdLimitSetDto {

	private String originationApplnNo;
	private String customerId;
	private String productCode;
	private double limitSanctioned;
	private String dtLimitSanctioned;
	private String dtLimitExpired;
	private String userId;
	private String purpose;
	private float subventionPer;
	private double subventionAmount;
	private String fullName;
	private Integer agencyId;
	private String agencyName;
}
