package com.samsoft.lms.newux.reports.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "v_lms_rep_od_cust_unutilised_limit")
public class CustomerUnutilizedLimit {

	@Id
	@Column(name = "sCustomerId")
	private String customerId;

    @Column(name = "sOriginationApplnNo")
    private String originationApplnNo;

    @Column(name = "sCustomerName")
	private String customerName;

	@Column(name = "nLimitSanctioned")
	private Double nlimitSanctioned;

	@Column(name = "nUtilizedLimit")
	private Double utilizedLimit;

	@Column(name = "nAvailableLimit")
	private String availableLimit;

	@Column(name = "dLimitSanctioned")
	private Date dLimitSanctioned;

	@Column(name = "dLimitExpired")
	private Date limitExpired;

	@Column(name = "nSubventionAmount")
	private Double subventionAmount;

	@Column(name = "nSubventionPer")
	private Float subventionPer;

	@Column(name = "sPurpose")
	private String purpose;

	@Column(name = "sProductCode")
	private String productCode; 
}