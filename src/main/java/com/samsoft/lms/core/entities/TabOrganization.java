package com.samsoft.lms.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "tab_organization")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabOrganization {

	@Id
	//@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name="sOrgID", length = 20)
	private String orgId;
	
	@Column(name="sOrgName")
	private String orgName;
	
	@Column(name="dBusiness")
	@Temporal(TemporalType.DATE)
	private Date dtBusiness;
	
	@Column(name="dLastUpdated")
	private String dtLastUpdated;
	
	@Column(name="sMaintenanceFlag")
	private String maintenanceFlag;
}
