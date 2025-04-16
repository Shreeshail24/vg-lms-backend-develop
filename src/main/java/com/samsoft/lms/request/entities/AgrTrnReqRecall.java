package com.samsoft.lms.request.entities;

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

@Entity
@Table(name = "agr_trn_req_recall")
@Data
@NoArgsConstructor
public class AgrTrnReqRecall {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nRecallID", length = 20)
	private Integer recallId;

	@Column(name = "sMasterAgrId", length = 20)
	private String masterAgrId;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "dTranDate")
	private Date dtTranDate;

	@Column(name = "sPortfolioCode", length = 20)
	private String portfolioCode;

	@Column(name = "sRecallStatus", length = 20)
	private String recallStatus;

	@Column(name = "sRemark", length = 200)
	private String remark;

	@Column(name = "nOutstandingAmount", length = 20)
	private Double outstandingAmount;

	@Column(name = "nBPIAmount", length = 20)
	private Double bpiAmount;

	@Column(name = "nTotalOutstandingAmount", length = 20)
	private Double totalOutstandingAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nReqId", nullable = false)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
