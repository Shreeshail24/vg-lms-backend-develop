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
@Table(name = "agr_trn_writeoff_dtl")
public class AgrTrnWriteoffDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nWriteoffDtlID", length = 20)
	private Integer writeoffDtlId;

	@Column(name = "nSrNo", length = 20)
	private Integer srNo;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;

	@Column(name = "sTranHead", length = 20)
	private String tranHead;

	@Column(name = "nAmount", length = 20)
	private Double amount;

	@Column(name = "nTaxAmount", length = 20)
	private Double taxAmount;

	@Column(name = "nTotalAmount", length = 20)
	private Double totalAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nWriteoffID", nullable = false)
	@JsonIgnore
	private AgrTrnWriteoff writeoffId;
}

