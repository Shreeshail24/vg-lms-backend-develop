package com.samsoft.lms.newux.dto.response.getloans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoanAccountDetailsResDto {

    private String mastAgrId;
    private String originationApplnNo;
}
