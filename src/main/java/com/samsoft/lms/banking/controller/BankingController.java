package com.samsoft.lms.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.banking.dto.request.CreateBankPaymentReqDto;
import com.samsoft.lms.banking.idfc.dto.request.UpdatePGPaymentReqDto;
import com.samsoft.lms.banking.service.BankPaymentService;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.dto.response.SFMasterAgreementsResDto;

import java.util.List;

@RestController
@RequestMapping("/banking")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class BankingController {

    @Autowired
    private BankPaymentService bankPaymentService;

    @PostMapping(value = "/createBankPayment")
    public FourFinResponse<Boolean> createBankPayment(@RequestBody CreateBankPaymentReqDto createBankPaymentReqDto) throws Exception {
        FourFinResponse<Boolean> response = new FourFinResponse<>();

        Boolean res = bankPaymentService.createBankPayment(createBankPaymentReqDto);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }

    @PatchMapping(value = "/updatePGPayment")
    public FourFinResponse<Boolean> updatePGPayment(@RequestParam String virtualId, @RequestBody UpdatePGPaymentReqDto updatePGPaymentReqDto) throws Exception {
        FourFinResponse<Boolean> response = new FourFinResponse<>();

        Boolean res = bankPaymentService.updatePGPayment(virtualId, updatePGPaymentReqDto);

        response.setHttpStatus(HttpStatus.OK);
        response.setResponseCode(HttpStatus.OK.value());
        response.setData(res);

        return response;
    }
}
