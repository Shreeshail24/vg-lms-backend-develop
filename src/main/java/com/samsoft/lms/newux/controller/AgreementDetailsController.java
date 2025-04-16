package com.samsoft.lms.newux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.dto.response.GetAgrInvoiceDetailsResDto;
import com.samsoft.lms.newux.services.AgreementDetailsService;

@RestController
@RequestMapping(value = "/AgreementDetails")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in","https://qa-los.4fin.in","http://10.0.153.25:80","http://10.0.153.25","http://10.0.153.25:9090", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class AgreementDetailsController {

    @Autowired
    private AgreementDetailsService agreementDetailsService;

    @GetMapping("/getAgrInvoiceDetails")
    public FourFinResponse<GetAgrInvoiceDetailsResDto> getAgrInvoiceDetails(@RequestParam String mastAgrId) throws Exception {

        FourFinResponse<GetAgrInvoiceDetailsResDto> response = new FourFinResponse<>();

        GetAgrInvoiceDetailsResDto getAgrInvoiceDetailsResDto = agreementDetailsService.getAgrInvoiceDetails(mastAgrId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(getAgrInvoiceDetailsResDto);

        return response;
    }
}
