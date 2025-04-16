package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_mst_pay_rule_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstPayRuleDtl {

	@Id
	@Column(name = "nSeqID", length = 20)
	private Integer seqId;

	@Column(name = "sPayHead", length = 20)
	private String payHead;

	@Column(name = "nPayGroupSeq", length = 10)
	private Integer payGroupSeq;
	
	@Column(name = "nDate1Seq", length = 10)
	private Integer date1Seq;
	
	@Column(name = "nDate2Seq", length = 10)
	private Integer date2Seq;
	
	@Column(name = "nSortSeq", length = 10)
	private Integer sortSeq;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="sPayRuleID")
	private TabMstPayRule payRule;

}
