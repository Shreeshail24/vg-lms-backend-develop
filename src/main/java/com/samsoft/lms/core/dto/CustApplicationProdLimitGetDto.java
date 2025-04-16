package com.samsoft.lms.core.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustApplicationProdLimitGetDto {

	private int custApplLimitId;
	private String originationApplnNo;
	private String customerId;
	private String productCode;
	private double limitSanctioned;
	private double utilizedLimit;
	private double availableLimit;
	private String dtLimitSanctioned;
	private String dtLimitExpired;
	private String purpose;
	private String userId;
	//private String dtLastUpdated;
	//private String dtUserDate;
	private float subventionPer;
	private double subventionAmount;
}
