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
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.request.entities.AgrTrnReqSettlement;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_settlement")
public class AgrTrnSettlement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nSettlementID", length = 20)
	private Integer settlementId;

	@Column(name = "sMasterAgrID", length = 20)
	private String masterAgrId;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "dTranDate")
	private Date dtTranDate;

	@Column(name = "nTotalReceivableAmount", length = 20)
	private Double totalReceivableAmount;

	@Column(name = "nSettlementAmount", length = 20)
	private Double settlementAmount;

	@Column(name = "nDeficitAmount", length = 20)
	private Double deficitAmount;

	@Column(name = "sReasonCode", length = 20)
	private String reasonCode;

	@Column(name = "nExcessAmount", length = 20)
	private Double excessAmount;

	@Column(name = "sRemark", length = 200)
	private String remark;

	@Column(name = "sSettlementMode", length = 20)
	private String settlementMode;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nTranId", nullable = false)
	@JsonIgnore
	private AgrTrnTranHeader hdr;
}
