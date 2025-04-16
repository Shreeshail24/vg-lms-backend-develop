package com.samsoft.lms.newux.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.dto.CustomerAgreementDetailsResDto;
import com.samsoft.lms.agreement.dto.VAgrTranHistoryDtoMain;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.dto.AgreementLoanInfoDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryHeaderDto;
import com.samsoft.lms.customer.dto.CustomerSearchDto;
import com.samsoft.lms.customer.dto.CustomerSearchDtoMain;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.newux.dto.response.GetCustomerDetailsResDto;
import com.samsoft.lms.newux.dto.response.GetLoanOverviewResDto;
import com.samsoft.lms.newux.dto.response.reports.SOAReportResDto;

import java.util.List;

@Service
@Slf4j
public class ReportService {

    @Autowired
    private LoanDetailsService loanDetailsService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private CustomerServices customerServices;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    public SOAReportResDto soaReport(String mastAgrId, String customerId) {

        SOAReportResDto soaReportResDto = new SOAReportResDto();

        try {
            //Get Agreement Info, Get Agreement Loan Info
            GetLoanOverviewResDto getLoanOverviewResDto = loanDetailsService.getLoanOverview(mastAgrId);
            if(getLoanOverviewResDto != null) {
                soaReportResDto.setAgreementInfo(getLoanOverviewResDto.getAgreementInfo());
                soaReportResDto.setAgreementLoanInfoList(getLoanOverviewResDto.getAgreementLoanInfoList());
            }

            //Get Transaction History Header
            VAgrTranHistoryHeaderDto vAgrTranHistoryHeaderDto = agreementService.getAgreementTranHistoryHeader(mastAgrId);
            if(vAgrTranHistoryHeaderDto != null) {
                soaReportResDto.setAgrTranHistoryHeader(vAgrTranHistoryHeaderDto);
            }

            //Get Transaction History
            
            // Create pageable object
	        Pageable pageable = PageRequest.of(0, 100);

            
            Page<VAgrTranHistoryDto> vAgrTranHistoryDtoList = agreementService.getAgreementTranHistoryList(mastAgrId, pageable);
            if(!vAgrTranHistoryDtoList.isEmpty()  && vAgrTranHistoryDtoList != null) {
            	VAgrTranHistoryDtoMain vAgrTranHistoryDtoMain = new VAgrTranHistoryDtoMain();
            	vAgrTranHistoryDtoMain.setHistoryList(vAgrTranHistoryDtoList.getContent());
            	vAgrTranHistoryDtoMain.setTotalRows(vAgrTranHistoryDtoList.getContent().size());
            	
                soaReportResDto.setAgrTranHistoryList(vAgrTranHistoryDtoMain);
            }

          Pageable paging = PageRequest.of(0, 10);
            //Get Customer List TODO here for customer list need to change return type 
//            Page<CustomerSearchDto> customerSearchDtoMain = customerServices.getCustomerList("master_agreement_id", mastAgrId ,paging);
//            if(!customerSearchDtoMain.isEmpty() && customerSearchDtoMain != null) {
//            	
//                soaReportResDto.setCustomerList(customerSearchDtoMain.getContent().get(0));
//            }

            //Get Customer Details, Contact Details, Address Details
            GetCustomerDetailsResDto getCustomerDetailsResDto = customerDetailsService.getCustomerDetails(mastAgrId, customerId);
            if(getCustomerDetailsResDto != null) {
                soaReportResDto.setCustomerDetails(getCustomerDetailsResDto);
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        return soaReportResDto;
    }
}
