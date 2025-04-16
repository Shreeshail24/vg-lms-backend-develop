package com.samsoft.lms.newux.reports.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "v_lms_rpt_customer_aging_bucket")
public class ReportCustomerAgingBucket {

    @Id
    @Column(name = "sMastAgrId")
    private String mastAgrId;

    @Column(name = "sCustomerId")
    private String customerId;

    @Column(name = "sPortfolioCode")
    private String portfolioCode;

    @Column(name = "sHomeBranch")
    private String homeBranch;

    @Column(name = "sAssetClass")
    private String assetClass;

    @Column(name = "nLoanAmount")
    private Double loanAmount;

    @Column(name = "nUnbilledPrincipal")
    private Double unbilledPrincipal;

    @Column(name = "LoanDPD")
    private Long loanDPD;

    @Column(name = "CustDPD")
    private Long custDPD;

    @Column(name = "dDisbDate")
    private Date disbDate;

    @Column(name = "Bucket_0_30")
    private Double bucket_0_30;

    @Column(name = "Bucket_31_60")
    private Double bucket_31_60;

    @Column(name = "Bucket_61_90")
    private Double bucket_61_90;

    @Column(name = "Bucket_91_180")
    private Double bucket_91_180;

    @Column(name = "Bucket_180_Above")
    private Double bucket_180_Above;

    @Column(name = "TotalOverDues")
    private Double totalOverDues;

    @Column(name = "sCustomerName")
    private String customerName;

    @Column(name = "sOriginationApplnNo")
    private String originationApplnNo;

    @Column(name = "NBFC")
    private String nbfc;

}
