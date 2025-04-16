package com.samsoft.lms.transaction.entities;

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
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_prepayment_dtl")
@Data
@NoArgsConstructor
public class AgrTrnPrepaymentDtl {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "nPrepayID")
	private int prepayId;

	@Column(name = "sMastAgrID")
	private String mastAgrId;

	@Column(name = "sLoanID")
	private String loanId;

	@Column(name = "sTranType")
	private String tranType;

	@Column(name = "dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;

	@Column(name = "nUnbilledPrincipal")
	private double unbilledPrincipal;

	@Column(name = "sChangeFactor")
	private String changeFactor;

	@Column(name = "nDiscountAmount")
	private double discountAmount;

	@Column(name = "nBPIAmount")
	private double bpiAmount;

	@Column(name = "nTaxAmount")
	private double taxAmount;

	@Column(name = "sStatus")
	private String status;

	@Column(name = "sPrepayReason")
	private String prepayReason;

	@Column(name = "nExcessAmount")
	private double excessAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nTranId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnTranHeader tranHdr;

}
