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
@Table(name="tab_mst_fee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstFee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sFeeID", length = 32)
	private Integer feeId;	

	@Column(name="sFEEDesc", length = 200)
	private String feeDesc;
	
	@Column(name="sTaxApplicable", length = 20)
	private String taxApplicable;
	
	@Column(name="sUserID", length = 200)
	private String userId;
	
	@Column(name="dUserDateTime")
	LocalDate dtUserDateTime = LocalDate.now();
	
	@Column(name="dLastUpdated")
	LocalDate dtLastUpdated = LocalDate.now();
}
