package com.samsoft.lms.newux.dto.response.getloans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllLoanDetailsResDto {

    private String loanType;
    private LoanAccountDetailsResDto loanAccountDetails;
    private CustomerDetailsResDto customerDetails;
    private ContactDetailsResDto contactDetails;
    private LoanDetailsResDto loanDetails;
    private String dueDate;
    private String bucket;
    private String agreementStatus;
    private String disbDate;

}
