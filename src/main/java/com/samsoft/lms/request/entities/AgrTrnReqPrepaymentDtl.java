package com.samsoft.lms.request.entities;

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

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="agr_trn_req_prepayment_dtl")
@Data
@NoArgsConstructor
public class AgrTrnReqPrepaymentDtl {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	
	@Column(name="nPrepayDtlID", length = 20)
	private int prepayDtlId;
	
	@Column(name="sMastAgrID", length = 20)
	private String mastAgrId;
	
	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name="dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;
	
	@Column(name="sDueCategory", length = 20)
	private String dueCategory;
	
	@Column(name="sDueHead", length = 10)
	private String dueHead;
	
	@Column(name="nDueAmount", length = 20)
	private double dueAmount;
	
	@Column(name="nInstallmentNo", length = 5)
	private int installmentNo;
	
	@Column(name="nTaxAmount", length = 20)
	private double taxAmount;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nPrepayID", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnReqPrepaymentHdr prepaymentHdr;
	
	
}
