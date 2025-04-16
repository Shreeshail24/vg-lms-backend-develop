package com.samsoft.lms.request.entities;

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

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_req_charges_waiver_dtl")
@Data
@NoArgsConstructor

public class AgrTrnReqChargesWaiverDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nChargeWaiveTranID", length = 20)
	private Integer chargeWaiveTranId;
	
	@Column(name = "nChargeBookTranID", length = 20)
	private Integer chargeBookTranId;

	@Column(name = "sMastAgrID" , length = 20)
	private String mastAgrId;

	@Column(name = "sLoanID" , length = 20)
	private String loanId;

	@Column(name = "dChargeWaiver")
	@Temporal(TemporalType.DATE)
	private Date dtChargeWaiver;

	@Column(name = "nChargeWaiveAmount", length = 20)
	private Double chargeWaiveAmount;
	
	@Column(name="sTranCategory", length = 10)
	private String tranCategory;
	
	@Column(name="sTranHead", length = 10)
	private String tranHead;
	
	@Column(name="nInstallmentNo", length = 3)
	private Integer installmentNo;

	@Column(name = "sReasonCode", length = 10)
	private String reasonCode;

	@Column(name = "sRemark", length = 20)
	private String remark;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dUserDateTime")
	@Temporal(TemporalType.DATE)
	private Date dtUserDateTime = new Date();

	@Column(name = "dLastUpdated")
	@Temporal(TemporalType.DATE)
	private Date dtLastUpdated = new Date();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqID", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
	

}
