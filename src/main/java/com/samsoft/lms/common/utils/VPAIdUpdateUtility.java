package com.samsoft.lms.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.samsoft.lms.banking.utils.BankingUtil;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Slf4j
public class VPAIdUpdateUtility {

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    @Autowired
    private BankingUtil bankingUtil;

    @Transactional
    @PostConstruct
    public void vpaIdUpdate() throws Exception {

        try {

            List<AgrMasterAgreement> agrMasterAgreementList =  agrMasterAgreementRepository.findByVpaIdIsNull();
//            log.info("VirtualIdUpdateUtil :: vpaIdUpdate :: agrMasterAgreementList: {}", agrMasterAgreementList);

            if(!agrMasterAgreementList.isEmpty()) {

                for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
//                    agrMasterAgreement.setVpaId(bankingUtil.createVPAId(agrMasterAgreement.getMastAgrId()));
                    agrMasterAgreementRepository.save(agrMasterAgreement);
                }

//                log.info("VirtualIdUpdateUtil :: vpaIdUpdate :: Updated.");
            }

        } catch (Exception e) {
            e.printStackTrace();

//            log.error("VirtualIdUpdateUtil :: vpaIdUpdate :: Method: vpaIdUpdate");
//            log.error("VirtualIdUpdateUtil :: vpaIdUpdate :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
