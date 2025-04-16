package com.samsoft.lms.newux.reports.service; 

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.newux.reports.entity.CustomerUnutilizedLimit;
import com.samsoft.lms.newux.reports.repository.CustomerUnutilizedLimitRepository;

import java.util.List;

@Service
@Slf4j
public class CustomerUnutilizedLimitService {
    @Autowired
    private CustomerUnutilizedLimitRepository customerUnutilizedLimitRepository;

    public List<CustomerUnutilizedLimit> getCustomerUnutilizedLimitByCustomerId(String customerId) throws Exception {
        try{
            log.info("Report CustomerUnutilizedLimit ==>" +   "customerId " + customerId );
            return customerUnutilizedLimitRepository.findAllByCustomerId(customerId);
        } catch(Exception e){
            e.printStackTrace();
            log.error("Method: CustomerUnutilizedLimit");
            log.error("Report CustomerUnutilizedLimit ==>" +   "customerId" + customerId );
            log.error("Error: " + e.getMessage());
            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
