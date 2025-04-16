package com.samsoft.lms.agreement.entities;

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

@Entity
@Table(name = "agr_colender_inc_share_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrColenderIncShareDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nCoLendDtlID")
	private Integer coLendDtlId;

	@Column(name = "sFeeCode", length = 20)
	private String feeCode;

	@Column(name = "nSharePer", length = 20)
	private Double sharePer;
	
	@Column(name = "nTotalDues", length = 20)
	private Double totalDues;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dUserDateTime")
	
	private LocalDate dtUserDateTime = LocalDate.now();

	@Column(name = "dLastUpdated")
	
	private LocalDate dLastUpdated = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nCoLendID")
	private AgrColenderDtl colender;
}

