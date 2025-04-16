package com.samsoft.lms.batch.entities;


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
@Table(name = "arc_agr_trn_bkp_tax_due_dtls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArcAgrTrnBkpTaxDueDetails {

	// Foreign Keys Pending 
	
	@Id
	@Column(name = "nDueTaxBackupId", length = 20)
	private Integer dueTaxBackupId;
	
	@Column(name = "sTaxCategory", length = 20)
	private String taxCategory;
	
	@Column(name = "sTaxHead", length = 20)
	private Integer taxHead;
	
	@Column(name = "nDueTaxAmount", length = 20)
	private Double dueTaxAmount;
	
	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "nDueBackupId")
	private ArcAgrTrnBkpDueDetails bkpDueDetail;
}