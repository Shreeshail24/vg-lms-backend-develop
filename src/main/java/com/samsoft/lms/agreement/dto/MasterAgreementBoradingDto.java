package com.samsoft.lms.agreement.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MasterAgreementBoradingDto {

	// private String mastAgrId;
	private String customerId;
	private String portfolioCode;
	private String productCode;
	private String homeBranch;
	private String servBranch;
	private String homeState;
	private String servState;
	private String gstExempted;
	private String prefRepayMode;
	private String tdsCode;
	private Float tdsRate;
	private Double sanctionedAmount;
	private String originationApplnNo;
	private Integer tenor;
	private Float interestRate;
	private String repayFreq;
	private Integer cycleDay;

}
