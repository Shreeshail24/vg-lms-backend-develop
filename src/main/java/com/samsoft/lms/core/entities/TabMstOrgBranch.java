package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tab_mst_org_branch")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstOrgBranch {

	@Id
	@Column(name="sOrgBrID", length = 32)
	private String orgBrId;	

	@Column(name="sOrgBrName", length = 200)
	private String orgBrName;
	
	@Column(name="sState", length = 20)
	private String state;
	
	@Column(name="sPIN", length = 20)
	private String pin;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name="dUserDateTime")
	LocalDate dtUserDateTime = LocalDate.now();
	
	@Column(name="dLastUpdated")
	LocalDate dtLastUpdated = LocalDate.now();
}
