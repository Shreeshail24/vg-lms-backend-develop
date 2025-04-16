package com.samsoft.lms.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAgreementDetailsResDto {

    private String customerName;
    private String mastAgrId;
    private String customerId;
    private String customerType="Individual";
    private String portfolioCode;
    private Integer totalTenor;
    private Integer balanceTenor;
    private String tenorUnit="MONTHLY";
    private String homeBranch;
    private String servBranch;
    private String gstExempted;
    private String agreementStatus;
    private Double outstandingAmount;
    private Double nextInstallmentAmount = 0.0;
    private String dtNextInstallment;
    private String status;
}
