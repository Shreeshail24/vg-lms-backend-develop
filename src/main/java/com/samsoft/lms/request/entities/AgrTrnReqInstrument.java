package com.samsoft.lms.request.entities;

import java.io.Serializable;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_req_instrument")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnReqInstrument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4951150459244231537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nInstrumetSrNo", length = 20)
	private Integer instrumetSrNo;

	@Column(name = "dInstrumentDate")
	@Temporal(TemporalType.DATE)
	private Date dtInstrumentDate;
	
	@Column(name = "sPayType", length = 20)
	private String payType;
	
	@Column(name = "sPayMode", length = 20)
	private String payMode;
	
	@Column(name = "sInstrumentType", length = 20)
	private String instrumentType;
	
	@Column(name = "sAccountNo", length = 20)
	private String accountNo;
	
	@Column(name = "sAccountType", length = 20)
	private String accountType;
	
	@Column(name = "sInstrumentNo", length = 20)
	private String instrumentNo;
	
	@Column(name = "sBankCode", length = 20)
	private String bankCode;
	
	@Column(name = "sBankBranchCode", length = 20)
	private String bankBranchCode;
	
	@Column(name = "sMICRCode", length = 7)
	private String micrCode;
	
	@Column(name = "sClearingLocation", length = 20)
	private String clearingLocation;
	
	@Column(name = "nInstrumentAmount", length = 20)
	private Double instrumentAmount = 0.0d;
	
	@Column(name = "sInstrumentStatus", length = 20)
	private String instrumentStatus;
	
	@Column(name = "sDepositBank", length = 20)
	private String depositBank;
	
	@Column(name = "sUTRNo", length = 20)
	private String utrNo;
	
	@Column(name = "sIFSCCode", length = 20)
	private String ifscCode;
	
	@Column(name = "dReceipt", length = 20)	
	private Date dtReceipt;
	
	@Column(name = "sDepositRefNo", length = 20)
	private String depositRefNo;
	
	@Column(name = "sCardHolderName", length = 20)
	private String cardHolderName;
	
	@Column(name = "sIssuingBank", length = 20)
	private String issuingBank;
	
	@Column(name = "sUPIVPA", length = 20)
	private String upiVpa;
	
	@Column(name = "sProcLoc", length = 20)
	private String procLoc;
	
	@Column(name = "sCardType", length = 20)
	private String cardType;
	
	@Column(name = "sNCLStatus", length = 20)
	private String nclStatus;
	
	@Column(name = "sCollectionAgency", length = 20)
	private String collectionAgency;
	
	@Column(name = "sCollectedBy", length = 20)
	private String collectedBy;
	
	@Column(name = "sProvisionalReceipt")
	private String provisionalReceipt ;
	
	@Column(name="nTdsAmount")
	private Double tdsAmount;
	
	@Column(name = "sUserID", length = 200)
	private String userId;
	
	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@Column(name = "sInstrumentLocation", length = 255)
	private String instrumentLocation;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "instrument", fetch = FetchType.LAZY)
	private List<AgrTrnReqInstrumentAllocDtl> instrumentAlloc;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
	
}
