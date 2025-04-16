package com.samsoft.lms.instrument.entities;

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
//@Table(name = "v_trn_ins_batch_instruments")
@Subselect("select * from v_lms_hc_instrument_vs_payapplied")
public class VLmsHcInstrumentVsPayapplied {

    @Id
    @Column(name = "nInstrumentId", length = 20)
    private Integer instrumentId;

    @Column(name = "sMastAgrID", length = 20)
    private String mastAgrId;

    @Column(name = "nBatchId", length = 20)
    private Integer batchId;

    @Column(name = "sCustomerId", length = 20)
    private String customerId;

    @Column(name = "umrn", length = 20)
    private String umrn;

    @Column(name = "LendingPartner", length = 100)
    private String lendingPartner;

    @Column(name = "sCustomerName", length = 222)
    private String customerName;

    @Temporal(TemporalType.DATE)
    @Column(name = "dInstrumentDate", length = 20)
    private Date dtInstrumentDate;

    @Column(name = "sInstrumentType", length = 20)
    private String instrumentType;

    @Column(name = "nInstrumentAmount", length = 20)
    private Double instrumentAmount;

    @Column(name = "sInstrumentStatus", length = 20)
    private String instrumentStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "dSettlementDate", length = 20)
    private Date dtSettlementDate;

    @Column(name = "sPayAppliedYN", length = 2)
    private String payAppliedYn;

    @Column(name = "sPayBounceYN", length = 2)
    private String payBounceYn;

    @Column(name = "EventAmt", length = 20)
    private Double eventAmt;

    @Column(name = "DtlAmount", length = 20)
    private Double dtlAmount;

}