package com.samsoft.lms.transaction.entities;

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
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_colen_due_dtl")
public class AgrTrnColenDueDtl {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nCoLenDueID", length=20)
	private Integer coLenDueId;

	@Column(name = "nCoLenderID", length=20)
	private Integer coLenderId;

	@Column(name = "sMastAgrID", length=20)
	private String mastAgrId;

	@Column(name = "sLoanID", length=20)
	private String loanId;

	@Column(name = "dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;

	@Column(name = "sDueCategory", length=50)
	private String dueCategory;

	@Column(name = "sDueHead", length=50)
	private String dueHead;

	@Column(name = "nDueAmount", length=20)
	private Double dueAmount;

	@Column(name = "nInstallmentNo", length=3)
	private Integer installmentNo;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "tranDtlId", nullable = false)
	@JsonIgnore
	private AgrTrnTranDetail tranDtl;

}
