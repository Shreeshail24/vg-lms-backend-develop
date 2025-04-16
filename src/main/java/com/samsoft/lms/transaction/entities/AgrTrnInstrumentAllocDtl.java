package com.samsoft.lms.transaction.entities;

import java.io.Serializable;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agr_trn_req_instrument_alloc_dtl")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter	
public class AgrTrnInstrumentAllocDtl implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8340017267487096858L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nAllocID", length = 20)
	private Integer allocId;
	
	@Column(name="sLoanID", length = 20)
	private String loanId;
	
	@Column(name = "sPayMode", length = 20)
	private String payMode;
	
	@Column(name = "sTranCategory", length = 20)
	private String tranCategory;
	
	@Column(name = "tranHead", length = 20)
	private String tranHead;

	@Column(name = "nAmout", length = 20)
	private Double amout = 0.0d;
	
	@Column(name = "sUserID", length = 200)
	private String userId;
	
	@Column(name = "dLastUpdated")
	@Temporal(TemporalType.DATE)
	private Date dtLastUpdated = new Date();

	@Column(name = "dUserDate")
	@Temporal(TemporalType.DATE)
	private Date dtUserDate = new Date();
	
	/*@OneToMany(cascade = CascadeType.ALL, mappedBy = "instrumentAlloc", fetch = FetchType.LAZY)
	private List<AgrTrnManualApportionDtl> instrumentManualAppor;*/

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="nInstrumetSrNo")
	private AgrTrnInstrument instrument;
	
}
