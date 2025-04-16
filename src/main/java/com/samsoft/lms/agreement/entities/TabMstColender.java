package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="tab_mst_colender")
@Data
@NoArgsConstructor
@ToString
public class TabMstColender {

	@Id
	@Column(name="nColenderID", length = 10)
	private Integer colenderId;
	
	@Column(name="sColenderName", length = 100)
	private String colenderName;
	
	@Column(name="sColenderAddr", length = 200)
	private String sColenderAddr;
	
	@Column(name="sUrl", length = 100)
	private String url;
	
	@Column(name="sShortname ", length = 10)
	private String shortname;
	
	@Column(name="sHomebranch  ", length = 10)
	private String homebranch;
	
	@Column(name="iCityCode ", length = 5)
	private Integer cityCode ;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name = "dUserDateTime")	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")	
	private LocalDate dLastUpdated = LocalDate.now();
	
	
}
