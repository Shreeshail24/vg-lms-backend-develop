package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.agreement.entities.AgrRepayVariation;
import com.samsoft.lms.agreement.services.AgreementService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sProdDtlId", length = 20)
	private int prodDtlId;

	@Column(name = "sProdCode", length = 20)
	private String prodCode;

	@Column(name = "sProdType", length = 20)
	private String prodType;

	@Column(name = "sInterestBasis", length = 20)
	private String interestBasis;

	@Column(name = "sEmiBasis", length = 20)
	private String emiBasis;

	@Column(name = "sBPITreatmentFlag", length = 20)
	private String bpiTreatmentFlag;

	@Column(name = "sInterestType", length = 20)
	private String interestType;

	@Column(name = "sInterestAccrualFrequ", length = 20)
	private String interestAccrualFrequ;

	@Column(name = "sAmortizationMethod", length = 20)
	private String amortizationMethod;

	@Column(name = "sAmortizationType", length = 20)
	private String amortizationType;

	@Column(name = "sCompoundingFreq", length = 20)
	private String compoundingFreq;

	@Column(name = "sEmiRounding", length = 20)
	private String emiRounding;

	@Column(name = "sLastRowEMIThreshold", length = 20)
	private Float lastRowEMIThreshold = 0.0f;

	@Column(name = "nPenalInterestRate", length = 20)
	private Float penalInterestRate = 0.0f;

	@Column(name = "sPenalInterestBasis", length = 20)
	private String penalInterestBasis;

	@Column(name = "sPenalAccountingBasis", length = 20)
	private String penalAccountingBasis;

	@Column(name = "nGraceDays", length = 3)
	private Integer graceDays;

	@Column(name = "nReschLockinPeriod", length = 3)
	private Integer reschLockinPeriod;

	@Column(name = "nPrepayAfterInstNo", length = 3)
	private Integer prepayAfterInstNo;

	@Column(name = "nPrepayBeforeInstNo", length = 3)
	private Integer prepayBeforeInstNo;

	@Column(name = "nMinInstallmentGapBetPrepay", length = 3)
	private Integer minInstallmentGapBetPrepay;

	@Column(name = "nMinPrepayAmount", length = 20)
	private Double minPrepayAmount = 0.0;

	@Column(name = "nForecloseAfterInstNo", length = 20)
	private Integer forecloseAfterInstNo;

	@Column(name = "nForecloseBeforeInstaNo", length = 20)
	private Integer forecloseBeforeInstaNo;

	@Column(name = "nMinTenor", length = 20)
	private Integer minTenor;

	@Column(name = "nMaxTenor", length = 20)
	private String maxTenor;

	@Column(name = "nMinInstallmentAmount", length = 20)
	private Double minInstallmentAmount = 0.0d;

	@Column(name = "nMaxInstallmentAmount", length = 20)
	private Double maxInstallmentAmount = 0.0d;

	@Column(name = "nMinInterestRate", length = 20)
	private Float minInterestRate = 0.0f;

	@Column(name = "nMaxInterestRate", length = 20)
	private Float maxInterestRate = 0.0f;

	@Column(name = "sDropLineODYN", length = 20)
	private String dropLineODYN;

	@Column(name = "nDropLinePerc", length = 20)
	private Float dropLinePerc = 0.0f;

	@Column(name = "sDropMode", length = 20)
	private String dropMode;

	@Column(name = "nDropLineAmount", length = 20)
	private Double dropLineAmount = 0.0d;

	@Column(name = "nDropLineCycleDay", length = 2)
	private Integer dropLineCycleDay;

	@Column(name = "sDropLIneFreq", length = 20)
	private String dropLIneFreq;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	// Supplier Finance Changes Start
	
	@Column(name = "nSubventionPer", length = 10)
	private Float subventionPer;

	@Column(name = "nSubventionAmount", length = 20)
	private Double subventionAmount;
	
	// Supplier Finance Changes End

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
	@JsonIgnore
	List<AgrProdSlabwiseInterest> slabwiseInterest;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;

}
