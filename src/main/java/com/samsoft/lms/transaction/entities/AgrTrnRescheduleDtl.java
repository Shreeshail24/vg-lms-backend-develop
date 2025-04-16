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
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_reschedule_dtl")
@Data
@NoArgsConstructor
public class AgrTrnRescheduleDtl {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id	
	@Column(name="nReschID", length = 20)
	private int reschId;
	
	@Column(name="sMasterAgrID", length = 20)
	private String masterAgrId;
	
	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name="dTranDate", length = 20)
	private Date dtTranDate;
	
	@Column(name="sRestructureReason", length = 20)
	private String restructureReason;
	
	@Column(name="sRestructureRemark", length = 20)
	private String  restructureRemark;
	
	@Column(name="nNetReceivable", length = 20)
	private double netReceivable;
	
	@Column(name="nDREAmount", length = 20)
	private double dreAmount;
	
	@Column(name="nOldUnbilledPrincipal", length = 20)
	private double oldUnbilledPrincipal;
	
	@Column(name="sOldAssetClass", length = 20)
	private String oldAssetClass;
	
	@Column(name="nOldCycleDay", length = 20)
	private int oldCycleDay;
	
	@Column(name="sOldRepayFrequency", length = 20)
	private String oldRepayFrequency;
	
	@Column(name="nOldInterestRate", length = 20)
	private float oldInterestRate;
	
	@Column(name="nOldIndexRate", length = 20)
	private float oldIndexRate;
	
	@Column(name="nOldSpreadRate", length = 20)
	private float oldSpreadRate;
	
	@Column(name="nOldOffsetRate", length = 20)
	private float oldOffsetRate;
	
	@Column(name="nOldTenor", length = 20)
	private int oldTenor;
	
	@Column(name="nOldInstallmentAmount", length = 20)
	private double oldInstallmentAmount;
	
	@Column(name="nNewFinanceAmt", length = 20)
	private double newFinanceAmt;
	
	@Column(name="sNewAssetClass", length = 20)
	private String newAssetClass;
	
	@Column(name="nNewCycleDay", length = 20)
	private int newCycleDay;
	
	@Column(name="sNewRepayFrequency", length = 20)
	private String newRepayFrequency;
	
	@Column(name="nNewInterestRate", length = 20)
	private double newInterestRate;
	
	@Column(name="nNewIndexRate", length = 20)
	private float newIndexRate;
	
	@Column(name="nNewSpreadRate", length = 20)
	private float newSpreadRate;
	
	@Column(name="nNewOffsetRate", length = 20)
	private float newOffsetRate;
	
	@Column(name="nNewTenor", length = 20)
	private int newTenor;
	
	@Column(name="dNewInstStartDate", length = 20)
	private Date dtNewInstStartDate;
	
	@Column(name="nNewInstallmentAmount", length = 20)
	private double newInstallmentAmount;
	
	@Column(name="sTranType", length = 20)
	private String tranType;
	
	@Column(name="sChangeFactor", length = 20)
	private String changeFactor;
	
	@Column(name="nBPIAmount", length = 20)
	private double bpiAmount;
	
	@Column(name="dBPIDueDate", length = 20)
	private Date dtBpiDueDate;
	
	@Column(name="sReason", length = 20)
	private String reason;
		
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade= CascadeType.ALL)
	@JoinColumn(name = "nTranID", nullable = false)
	@JsonIgnore
	private AgrTrnTranHeader tranHdr;
}
