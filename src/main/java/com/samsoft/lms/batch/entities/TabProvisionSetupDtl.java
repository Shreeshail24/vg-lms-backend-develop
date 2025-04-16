package com.samsoft.lms.batch.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_provision_setup_dtl")
@Data
@NoArgsConstructor
public class TabProvisionSetupDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nSrNo", length = 20)
	private Integer srNo;

	@Column(name = "sAssetClassCd", length = 20)
	private String assetClassCd;

	@Column(name = "nDPDFrom", length = 3)
	private Integer dpdFrom;

	@Column(name = "nDPDTo", length = 3)
	private Integer dpdTo;

	@Column(name = "nSecuredPer")
	private Float securedPer;

	@Column(name = "nUnSecuredPer")
	private Float unSecuredPer;

	@Column(name = "sNPAFlag", length = 2)
	private String npaFlag;

	@Column(name = "dLastUpdated")
	private DateTime dtLastUpdated = new DateTime();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nSeqNo")
	private TabProvisionSetupHdr provisionHdr;

}
