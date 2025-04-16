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

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_req_prepayment_hdr")
@Data
@NoArgsConstructor
public class AgrTrnReqPrepaymentHdr {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	
	@Column(name="nPrepayID", length = 20)
	private int prepayId;
	
	@Column(name="sMastAgrID", length = 20)
	private String mastAgrId;
	
	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name="sTranType", length = 20)
	private String tranType;
	
	@Column(name="dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;
	
	@Column(name="nUnbilledPrincipal", length = 20)
	private double unbilledPrincipal;
	
	@Column(name="sChangeFactor", length = 20)
	private String changeFactor;
	
	@Column(name="nDiscountAmount", length = 20)
	private double discountAmount;
	
	@Column(name="nBPIAmount", length = 20)
	private double bpiAmount;
	
	@Column(name="sPrepayCharge", length = 20)
	private double prepayCharge;
	
	@Column(name="nTaxAmount", length = 20)
	private double taxAmount;
	
	@Column(name="sStatus", length = 20)
	private String status;
	
	@Column(name="sPrepayReason", length = 20)
	private String prepayReason;
	
	@Column(name="nExcessAmount", length = 20)
	private double excessAmount;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
	
	
}
