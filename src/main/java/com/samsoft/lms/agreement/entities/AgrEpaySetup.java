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
import com.samsoft.lms.customer.entities.AgrCustomer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_epay_setup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrEpaySetup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sEpayID", length = 20)
	private Integer epayId;	

	@Column(name="sMandateStatus", length = 20)
	private String mandateStatus;
	
	@Column(name="sCustomerID", length = 20)
	private String customerId;
	
	@Column(name="sInstrumentType", length = 20)
	private String instrumentType;
	
	@Column(name="dFromDate")
	@Temporal(TemporalType.DATE)
	private Date dtFromDate;
	
	@Column(name="dToDate")
	@Temporal(TemporalType.DATE)
	private Date dtToDate;
	
	@Column(name="sBankCode", length = 20)
	private String bankCode;
	
	@Column(name="sBankBranchCode", length = 20)
	private String bankBranchCode;
	
	@Column(name="sAccountType", length = 20)
	private String accountType;
	
	@Column(name="sAccountNo", length = 20)
	private String accountNo;

	@Column(name = "sAccountHolderName")
	private String accountHolderName;
	
	@Column(name="nMaxCeilAmount", length = 20)
	private Double maxCeilAmount;
	
	@Column(name="nInstallmentAmount", length = 20)
	private Double installmentAmount;
	
	@Column(name="nMaxInstallmentAmount", length = 20)
	private Double maxInstallmentAmount;
	
	@Column(name="sMandateRefNo", length = 20)
	private String mandateRefNo;
	
	@Column(name="sDepositBank", length = 20)
	private String depositBank;
	
	@Column(name="sMandateType", length = 20)
	private String mandateType;
	
	@Column(name="sFrequency", length = 20)
	private String frequency;
	
	@Column(name="sIFSCCode", length = 20)
	private String ifscCode;
	
	@Column(name="sUTRNo", length = 20)
	private String utrNo;
	
	@Column(name="sIBAN", length = 20)
	private String iBan;
	
	@Column(name="sActive", length = 10)
	private String active;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")	
	private LocalDate dLastUpdated = LocalDate.now();

/*	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement mastAgreement;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement mastAgreement;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "custInternalId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrCustomer customer;
	
}
