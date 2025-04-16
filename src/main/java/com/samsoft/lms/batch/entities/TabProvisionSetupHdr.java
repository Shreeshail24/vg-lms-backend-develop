package com.samsoft.lms.batch.entities;

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
@Table(name="tab_provision_setup_hdr")
@Data
@NoArgsConstructor

public class TabProvisionSetupHdr {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nSeqNo", length = 20)
	private Integer seqNo;
	
	@Column(name="sPortfolioCd", length = 20)
	private String portfolioCd;
	
	@Column(name="sActive", length = 2)
	private String active;
	
	@Column(name = "dLastUpdated")
	private DateTime dtLastUpdated = new DateTime();

	
	
}
