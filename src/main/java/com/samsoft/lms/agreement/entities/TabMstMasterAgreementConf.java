package com.samsoft.lms.agreement.entities;

import java.util.Date;

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
@Table(name="TAB_MST_MATER_AGREEMENT_CONF")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstMasterAgreementConf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nID")
	private Integer nId;
	
	@Column(name="sPortfolioCode")
	private String portfolioCode;

	@Column(name="sBranchCode")
	private String branchCode;
	
	@Column(name="nSeqNo")
	private Integer seqNo;
	
	@Column(name="sActive", columnDefinition = "varchar(10) default 'Y'")
	private String active;
	
	@Column(name="sUserID", length = 200)
	private String userId;	
	
	@Column(name = "dUserDateTime")
	@Temporal(TemporalType.DATE)
	private Date dtUserDateTime = new Date();

	@Column(name = "dLastUpdated")
	@Temporal(TemporalType.DATE)
	private Date dLastUpdated = new Date();	
	
	
}
