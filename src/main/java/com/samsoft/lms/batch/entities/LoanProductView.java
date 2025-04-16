package com.samsoft.lms.batch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="v_agr_tran_loan_data")
@Subselect("select * from v_agr_tran_loan_data")
public class LoanProductView {

	@Id
	@Column(name = "sLoanId", length = 20)
	private String loanId;
	
	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;
	
	@Column(name = "sStatus", length = 10)
	private String status;

	@Column(name = "nUnbilledPrincipal", length = 20)
	private Double unbilledPrincipal;

	@Column(name = "nInterestRate", length = 10)
	private Float interestRate;

	@Column(name = "sAssetClass", length = 20)
	private String assetClass;

	@Column(name = "nPenalInterestRate", length = 10)
	private Float penalInterestRate;

	@Column(name = "sInterestBasis", length = 20)
	private String interestBasis;
	
	@Column(name = "sPenalInterestBasis", length = 20)
	private String penalInterestBasis;

	@Column(name = "nDpd", length = 3)
	private Integer dpd;

	@Column(name = "nPenalCycleDay", length = 3)
	private Integer penalCycleDay;
	
	@Column(name = "nExcessAmount", length = 20)
	private Double excessAmount;
	
	@Column(name = "nCycleDay", length = 3)
	private Integer cycleDay;
	
	@Column(name = "nCurrentInstallmentNo", length = 5)
	private Integer currentInstallmentNo;

	@Column(name = "sLoanType", length = 20)
	private String loanType;

}
