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
@Table(name = "agr_disbursement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrDisbursement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sDisbID", length = 20)
	private Integer disbId;

	@Column(name = "nDisbSrNo", length = 20)
	private Integer disbSrNo;
	
	@Column(name = "dDisbDate")
	@Temporal(TemporalType.DATE)
	private Date dtDisbDate;
	
	@Column(name = "dInstallmentStartDate")
	@Temporal(TemporalType.DATE)
	private Date dtInstallmentStartDate;
	
	@Column(name = "nDisbAmount", length = 20)
	private Double disbAmount;
	
	@Column(name="sFinalDisbYN", length = 2)
	private String finalDisbYn;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name="sIfscCode", length = 12)
	private String ifscCode;
	
	@Column(name="sAccountNo", length = 20)
	private String accountNo;
	
	@Column(name="sPaymentMode", length = 10)
	private String paymentMode;
	
	@Column(name="sUtrNo", length = 20)
	private String utrNo;
	
	@Column(name = "dUserDateTime")	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")	
	private LocalDate dLastUpdated = LocalDate.now();
	
	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="sMastAgrID")
	private AgrMasterAgreement mastAgr;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement mastAgr;
	

}
