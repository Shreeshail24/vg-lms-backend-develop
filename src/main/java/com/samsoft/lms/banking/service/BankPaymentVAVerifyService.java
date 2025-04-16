package com.samsoft.lms.banking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.banking.dto.request.CreateBankPaymentLogsReqDto;
import com.samsoft.lms.banking.dto.request.CreateBankPaymentVAVerifyReqDto;
import com.samsoft.lms.banking.entity.BankPaymentVAVerify;
import com.samsoft.lms.banking.repository.BankPaymentVAVerifyRepository;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Slf4j
public class BankPaymentVAVerifyService {

    @Autowired
    private BankPaymentVAVerifyRepository bankPaymentVAVerifyRepository;

    @Transactional
    public BankPaymentVAVerify createBankPaymentVAVerify(CreateBankPaymentVAVerifyReqDto createBankPaymentVAVerifyReqDto) {

        try {

            BankPaymentVAVerify bankPaymentVAVerify = BankPaymentVAVerify.builder()
                    .virtualId(createBankPaymentVAVerifyReqDto.getVirtualId())
                    .amount(createBankPaymentVAVerifyReqDto.getAmount())
                    .vaValidationJsonData(createBankPaymentVAVerifyReqDto.getVaValidationJsonData())
                    .createdDateTime(new Date())
                    .build();

           return bankPaymentVAVerifyRepository.save(bankPaymentVAVerify);

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentVAVerifyService :: createBankPaymentVAVerify :: Method: createBankPaymentVAVerify");
            log.error("BankPaymentVAVerifyService :: createBankPaymentVAVerify :: Request: {}", createBankPaymentVAVerifyReqDto);
            log.error("BankPaymentVAVerifyService :: createBankPaymentVAVerify :: Error: {}", e.getMessage());
        }

        return null;
    }
}
