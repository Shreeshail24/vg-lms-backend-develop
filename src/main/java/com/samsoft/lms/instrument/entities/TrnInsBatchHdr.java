package com.samsoft.lms.instrument.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trn_ins_batch_hdr")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"batchInstrument", "batchSts"})
public class TrnInsBatchHdr {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nBatchId", length = 20)
	private Integer batchId;

	@Column(name = "dBatchDate")
	@Temporal(TemporalType.DATE)
	private Date dtBatchDate;

	@Column(name = "sInstrumentType", length = 20)
	private String instrumentType;

	@Column(name = "sDepositBankIfsc", length = 20)
	private String depositBankIfsc;

	@Column(name = "sDepositBankName", length = 20)
	private String depositBankName;

	@Column(name = "sDepositBankBranch", length = 20)
	private String depositBankBranch;

	@Column(name = "nTotalInstruments", length = 20)
	private Integer totalInstruments;

	@Column(name = "nTotalClearedInstruments", length = 20)
	private Integer totalClearedInstruments;

	@Column(name = "nTotalBounceInstruments", length = 20)
	private Integer totalBounceInstruments;

	@Column(name = "sBatchStatus", length = 20)
	private String batchStatus;
	
	@Column(name = "sColenderId", length = 20)
	private String colenderId;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "batchHdr")
	private List<TrnInsBatchInstruments> batchInstrument;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "batchHdr" )
	private List<TrnInsBatchStatus> batchSts;
}
