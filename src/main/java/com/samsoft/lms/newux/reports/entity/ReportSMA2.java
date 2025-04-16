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
@Table(name = "v_lms_rep_SMA2")
public class ReportSMA2 {
    @Id
    @Column(name = "sMastAgrId")
    private String  mastAgrId;
    
    @Column(name = "nDpd")
    private Integer  dpd;

    @Column(name = "sAssetClass")
    private String  assetClass;

    @Column(name = "sServBranch")
    private String  servBranch;

    @Column(name = "sServState")
    private String  servState;

    @Column(name = "sHomeBranch")
    private String  homeBranch;

    @Column(name = "sHomeState")
    private String  homeState;

    @Column(name = "sPAN")
    private String pan;

    @Column(name = "sCustomerName")
    private String  customerName;

    @Column(name = "SMA2Date")
    private Date sma2Date;

    @Column(name = "OutstandingAmount")
    private Double  outstandingAmount;

    @Column(name = "SecuredAmount")
    private Double  securedAmount;

    @Column(name = "sColtrlType")
    private String  coltrlType;

    @Column(name = "ClassifedAsSMA1")
    private String  classifedAsSMA1;

}
