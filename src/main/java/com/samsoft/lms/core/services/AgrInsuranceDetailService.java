package com.samsoft.lms.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.dto.CreateAgrInsuranceDetailReqDto;
import com.samsoft.lms.core.entities.AgrInsuranceDetail;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.repositories.AgrInsuranceDetailRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;

@Service
@Slf4j
public class AgrInsuranceDetailService {

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    @Autowired
    private AgrInsuranceDetailRepository agrInsuranceDetailRepository;

    public AgrInsuranceDetail createAgrInsuranceDetail(CreateAgrInsuranceDetailReqDto createAgrInsuranceDetailReqDto) throws Exception {

        try {

            log.info("AgrInsuranceDetailService :: createAgrInsuranceDetail :: Request Body: {}", createAgrInsuranceDetailReqDto);

            if(createAgrInsuranceDetailReqDto != null) {

                AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByMastAgrId(createAgrInsuranceDetailReqDto.getMastAgrId());

                AgrInsuranceDetail agrInsuranceDetail = AgrInsuranceDetail.builder()
                        .insuranceCompany(createAgrInsuranceDetailReqDto.getInsuranceCompany())
                        .insurancePremiumAmount(createAgrInsuranceDetailReqDto.getInsurancePremiumAmount())
                        .customerId(agrMasterAgreement.getCustomerId())
                        .masterAgreement(agrMasterAgreement)
                        .build();

               return agrInsuranceDetailRepository.save(agrInsuranceDetail);

            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("AgrInsuranceDetailService :: createAgrInsuranceDetail :: Method: createAgrInsuranceDetail");
            log.error("AgrInsuranceDetailService :: createAgrInsuranceDetail :: Request: createAgrInsuranceDetailReqDto: {}", createAgrInsuranceDetailReqDto);
            log.error("AgrInsuranceDetailService :: createAgrInsuranceDetail :: Error: {}", e.getMessage());

            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return null;
    }


    public AgrInsuranceDetail getinsuranceDetail(String sCustomerId) throws Exception {
        try{
            log.info("Request: sCustomerId===> " + sCustomerId);
            return agrInsuranceDetailRepository.findBysCustomerId(sCustomerId);

        }
        catch(Exception e){
            e.printStackTrace();
            log.error("AgrInsuranceDetailService :: getinsuranceDetail :: Method: getinsuranceDetail");
            log.error("AgrInsuranceDetailService :: getinsuranceDetail :: Request: createAgrInsuranceDetailReqDto: {}", agrInsuranceDetailRepository);
            log.error("AgrInsuranceDetailService :: getinsuranceDetail :: Error: {}", e.getMessage());
            throw new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
