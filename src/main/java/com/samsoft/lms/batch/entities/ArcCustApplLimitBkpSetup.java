package com.samsoft.lms.batch.entities;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "arc_cust_appl_limit_bkp_setup")
public class ArcCustApplLimitBkpSetup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nCustApplLimitBkpID", length = 20)
	private int custApplLimitBkpId;
	
	@Column(name = "nCustApplLimitID", length = 20)
	private int custApplLimitId;

	@Column(name = "sOriginationApplnNo", length = 20)
	private String originationApplnNo;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "sProductCode", length = 20)
	private String productCode;

	@Column(name = "nLimitSanctioned", length = 20)
	private double limitSanctioned;

	@Column(name = "nUtilizedLimit", length = 20)
	private double utilizedLimit;

	@Column(name = "nAvailableLimit", length = 20)
	private double availableLimit;

	@Temporal(TemporalType.DATE)
	@Column(name = "dLimitSanctioned")
	private Date dtLimitSanctioned;

	@Temporal(TemporalType.DATE)
	@Column(name = "dLimitExpired")
	private Date dtLimitExpired;

	@Column(name = "sPurpose", length = 200)
	private String purpose;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sBackUpId")
	private ArcAgrTrnBkpSummary bkpSummary;

}
