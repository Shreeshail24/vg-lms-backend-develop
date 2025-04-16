package com.samsoft.lms.newux.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.dto.AgreementLoanInfoDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetLoanOverviewResDto {

    private AgreementInfoDto agreementInfo;
    private List<AgreementLoanInfoDto> agreementLoanInfoList;
}
