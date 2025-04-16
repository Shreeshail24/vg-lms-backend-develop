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
@Table(name = "v_lms_rep_trial_balance")
public class ReportTrialBalance {

    @Id
    @Column(name = "sGLHeadCode")
    private String glHeadCode;

    @Column(name = "DrAmount")
    private Double drAmount;

    @Column(name = "CrAmount")
    private Double crAmount;

    @Column(name = "dTranDate")
    private Date tranDate;
}

//mysql> desc v_lms_rep_trial_balance;+-------------+-------------+------+-----+---------+-------+
//        | Field       | Type        | Null | Key | Default | Extra |
//        +-------------+-------------+------+-----+---------+-------+
//        | dTranDate   | date        | YES  |     | NULL    |       |
//        | sGLHeadCode | varchar(50) | YES  |     | NULL    |       |
//        | DrAmount    | double      | YES  |     | NULL    |       |
//        | CrAmount    | double      | YES  |     | NULL    |       |
//        +-------------+-------------+------+-----+---------+-------+
//        4 rows in set (0.01 sec)