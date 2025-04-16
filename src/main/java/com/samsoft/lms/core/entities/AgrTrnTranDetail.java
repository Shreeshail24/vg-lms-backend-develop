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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_tran_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AgrTrnTranDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nTranDtlId", length = 20)
	private Integer tranDtlId;

	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;

	@Column(name = "sTranHead", length = 20)
	private String tranHead;

	@Column(name = "nTranAmount", length = 20)
	private Double tranAmount = 0.0;

	@Column(name = "dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;

	@Column(name = "sTranSide", length = 20)
	private String tranSide;

	@Column(name = "nInstallmentNo", length = 3)
	private Integer installmentNo;

	@Column(name = "nRevAmount", length = 20)
	private Double revAmount = 0.0;

	@Column(name = "nTaxOnCharge", length = 20)
	private Double taxOnCharge = 0.0;

	@Column(name = "nTaxPaid", length = 20)
	private Double taxPaid = 0.0;

	@Column(name = "sDtlRemark", length = 200)
	private String dtlRemark;

	@Column(name = "nAvailableLimit", length = 20)
	private Double availableLimit = 0.0;

	@Column(name = "nUtilizedLimit", length = 20)
	private Double utilizedLimit = 0.0;

	@Column(name = "nRefTranDtlId", length = 20)
	private Integer refTranDtlId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	/*
	 * @OneToMany(cascade = CascadeType.ALL, mappedBy = "tranDetails", fetch =
	 * FetchType.LAZY) private List<AgrTrnDueDetails> tranDueDetails;
	 */

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nEventId")
	private AgrTrnEventDtl eventDtl;

	/*
	 * @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name="sMastAgrId") private AgrMasterAgreement masterAgr;
	 */

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sLoanId")
	private AgrLoans loan;

	/*
	 * @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name="nTranId") private AgrTrnTranHeader tranHeader;
	 */

}
