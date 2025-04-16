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
@Table(name = "tab_mst_pay_rule_manager_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstPayRuleManagerDtl {

	@Id
	@Column(name = "nPayRulManDtlId", length = 20)
	private Integer payRulManDtlId;

	@Column(name = "sDec1Val", length = 20)
	private String dec1Val;

	@Column(name = "sDec2Val", length = 20)
	private String dec2Val;
	
	@Column(name = "sDec3Val", length = 20)
	private String dec3Val;

	@Column(name = "sDec4Val", length = 20)
	private String dec4Val;
	
	@Column(name = "sDec5Val", length = 20)
	private String dec5Val;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="nPayRulManID")
	private TabMstPayRuleManager payRuleManager;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="sPayRuleID")
	private TabMstPayRule payRule;
}
