package com.samsoft.lms.transaction.entities;

import java.time.LocalDate;
import java.util.Date;

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

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_settlement_dtl")
public class AgrTrnSettlementDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nSeqID", length = 20)
	private Integer seqId;

	@Column(name = "sMasterAgrID", length = 20)
	private String masterAgrId;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;

	@Column(name = "sTranHead", length = 20)
	private String tranHead;

	@Column(name = "nDueAmount", length = 20)
	private Double dueAmount;

	@Column(name = "nTaxAmount", length = 20)
	private Double taxAmount;

	@Column(name = "nTotalAmount", length = 20)
	private Double totalAmount;

	@Column(name = "nPaymentAmount", length = 20)
	private Double paymentAmount;

	@Column(name = "nDeficitAmount", length = 20)
	private Double deficitAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nSettlementID", nullable = false)
	@JsonIgnore
	private AgrTrnSettlement settlementId;
}
