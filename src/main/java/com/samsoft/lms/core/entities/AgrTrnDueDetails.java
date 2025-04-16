package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.Date;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_trn_due_dtl")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class AgrTrnDueDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nDueDtlId", length = 20)
	private Integer dueDtlId;

	@Column(name = "sLoanId", length = 20)
	private String loanId;

	@Column(name = "dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;

	@Column(name = "sDueCategory", length = 20)
	private String dueCategory;

	@Column(name = "sDueHead", length = 20)
	private String dueHead;

	@Column(name = "nDueAmount", length = 20)
	private Double dueAmount;

	@Column(name = "nInstallmentNo", length = 5)
	private Integer installmentNo;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "dueDetail", fetch = FetchType.LAZY)
	private List<AgrTrnTaxDueDetails> taxDueDetails;

	/*@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "nTranDtlId")
	private AgrTrnTranDetail tranDetails;*/
	
	@Column(name = "nTranDtlId")
	private Integer tranDtlId;

	/*@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;*/
	
	@Column(name = "sMastAgrId")	
	private String mastAgrId;

}
