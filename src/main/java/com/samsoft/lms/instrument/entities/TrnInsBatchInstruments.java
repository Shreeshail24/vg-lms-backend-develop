package com.samsoft.lms.instrument.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trn_ins_batch_instruments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "batchHdr")
public class TrnInsBatchInstruments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nBatchInstrumentId", length = 20)
	private Integer batchInstrumentId;

	@Column(name = "sInstrumentStatus", length = 20)
	private String instrumentStatus;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nBatchId")
	private TrnInsBatchHdr batchHdr;

	/*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="nInstrumentId")
	private TrnInsInstrument instrument;*/
	
	@Column(name="nInstrumentId")
	private Integer instrumentId;

}
