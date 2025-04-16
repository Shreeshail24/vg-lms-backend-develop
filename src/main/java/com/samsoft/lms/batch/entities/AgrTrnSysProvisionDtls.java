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

import org.joda.time.DateTime;

import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_sys_provision_dtls")
@Data
@NoArgsConstructor

public class AgrTrnSysProvisionDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nProvID", length = 20)
	private Integer provId;

	@Column(name = "dProvisionDate")
	@Temporal(TemporalType.DATE)
	private Date dtProvisionDate;

	@Column(name = "sAssetClassCd", length = 20)
	private String assetClassCd;

	@Column(name = "nOSPrincipal", length = 20)
	private Double oSPrincipal;

	@Column(name = "nOverDuePrincipal", length = 20)
	private Double overDuePrincipal;

	@Column(name = "nSecuredPrincipal", length = 20)
	private Double securedPrincipal;

	@Column(name = "nNonSecuredPrincipal", length = 20)
	private Double nonSecuredPrincipal;

	@Column(name = "nSecuredProvRate", length = 10)
	private Float securedProvRate;

	@Column(name = "nNonSecuredProvRate", length = 10)
	private Float nonSecuredProvRate;

	@Column(name = "nSecuredProvAmount", length = 20)
	private Double securedProvAmount;

	@Column(name = "nNonSecuredProvAmount", length = 20)
	private Double nonSecuredProvAmount;

	@Column(name = "nProvisionAmount", length = 20)
	private Double provisionAmount;

	@Column(name = "sNPAFlag", length = 2)
	private String npaFlag;

	@Column(name = "sRevFlag", length = 2)
	private String revFlag;

	@Column(name = "nDPD", length = 3)
	private Integer dpd;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement master;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sLoanId")
	private AgrLoans loan;
	
}
