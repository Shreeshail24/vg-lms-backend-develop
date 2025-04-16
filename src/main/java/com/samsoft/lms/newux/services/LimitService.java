package com.samsoft.lms.newux.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.customer.entities.AgrCustomer;

import java.util.List;

@Service
@Slf4j
public class LimitService {

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    @Autowired
    private CustApplLimitSetupRepository custApplLimitSetupRepository;

    public CustApplLimitSetup getCustomerLimitDetails(String originationApplnNo) throws Exception {

        try {
            //Get Master Agreement Details
			/*
			 * AgrMasterAgreement agrMasterAgreement =
			 * agrMasterAgreementRepository.findByOriginationApplnNo(originationApplnNo);
			 * log.info("LimitService :: getCustomerLimitDetails :: agrMasterAgreement: {}",
			 * agrMasterAgreement);
			 * 
			 * if (agrMasterAgreement == null) { throw new
			 * ApiException("Agreement details not available.", HttpStatus.NOT_FOUND); }
			 */

            //Get Customer applicant limit setup Details
            CustApplLimitSetup custApplLimitSetup = custApplLimitSetupRepository.findByOriginationApplnNo(originationApplnNo);
            log.info("LimitService :: getCustomerLimitDetails :: custApplLimitSetup: {}", custApplLimitSetup);

            if(custApplLimitSetup == null) {
                throw new ApiException("Customer Limit details not available.", HttpStatus.NOT_FOUND);
            }

            return custApplLimitSetup;

        } catch (ApiException ex) {

            ex.printStackTrace();
            log.error("LimitService :: getCustomerLimitDetails :: Method: getCustomerLimitDetails");
            log.error("LimitService :: getCustomerLimitDetails :: Request: {}", originationApplnNo);
            log.error("LimitService :: getCustomerLimitDetails :: Error: {}", ex.getMessage());

            throw new ApiException(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("LimitService :: getCustomerLimitDetails :: Method: getCustomerLimitDetails");
            log.error("LimitService :: getCustomerLimitDetails :: Request: {}", originationApplnNo);
            log.error("LimitService :: getCustomerLimitDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    protected String getCustomerName(AgrCustomer customer) {

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

    public List<CustApplLimitSetup> getCustomerLimitDetailsByAgencyId(Integer agencyId) throws Exception{
        try{
            log.info("in method getCustomerLimitDetailsByAgencyId====================>");
            List<CustApplLimitSetup> data = custApplLimitSetupRepository.findByAgencyId(agencyId);
            log.info("getCustomerLimitDetailsByAgencyId: {}", data);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            log.error("IN method getCustomerLimitDetailsByAgencyId: {}", e.getMessage());
            log.error("agencyId: {}",agencyId);
            throw e;
        }
    }
}
