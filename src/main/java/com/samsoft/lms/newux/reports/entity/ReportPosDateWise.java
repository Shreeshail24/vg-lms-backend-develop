package com.samsoft.lms.newux.reports.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "v_lms_rep_pos_datewise")
public class ReportPosDateWise {

    @Id
    @Column(name = "smastagrid")
    private String mastAgrId;

    @Column(name = "dBackup")
    private LocalDate backupDate;

    @Column(name = "sColenderCode")
    private Integer colenderCode;

    @Column(name = "LendingPartner")
    private String lendingPartner;

    @Column(name = "sPortfolioCode")
    private String portfolioCode;

    @Column(name = "nUnbilledPrincipal")
    private Double unbilledPrincipal;

    @Column(name = "Total_Dues")
    private Double totalDues;

    @Column(name = "Interest_Dues")
    private Double interestDues;

    @Column(name = "Principal_Dues")
    private Double principalDues;

    @Column(name = "ChargesDues")
    private Double chargesDues;

    @Column(name = "sCustomerID")
    private String customerID;

    @Column(name = "sCustomerName")
    private String customerName;

    @Column(name = "sCity")
    private String city;

    @Column(name = "DPD_Bucket")
    private String dpdBucket;

    @Column(name = "sProductCode")
    private String productCode;

}

//                        +--------------------+--------------+------+-----+---------+-------+
//                        | Field              | Type         | Null | Key | Default | Extra |
//                        +--------------------+--------------+------+-----+---------+-------+
//                        | dBackup            | date         | YES  |     | NULL    |       |
//                        | smastagrid         | varchar(20)  | YES  |     | NULL    |       |
//                        | sColenderCode      | varchar(20)  | YES  |     | NULL    |       |
//                        | LendingPartner     | varchar(100) | YES  |     | NULL    |       |
//                        | sPortfolioCode     | varchar(20)  | YES  |     | NULL    |       |
//                        | nUnbilledPrincipal | double       | YES  |     | NULL    |       |
//                        | Total_Dues         | double       | YES  |     | NULL    |       |
//                        | Interest_Dues      | double       | YES  |     | NULL    |       |
//                        | Principal_Dues     | double       | YES  |     | NULL    |       |
//                        | ChargesDues        | double       | YES  |     | NULL    |       |
//                        | sCustomerID        | varchar(20)  | YES  |     | NULL    |       |
//                        | sCustomerName      | varchar(222) | YES  |     | NULL    |       |
//                        | sCity              | varchar(20)  | YES  |     | NULL    |       |
//                        | DPD_Bucket         | varchar(5)   | YES  |     | NULL    |       |
//                        | sProductCode       | varchar(20)  | YES  |     | NULL    |       |
//                        +--------------------+--------------+------+-----+---------+-------+
