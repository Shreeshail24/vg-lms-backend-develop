package com.samsoft.lms.newux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.dto.response.*;
import com.samsoft.lms.newux.services.MyLoanAccountService;

import java.util.List;

@RestController
@RequestMapping(value = "/myaccounts")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class MyLoanAccountController {

    @Autowired
    private MyLoanAccountService myLoanAccountService;

    @GetMapping(value = "/getNonODAccountDetails")
    public FourFinResponse<List<GetNonODAccountDetailsResDto>> getNonODAccountDetails(@RequestParam String customerId) throws Exception {
        FourFinResponse<List<GetNonODAccountDetailsResDto>> response = new FourFinResponse<>();

        List<GetNonODAccountDetailsResDto> res = myLoanAccountService.getNonODAccountDetails(customerId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @GetMapping(value = "/getODAccountDetails")
    public FourFinResponse<List<GetODAccountDetailsResDto>> getODAccountDetails(@RequestParam String customerId) throws Exception {
        FourFinResponse<List<GetODAccountDetailsResDto>> response = new FourFinResponse<>();

        List<GetODAccountDetailsResDto> res = myLoanAccountService.getODAccountDetails(customerId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @GetMapping(value = "/getSFAccountDetails")
    public FourFinResponse<List<GetSFAccountDetailsResDto>> getSFAccountDetails(@RequestParam String customerId) throws Exception {
        FourFinResponse<List<GetSFAccountDetailsResDto>> response = new FourFinResponse<>();

        List<GetSFAccountDetailsResDto> res = myLoanAccountService.getSFAccountDetails(customerId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @GetMapping(value = "/getNonODDetails")
    public FourFinResponse<GetNonODDetails> getNonODDetails(@RequestParam String mastAgrId) throws Exception {
        FourFinResponse<GetNonODDetails> response = new FourFinResponse<>();

        GetNonODDetails res = myLoanAccountService.getNonODDetails(mastAgrId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @GetMapping(value = "/getODAndSFDetails")
    public FourFinResponse<GetODAndSFDetails> getODAndSFDetails(@RequestParam String mastAgrId) throws Exception {
        FourFinResponse<GetODAndSFDetails> response = new FourFinResponse<>();

        GetODAndSFDetails res = myLoanAccountService.getODAndSFDetails(mastAgrId);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @GetMapping(value = "/getsfmasteragreements")
    public FourFinResponse<List<SFMasterAgreementsResDto>> getSFMasterAgreements(@RequestParam String loanApplicationNo) throws Exception {
        FourFinResponse<List<SFMasterAgreementsResDto>> response = new FourFinResponse<>();

        List<SFMasterAgreementsResDto> res = myLoanAccountService.getSFMasterAgreements(loanApplicationNo);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }
}
