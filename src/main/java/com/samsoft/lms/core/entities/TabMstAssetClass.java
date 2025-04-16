package com.samsoft.lms.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_mst_asset_class")
@Data
@NoArgsConstructor
public class TabMstAssetClass {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nID", length = 20)
	private Integer nId;

	@Column(name = "sAssetClassCd", length = 20)
	private String assetClassCd;

	@Column(name = "sAssetClassDesc", length = 60)
	private String assetClassDesc;

	@Column(name = "sActive", length = 2)
	private String active;

	@Column(name = "nPriority", length = 2)
	private Integer priority;

	@Column(name = "dLastUpdated")
	private DateTime dtLastUpdated = new DateTime();

	
}
