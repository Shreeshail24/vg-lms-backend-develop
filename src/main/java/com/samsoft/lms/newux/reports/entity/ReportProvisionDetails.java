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
@Table(name = "v_lms_rep_provision_details")
public class ReportProvisionDetails {

    @Id
    @Column(name = "sMastAgrId")
    private String mastAgrId;
    @Column(name = "sLoanId")
    private String loanId;

    @Column(name = "sHomeBranch")
    private String homeBranch;

    @Column(name = "sServBranch")
    private String servBranch;


    @Column(name = "sPortfolioCode")
    private String portfolioCode;

    @Column(name = "sProductCode")
    private String productCode;

    @Column(name = "Customer")
    private String customer;

    @Column(name = "nProvisionAmount")
    private Double provisionAmount;

    @Column(name = "nProvisionRevAmount")
    private Double provisionRevAmount;

    @Column(name = "dProvisionDate")
    private Date provisionDate;

    @Column(name = "CurrentDate")
    private Date currentDate;

    @Column(name = "NBFC")
    private String nbfc;

    @Column(name = "dDisbDate")
    private Date disbDate;

    @Column(name = "sOriginationApplnNo")
    private String originationApplnNo;

}


//       mysql> desc v_lms_rep_provision_details;
//        +---------------------+--------------+------+-----+---------+-------+
//        | Field               | Type         | Null | Key | Default | Extra |
//        +---------------------+--------------+------+-----+---------+-------+
//        | sMastAgrId          | varchar(20)  | YES  |     | NULL    |       |
//        | sLoanId             | varchar(20)  | YES  |     | NULL    |       |
//        | sHomeBranch         | varchar(20)  | YES  |     | NULL    |       |
//        | sServBranch         | varchar(20)  | YES  |     | NULL    |       |
//        | sPortfolioCode      | varchar(20)  | YES  |     | NULL    |       |
//        | sProductCode        | varchar(20)  | YES  |     | NULL    |       |
//        | Customer            | varchar(4)   | NO   |     |         |       |
//        | nProvisionAmount    | double       | YES  |     | NULL    |       |
//        | nProvisionRevAmount | double       | YES  |     | NULL    |       |
//        | dProvisionDate      | date         | YES  |     | NULL    |       |
//        | CurrentDate         | date         | YES  |     | NULL    |       |
//        | NBFC                | varchar(100) | YES  |     | NULL    |       |
//        | dDisbDate           | date         | YES  |     | NULL    |       |
//        | sOriginationApplnNo | varchar(20)  | YES  |     | NULL    |       |
//        +---------------------+--------------+------+-----+---------+-------+
//        14 rows in set (0.01 sec)
