package com.samsoft.lms.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
//@Table(name="v_mst_payrule_manager")
@Subselect("select * from v_mst_payrule_manager")
public class VMstPayRuleManager {

	@Id
	@Column(name = "sColumnName", length = 20)
	private String columnName;
	
	@Column(name = "sDecKey", length = 20)
	private String decKey;

}

