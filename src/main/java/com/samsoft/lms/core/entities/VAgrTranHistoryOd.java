package com.samsoft.lms.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="v_agr_tran_history_od")
@Subselect("select * from v_agr_tran_history_od")
public class VAgrTranHistoryOd {

	@Id	
	@Column(name="nTranDtlId")
	private Integer tranDtlId;
	
	@Column(name = "nTranId", length = 20)
	private Integer tranId;
	
	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;
	
	@Column(name = "dTranDate")
	@Temporal(TemporalType.DATE)
	private Date dtTranDate;

	@Column(name = "sTranType", length = 20)
	private String tranType;	
	
	@Column(name = "sRemark", length = 20)
	private String remark;
	
	@Column(name = "formatDate", length = 20)
	private String formatDate;

	@Column(name = "Withdrawal", length = 20)
	private Double withdrawal;

	@Column(name = "Deposit", length = 20)
	private Double deposit;
	
	@Column(name = "nSanctionedLimit", length = 20)
	private Double sanctionedLimit;

	@Column(name = "nUtilizedLimit", length = 20)
	private Double utilizedLimit;
	
	@Column(name = "nAvailableLimit", length = 20)
	private Double availableLimit;


}

