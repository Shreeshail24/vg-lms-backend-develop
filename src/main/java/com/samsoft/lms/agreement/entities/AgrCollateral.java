package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_collateral")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrCollateral {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sColtrlID", length = 20)
	private Integer coltrlId;	

	@Column(name="sColtrlType", length = 20)
	private String coltrlType;
	
	@Column(name="sInsuranceReqYN", length = 5)
	private String insuranceReqYn;
	
	@Column(name="dValuation")
	@Temporal(TemporalType.DATE)
	private Date dtValuation;
	
	@Column(name="dCreation")
	@Temporal(TemporalType.DATE)
	private Date dtCreation;
	
	@Column(name="sStatus", length = 20)
	private String status;
	
	@Column(name="sHandoverYN", length = 10)
	private String handoverYN;
	
	@Column(name="sValuationFreq", length = 20)
	private String valuationFreq;
	
	@Column(name="sServBranch", length = 20)
	private String servBranch;
	
	@Column(name="nColtrlValue", length = 20)
	private Double coltrlValue;
	
	@Column(name="sReleaseNote", length = 20)
	private String releaseNote;
	
	@Column(name="sReleaseReason", length = 20)
	private String releaseReason;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")
	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")
	
	private LocalDate dLastUpdated = LocalDate.now();

	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sMastAgrId")
	private AgrMasterAgreement mastAgr;*/
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement mastAgr;

	
}
