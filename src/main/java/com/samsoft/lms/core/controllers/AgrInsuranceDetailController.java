package com.samsoft.lms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.CreateAgrInsuranceDetailReqDto;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.entities.AgrInsuranceDetail;
import com.samsoft.lms.core.services.AgrInsuranceDetailService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/insuranceDetails")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
        "https://qa-los.4fin.in", "http://10.0.153.25:80", "http://10.0.153.25", "http://10.0.153.25:9090",
        "https://qa-losone.4fin.in" }, allowedHeaders = "*")
public class AgrInsuranceDetailController {

    @Autowired
    private AgrInsuranceDetailService insuranceDetailService;

    @PostMapping
    public FourFinResponse<AgrInsuranceDetail> createAgrInsuranceDetail(
            @Valid @RequestBody CreateAgrInsuranceDetailReqDto createAgrInsuranceDetailReqDto) throws Exception {

        FourFinResponse<AgrInsuranceDetail> response = new FourFinResponse<>();

        AgrInsuranceDetail agrInsuranceDetail = insuranceDetailService
                .createAgrInsuranceDetail(createAgrInsuranceDetailReqDto);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setResponseMessage("Created Successfully.");
        response.setData(agrInsuranceDetail);

        return response;

    }

    @GetMapping(value = "/getInsuranceDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<AgrInsuranceDetail> getInsuranceDetail(@RequestParam String sCustomerId) throws Exception {

        FourFinResponse<AgrInsuranceDetail> response = new FourFinResponse<>();

        AgrInsuranceDetail res = insuranceDetailService.getinsuranceDetail(sCustomerId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }
}
