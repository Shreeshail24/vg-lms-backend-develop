package com.samsoft.lms.request.entities;

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

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_req_est_repay_schedule")
@Data
@NoArgsConstructor
public class AgrTrnReqEstRepaySchedule {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	
	@Column(name="nSchID", length = 20)
	private int schId;

	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name="nInstallmentNo", length = 10)
	private int installmentNo;
	
	@Column(name="dInstallmentDate", length = 10)
	private Date dtInstallmentDate;
	
	@Column(name="nOpeningPrincipal", length = 10)
	private double openingPrincipal;
	
	@Column(name="nPrincipalAmount", length = 20)
	private double principalAmount;
	
	@Column(name="nInterestAmount", length = 20)
	private double interestAmount;
	
	@Column(name="nBPIAmount", length = 20)
	private double bpiAmount;
	
	@Column(name="nInstallmentAmount", length = 20)
	private double installmentAmount;
	
	@Column(name="nClosingPrincipal", length = 20)
	private double closingPrincipal;
	
	@Column(name="nInterestRate", length = 20)
	private float interestRate;
	
	@Column(name="sInterestBasis", length = 20)
	private String interestBasis;
	
	@Column(name="sIntAccrualFreq", length = 20)
	private String intAccrualFreq;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade= CascadeType.ALL)
	@JoinColumn(name = "nReschID", nullable = false)
	@JsonIgnore
	private AgrTrnReqRescheduleDtl rescheduleDtl;
}
