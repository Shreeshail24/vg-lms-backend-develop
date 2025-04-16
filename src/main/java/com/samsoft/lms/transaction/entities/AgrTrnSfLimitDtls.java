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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "agr_trn_sf_limit_dtls")
public class AgrTrnSfLimitDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nLimitTranID", length = 20)
	private Integer limitTranId;

	@Temporal(TemporalType.DATE)
	@Column(name = "dTranDate")
	private Date dtTranDate;

	@Column(name = "sTranType", length = 20)
	private String tranType;

	@Column(name = "nRefTranID", length = 20)
	private Integer refTranId;

	@Column(name = "nSactionedLimit", length = 20)
	private Double sactionedLimit;

	@Column(name = "nUtilizedLimitOpn", length = 20)
	private Double utilizedLimitOpn;

	@Column(name = "nWithdrawalAmount", length = 20)
	private Double withdrawalAmount;

	@Column(name = "nDepositAmount", length = 20)
	private Double depositAmount;

	@Column(name = "nUtilizedLimitCls", length = 20)
	private Double utilizedLimitCls;

	@Column(name = "nAvailableLimit", length = 20)
	private Double availableLimit;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@JsonIgnore
	private AgrMasterAgreement master;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nCustApplLimitID", nullable = false)
	@JsonIgnore
	private CustApplLimitSetup custLimit;

}
