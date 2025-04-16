package com.samsoft.lms.newux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.customer.exceptions.CustomerDataNotFoundException;
import com.samsoft.lms.customer.exceptions.CustomerInternalServerError;
import com.samsoft.lms.newux.dto.response.CustomerInfoResDto;
import com.samsoft.lms.newux.dto.response.GetCustomerDetailsResDto;
import com.samsoft.lms.newux.services.CustomerDetailsService;

@RestController
@RequestMapping(value = "/customerdetails")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class CustomerDetailsController {

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @GetMapping(value = "/getCustomerInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<CustomerInfoResDto> getCustomerInfo(@RequestParam String mastAgrId, @RequestParam String customerId) throws Exception {
        FourFinResponse<CustomerInfoResDto> response = new FourFinResponse<>();
        try {
            CustomerInfoResDto customerInfoResDto = customerDetailsService.getCustomerInfo(mastAgrId, customerId);
            if(customerInfoResDto != null) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(customerInfoResDto);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setData(null);
            }
        } catch (CustomerDataNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new CustomerInternalServerError(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/getCustomerDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<GetCustomerDetailsResDto> getCustomerDetails(@RequestParam String mastAgrId, @RequestParam String customerId) throws Exception {
        FourFinResponse<GetCustomerDetailsResDto> response = new FourFinResponse<>();
        try {
            GetCustomerDetailsResDto customerDetails = customerDetailsService.getCustomerDetails(mastAgrId, customerId);
            if(customerDetails != null) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(customerDetails);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setData(null);
            }
        } catch (CustomerDataNotFoundException e) {

            response.setHttpStatus(HttpStatus.NOT_FOUND);
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new CustomerInternalServerError(e.getMessage());
        }

        return response;
    }

}
