package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNonODDetails {

    private String loanApplicationNo;
    private String customerName;
    private Double sanctionedAmount;
    private Float roi;
    private Integer totalTenure;
    private Integer balanceTenure;
    private String emiDate;
    private Double emiAmount;

}
