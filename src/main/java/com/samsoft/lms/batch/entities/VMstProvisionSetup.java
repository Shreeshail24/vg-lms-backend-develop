package com.samsoft.lms.batch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name = "v_mst_provision_setup")
@Data
@NoArgsConstructor
@Immutable
@Subselect("select * from v_mst_provision_setup")

public class VMstProvisionSetup {

	@Id
	@Column(name = "nSeqNo")
	private Integer seqNo;

	@Column(name = "sPortfolioCd")
	private String portfolioCd;

	@Column(name = "sAssetClassCd")
	private String assetClassCd;

	@Column(name = "sNPAFlag")
	private String npaFlag;

	@Column(name = "nDPDFrom")
	private Integer dpdFrom;

	@Column(name = "nDPDTo")
	private Integer dpdTo;

	@Column(name = "nSecuredPer")
	private Float securedPer;

	@Column(name = "nUnSecuredPer")
	private Float unSecuredPer;
	
	@Column(name = "sActive")
	private String active;
	
	@Column(name = "nSrNo")
	private Integer srNo;

}
