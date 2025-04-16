package com.samsoft.lms.newux.dto.response.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.samsoft.lms.agreement.dto.VAgrTranHistoryDtoMain;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.dto.AgreementLoanInfoDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryHeaderDto;
import com.samsoft.lms.customer.dto.CustomerSearchDtoMain;
import com.samsoft.lms.newux.dto.response.GetCustomerDetailsResDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SOAReportResDto {

    private AgreementInfoDto agreementInfo;
    private List<AgreementLoanInfoDto> agreementLoanInfoList;
    private VAgrTranHistoryHeaderDto agrTranHistoryHeader;
    private VAgrTranHistoryDtoMain agrTranHistoryList;
    private CustomerSearchDtoMain customerList;
    private GetCustomerDetailsResDto customerDetails;
}
