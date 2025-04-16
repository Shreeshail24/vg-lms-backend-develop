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
public class VirtualIdUpdateUtil {

	@Autowired
	private AgrMasterAgreementRepository agrMasterAgreementRepository;

	@Autowired
	private BankingUtil bankingUtil;

	@Transactional
	@PostConstruct
	public void virtualIdUpdate() throws Exception {

		try {

			List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository.findByVirtualIdIsNull();
//            log.info("VirtualIdUpdateUtil :: virtualIdUpdate :: agrMasterAgreementList: {}", agrMasterAgreementList);

			if (!agrMasterAgreementList.isEmpty()) {

				for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
//                    agrMasterAgreement.setVirtualId(bankingUtil.createVirtualId(agrMasterAgreement.getMastAgrId()));
					agrMasterAgreementRepository.save(agrMasterAgreement);
				}

//                log.info("VirtualIdUpdateUtil :: virtualIdUpdate :: Updated.");
			}

		} catch (Exception e) {
			e.printStackTrace();

//            log.error("VirtualIdUpdateUtil :: virtualIdUpdate :: Method: virtualIdUpdate");
//            log.error("VirtualIdUpdateUtil :: virtualIdUpdate :: Error: {}", e.getMessage());

			throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
