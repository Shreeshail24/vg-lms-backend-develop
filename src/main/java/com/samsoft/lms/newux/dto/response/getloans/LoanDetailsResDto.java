package com.samsoft.lms.newux.dto.response.getloans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanDetailsResDto {

    private Double loanAmount = 0.0;
    private Double nextInstallmentAmount = 0.0;
}
