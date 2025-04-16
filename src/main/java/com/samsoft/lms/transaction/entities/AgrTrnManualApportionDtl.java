package com.samsoft.lms.transaction.entities;

import java.io.Serializable;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agr_trn_manual_apportion_dtl")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnManualApportionDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8366264007923556952L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nManApportionID", length = 20)
	private Integer manApportionId;

	@Column(name = "nSrNo", length = 20)
	private Integer srNo;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;

	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;

	@Column(name = "sTranHead", length = 20)
	private String tranHead;

	@Column(name = "nApportionAmount", length = 20)
	private Double apportionAmount = 0.0d;

	@Column(name = "nInstallmentNo", length = 20)
	private Integer installmentNo;

	@Column(name = "nChargeBookTranID", length = 20)
	private Integer chargeBookTranId;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	@Temporal(TemporalType.DATE)
	private Date dtLastUpdated = new Date();

	@Column(name = "dUserDate")
	@Temporal(TemporalType.DATE)
	private Date dtUserDate = new Date();

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nAllocID")
	private AgrTrnInstrumentAllocDtl instrumentAlloc;

}
