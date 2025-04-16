package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSFAccountDetailsResDto {

    private String loanApplicationNo;
    private Double sanctionedLimit;
    private Double availableLimit;
    private Double utilizedLimit;
    private Double overdueAmount;
    private String loanStatus;
    private String portfolioCode;
    private String customerId;
    private String mastAgrId;
    private String customerName;
    private Integer limitId;

}
