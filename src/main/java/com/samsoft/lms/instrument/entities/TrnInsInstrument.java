package com.samsoft.lms.instrument.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trn_ins_instrument")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class TrnInsInstrument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nInstrumentId", length = 20)
	private Integer instrumentId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "sSource", length = 20)
	private String source;

	@Column(name = "sSourceId", length = 20)
	private String sourceId;

	@Column(name = "dInstrumentDate")
	@Temporal(TemporalType.DATE)
	private Date dtInstrumentDate;

	@Column(name = "sPayType", length = 20)
	private String payType;

	@Column(name = "sInstrumentType", length = 20)
	private String instrumentType;

	@Column(name = "sAccountNo", length = 20)
	private String accountNo;

	@Column(name = "sAccountType", length = 20)
	private String accountType;

	@Column(name = "sInstrumentNo", length = 20)
	private String instrumentNo;

	@Column(name = "sIfscCode", length = 20)
	private String ifscCode;

	@Column(name = "sBankName", length = 20)
	private String bankName;

	@Column(name = "sBankBranchName", length = 50)
	private String bankBranchName;

	@Column(name = "nInstrumentAmount", length = 20)
	private Double instrumentAmount = 0.0;

	@Column(name = "sInstrumentStatus", columnDefinition = "varchar(255) default 'NEW' ", length = 10)
	private String instrumentStatus;

	@Column(name = "sBounceReason", length = 20)
	private String bounceReason;

	@Column(name = "sDepositBankIfsc", length = 20)
	private String depositBankIfsc;

	@Column(name = "sDepositBankName", length = 20)
	private String depositBankName;

	@Column(name = "sDepositBankBranch", length = 20)
	private String depositBankBranch;

	@Column(name = "sUtrNo", length = 50)
	private String utrNo;
	
	@Column(name = "sUmrn", length = 50)
	private String umrn;
	
	@Column(name = "sColenderId", length = 20)
	private String colenderId;

	@Column(name = "dReceipt")
	@Temporal(TemporalType.DATE)
	private Date dtReceipt;

	@Column(name = "sDepositRefNo", length = 20)
	private String depositRefNo;

	@Column(name = "sCardHolderName")
	private String cardHolderName;

	@Column(name = "sIssuingBank", length = 20)
	private String issuingBank;

	@Column(name = "sUpiVpa", length = 20)
	private String upiVpa;

	@Column(name = "sCardType", length = 20)
	private String cardType;

	@Column(name = "sNclStatus", length = 20)
	private String nclStatus;
	
	@Column(name = "sPayAppliedYN", length = 2, columnDefinition = "varchar(2) default 'N' ")
	private String payAppliedYn="N";
	
	@Column(name = "sPayBounceYN", length = 2, columnDefinition = "varchar(2) default 'N' ")
	private String payBounceYn="N";
	
	@Column(name = "sPayMode", length = 20)
	private String payMode;
	
	@Column(name = "sMICRCode", length = 7)
	private String micrCode;
	
	@Column(name = "sClearingLocation", length = 20)
	private String clearingLocation;
	
	@Column(name = "sDepositBank", length = 20)
	private String depositBank;
	
	@Column(name = "sCollectionAgency", length = 20)
	private String collectionAgency;
	
	@Column(name = "sCollectedBy", length = 20)
	private String collectedBy;
	
	@Column(name = "sProvisionalReceipt ")
	private String provisionalReceipt ;
	
	@Column(name = "sProcLoc", length = 20)
	private String procLoc;
	
	@Column(name = "nTDSAmount")
	private Double tdsAmount = 0d;
		
	@Column(name = "dPayApplied")	
	@Temporal(TemporalType.DATE)
	private Date dtPayApplied;
	
	@Column(name = "dPayBounce")	
	@Temporal(TemporalType.DATE)
	private Date dtPayBounce;
	
	@Column(name = "dStatusUpdate")	
	@Temporal(TemporalType.DATE)
	private Date dtStatusUpdate;
	
	@Column(name = "dSettlementDate")
	@Temporal(TemporalType.DATE)
	private Date dtSettlementDate;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "instrument", fetch = FetchType.LAZY)
	private List<TrnInsInstrumentAlloc> instAlloc;

	/*@OneToOne(cascade = CascadeType.ALL, mappedBy = "instrument", fetch = FetchType.LAZY)
	private TrnInsBatchInstruments batchInst;*/
	
	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement masterAgr;*/
	

	/*@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore*/
	@Column(name = "masterAgr", length = 20)
	private String masterAgr;
}
