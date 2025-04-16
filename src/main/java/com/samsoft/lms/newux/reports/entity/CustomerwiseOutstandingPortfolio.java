package com.samsoft.lms.newux.reports.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "v_lms_rep_customerwise_outstanding_portfolio")
public class CustomerwiseOutstandingPortfolio {

	@Id
	@Column(name="sHomeBranch")
	private String homeBranch;
	
	@Column(name="sAssetClass")
	private String assetClass;
	
	@Column(name="TotalNoofCases")
	private Long totalNoofCases;
	
	@Column(name="TotLoanAmt")
	private Double totLoanAmt;
	
	@Column(name="TotalNoOfCasesBL")
	private Double totalNoOfCasesBL;
	
	@Column(name="TotLoanAmtBL")
	private Double totLoanAmtBL;
	
	@Column(name="TotOutStandingAmtBL")
	private Double totOutStandingAmtBL;
	
	@Column(name="TotalNoOfCasesAU")
	private Double totalNoOfCasesAU;
	
	@Column(name="TotLoanAmtAU")
	private Double totLoanAmtAU;
	
	@Column(name="TotOutStandingAmtAU")
	private Double totOutStandingAmtAU;
	
	@Column(name="TotalNoOfCasesOD")
	private Double totalNoOfCasesOD;
	
	@Column(name="TotLoanAmtOD")
	private Double totLoanAmtOD;
	
	@Column(name="TotOutStandingAmtOD")
	private Double totOutStandingAmtOD;
}
