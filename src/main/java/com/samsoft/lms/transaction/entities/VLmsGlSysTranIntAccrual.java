package com.samsoft.lms.transaction.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Subselect("select * from v_lms_gl_sys_tran_int_accrual")
public class VLmsGlSysTranIntAccrual {

	@Id

	@Column(name = "ntranid")
	private Integer tranId;

	@Column(name = "sServBranch")
	private String servBranch;

	@Column(name = "sHomeBranch")
	private String homeBranch;

	@Column(name = "sPortfolioCode")
	private String portfolioCode;

	@Column(name = "sProductCode")
	private String productCode;

	@Column(name = "sCustomerId")
	private String customerId;

	@Column(name = "nGLGeneratedYN")
	private String glGeneratedYn;

	@Column(name = "formatDate")
	private String formatDate;

	@Column(name = "dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;

	@Column(name = "strantype")
	private String tranType;
	
	@Column(name = "sTranHead")
	private String tranHead;

	@Column(name = "sTranEvent")
	private String tranEvent;

	@Column(name = "sremark")
	private String remark;

	@Column(name = "smastagrid")
	private String mastAgrId;
	
	@Column(name="nTranDtlId")
	private Integer tranDtlId;

	@Column(name = "sTranCategory")
	private String tranCategory;

	@Column(name = "sUserId", length = 200)
	private String userId;

	@Column(name = "sLoanId")
	private String loanId;

	@Column(name = "nTranAmount")
	private Double tranAmount;

	@Column(name = "nEventId")
	private Integer eventId;
	
	@Column(name = "sNBFC")
	private String nbfc;

}
