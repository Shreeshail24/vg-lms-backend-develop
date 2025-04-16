package com.samsoft.lms.core.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.samsoft.lms.master.entity.Agency;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cust_appl_limit_setup")
public class CustApplLimitSetup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(name = "nSubventionPer", length = 10)
	private Float subventionPer;

	@Column(name = "nSubventionAmount", length = 20)
	private Double subventionAmount;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@Column(name = "sFullName", length = 200)
	private String fullName;

	@Column(name = "iagencyid", nullable = false)
	private Integer agencyId;

}
