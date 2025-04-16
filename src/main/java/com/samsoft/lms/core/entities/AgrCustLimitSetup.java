package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="agr_cust_limit_setup")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgrCustLimitSetup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sLimitId", length = 20)
	private Integer limitId;
	
	@Column(name="sLoanId", length = 20)
	private String loanId;
	
	@Column(name="nUtilizedLimit", length = 20)
	private Double utilizedLimit=0.0;
	
	@Column(name="dLimitSanctioned")
	@Temporal(TemporalType.DATE)
	private Date dtLimitSanctioned;
		
	@Column(name="dLimitExpired")
	@Temporal(TemporalType.DATE)
	private Date dtLimitExpired;
	
	@Column(name="sPurpose", length = 20)
	private String purpose;
	
	@Column(name="nLimitSanctionAmount", length = 20)
	private Double limitSanctionAmount;
	
	@Column(name="nAvailableLimit", length = 20)
	private Double availableLimit;
	
	@Column(name="sUserId", length = 200)
	private String userId;
	
	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();

	@Column(name = "dltv")
	private Double ltv;
	
	/*@Column(name = "dUserDate")	
	private DateTime dtUserDate = new DateTime();*/
	
	
/*	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="sMastAgrId")
	private AgrMasterAgreement masterAgreement;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;
	
	
}
