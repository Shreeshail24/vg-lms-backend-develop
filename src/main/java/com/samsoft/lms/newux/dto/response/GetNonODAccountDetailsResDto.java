package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNonODAccountDetailsResDto {

    private String loanApplicationNo;
    private Double sanctionAmount;
    private Double emiAmount;
    private String loanStatus;
    private String portfolioCode;
    private String customerId;
    private String mastAgrId;
    private String customerName;
    private String mobile;
    private String email;
    private String virtualId;
    private String vpaId;
    private Integer limitId;

}
