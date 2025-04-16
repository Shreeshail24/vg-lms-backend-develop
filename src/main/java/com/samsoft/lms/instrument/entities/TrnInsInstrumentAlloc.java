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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "trn_ins_instrument_alloc")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"instrument"})
public class TrnInsInstrumentAlloc {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nAllocId", length = 20)
	private Integer allocId;
	
	@Column(name="sLoanId", length = 20)
	private String loanId;
	
	@Column(name="sTranCategory", length = 20)
	private String tranCategory;
	
	@Column(name="sTranHead", length = 20)
	private String tranHead;
	
	@Column(name="nApportionAmount", length = 20)
	private Double apportionAmount;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	@JoinColumn(name="nInstrumentId")
	private TrnInsInstrument instrument;
	
}
