package com.samsoft.lms.banking.idfc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.banking.idfc.dto.response.CallbackInstaAlertResDto;
import com.samsoft.lms.banking.idfc.dto.response.CallbackVAValidationResDto;
import com.samsoft.lms.banking.idfc.service.IDFCCallbackService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/banking/idfc")
public class IDFCCallbackController {

    @Autowired
    private IDFCCallbackService idfcCallbackService;

    @RequestMapping(value = "/callback-va-validation")
    public ResponseEntity<CallbackVAValidationResDto> callbackVAValidation(HttpServletRequest request) throws Exception {

        CallbackVAValidationResDto res = idfcCallbackService.callbackVAValidation(request);

        return ResponseEntity.ok(res);
    }

    @RequestMapping(value = "/callback-insta-alert")
    public ResponseEntity<CallbackInstaAlertResDto> callbackInstaAlert(HttpServletRequest request) throws Exception {

        CallbackInstaAlertResDto res = idfcCallbackService.callbackInstaAlert(request);

        return ResponseEntity.ok(res);
    }
}
