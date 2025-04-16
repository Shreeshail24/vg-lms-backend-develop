package com.samsoft.lms.batch.entities;

import java.time.LocalDate;
import java.util.Date;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_bkp_loan_dtls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnBkpLoanDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sLoanBackUpId", length = 20)
	private int loanBackupId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "sLoanId", length = 20)
	private String loanId;

	@Column(name = "nCycleDay", length = 3)
	private Integer cycleDay;

	@Column(name = "nCurrentInstallmentNo", length = 10)
	private Integer currentInstallmentNo;

	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;

	@Column(name = "sLoanAdditionalStatus", length = 10)
	private String loanAdditionalStatus;

	@Column(name = "nUnbilledPrincipal", length = 20)
	private Double unbilledPrincipal;

	@Column(name = "nInterestRate", length = 10)
	private Float interestRate;

	@Column(name = "nPenalInterestRate", length = 10)
	private Float penalInterestRate;

	@Column(name = "sInterestBasis", length = 20)
	private String interestBasis;

	@Column(name = "sPenalInterestBasis", length = 20)
	private String penalInterestBasis;

	@Column(name = "sAssetClass", length = 20)
	private String assetClass;

	@Column(name = "nDPD", length = 5)
	private Integer dpd;

	@Column(name = "nPenalCycleDay", length = 5)
	private Integer penalCycleDay;

	@Column(name = "dLastPenalBooked")
	@Temporal(TemporalType.DATE)
	private Date dtLastPenalBooked;

	@Column(name = "nExcessAmount", length = 20)
	private Double excessAmount;

	@Column(name = "nTotalDues", length = 20)
	private Double totalDues = 0.0d;

	@Column(name = "sErrorFlag", length = 20)
	private String errorFlag;

	@Column(name = "sLoanType", length = 20)
	private String loanType;

	@Column(name = "dError")
	@Temporal(TemporalType.DATE)
	private Date dtError;

	@Column(name = "dLastUpdated")

	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")

	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sBackUpId")
	private AgrTrnBkpSummary bkpSummary;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bkpLoanDetails", fetch = FetchType.LAZY)
	private List<AgrTrnBkpDueDetails> bkpDueDetails;
}
