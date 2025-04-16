package com.samsoft.lms.newux.reports.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="v_lms_rep_billofsupply")
public class ReportBillOfSupply {

    @Id
    @Column(name="sMastAgrId")
    private String mastAgrId;

    @Column(name="dtInstallmentDate")
    private String installmentDate;

    @Column(name="BillofSupplyNo")
    private String billofSupplyNo;

    @Column(name="BillofSupplyDate")
    private String billofSupplyDate;

    @Column(name = "BillTo")
    private String billTo;

    @Column(name = "sCustomerName")
    private String customerName;

    @Column(name = "CustAddr")
    private String custAddr;

    @Column(name = "CustState")
    private String custState;

    @Column(name = "StateCode")
    private String stateCode;

    @Column(name = "GSTIN")
    private String gstin;

    @Column(name="SrNo")
    private  Integer srNo;

    @Column(name="Principal")
    private Double principal;

    @Column(name="Interest")
    private Double interest;

    @Column(name="SAC")
    private String sac;

    @Column(name="GrossAmount")
    private Double grossAmount;

    @Column(name = "Discount")
    private String discount;

    @Column(name = "Abatement")
    private String abatement;

    @Column(name = "NetValue")
    private Double netValue;

    @Column(name = "OtherExpenses")
    private String otherExpenses;

    @Column(name = "Total")
    private Double total;

    @Column(name = "TotalValuePayable")
    private Double totalValuePayable;

    @Column(name = "BankDtlsforPayment")
    private String bankDtlsforPayment;

    @Column(name = "ReceiversCorrespondent")
    private String receiversCorrespondent;

    @Column(name = "SwiftCode")
    private String swiftCode;

    @Column(name = "IntermediateryBank")
    private String intermediateryBank;

    @Column(name = "BankCode")
    private String bankCode;

    @Column(name = "AccountName")
    private String accountName;

    @Column(name = "AccountNumber")
    private String accountNumber;

    @Column(name = "PANNo")
    private String panNo;

    @Column(name = "Declaration")
    private String declaration;

}

//mysql> desc v_lms_rep_billofsupply;
//        +------------------------+--------------+------+-----+---------+-------+
//        | Field                  | Type         | Null | Key | Default | Extra |
//        +------------------------+--------------+------+-----+---------+-------+
//        | sMastAgrId             | varchar(20)  | YES  |     | NULL    |       |
//        | dtInstallmentDate      | date         | YES  |     | NULL    |       |
//        | BillofSupplyNo         | varchar(20)  | YES  |     | NULL    |       |
//        | BillofSupplyDate       | date         | YES  |     | NULL    |       |
//        | BillTo                 | varchar(222) | YES  |     | NULL    |       |
//        | sCustomerName          | varchar(222) | YES  |     | NULL    |       |
//        | CustAddr               | text         | YES  |     | NULL    |       |
//        | CustState              | varchar(20)  | YES  |     | NULL    |       |
//        | StateCode              | varchar(6)   | YES  |     | NULL    |       |
//        | GSTIN                  | char(0)      | NO   |     |         |       |
//        | SrNo                   | int          | NO   |     | 0       |       |
//        | Principal              | double       | YES  |     | NULL    |       |
//        | Interest               | double       | YES  |     | NULL    |       |
//        | SAC                    | char(0)      | NO   |     |         |       |
//        | GrossAmount            | double       | YES  |     | NULL    |       |
//        | Discount               | char(0)      | NO   |     |         |       |
//        | Abatement              | char(0)      | NO   |     |         |       |
//        | NetValue               | double       | YES  |     | NULL    |       |
//        | OtherExpenses          | char(0)      | NO   |     |         |       |
//        | Total                  | double       | YES  |     | NULL    |       |
//        | TotalValuePayable      | double       | YES  |     | NULL    |       |
//        | BankDtlsforPayment     | char(0)      | NO   |     |         |       |
//        | ReceiversCorrespondent | char(0)      | NO   |     |         |       |
//        | SwiftCode              | char(0)      | NO   |     |         |       |
//        | IntermediateryBank     | char(0)      | NO   |     |         |       |
//        | BankCode               | char(0)      | NO   |     |         |       |
//        | AccountName            | char(0)      | NO   |     |         |       |
//        | AccountNumber          | char(0)      | NO   |     |         |       |
//        | PANNo                  | char(0)      | NO   |     |         |       |
//        | Declaration            | varchar(107) | NO   |     |         |       |
//        +------------------------+--------------+------+-----+---------+-------+
//        30 rows in set (0.00 sec)