package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SFMasterAgreementsResDto {
    private String loanApplicationNo;
    private Double overdueAmount;
    private String loanStatus;
    private String portfolioCode;
    private String customerId;
    private String mastAgrId;
    private String customerName;
    private Date invoiceDate;
    private Double sanctionedLimit;
    private Double availableLimit;
    private Double utilizedLimit;
    private String virtualId;
    private String vpaId;

}
