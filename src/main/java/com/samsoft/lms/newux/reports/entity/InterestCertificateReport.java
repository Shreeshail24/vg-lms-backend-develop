package com.samsoft.lms.newux.reports.entity;

import java.util.Date;

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
@Table(name = "v_lms_rep_interest_certificate")
public class InterestCertificateReport {

	@Id
	@Column(name = "sMastAgrId")
	private String mastAgrId;

	@Column(name = "sCustomerId")
	private String customerId;

	@Column(name = "sPortfolioCode")
	private String portfolioCode;

	@Column(name = "sProductCode")
	private String productCode;

	@Column(name = "dTranDate")
	private Date tranDate;

	@Column(name = "InterestDueAmount")
	private Double interestDueAmount;

	@Column(name = "PrincipalDueAmount")
	private Double principalDueAmount;

	@Column(name = "InterestPaidAmount")
	private Double interestPaidAmount;

	@Column(name = "PrincipalPaidAmount")
	private Double principalPaidAmount;

	@Column(name = "sCustomerName")
	private String customerName;

	@Column(name = "dDisbDate")
	private Date disbDate;

	@Column(name = "sOriginationApplnNo")
	private String originationApplnNo;

	@Column(name = "NBFC")
	private String nbfc;

}

//      +---------------------+--------------+------+-----+---------+-------+
//		| Field               | Type         | Null | Key | Default | Extra |
//		+---------------------+--------------+------+-----+---------+-------+
//		| sMastAgrId          | varchar(20)  | NO   |     | NULL    |       |
//		| sCustomerId         | varchar(20)  | YES  |     | NULL    |       |
//		| sPortfolioCode      | varchar(20)  | YES  |     | NULL    |       |
//		| sProductCode        | varchar(20)  | YES  |     | NULL    |       |
//		| dTranDate           | date         | YES  |     | NULL    |       |
//		| InterestDueAmount   | double       | YES  |     | NULL    |       |
//		| PrincipalDueAmount  | double       | YES  |     | NULL    |       |
//		| InterestPaidAmount  | double       | YES  |     | NULL    |       |
//		| PrincipalPaidAmount | double       | YES  |     | NULL    |       |
//		| sCustomerName       | varchar(222) | YES  |     | NULL    |       |
//		| dDisbDate           | date         | YES  |     | NULL    |       |
//		| sOriginationApplnNo | varchar(20)  | YES  |     | NULL    |       |
//		| NBFC                | varchar(100) | YES  |     | NULL    |       |
//		+---------------------+--------------+------+-----+---------+-------+
