package com.samsoft.lms.odMgmt.utility;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.transaction.entities.AgrTrnSfLimitDtls;
import com.samsoft.lms.transaction.repositories.AgrTrnSfLimitDtlsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierFinanceUtility {

	@Autowired
	private CustApplLimitSetupRepository custLimitRepo;
	@Autowired
	private AgrTrnSfLimitDtlsRepository sfLimitRepo;
	@Autowired
	private AgrMasterAgreementRepository agrRepo;
	@Autowired
	private CommonServices commonService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public void updateCustomerLimit(String mastAgrId, String originationApplnNo, String customerId, String productCode,
			String method, Double amount, Integer refTranId, String userId, Date tranDate) {
		try {
			log.info("updateLimit ");
			AgrMasterAgreement masterAgr = agrRepo.findByMastAgrId(mastAgrId);
			CustApplLimitSetup custLimit = custLimitRepo
					.findByOriginationApplnNoAndCustomerIdAndProductCode(originationApplnNo, customerId, productCode);
			String tranType = "";
			double availableLimit = custLimit.getAvailableLimit();
			if (method.equalsIgnoreCase("ADD")) {
				log.info("updateLimit 1");
				tranType = "CR";
				custLimit.setAvailableLimit(commonService.numberFormatter(custLimit.getAvailableLimit() + amount));
				custLimit.setUtilizedLimit(commonService.numberFormatter(custLimit.getUtilizedLimit() - amount));
			} else {
				tranType = "DR";
				custLimit.setAvailableLimit(commonService.numberFormatter(custLimit.getAvailableLimit() - amount));
				custLimit.setUtilizedLimit(commonService.numberFormatter(custLimit.getUtilizedLimit() + amount));
			}

			AgrTrnSfLimitDtls sfLimit = new AgrTrnSfLimitDtls();
			sfLimit.setAvailableLimit(availableLimit);
			sfLimit.setCustLimit(custLimit);
			sfLimit.setDepositAmount(method.equals("ADD") ? amount : 0);
			sfLimit.setDtTranDate(tranDate);
			sfLimit.setMaster(masterAgr);
			sfLimit.setRefTranId(refTranId);
			sfLimit.setSactionedLimit(custLimit.getLimitSanctioned());
			sfLimit.setTranType(tranType);
			sfLimit.setUserId(userId);
			sfLimit.setUtilizedLimitCls(custLimit.getUtilizedLimit());
			sfLimit.setUtilizedLimitOpn(
					commonService.numberFormatter(custLimit.getLimitSanctioned() - custLimit.getUtilizedLimit()));
			sfLimit.setWithdrawalAmount(method.equals("ADD") ? 0 : amount);

			custLimitRepo.save(custLimit);
			sfLimitRepo.save(sfLimit);

		} catch (Exception e) {
			throw e;
		}
	}

}
