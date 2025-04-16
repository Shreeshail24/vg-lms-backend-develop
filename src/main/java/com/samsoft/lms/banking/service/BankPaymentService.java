package com.samsoft.lms.banking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.banking.dto.request.CreateBankPaymentLogsReqDto;
import com.samsoft.lms.banking.dto.request.CreateBankPaymentReqDto;
import com.samsoft.lms.banking.dto.request.CreateBankPaymentVAVerifyReqDto;
import com.samsoft.lms.banking.dto.request.UpdateBankPaymentReqDto;
import com.samsoft.lms.banking.entity.BankPayment;
import com.samsoft.lms.banking.entity.BankPaymentVAVerify;
import com.samsoft.lms.banking.idfc.dto.request.UpdatePGPaymentReqDto;
import com.samsoft.lms.banking.repository.BankPaymentRepository;
import com.samsoft.lms.common.utils.DateUtil;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.DreApplicationService;
import com.samsoft.lms.request.dto.DreAllocationDto;
import com.samsoft.lms.request.dto.DreRequestOnlineDto;
import com.samsoft.lms.request.services.DreRequestService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BankPaymentService {

    @Autowired
    private BankPaymentRepository bankPaymentRepository;

    @Autowired
    private AgrMasterAgreementRepository agreementRepository;

    @Autowired
    private DreRequestService dreRequestService;

    @Autowired
    private DreApplicationService dreAppService;

    @Autowired
    private BankPaymentLogsService bankPaymentLogsService;

    @Autowired
    private BankPaymentVAVerifyService bankPaymentVAVerifyService;

    @Transactional
    public Boolean createBankPayment(CreateBankPaymentReqDto createBankPaymentReqDto) throws Exception {

        try {

            if (!createBankPaymentReqDto.getSource().equalsIgnoreCase("IDFC")) {

                BankPayment bankPayment = BankPayment.builder()
                        .virtualId(createBankPaymentReqDto.getVirtualId())
                        .amount(createBankPaymentReqDto.getAmount())
                        .source(createBankPaymentReqDto.getSource())
                        .paymentStatus(createBankPaymentReqDto.getPaymentStatus())
                        .createdDateTime(new Date())
                        .paymentSessionId(createBankPaymentReqDto.getPaymentSessionId())
                        .cfOrderId(createBankPaymentReqDto.getCfOrderId())
                        .orderStatus(createBankPaymentReqDto.getOrderStatus())
                        .paymentGatewayJsonData(createBankPaymentReqDto.getPaymentGatewayJsonData())
                        .mastAgrId(createBankPaymentReqDto.getMastAgrId())
                        .customerName(createBankPaymentReqDto.getCustomerName())
                        .build();

                bankPayment = bankPaymentRepository.save(bankPayment);

                try {
                    CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                            .paymentId(bankPayment.getPaymentId())
                            .jsonData(createBankPaymentReqDto.getPaymentGatewayJsonData())
                            .build();

                    bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

                } catch (Exception e) {
                    e.printStackTrace();

                    log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                    log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());
                }

            } else {

                CreateBankPaymentVAVerifyReqDto createBankPaymentVAVerifyReqDto = CreateBankPaymentVAVerifyReqDto.builder()
                        .virtualId(createBankPaymentReqDto.getVirtualId())
                        .amount(createBankPaymentReqDto.getAmount())
                        .vaValidationJsonData(createBankPaymentReqDto.getVaValidationJsonData())
                        .build();

                BankPaymentVAVerify bankPaymentVAVerify = bankPaymentVAVerifyService.createBankPaymentVAVerify(createBankPaymentVAVerifyReqDto);
                log.info("BankPaymentService :: createBankPayment :: Else :: BankPaymentVAVerify: {}", bankPaymentVAVerify);

                try {
                    CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                            .vaPaymentId(bankPaymentVAVerify.getVaPaymentId())
                            .jsonData(createBankPaymentReqDto.getVaValidationJsonData())
                            .build();

                    bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

                } catch (Exception e) {
                    e.printStackTrace();

                    log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                    log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());
                }
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentService :: createBankPayment :: Method: createBankPayment");
            log.error("BankPaymentService :: createBankPayment :: Request: {}", createBankPaymentReqDto);
            log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Boolean updateBankPayment(String virtualId, UpdateBankPaymentReqDto updateBankPaymentReqDto) throws Exception {

        try {

            BankPayment bankPayment = BankPayment.builder()
                    .virtualId(virtualId)
                    .amount(updateBankPaymentReqDto.getAmount())
                    .instaAlertJsonData(updateBankPaymentReqDto.getInstaAlertJsonData())
                    .source("IDFC")
                    .paymentStatus(updateBankPaymentReqDto.getPaymentStatus())
                    .utrNo(updateBankPaymentReqDto.getUtrNo())
                    .createdDateTime(new Date())
                    .build();

            bankPayment = bankPaymentRepository.save(bankPayment);

            Boolean response = this.processPayment(bankPayment);
            log.error("BankPaymentService :: updatePGPayment :: processPayment Response: {}", response);

            if (response) {
                bankPayment.setPaymentStatus("Processed");
                bankPayment.setUpdatedDateTime(new Date());

                bankPaymentRepository.save(bankPayment);
            }

            try {
                CreateBankPaymentLogsReqDto createBankPaymentLogsReqDto = CreateBankPaymentLogsReqDto.builder()
                        .paymentId(bankPayment.getPaymentId())
                        .jsonData(updateBankPaymentReqDto.getInstaAlertJsonData())
                        .build();

                bankPaymentLogsService.createBankPaymentLogs(createBankPaymentLogsReqDto);

            } catch (Exception e) {
                e.printStackTrace();

                log.error("BankPaymentService :: createBankPayment :: Method: createBankPaymentLogs");
                log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentService :: createBankPayment :: Method: createBankPayment");
            log.error("BankPaymentService :: createBankPayment :: Request: {}", updateBankPaymentReqDto);
            log.error("BankPaymentService :: createBankPayment :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Boolean updatePGPayment(String virtualId, UpdatePGPaymentReqDto updatePGPaymentReqDto) throws Exception {

        try {

            BankPayment bankPayment = bankPaymentRepository.findByVirtualIdAndPaymentStatusAndPaymentSessionId(virtualId, "PGORDERCRE", updatePGPaymentReqDto.getPaymentSessionId());
            log.error("BankPaymentService :: updatePGPayment :: bankPayment: {}", bankPayment);

            if (bankPayment != null) {

                bankPayment.setUtrNo(updatePGPaymentReqDto.getOrderId());
                bankPayment.setPaymentStatus(updatePGPaymentReqDto.getStatus());
                bankPayment.setUpdatedDateTime(new Date());

                bankPayment = bankPaymentRepository.save(bankPayment);

                if (updatePGPaymentReqDto.getStatus().equalsIgnoreCase("SUCCESS")) {

                    Boolean response = this.processPayment(bankPayment);
                    log.error("BankPaymentService :: updatePGPayment :: processPayment Response: {}", response);

                    if (response) {
                        bankPayment.setPaymentStatus("Processed");
                        bankPayment.setUpdatedDateTime(new Date());

                        bankPaymentRepository.save(bankPayment);
                    }
                } else {
                    bankPayment.setPaymentStatus("Received");
                    bankPayment.setUpdatedDateTime(new Date());

                    bankPaymentRepository.save(bankPayment);
                }

                return Boolean.TRUE;
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentService :: updatePGPayment :: Method: updatePGPayment");
            log.error("BankPaymentService :: updatePGPayment :: Request: {}", updatePGPaymentReqDto);
            log.error("BankPaymentService :: updatePGPayment :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return Boolean.FALSE;
    }

    @Transactional
    private Boolean processPayment(BankPayment bankPayment) throws Exception {

        List<DreAllocationDto> dreAllocationDtoList = new ArrayList<>();

        try {

            AgrMasterAgreement masterAgreement = agreementRepository.findByVirtualId(bankPayment.getVirtualId());
            log.error("BankPaymentService :: processPayment :: masterAgreement: {}", masterAgreement);

            if (masterAgreement == null) {
                throw new ApiException("Master Agreement Details Not Found.", HttpStatus.NOT_FOUND);
            }

            //Create DRE Request Online
            DreAllocationDto dreAllocationDto = DreAllocationDto.builder()
                    .loanId("")
                    .allocatedAmount(0d)
                    .tranCategory("")
                    .tranHead("")
                    .installmentNo(0)
                    .chargeBookTranId(0)
                    .build();

            dreAllocationDtoList.add(dreAllocationDto);

            DreRequestOnlineDto dreRequestOnlineDto = DreRequestOnlineDto.builder()
                    .masterAgreementId(masterAgreement.getMastAgrId())
                    .requestDate(DateUtil.convertDateToString(new Date()))
                    .flowType("NF")
                    .requestStatus("PND")
                    .reason("CP")
                    .remark("CP")
                    .userId("SYSTEM")
                    .instrumentDate(DateUtil.convertDateToString(new Date()))
                    .instrumentAmount(bankPayment.getAmount())
                    .instrumentType("UPI")
                    .paymentType("ALLDUES")
                    .paymentFor("ALLDUES")
                    .paymentMode("ONLINE")
                    .depositRefNo(bankPayment.getCfOrderId())
                    .collectionAgency("AGENCY1")
                    .collectedBy("COLLECTOR1")
                    .provisionalReceiptFlag("N")
                    .utrNo(bankPayment.getCfOrderId())
                    .ifscCode("")
                    .bankCode("")
                    .bankBranchCode("")
                    .dreAllocation(dreAllocationDtoList)
                    .build();

            String reqId = dreRequestService.dreRequestOnline(dreRequestOnlineDto);
            log.error("BankPaymentService :: processPayment :: dreRequestOnline Response: {}", reqId);

            if (reqId != null) {

                //Create Transactional DRE Application
                String response = dreAppService.dreApplication(Integer.parseInt(reqId), masterAgreement.getMastAgrId(), new Date(), "SYSTEM");
                log.error("BankPaymentService :: processPayment :: dreApplication Response: {}", response);

                return Boolean.TRUE;

            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("BankPaymentService :: processPayment :: Method: processPayment");
            log.error("BankPaymentService :: processPayment :: Request: {}", bankPayment);
            log.error("BankPaymentService :: processPayment :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return Boolean.FALSE;
    }

}
