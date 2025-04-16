package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCustomerDetailsResDto {

    private String customerName;
    private String mobile;
    private String email;
    private Double totalLoanAmount;
    private Integer activeLoans;
    private String nextEmi;
}
