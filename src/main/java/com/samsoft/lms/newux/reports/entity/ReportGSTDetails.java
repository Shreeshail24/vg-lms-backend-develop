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
@Table(name = "v_lms_rpt_GST_Details")
public class ReportGSTDetails {

    @Id
    @Column(name = "sMastAgrId")
    private String mastAgrId;

    @Column(name = "sCustomerId")
    private String customerId;

    @Column(name = "sCustomerName")
    private String customerName;

    @Column(name = "sPortfolioCode")
    private String portfolioCode;

    @Column(name = "sProductCode")
    private String productCode;

    @Column(name = "sHomeState")
    private String homeState;

    @Column(name = "sServState")
    private String servState;

    @Column(name = "CustomerGSTIN")
    private String customerGSTIN;

    @Column(name = "sGstExempted")
    private String gstExempted;

    @Column(name = "ChargeHead")
    private String chargeHead;

    @Column(name = "TaxableAmount")
    private Double taxableAmount;

    @Column(name = "IGSTAmt")
    private Double iGSTAmt;

    @Column(name = "CGSTAmt")
    private Double cGSTAmt;

    @Column(name = "SGSTAmt")
    private Double sGSTAmt;

    @Column(name = "nSGSTPer")
    private Float nSGSTPer;

    @Column(name = "nCGSTPer")
    private Float nCGSTPer;

    @Column(name = "nIGSTPer")
    private Float nIGSTPer;

    @Column(name = "dTranDate")
    private Date dTranDate;

    @Column(name = "ProcessingFee")
    private Double processingFee;

}

//    mysql> desc v_lms_rpt_GST_Details;
//        +----------------+--------------+------+-----+---------+-------+
//        | Field          | Type         | Null | Key | Default | Extra |
//        +----------------+--------------+------+-----+---------+-------+
//        | sMastAgrId     | varchar(20)  | NO   |     | NULL    |       |
//        | sCustomerId    | varchar(20)  | YES  |     | NULL    |       |
//        | sCustomerName  | varchar(222) | YES  |     | NULL    |       |
//        | sPortfolioCode | varchar(20)  | YES  |     | NULL    |       |
//        | sProductCode   | varchar(20)  | YES  |     | NULL    |       |
//        | sHomeState     | varchar(20)  | YES  |     | NULL    |       |
//        | sServState     | varchar(20)  | YES  |     | NULL    |       |
//        | CustomerGSTIN  | varchar(15)  | YES  |     | NULL    |       |
//        | sGstExempted   | varchar(20)  | YES  |     | NULL    |       |
//        | ChargeHead     | varchar(20)  | YES  |     | NULL    |       |
//        | TaxableAmount  | double       | YES  |     | NULL    |       |
//        | IGSTAmt        | double       | YES  |     | NULL    |       |
//        | CGSTAmt        | double       | YES  |     | NULL    |       |
//        | SGSTAmt        | double       | YES  |     | NULL    |       |
//        | nSGSTPer       | float        | YES  |     | NULL    |       |
//        | nCGSTPer       | float        | YES  |     | NULL    |       |
//        | nIGSTPer       | float        | YES  |     | NULL    |       |
//        | dTranDate      | date         | YES  |     | NULL    |       |
//        | ProcessingFee  | double       | YES  |     | NULL    |       |
//        +----------------+--------------+------+-----+---------+-------+
//        19 rows in set (0.00 sec)
