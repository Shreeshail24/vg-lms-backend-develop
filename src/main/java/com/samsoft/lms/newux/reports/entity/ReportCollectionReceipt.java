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
@Table(name = "v_lms_rep_recipts_new")
public class ReportCollectionReceipt {
    @Id
    @Column(name = "TranId")
    private Integer tranId;
    @Column(name = "nColenderID")
    private Integer colenderID;
    @Column(name = "sColenderName")
    private String colenderName;
    @Column(name="sCustomerId")
    private String customerId;
    @Column(name="sCustomerName")
    private String customerName;
    @Column(name="dReceiptDate")
    private Date receiptDate;
    @Column(name="dInstrumentDate")
    private Date instrumentDate;
    @Column(name="dSettlementDate")
    private Date settlementDate;
    @Column(name="smastagrid")
    private String mastagrid;
    @Column(name="sLoanId")
    private String loanId;
    @Column(name="nReceiptAmount")
    private double receiptAmount;
    @Column(name = "sUtrNo")
    private String utrNo;
    @Column(name = "interestRate")
    private Float interestRate;
    @Column(name = "Principal")
    private Double principal;
    @Column(name = "Interest")
    private Double interest;
    @Column(name = "Charges")
    private Double charges;
    @Column(name = "Excess")
    private Double excess;
    @Column(name = "sPayMode")
    private String payMode;
    @Column(name = "sInstrumentType")
    private String instrumentType;
    @Column(name = "sProductCode")
    private String productCode;
    @Column(name = "sPortfolioCode")
    private String portfolioCode;
    @Column(name = "sOriginationApplnNo")
    private String originationApplnNo;
}


//mysql> desc v_lms_rep_recipts_new;
//        +---------------------+--------------+------+-----+---------+-------+
//        | Field               | Type         | Null | Key | Default | Extra |
//        +---------------------+--------------+------+-----+---------+-------+
//        | TranId              | int          | YES  |     | 0       |       |
//        | nColenderID         | varchar(20)  | NO   |     | NULL    |       |
//        | sColenderName       | varchar(100) | YES  |     | NULL    |       |
//        | sCustomerId         | varchar(20)  | YES  |     | NULL    |       |
//        | sCustomerName       | varchar(222) | YES  |     | NULL    |       |
//        | dReceiptDate        | date         | YES  |     | NULL    |       |
//        | dInstrumentDate     | date         | YES  |     | NULL    |       |
//        | dSettlementDate     | date         | YES  |     | NULL    |       |
//        | smastagrid          | varchar(20)  | YES  |     | NULL    |       |
//        | sLoanId             | varchar(20)  | NO   |     | NULL    |       |
//        | nReceiptAmount      | double       | YES  |     | NULL    |       |
//        | sUtrNo              | varchar(100) | YES  |     | NULL    |       |
//        | interestRate        | float        | YES  |     | NULL    |       |
//        | Principal           | double       | YES  |     | NULL    |       |
//        | Interest            | double       | YES  |     | NULL    |       |
//        | Charges             | double       | YES  |     | NULL    |       |
//        | Excess              | double       | YES  |     | NULL    |       |
//        | sPayMode            | varchar(20)  | YES  |     | NULL    |       |
//        | sInstrumentType     | varchar(20)  | YES  |     | NULL    |       |
//        | sProductCode        | varchar(20)  | YES  |     | NULL    |       |
//        | sPortfolioCode      | varchar(20)  | YES  |     | NULL    |       |
//        | sOriginationApplnNo | varchar(20)  | YES  |     | NULL    |       |
//        +---------------------+--------------+------+-----+---------+-------+
//        22 rows in set (0.00 sec)