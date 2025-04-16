package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_mst_pay_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstPayRule {

	@Id
	@Column(name = "sPayRuleID", length = 20)
	private String payRuleId;

	@Column(name = "sPayRuleDesc", length = 20)
	private String payRuleDesc;

	@Column(name = "sActive", length = 10)
	private String active;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "payRule")
	private List<TabMstPayRuleDtl> payRuleDtl;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "payRule")
	private List<TabMstPayRuleManagerDtl> payRuleManagerDtl;

}
