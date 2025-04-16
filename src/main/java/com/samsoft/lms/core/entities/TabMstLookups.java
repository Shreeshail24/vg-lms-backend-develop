package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tab_mst_lookup")
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class TabMstLookups {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nLookupId", length = 20)
	private Integer lookupId;

	@Column(name = "sLookupType", length = 20)
	private String lookupType;

	@Column(name = "sCode", length = 20)
	private String code;

	@Column(name = "sDescription")
	private String description;

	@Column(name = "sActive", columnDefinition = "varchar(255) default 'Y' ", length = 10)
	private String active;

	@Column(name = "sUserID", length = 200)
	private String userId;

	public Integer getLookupId() {
		return lookupId;
	}

	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public String getLookupType() {
		return lookupType;
	}

	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

}
