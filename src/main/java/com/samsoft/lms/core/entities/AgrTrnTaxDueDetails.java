package com.samsoft.lms.core.entities;


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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_tax_due_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnTaxDueDetails {

	// Foreign Keys Pending 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nTaxDueId", length = 20)
	private Integer taxDueId;
	
	@Column(name = "sTaxCategory", length = 20)
	private String taxCategory;
	
	@Column(name = "sTaxHead", length = 20)
	private String taxHead;
	
	@Column(name = "nDueTaxAmount", length = 20)
	private Double dueTaxAmount;
		
	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "nDueDtlId")
	private AgrTrnDueDetails dueDetail;
}