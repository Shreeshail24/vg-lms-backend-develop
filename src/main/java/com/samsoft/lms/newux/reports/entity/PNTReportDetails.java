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
@Table(name = "v_lms_hc_instrument_vs_payapplied")
public class PNTReportDetails {
	 
	@Id
	@Column(name = "sMastAgrId")
	private String mastAgrId;
	
	@Column(name = "sCustomerId")
	private String customerId;

	@Column(name = "nBatchId")
	private Integer batchId;

	@Column(name = "nInstrumentId")
	private String instrumentId;

	@Column(name = "sInstrumentType")
	private String instrumentType;

	@Column(name = "dInstrumentDate")
	private Date instrumentDate;

	@Column(name = "sInstrumentStatus")
	private String instrumentStatus;

	@Column(name = "nInstrumentAmount")
	private Double instrumentAmount;

	@Column(name = "dSettlementDate")
	private Date settlementDate;

	@Column(name = "sPayAppliedYN")
	private String payAppliedYN;

	@Column(name = "sPayBounceYN")
	private String payBounceYN;

	@Column(name = "EventAmt")
	private Double eventAmt;

	@Column(name = "DtlAmount")
	private Double dtlAmount;

	@Column(name = "umrn")
	private String umrn;

	@Column(name = "LendingPartner")
	private String lendingPartner; 

	@Column(name = "sCustomerName")
	private String customerName;

}

// +-------------------+--------------+------+-----+---------+-------+
// | Field             | Type         | Null | Key | Default | Extra |
// +-------------------+--------------+------+-----+---------+-------+
// | nBatchId          | bigint       | YES  |     | NULL    |       |
// | sCustomerId       | varchar(20)  | YES  |     | NULL    |       |
// | sMastAgrId        | varchar(20)  | YES  |     | NULL    |       |
// | umrn              | varchar(20)  | YES  |     | NULL    |       |
// | LendingPartner    | varchar(100) | YES  |     | NULL    |       |
// | sCustomerName     | varchar(222) | YES  |     | NULL    |       |
// | nInstrumentId     | int          | YES  |     | 0       |       |
// | sInstrumentType   | varchar(20)  | YES  |     | NULL    |       |
// | dInstrumentDate   | date         | YES  |     | NULL    |       |
// | sInstrumentStatus | varchar(10)  | YES  |     | NEW     |       |
// | nInstrumentAmount | double       | YES  |     | NULL    |       |
// | dSettlementDate   | date         | YES  |     | NULL    |       |
// | sPayAppliedYN     | varchar(2)   | YES  |     | N       |       |
// | sPayBounceYN      | varchar(2)   | YES  |     | N       |       |
// | EventAmt          | double       | YES  |     | NULL    |       |
// | DtlAmount         | double       | YES  |     | NULL    |       |
// +-------------------+--------------+------+-----+---------+-------+