package com.samsoft.lms.banking.idfc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.samsoft.lms.banking.dto.request.CreateBankPaymentLogsReqDto;
import com.samsoft.lms.banking.dto.request.CreateBankPaymentReqDto;
import com.samsoft.lms.banking.dto.request.UpdateBankPaymentReqDto;
import com.samsoft.lms.banking.idfc.dto.request.CallbackInstaAlertReqDto;
import com.samsoft.lms.banking.idfc.dto.request.CallbackVAValidationReqDto;
import com.samsoft.lms.banking.idfc.dto.response.CallbackInstaAlertResDto;
import com.samsoft.lms.banking.idfc.dto.response.CallbackVAValidationResDto;
import com.samsoft.lms.banking.service.BankPaymentLogsService;
import com.samsoft.lms.banking.service.BankPaymentService;
import com.samsoft.lms.banking.utils.BankingUtil;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IDFCCallbackService {

    @Autowired
    private BankPaymentService bankPaymentService;

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    @Autowired
    private BankPaymentLogsService bankPaymentLogsService;

    @Autowired
    private BankingUtil bankingUtil;

    @Value("${credit-base-url}")
    private String creditBaseUrl;

    @Value("${credit_one_year_token}")
    private String creditOneYearToken;

    public CallbackVAValidationResDto callbackVAValidation(HttpServletRequest request) throws Exception {

        CallbackVAValidationResDto callbackVAValidationResDto = null;
        CallbackVAValidationReqDto callbackVAValidationReqDto = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("IDFCCallbackService :: callbackVAValidation :: Response: {}", httpServletRequestToString(request));

            callbackVAValidationReqDto = objectMapper.readValue(request.getReader(), CallbackVAValidationReqDto.class);
            log.info("IDFCCallbackService :: callbackVAValidation :: callbackVAValidationReqDto: {}", callbackVAValidationReqDto);

            if(callbackVAValidationReqDto != null) {

                Character virtualIdLastCharacter = bankingUtil.getVirtualIdLastCharacter(callbackVAValidationReqDto.getVANum());

                if(virtualIdLastCharacter.equals('X')) {

                    //LMS
                    AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByVirtualId(callbackVAValidationReqDto.getVANum());

                    if (agrMasterAgreement == null) {

                        callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                .vANum(callbackVAValidationReqDto.getVANum())
                                .bankRef(callbackVAValidationReqDto.getBankRef())
                                .status("F")
                                .statusDesc("Failed")
                                .errorCode("02")
                                .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                                .build();

                        try {
                            CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                                    .jsonData(objectMapper.writeValueAsString(callbackVAValidationReqDto))
                                    .build();

                            bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

                        } catch (Exception e) {
                            e.printStackTrace();

                            log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                            log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());

                            callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                    .vANum(callbackVAValidationReqDto.getVANum())
                                    .bankRef(callbackVAValidationReqDto.getBankRef())
                                    .status("F")
                                    .statusDesc("Failed")
                                    .errorCode("02")
                                    .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                                    .build();
                        }

                    } else {

                        CreateBankPaymentReqDto createBankPaymentReqDto = CreateBankPaymentReqDto.builder()
                                .virtualId(callbackVAValidationReqDto.getVANum())
                                .amount(Double.parseDouble(callbackVAValidationReqDto.getTxnAmt()))
                                .vaValidationJsonData(objectMapper.writeValueAsString(callbackVAValidationReqDto))
                                .source("IDFC")
                                .paymentStatus("VAV")
                                .build();

                        log.info("IDFCCallbackService :: callbackVAValidation :: createBankPaymentReqDto: {}", createBankPaymentReqDto);

                        Boolean bankPaymentRes = bankPaymentService.createBankPayment(createBankPaymentReqDto);
                        log.info("IDFCCallbackService :: callbackVAValidation :: bankPaymentRes: {}", bankPaymentRes);

                        callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                .vANum(callbackVAValidationReqDto.getVANum())
                                .bankRef(callbackVAValidationReqDto.getBankRef())
                                .status("000")
                                .statusDesc("Success")
                                .errorCode("000")
                                .errorDesc("Success")
                                .build();

                    }
                } else {

                    //LOS
                    String requestBody = new Gson().toJson(callbackVAValidationReqDto);
                    log.info("IDFCCallbackService :: callbackVAValidation :: Request Body: {}", requestBody);

                    Unirest.setTimeouts(0, 0);
                    HttpResponse<Boolean> response = Unirest.post(creditBaseUrl + "/banking/idfc/callback-va-validation")
                            .header("Content-Type", "application/json")
                            .header("Authorization", creditOneYearToken)
                            .body(requestBody).asObject(Boolean.class);

                    log.info("IDFCCallbackService :: callbackVAValidation :: Response: {}", response.getBody());

                    if(response.getStatus() == 200) {

                        if(response.getBody()) {

                            callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                    .vANum(callbackVAValidationReqDto.getVANum())
                                    .bankRef(callbackVAValidationReqDto.getBankRef())
                                    .status("000")
                                    .statusDesc("Success")
                                    .errorCode("000")
                                    .errorDesc("Success")
                                    .build();

                        } else {
                            callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                    .vANum(callbackVAValidationReqDto.getVANum())
                                    .bankRef(callbackVAValidationReqDto.getBankRef())
                                    .status("F")
                                    .statusDesc("Failed")
                                    .errorCode("02")
                                    .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                                    .build();
                        }

                    } else {
                        callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                                .vANum(callbackVAValidationReqDto.getVANum())
                                .bankRef(callbackVAValidationReqDto.getBankRef())
                                .status("F")
                                .statusDesc("Failed")
                                .errorCode("02")
                                .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                                .build();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("IDFCCallbackService :: callbackVAValidation :: Method: callbackVAValidation");
            log.error("IDFCCallbackService :: callbackVAValidation :: Request: {}", httpServletRequestToString(request));
            log.error("IDFCCallbackService :: callbackVAValidation :: Error: {}", e.getMessage());

            callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                    .vANum(callbackVAValidationReqDto.getVANum())
                    .bankRef(callbackVAValidationReqDto.getBankRef())
                    .status("F")
                    .statusDesc("Failed")
                    .errorCode("02")
                    .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                    .build();

            try {
                CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                        .jsonData(objectMapper.writeValueAsString(callbackVAValidationReqDto))
                        .error(e.getMessage())
                        .build();

                bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

            } catch (Exception e1) {
                e1.printStackTrace();

                log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                log.error("BankPaymentService :: createBankPayment :: Error: {}", e1.getMessage());

                callbackVAValidationResDto = CallbackVAValidationResDto.builder()
                        .vANum(callbackVAValidationReqDto.getVANum())
                        .bankRef(callbackVAValidationReqDto.getBankRef())
                        .status("F")
                        .statusDesc("Failed")
                        .errorCode("02")
                        .errorDesc("VA_Number_does_not_exist_or_amount_invalid")
                        .build();
            }
        }

        return callbackVAValidationResDto;
    }

    public CallbackInstaAlertResDto callbackInstaAlert(HttpServletRequest request) throws Exception {

        CallbackInstaAlertResDto callbackInstaAlertResDto = null;
        CallbackInstaAlertReqDto callbackInstaAlertReqDto = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("IDFCCallbackService :: callbackInstaAlert :: Response: {}", httpServletRequestToString(request));

            callbackInstaAlertReqDto = objectMapper.readValue(request.getReader(), CallbackInstaAlertReqDto.class);

            if(callbackInstaAlertReqDto != null) {

                Character virtualIdLastCharacter = bankingUtil.getVirtualIdLastCharacter(callbackInstaAlertReqDto.getVaNumber());

                if(virtualIdLastCharacter.equals('X')) {

                    //LMS
                    AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByVirtualId(callbackInstaAlertReqDto.getVaNumber());

                    if (agrMasterAgreement == null) {

                        callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                .status("F")
                                .statusDesc("Failure")
                                .errorCode("1001")
                                .errorDesc("VirtualAccountNumber provided does not exist")
                                .build();

                        try {
                            CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                                    .jsonData(objectMapper.writeValueAsString(callbackInstaAlertReqDto))
                                    .build();

                            bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

                        } catch (Exception e) {
                            e.printStackTrace();

                            log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                            log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());

                            callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                    .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                    .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                    .status("F")
                                    .statusDesc("Failure")
                                    .errorCode("1001")
                                    .errorDesc("VirtualAccountNumber provided does not exist")
                                    .build();

                        }

                    } else {

                        //Update Bank Payment
                        UpdateBankPaymentReqDto updateBankPaymentReqDto = UpdateBankPaymentReqDto.builder()
                                .utrNo(callbackInstaAlertReqDto.getUtrNo())
                                .amount(Double.parseDouble(callbackInstaAlertReqDto.getBatchAmt()))
                                .instaAlertJsonData(objectMapper.writeValueAsString(callbackInstaAlertReqDto))
                                .paymentStatus("Received")
                                .build();

                        Boolean bankPaymentRes = bankPaymentService.updateBankPayment(callbackInstaAlertReqDto.getVaNumber(), updateBankPaymentReqDto);
                        log.info("IDFCCallbackService :: callbackInstaAlert :: bankPaymentRes: {}", bankPaymentRes);

                        callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                .status("S")
                                .statusDesc("Success")
                                .errorCode("000")
                                .errorDesc("Success")
                                .build();

                    }
                } else {

                    //LOS
                    String requestBody = new Gson().toJson(callbackInstaAlertReqDto);
                    log.info("IDFCCallbackService :: callbackInstaAlert :: Request Body: {}", requestBody);

                    Unirest.setTimeouts(0, 0);
                    HttpResponse<Boolean> response = Unirest.post(creditBaseUrl + "/banking/idfc/callback-insta-alert")
                            .header("Content-Type", "application/json")
                            .header("Authorization", creditOneYearToken)
                            .body(requestBody)
                            .asObject(Boolean.class);

                    log.info("IDFCCallbackService :: callbackInstaAlert :: Response: {}", response.getBody());

                    if(response.getStatus() == 200) {

                        if(response.getBody()) {

                            callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                    .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                    .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                    .status("S")
                                    .statusDesc("Success")
                                    .errorCode("000")
                                    .errorDesc("Success")
                                    .build();

                        } else {
                            callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                    .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                    .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                    .status("F")
                                    .statusDesc("Failure")
                                    .errorCode("1001")
                                    .errorDesc("VirtualAccountNumber provided does not exist")
                                    .build();
                        }

                    } else {
                        callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                                .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                                .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                                .status("F")
                                .statusDesc("Failure")
                                .errorCode("1001")
                                .errorDesc("VirtualAccountNumber provided does not exist")
                                .build();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("IDFCCallbackService :: callbackInstaAlert :: Method: callbackInstaAlert");
            log.error("IDFCCallbackService :: callbackInstaAlert :: Request: {}", httpServletRequestToString(request));
            log.error("IDFCCallbackService :: callbackInstaAlert :: Error: {}", e.getMessage());

            callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                    .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                    .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                    .status("F")
                    .statusDesc("Failure")
                    .errorCode("1001")
                    .errorDesc("VirtualAccountNumber provided does not exist")
                    .build();

            try {
                CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                        .jsonData(objectMapper.writeValueAsString(callbackInstaAlertReqDto))
                        .error(e.getMessage())
                        .build();

                bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

            } catch (Exception e1) {
                e1.printStackTrace();

                log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                log.error("BankPaymentService :: createBankPayment :: Error: {}", e1.getMessage());

                callbackInstaAlertResDto = CallbackInstaAlertResDto.builder()
                        .corRefNo(callbackInstaAlertReqDto.getVaNumber())
                        .reqrefNo(callbackInstaAlertReqDto.getUtrNo())
                        .status("F")
                        .statusDesc("Failure")
                        .errorCode("1001")
                        .errorDesc("VirtualAccountNumber provided does not exist")
                        .build();
            }
        }

        return callbackInstaAlertResDto;

    }

    private String httpServletRequestToString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("Request Method = [" + request.getMethod() + "], ");
        sb.append("Request URL Path = [" + request.getRequestURL() + "], ");

        String headers =
                Collections.list(request.getHeaderNames()).stream()
                        .map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)) )
                        .collect(Collectors.joining(", "));

        if (headers.isEmpty()) {
            sb.append("Request headers: NONE,");
        } else {
            sb.append("Request headers: ["+headers+"],");
        }

        String parameters =
                Collections.list(request.getParameterNames()).stream()
                        .map(p -> p + " : " + Arrays.asList( request.getParameterValues(p)) )
                        .collect(Collectors.joining(", "));

        if (parameters.isEmpty()) {
            sb.append("Request parameters: NONE.");
        } else {
            sb.append("Request parameters: [" + parameters + "].");
        }

        return sb.toString();
    }
}
