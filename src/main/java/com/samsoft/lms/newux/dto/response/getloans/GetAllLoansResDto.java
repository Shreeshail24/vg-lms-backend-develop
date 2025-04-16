package com.samsoft.lms.newux.dto.response.getloans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.samsoft.lms.newux.dto.response.getloans.AllLoanDetailsResDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetAllLoansResDto {

    private Integer totalLoans = 0;
    private Integer totalLiveLoans = 0;
    private Integer totalClosedLoans = 0;
    private List<AllLoanDetailsResDto> loans;
}
