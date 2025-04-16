package com.samsoft.lms.request.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_req_restructure_dtl")
@Data
@NoArgsConstructor
public class AgrTrnReqRestructureDtl {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	
	@Column(name="nRestrctureID", length = 20)
	private int restrctureId;
	
	@Column(name="sMasterAgrID", length = 20)
	private String masterAgrId;
	
	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name="sTranType", length = 20)
	private String tranType;
	
	@Column(name="sTranHead", length = 20)
	private String tranHead;
	
	@Column(name="nDueAmount", length = 20)
	private double dueAmount;
	
	@Column(name="nTaxAmount", length = 20)
	private double taxAmount;
	
	@Column(name="nTotalOS", length = 20)
	private double totalOS;
	
	@Column(name="nWaiveAmount", length = 20)
	private double waiveAmount;
	
	@Column(name="nCapitalizeAmount", length = 20)
	private double capitalizeAmount;
	
	@Column(name="nBalanceAmount", length = 20)
	private double balanceAmount;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade= CascadeType.ALL)
	@JoinColumn(name = "nReqId", nullable = false)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
