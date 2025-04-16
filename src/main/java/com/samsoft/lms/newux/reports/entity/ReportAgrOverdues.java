package com.samsoft.lms.newux.reports.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "v_lms_rpt_agr_overdues")
public class ReportAgrOverdues {

	@Id
	@Column(name = "sMastAgrId")
	private String mastAgrId;
	
	@Column(name = "sCustomerId")
	private String customerId;
	
	@Column(name = "sCustomerName")
	private String customerName;
	
	@Column(name = "sPortfolioCode")
	private String portfolioCode;
	
	@Column(name = "sProductCode")
	private String productCode;
	
	@Column(name = "sHomeBranch")
	private String homeBranch;
	
	@Column(name = "sAssetClass")
	private String assetClass;
	
	@Column(name = "nLoanAmount")
	 private Double loanAmount;
	
	@Column(name = "nUnbilledPrincipal")
	 private Double unbilledPrincipal;
	
	@Column(name = "nDpd")
	 private Integer dpd;
	
	@Column(name = "PrincipalOverdue")
	 private Double principalOverdue;
	
	@Column(name = "InterestOverdue")
	 private Double interestOverdue;
	
	@Column(name = "PenalOverdue")
	 private Double penalOverdue;
	
	@Column(name = "BounceOverdue")
	 private Double bounceOverdue;
	
	@Column(name = "OtherChargesOverdue")
	 private Double OtherChargesOverdue;
	
	@Column(name = "TotalOverdue")
	private Double TotalOverdue;

	@Column(name = "dDisbDate")
	private Date disbDate;

	@Column(name = "sOriginationApplnNo")
	private String originationApplnNo;

	@Column(name = "NBFC")
    private String nbfc;
	
}

//      mysql> desc v_lms_rpt_agr_overdues;
//		+---------------------+--------------+------+-----+---------+-------+
//		| Field               | Type         | Null | Key | Default | Extra |
//		+---------------------+--------------+------+-----+---------+-------+
//		| sMastAgrId          | varchar(20)  | YES  |     | NULL    |       |
//		| sCustomerId         | varchar(20)  | YES  |     | NULL    |       |
//		| sCustomerName       | varchar(222) | YES  |     | NULL    |       |
//		| sPortfolioCode      | varchar(20)  | YES  |     | NULL    |       |
//		| sProductCode        | varchar(20)  | YES  |     | NULL    |       |
//		| sHomeBranch         | varchar(20)  | YES  |     | NULL    |       |
//		| sAssetClass         | varchar(20)  | YES  |     | NULL    |       |
//		| nLoanAmount         | double       | YES  |     | NULL    |       |
//		| nUnbilledPrincipal  | double       | YES  |     | NULL    |       |
//		| nDpd                | int          | YES  |     | NULL    |       |
//		| PrincipalOverdue    | double       | YES  |     | NULL    |       |
//		| InterestOverdue     | double       | YES  |     | NULL    |       |
//		| PenalOverdue        | double       | YES  |     | NULL    |       |
//		| BounceOverdue       | double       | YES  |     | NULL    |       |
//		| OtherChargesOverdue | double       | YES  |     | NULL    |       |
//		| TotalOverdue        | double       | YES  |     | NULL    |       |
//		| dDisbDate           | date         | YES  |     | NULL    |       |
//		| sOriginationApplnNo | varchar(20)  | YES  |     | NULL    |       |
//		| NBFC                | varchar(100) | YES  |     | NULL    |       |
//		+---------------------+--------------+------+-----+---------+-------+
//		19 rows in set (0.01 sec)


