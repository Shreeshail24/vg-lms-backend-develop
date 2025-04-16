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

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_req_settlement_pay_sch")
public class AgrTrnReqSettlementPaySch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nSeqID", length = 20)
	private Integer seqId;

	@Column(name = "sMasterAgrID", length = 20)
	private String masterAgrId;

	@Column(name = "dPromiseDate", length = 20)
	private Date dtPromiseDate;

	@Column(name = "nPromiseAmount", length = 20)
	private double promiseAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nSettlementID", nullable = false)
	@JsonIgnore
	private AgrTrnReqSettlement settlementId;
}
