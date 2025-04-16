package com.samsoft.lms.newux.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;
import com.samsoft.lms.agreement.services.AgrInvoiceDetailsService;
import com.samsoft.lms.aws.dto.DocumentViewResDto;
import com.samsoft.lms.aws.service.AWSService;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.newux.dto.response.GetAgrInvoiceDetailsResDto;

@Service
@Slf4j
public class AgreementDetailsService {

    @Autowired
    private AgrInvoiceDetailsService agrInvoiceDetailsService;

    @Autowired
    private AWSService awsService;

    public GetAgrInvoiceDetailsResDto getAgrInvoiceDetails(String mastAgrId) throws Exception {

        try {

            AgrInvoiceDetails agrInvoiceDetails = agrInvoiceDetailsService.getAgrInvoiceDetailsByMastAgrId(mastAgrId);
            log.info("AgreementDetailsService :: getAgrInvoiceDetails :: agrInvoiceDetails: {}", agrInvoiceDetails);

            if(agrInvoiceDetails != null) {

                DocumentViewResDto documentViewResDto = awsService.documentView(agrInvoiceDetails.getInvoiceImageURL());
                log.info("AgreementDetailsService :: getAgrInvoiceDetails :: getInvoiceImageURL: {}", agrInvoiceDetails.getInvoiceImageURL());

                return GetAgrInvoiceDetailsResDto.builder()
                        .invoiceDetails(agrInvoiceDetails)
                        .invoiceDocumentView(documentViewResDto)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("AgreementDetailsService :: getAgrInvoiceDetails :: Method: getAgrInvoiceDetails");
            log.error("AgreementDetailsService :: getAgrInvoiceDetails :: Request: mastAgrId: {}", mastAgrId);
            log.error("AgreementDetailsService :: getAgrInvoiceDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return null;

    }

}
