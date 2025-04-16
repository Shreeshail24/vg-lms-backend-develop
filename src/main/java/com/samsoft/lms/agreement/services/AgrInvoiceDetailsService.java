package com.samsoft.lms.agreement.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;
import com.samsoft.lms.agreement.repositories.AgrInvoiceDetailsRepository;
import com.samsoft.lms.core.exceptions.ApiException;

@Service
@Slf4j
public class AgrInvoiceDetailsService {

    @Autowired
    private AgrInvoiceDetailsRepository agrInvoiceDetailsRepository;

    @Cacheable
    public AgrInvoiceDetails getAgrInvoiceDetailsByMastAgrId(String mastAgrId) throws Exception {

        try {

            return agrInvoiceDetailsRepository.findByMasterMastAgrId(mastAgrId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("AgrInvoiceDetailsService :: getAgrInvoiceDetailsByMastAgrId :: Method: getAgrInvoiceDetailsByMastAgrId");
            log.error("AgrInvoiceDetailsService :: getAgrInvoiceDetailsByMastAgrId :: Request: mastAgrId: {}", mastAgrId);
            log.error("AgrInvoiceDetailsService :: getAgrInvoiceDetailsByMastAgrId :: Error: {}", e.getMessage());

            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
