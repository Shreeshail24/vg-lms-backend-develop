package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_loans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrLoans {

	@Id
	@Column(name = "sLoanId", length = 20)
	private String loanId;

	@Column(name = "customerId", length = 20)
	private String customerId;

	@Column(name = "sLoanAdditionalStatus")
	private String loanAdditionalStatus;

	@Column(name = "nCycleDay", length = 3)
	private Integer cycleDay;
	
	@Column(name = "nCurrentInstallmentNo", length = 10, columnDefinition = "integer default 1")
	private Integer currentInstallmentNo=1;

	@Column(name = "sRepayFreq", length = 20)
	private String repayFreq;

	@Column(name = "sAssetClass", length = 20)
	private String assetClass;

	@Column(name = "nDpd", length = 3)
	private Integer dpd = 0;

	@Column(name = "nTenor", length = 3)
	private Integer tenor;

	@Column(name = "sTenorUnit", length = 20)
	private String tenorUnit;

	@Column(name = "nUnbilledPrincipal", length = 20)
	private Double unbilledPrincipal = 0.0;

	@Column(name = "nInterestRate", length = 20)
	private Float interestRate = 0.0f;

	@Column(name = "sInterestType", length = 20)
	private String interestType;

	@Column(name = "sIndexCode", length = 20)
	private String indexCode;

	@Column(name = "nIndexRate", length = 10)
	private Float indexRate = 0.0f;

	@Column(name = "nOffsetRate", length = 10)
	private Float offsetRate = 0.0f;

	@Column(name = "nSpreadRate", length = 10)
	private Float spreadRate = 0.0f;

	@Column(name = "nPenalCycleDay", length = 10)
	private Integer penalCycleDay;

	@Column(name = "nTenorStartDate")
	@Temporal(TemporalType.DATE)
	private Date dtTenorStartDate;

	@Column(name = "nTenorEndDate")
	@Temporal(TemporalType.DATE)
	private Date dtTenorEndDate;

	@Column(name = "dLastDisbDate")
	@Temporal(TemporalType.DATE)
	private Date dtLastDisbDate;

	@Column(name = "sFinalDisbursement", length = 20)
	private String finalDisbursement;

	@Column(name = "sPreEMIYN", length = 10)
	private String preEmiyn;

	@Column(name = "sEmiOnSanctionedAmount", length = 10)
	private String emiOnSanctionedAmount;

	@Column(name = "sLoanType", length = 20)
	private String loanType;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "loanAmount", length = 20)
	private Double loanAmount = 0.0;
	
	@Column(name = "nNoOfAdvEMI", length = 3)
	private Integer noOfAdvEmi;

	@Column(name = "sAdvEMIFlag", length = 10, columnDefinition = "varchar(20) default 'S'")
	private String sAdvEMIFlag;
	
	@Column(name = "nTotalDues", length = 20)
	private Double totalDues = 0.0;
	
	@Column(name = "nTaxDues", length = 20)
	private Double taxDues = 0.0;
	
	@Column(name = "nPrincipalDues", length = 20)
	private Double principalDues = 0.0;	
	
	@Column(name = "nInterestDues", length = 20)
	private Double interestDues = 0.0;
	
	@Column(name = "nPenalDues", length = 20)
	private Double penalDues = 0.0;
	
	@Column(name = "nOtherChargesDues", length = 20)
	private Double otherChargesDues = 0.0;
	
	@Column(name = "nOtherTaxDues", length = 20)
	private Double otherTaxDues = 0.0;
	
	@Column(name = "nBalTenor", length = 3)
	private Integer balTenor;
	
	@Column(name = "nLastProvID", length = 3)
	private Integer lastProvId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();

	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement masterAgreement;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loans", fetch = FetchType.LAZY)
	@JsonIgnore
	List<AgrRepayVariation> repayVariation;
	
}
