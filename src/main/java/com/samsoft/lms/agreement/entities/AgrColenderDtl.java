package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_colender_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrColenderDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nCoLendID")
	private Integer coLendId;

	@Column(name = "sColenderCode", length = 20)
	private String colenderCode;

	@Column(name = "nPrincipalShare", length = 20)
	private Integer principalShare;

	@Column(name = "nInterestShare", length = 20)
	private Integer interestShare;

	@Column(name = "sColenderApplNo", length = 20)
	private String colenderApplNo;

	@Column(name = "sColenderAgrNo", length = 20)
	private String colenderAgrNo;

	@Column(name = "sColenderCustID", length = 20)
	private String colenderCustId;
	
	@Column(name = "nTotalDues", length = 20)
	private Double totalDues=0.0;
	
	@Column(name = "nPrincipalDues", length = 20)
	private Double principalDues=0.0;
	
	@Column(name = "nInterestDues", length = 20)
	private Double interestDues=0.0;
	
	@Column(name = "nChargesDues", length = 20)
	private Double chargesDues=0.0;
	
	@Column(name = "nPeanlDues", length = 20)
	private Double peanlDues=0.0;
	
	@Column(name = "nOtherChargesDues", length = 20)
	private Double otherChargesDues=0.0;

	@Column(name = "sInstrumentPresenterYn", length = 1, columnDefinition  = "varchar(1) default 'Y'" )
	private String instrumentPresenterYn;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dUserDateTime")
	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")
	
	private LocalDate dLastUpdated = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "colender", fetch = FetchType.LAZY)
	private List<AgrColenderIncShareDtl> colenderShare;

/*	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement mastAgr;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;
}
