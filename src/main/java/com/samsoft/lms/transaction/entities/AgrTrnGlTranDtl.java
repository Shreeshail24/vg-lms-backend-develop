package com.samsoft.lms.transaction.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_gl_tran_dtl")
@Data
@NoArgsConstructor

public class AgrTrnGlTranDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nGLTranID", length = 20)
	private Integer glTranId;

	@Column(name = "nTranDtlID", length = 20)
	private Integer tranDtlId;

	@Column(name = "nEventId", length = 20)
	private Integer eventId;

	@Column(name = "nGLTranSrNo", length = 20)
	private Integer glTranSrNo;

	@Column(name = "sGLHeadCode", length = 50)
	private String glHeadCode;

	@Column(name = "sDrCr", length = 2)
	private String drCr;

	@Column(name = "sServBranch", length = 20)
	private String servBranch;

	@Column(name = "sPortfolioCode", length = 20)
	private String portfolioCode;

	@Column(name = "sProdCode", length = 20)
	private String prodCode;

	@Column(name = "sDownloadYN", columnDefinition = "varchar(2) default 'N' ")
	private String downloadYn;

	@Temporal(TemporalType.DATE)
	@Column(name = "dDownloadDt")
	private Date dtDownloadDt;

	@Column(name = "sDownloadRefNo", length = 20)
	private String downloadRefNo;

	@Column(name = "sNarration", length = 100)
	private String narration;

	@Column(name = "nTranAmount", length = 20)
	private Double tranAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "dValueDate")
	private Date dtValueDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "dTranDate")
	private Date dtTranDate;

	@Column(name = "sCustomerID", length = 20)
	private String customerId;

	@Column(name = "sMasterAgrID", length = 20)
	private String masterAgrId;

	@Column(name = "sVoucherType", length = 20)
	private String voucherType;

	@Column(name = "sGLEvent", length = 100)
	private String glEvent;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "sHomeBranch", length = 20)
	private String homeBranch;

	@Column(name = "sUserID", length = 200)
	private String userID;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

}
