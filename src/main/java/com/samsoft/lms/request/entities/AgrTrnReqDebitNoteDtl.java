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
@Table(name = "agr_trn_req_debit_note_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnReqDebitNoteDtl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nDrNoteID", length = 20)
	private Integer drNoteId;
	
	@Column(name = "sMastAgrID", length = 20)
	private String mastAgrId;
	
	@Column(name = "sLoanId", length = 20)
	private String loanId;
	
	@Column(name = "dDebitNote")
	@Temporal(TemporalType.DATE)
	private Date dtDebitNote;
	
	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;
	
	@Column(name = "nDebitNoteAmount", length = 20)
	private Double debitNoteAmount;
	
	@Column(name = "sDebitNoteHead", length = 20)
	private String debitNoteHead;
	
	@Column(name = "sReasonCode", length = 20)
	private String reasonCode;
	
	@Column(name = "sRemark", length = 20)
	private String remark;
	
	@Column(name = "nInstallmentNo", length = 20)
	private Integer installmentNo;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
