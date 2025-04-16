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
import lombok.ToString;

@Entity
@Table(name = "agr_repay_variation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"loans"})
public class AgrRepayVariation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sRepayVarID", length = 20)
	private Integer repayVarId;

	@Column(name = "nSrNo", length = 10)
	private Integer srNo;
	
/*	@Column(name = "dDisbDate")
	
	private Integer dtDisbDate;
	
	@Column(name = "nDisbAmount", length = 20)
	private Double disbAmount;*/
	
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
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")	
	private LocalDate dLastUpdated = LocalDate.now();
		
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="sLoanID")
	private AgrLoans loans;
	

}
