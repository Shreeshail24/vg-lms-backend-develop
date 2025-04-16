package com.samsoft.lms.transaction.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_limit_dtls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnLimitDtls {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="nLimitTranID" , length=20)
	private Integer limitTranId;
	
	@Column(name="nTranAmount", length=20)
	private Double tranAmount;
	
	@Column(name="sTranType", length=3)
	private String tranType;
	
	@Column(name="sPurpose", length=20)
	private String purpose;
	
	@Column(name="nRefTranID", length=10)
	private Integer refTranId;
	
	@Column(name="nLimitSanctionAmount", length = 20)
	private Double limitSanctionAmount;
	
	@Column(name="nUtilizedLimit", length = 20)
	private Double utilizedLimit=0.0;
	
	@Column(name="nAvailableLimit", length = 20)
	private Double availableLimit;
	
	@Column(name = "nCurrentDroppedLimit", length = 20)
	private Double currentDroppedLimit = 0.0;
	
	@Column(name="sUserID", length=200)
	private String userId;
	
	@Column(name="dUserDateTime")
	private LocalDate dtUserDateTime = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;
	
	

}
