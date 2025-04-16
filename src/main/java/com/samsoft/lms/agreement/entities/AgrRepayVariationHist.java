package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;

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

import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_repay_variation_hist")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrRepayVariationHist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nSrNo", length = 10)
	private Integer srNo;
	
	@Column(name = "nSeqNo", length = 10)
	private Integer seqNo;
	
	@Column(name = "sRepayVarID", length = 20)
	private Integer repayVarId;
	
	@Column(name = "sLoanId", length = 20)
	private String loanId;
	
	@Column(name="sVariationType", length = 20)
	private String variationType;
	
	@Column(name="nFromInstallmentNo", length = 20)
	private int fromInstallmentNo;
	
	@Column(name = "nNoofInstallments", length = 2)
	private Integer noOfInstallments;
	
	@Column(name="sVariationOption", length = 20)
	private String variationOption;
	
	@Column(name="sVariationValue", length = 20)
	private double variationValue;
	
	@Column(name="sAdjustmentFactor", length = 20)
	private String adjustmentFactor;
	
	@Column(name="nBallonPay", length = 20)
	private String ballonPay;
		
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")	
	private LocalDate dLastUpdated = LocalDate.now();

	

}
