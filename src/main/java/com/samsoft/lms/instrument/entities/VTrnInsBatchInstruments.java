package com.samsoft.lms.instrument.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "v_trn_ins_batch_instruments")
@Subselect("select * from v_trn_ins_batch_instruments")
public class VTrnInsBatchInstruments {

	@Id
	@Column(name = "nBatchInstrumentId", length = 20)
	private Integer batchInstrumentId;

	@Column(name = "nBatchId", length = 20)
	private Integer batchId;

	@Column(name = "nInstrumentId", length = 20)
	private Integer instrumentId;
	
	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "dInstrumentDate", length = 20)
	private Date dtInstrumentDate;

	@Column(name = "sInstrumentNo", length = 20)
	private Integer instrumentNo;

	@Column(name = "nInstrumentAmount", length = 20)
	private Double instrumentAmount;

	@Column(name = "sIfscCode", length = 20)
	private String ifscCode;

	@Column(name = "sAccountNo", length = 20)
	private String accountNo;

	@Column(name = "sAccountType", length = 20)
	private String accountType;

	@Column(name = "sBankName", length = 20)
	private String bankName;

	@Column(name = "sBankBranchName", length = 20)
	private String bankBranchName;

	@Column(name = "sUtrNo", length = 20)
	private String utrNo;

	@Column(name = "sUpiVpa", length = 20)
	private String upiVpa;

	@Column(name = "sInstrumentStatus", length = 20)
	private String instrumentStatus;

	@Column(name = "sBounceReason", length = 20)
	private String bounceReason;
	
	@Column(name = "sUmrn", length = 20)
	private String umrn;

	@Column(name = "sColenderName")
	private String colenderName;

}
