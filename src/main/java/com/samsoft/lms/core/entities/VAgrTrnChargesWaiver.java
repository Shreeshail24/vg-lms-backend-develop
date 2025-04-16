package com.samsoft.lms.core.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Subselect("select * from v_agr_trn_charges_waivable")
public class VAgrTrnChargesWaiver {
	
	    @Id
	    @Column(name = "nTranId", length = 20)
	    private Integer transactionId;

	    @Column(name = "dTranDate")
	    @Temporal(TemporalType.DATE)
	    private Date transactionDate;

	    @Column(name = "sDueHead", length = 20)
	    private String dueHead;

	    @Column(name = "nTranAmount", length = 20)
	    private Double transactionAmount;

	    @Column(name = "nTaxAmount", length = 20)
	    private Double taxAmount;

	    @Column(name = "nWaivableAmount", length = 20)
	    private Double waivableAmount;

	    @Column(name = "nDueDtlID", length = 20)
	    private Integer dueDetailId;

	    @Column(name = "nTranDtlId", length = 20)
	    private Integer transactionDetailId;

	    @Column(name = "sMastAgrId", length = 20)
	    private String mastAgrId;

	    @Column(name = "sTranType", length = 20)
	    private String transactionType;


}


