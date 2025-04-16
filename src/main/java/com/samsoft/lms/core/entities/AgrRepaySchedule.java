package com.samsoft.lms.core.entities;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_repay_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgrRepaySchedule implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nRepaySchId", length = 20)
	private Integer repaySchId;

	@Column(name = "sMasterAgrId", length = 20)
	private String masterAgrId;

	@Column(name = "nInstallmentNo", length = 10)
	private Integer installmentNo;

	@Column(name = "dtInstallmentDate")
	@Temporal(TemporalType.DATE)
	private Date dtInstallment;

	@Column(name = "nOpeningPrincipal", length = 20)
	private Double openingPrincipal;

	@Column(name = "nPrincipalAmount", length = 20)
	private Double principalAmount;

	@Column(name = "nInterestAmount", length = 20)
	private Double interestAmount;

	@Column(name = "nBPIAmount", length = 20)
	private Double bpiAmount = 0.0;

	@Column(name = "nInstallmentAmount", length = 20)
	private Double installmentAmount;

	@Column(name = "nClosingPrincipal", length = 20)
	private Double closingPrincipal;

	@Column(name = "nInterestRate", length = 10)
	private Float interestRate;

	@Column(name = "sInterstBasis", length = 20)
	private String interestBasis;

	@Column(name = "sEMIType", length = 10)
	private String emiType;

	@Column(name = "nTDSAmount")
	private Double tdsAmount = 0d;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dPaymentDate")
	@Temporal(TemporalType.DATE)
	private Date dtPaymentDate;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDateTime")
	private LocalDate dtUserDateTime = LocalDate.now();

	/*
	 * @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "sLoanId") private AgrLoans loan;
	 */

	@Column(name = "sLoanId")
	private String loanId;

}
