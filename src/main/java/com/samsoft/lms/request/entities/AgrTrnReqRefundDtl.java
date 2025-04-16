package com.samsoft.lms.request.entities;

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
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_req_refund_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnReqRefundDtl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nRefundID", length = 20)
	private Integer refundId;
	
	@Column(name = "sMastAgrID", length = 20)
	private String mastAgrId;
	
	@Column(name = "dRefund")
	@Temporal(TemporalType.DATE)
	private Date dtRefund;
	
	@Column(name = "nRefundAmount", length = 20)
	private Double refundAmount;
	
	@Column(name = "sReasonCode", length = 20)
	private String reasonCode;
	
	@Column(name = "sRemark", length = 20)
	private String remark;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade= CascadeType.ALL)
	@JoinColumn(name = "nReqId", nullable = false)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
