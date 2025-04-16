package com.samsoft.lms.instrument.entities;

import java.time.LocalDate;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trn_ins_batch_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "batchHdr")
public class TrnInsBatchStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nBatchStatusId", length = 20)
	private Integer batchStatusId;

	@Column(name = "sBatchStatus", length = 20)
	private String batchStatus;
	
	@Column(name = "sBatchStatusChangeDt")
	@Temporal(TemporalType.DATE)
	private Date batchStatusChangeDt;

	@Column(name = "sFileName", length = 100)
	private String fileName;

	@Column(name = "sFileLocation")
	private String fileLocation;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "batchId")
	private TrnInsBatchHdr batchHdr;


}
