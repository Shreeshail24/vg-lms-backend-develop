package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tab_mst_tax")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstTax {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nTaxID", length = 32)
	private Integer taxId;
	
	@Column(name="sTaxDescription", length = 200)
	private String taxDescription;
	
	@Column(name="sTaxCode", length = 20)
	private String taxCode;
	
	@Column(name="nTaxPer", length = 10)
	private Float taxPer;
	
	@Column(name="sTaxApplicability", length = 2)
	private String taxApplicability;
	
	@Column(name="sState", length = 20)
	private String state;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name="dUserDateTime")
	LocalDate dtUserDateTime = LocalDate.now();
	
	@Column(name="dLastUpdated")
	LocalDate dtLastUpdated = LocalDate.now();
	
	
}
