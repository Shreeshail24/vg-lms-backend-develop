package com.samsoft.lms.core.entities;

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_fee_param")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class AgrFeeParam {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sFeeParamId", length = 20)
	private Integer feeParamId;

	@Column(name = "sFeeCode", length = 20)
	private String feeCode;

	@Column(name = "sFeeEvent", length = 20)
	private String feeEvent;

	@Column(name = "nMinTenor", length = 5)
	private Integer minTenor;

	@Column(name = "nMaxTenor", length = 5)
	private Integer maxTenor;

	@Column(name = "nMinAmount", length = 20)
	private Double minAmount;

	@Column(name = "nMaxAmount", length = 20)
	private Double maxAmount;

	@Column(name = "nMinFee", length = 20)
	private Double minFee;	
	
	@Column(name = "nMaxFee", length = 20)
	private Double maxFee;
	
	@Column(name = "sFeeType", length = 20)
	private String feeType;
	
	@Column(name = "nFeePercentage", length = 10)
	private Float feePercentage;
	
	@Column(name = "nFeeAmount", length = 20)
	private Double feeAmount;
	
	@Column(name = "sTaxApplicatbleYN", length = 1)
	private String taxApplicatbleYN;
	
	@Column(name="sFeeAccountingBasis", length = 10)
	private String feeAccountingBasis;
	
	@Column(name="sPercentageBasis")
	private String percentageBasis;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	//@Temporal(TemporalType.DATE)
	private LocalDate dtLastUpdated =  LocalDate.now();

	@Column(name = "dUserDate")
	//@Temporal(TemporalType.DATE)
	private LocalDate dtUserDate =  LocalDate.now();

	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "mastAgrId")
	private AgrMasterAgreement masterAgreement;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;

}
