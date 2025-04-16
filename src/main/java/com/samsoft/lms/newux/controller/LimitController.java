package com.samsoft.lms.newux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.newux.services.LimitService;

import java.util.List;

@RestController
@RequestMapping(value = "/limit")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class LimitController {

    @Autowired
    private LimitService limitService;

    @GetMapping("/getCustomerLimitDetails")
    public FourFinResponse<CustApplLimitSetup> getCustomerLimitDetails(@RequestParam String originationApplnNo) throws Exception {

        CustApplLimitSetup custApplLimitSetup = limitService.getCustomerLimitDetails(originationApplnNo);

        FourFinResponse<CustApplLimitSetup> response = new FourFinResponse<>();
        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(custApplLimitSetup);

        return response;
    }

    @GetMapping("/getcustomerlimitdetailsbyagencyid")
    public FourFinResponse<List<CustApplLimitSetup>> getCustomerLimitDetailsByAgencyId(@RequestParam Integer agencyId) throws Exception {

        List<CustApplLimitSetup> custApplLimitSetupList = limitService.getCustomerLimitDetailsByAgencyId(agencyId);

        FourFinResponse<List<CustApplLimitSetup>> response = new FourFinResponse<>();
        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(custApplLimitSetupList);

        return response;
    }
}
