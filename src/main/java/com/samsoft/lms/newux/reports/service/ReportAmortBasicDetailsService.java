package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.entities.TabMstColender;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.repositories.TabMstColenderRepository;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.customer.dto.CustomerContactDto;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.newux.dto.response.reports.GetAmortBasicDetailsReportResDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportAmortBasicDetailsService {

    @Autowired
    private CustomerServices customerServices;

    @Autowired
    private AgrDisbursementRepository agrDisbursementRepository;

    @Autowired
    private AgrMasterAgreementRepository agrRepo;

    @Autowired
    private AgrColenderDtlRepository colenderRepo;

    @Autowired
    private TabMstColenderRepository colenderMstRepo;


    public GetAmortBasicDetailsReportResDto getAmortBasicDetailsReport(String mastAgrId, String customerId) throws Exception {

        GetAmortBasicDetailsReportResDto getAmortBasicDetailsReportResDto = new GetAmortBasicDetailsReportResDto();

        try {

            //Customer Contact Details
            CustomerContactDto customerContactDetails = customerServices.getCustomerContactInfo(customerId);
            if(customerContactDetails != null) {
                getAmortBasicDetailsReportResDto.setCustomerName(getCustomerName(customerContactDetails));
            }

            //Get Agr Disbursement
            AgrDisbursement agrDisbursement = agrDisbursementRepository.findByMastAgrMastAgrId(mastAgrId);
            if(agrDisbursement != null) {
                getAmortBasicDetailsReportResDto.setDisbDate(new SimpleDateFormat("dd-MM-yyyy").format(agrDisbursement.getDtDisbDate()));
            }

            //Get Agreement Details
            AgrMasterAgreement agreement = agrRepo.findByMastAgrId(mastAgrId);
            if(agreement != null) {
                getAmortBasicDetailsReportResDto.setOriginationApplnNo(agreement.getOriginationApplnNo());
            }

            //Get Colendor Details
            List<AgrColenderDtl> colenderList = colenderRepo.findByMasterAgrMastAgrId(mastAgrId);
            List<String> colenderTempList = new ArrayList<String>();
            for (AgrColenderDtl colender : colenderList) {
                TabMstColender tabMstColender = colenderMstRepo.findByColenderId(Integer.parseInt(colender.getColenderCode()));
                if(tabMstColender != null) {
                    colenderTempList.add(tabMstColender.getColenderName());
                }
            }
            getAmortBasicDetailsReportResDto.setNbfc(colenderTempList.get(0));

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Method: getAmortBasicDetailsReport");
            log.info("getAmortBasicDetailsReport :: Request :: mastAgrId: {}",mastAgrId);
            log.error("getAmortBasicDetailsReport :: Error: {}", e.getMessage());
        }

        return getAmortBasicDetailsReportResDto;
    }

    protected String getCustomerName(CustomerContactDto customer) {

        if(customer != null) {
            String name = "";
            if (customer.getFirstName() != null) {
                name = customer.getFirstName();
            }
            if (customer.getMiddleName() != null) {
                name = name + " " + customer.getMiddleName();
            }
            if (customer.getLastName() != null) {
                name = name + " " + customer.getLastName();
            }
            return name;
        }

        return null;
    }
}
