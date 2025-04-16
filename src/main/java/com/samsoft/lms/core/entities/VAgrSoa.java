package com.samsoft.lms.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name="v_agr_soa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Subselect("select * from v_agr_soa")
public class VAgrSoa {

	@Id
	@Column(name="ntranid")
	private Integer tranId;
	
	@Column(name="dtrandate")
	private String dtTrandate;
	
	@Column(name="strantype")
	private String tranType;
	
	@Column(name="sTranEvent")
	private String tranEvent;
	
	@Column(name="sremark")
	private String remark;
	
	@Column(name="smastagrid")
	private String mastAgrId;
	
	@Column(name="nEventId")
	private Integer eventId;
	
	@Column(name="nTranAmount")
	private Double tranAmount;
	
	
}
