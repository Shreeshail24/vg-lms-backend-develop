package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_pdc_setup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrPdcSetup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sPDCID", length = 20)
	private Integer pdcId;
	
	@Column(name="nSrNo", length = 20)
	private Integer srNo;
	
	@Column(name="dInstrumentDate")
	@Temporal(TemporalType.DATE)
	private Date dtInstrumentDate;
	
	@Column(name="sBankCode", length = 20)
	private String bankCode;
	
	@Column(name="sBankBranchCode", length = 20)
	private String bankBranchCode;
	
	@Column(name="sAccountNo", length = 20)
	private String accountNo;
	
	@Column(name="sAccountType", length = 20)
	private String accountType;
	
	@Column(name="sInstrumentNo", length = 20)
	private String instrumentNo;
	
	@Column(name="nIntrumentAmount", length = 20)
	private Double intrumentAmount;
	
	@Column(name="sIFSCCode", length = 20)
	private String ifscCode;
	
	@Column(name="sMICRCode", length = 20)
	private String micrCode;
	
	@Column(name="sClearingLocation", length = 20)
	private String clearingLocation;
	
	@Column(name="sInstrumentStatus", length = 20)
	private String instrumentStatus;
	
	@Column(name="sDepositBank", length = 20)
	private String depositBank;
	
	@Column(name="dReceipt")
	@Temporal(TemporalType.DATE)
	private Date dtReceipt;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")
	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")
	
	private LocalDate dLastUpdated = LocalDate.now();

	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement mastAgr;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;
	
}
