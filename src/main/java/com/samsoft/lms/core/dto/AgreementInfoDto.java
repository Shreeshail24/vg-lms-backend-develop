package com.samsoft.lms.core.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementInfoDto {

	private String customerId;
	private String mastAgrId;
	private String agreementStatus;
	//private String addStatus;
	private String assetClass;
	private Integer dpd = 0;
	private String npaStatus;	
	private String homeBranch;
	private String servBranch;
	//private String preEmiyn;
	private String portfolioCode;
	private Double sanctionedAmount=0.0;
	private Double loanAmount=0.0;
	private Double overdueAmount=0.0;
	private Double outstandingAmount=0.0;
	private Double excessAmount=0.0;
	private String dtPrevInstallment;
	private String dtNextInstallment;
	private Double nextInstallmentAmount;
	private Double prevInstallmentAmount;
	private String productCode;
	private Double utilizedLimit=0.0;
	private Double availableLimit=0.0;
	private Double advanceEmiAmount=0.0;
	private String dtOdClosure;
	private String dtSanctionedDate;
	private String odYn;
	private Double accruedPenal=0.0;
	private Double accruedInterest=0.0;
	private Double totalReceivable=0.0;
	private List<String> colender;
	private Integer tenor;
	private Float interestRate;
	private String dropLineFreq;
	private String repayFreq;
	private Double currentDroppedLimit = 0.0;
	private String prefRepayMode;
	private String originationApplnNo;
	private Double sanctionedLimit;
	private String finalDisbursement;

}
