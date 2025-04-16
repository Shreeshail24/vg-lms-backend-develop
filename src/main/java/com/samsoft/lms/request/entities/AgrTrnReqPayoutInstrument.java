package com.samsoft.lms.request.entities;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_req_payout_instrument")
public class AgrTrnReqPayoutInstrument {

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

	@Column(name = "nIntrumentAmount", length = 20)
	private Double instrumentAmount;

	@Column(name = "sInstrumentStatus", length = 20)
	private String instrumentStatus;

	@Column(name = "sUTRNo", length = 20)
	private String utrNo;

	@Column(name = "sIFSCCode", length = 20)
	private String ifscCode;

	@Column(name = "dPayment")
	@Temporal(TemporalType.DATE)
	private Date dtPayment;

	@Column(name = "sDepositRefNo", length = 20)
	private String depositRefNo;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@Column(name = "sInstrumentLocation", length = 255)
	private String instrumentLocation;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
