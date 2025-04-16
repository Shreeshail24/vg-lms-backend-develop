package com.samsoft.lms.transaction.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
//@Table(name="v_agr_trn_charges_history")
@Data
@NoArgsConstructor
@Subselect("select * from v_agr_trn_charges_history")
public class VAgrTrnChargesHistory {

	@Id
	@Column(name="nTranID")
	private Integer tranId;
	
	@Column(name="nWaivableAmount")
	private Double waivableAmount;
	
	@Column(name="nTranDtlId")
	private Integer tranDtlId;
	
	@Column(name="nDueDtlID")
	private Integer dueDtlId;
	
	@Column(name="sMastAgrId")
	private String mastAgrId;
	
	@Column(name="sTranType")
	private String tranType;
	
	@Column(name="sTranHead")
	private String tranHead;
	
	@Column(name="sLoanId")
	private String loanId;
	
	@Column(name="dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;
}
