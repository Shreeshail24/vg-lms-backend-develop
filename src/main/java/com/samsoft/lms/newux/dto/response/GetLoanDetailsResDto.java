package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.samsoft.lms.agreement.dto.CustomerAgreementDetailsResDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetLoanDetailsResDto {

    List<String> mastAgrIds;
    CustomerAgreementDetailsResDto customerAgreementDetails;
}
