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
@Table(name = "tab_mst_pay_rule_manager")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstPayRuleManager {

	@Id
	@Column(name = "nPayRulManID", length = 20)
	private String payRulManId;

	@Column(name = "sDecKey1", length = 20)
	private String decKey1;

	@Column(name = "sDecKey2", length = 20)
	private String decKey2;
	
	@Column(name = "sDecKey3", length = 20)
	private String decKey3;

	@Column(name = "sDecKey4", length = 20)
	private String decKey4;
	
	@Column(name = "sDecKey5", length = 20)
	private String decKey5;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "payRuleManager")
	private List<TabMstPayRuleManagerDtl> payRuleManagerDtl;

}
