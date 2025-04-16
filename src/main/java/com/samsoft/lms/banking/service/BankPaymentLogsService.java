package com.samsoft.lms.banking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.banking.dto.request.CreateBankPaymentLogsReqDto;
import com.samsoft.lms.banking.entity.BankPaymentLogs;
import com.samsoft.lms.banking.repository.BankPaymentLogsRepository;
import com.samsoft.lms.core.exceptions.ApiException;

import java.util.Date;

@Service
@Slf4j
public class BankPaymentLogsService {

    @Autowired
    private BankPaymentLogsRepository bankPaymentLogsRepository;

    public void createBankPaymentLogs(CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto) throws Exception {

        try {

            BankPaymentLogs bankPaymentLogs = BankPaymentLogs.builder()
                    .paymentId(createBankPaymentLogsReqDto.getPaymentId())
                    .vaPaymentId(createBankPaymentLogsReqDto.getVaPaymentId())
                    .jsonData(createBankPaymentLogsReqDto.getJsonData())
                    .error(createBankPaymentLogsReqDto.getError())
                    .createdDateTime(new Date())
                    .build();

            bankPaymentLogsRepository.save(bankPaymentLogs);

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentLogsService :: createBankPaymentLogs :: Method: createBankPaymentLogs");
            log.error("BankPaymentLogsService :: createBankPaymentLogs :: Request: {}", createBankPaymentLogsReqDto);
            log.error("BankPaymentLogsService :: createBankPaymentLogs :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
