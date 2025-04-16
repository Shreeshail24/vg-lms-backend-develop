package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetODAndSFDetails {

    private String loanApplicationNo;
    private String customerName;
    private Double sanctionedLimit;
    private Double utilizedLimit;
    private Double availableLimit;
    private Float roi;
    private Double pf;
    private Double overdueAmount;
    private String bankCode;
    private String accountNo;
    private String accountHolderName;
    private String accountType;
    private String ifscCode;
}
