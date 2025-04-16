package com.samsoft.lms.batch.services;

import java.util.Date;

import org.hibernate.internal.util.xml.ErrorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.EodLog;
import com.samsoft.lms.batch.entities.EodStatus;
import com.samsoft.lms.batch.repositories.EodLogRepository;
import com.samsoft.lms.batch.repositories.EodStatusRepository;

@Service
public class EodErrorService {
	@Autowired
	private EodLogRepository eodLogRepo;
	
	@Autowired
	private EodStatusRepository eodStatusRepo;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean eodStaus(String customerId, String processName, String processStatus, Date businessDate) {
		Boolean result = true;
		try {
			EodStatus status = new EodStatus();
			status.setCustomerId(customerId);
			status.setProcessName(processName);
			status.setProcessStatus(processStatus);
			status.setBusinessDate(businessDate);
			eodStatusRepo.save(status);
		} catch (Exception e) {
			result = false;
			EodLog error = new EodLog();
			error.setComponent("EOD_STATUS");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(customerId);
			error.setBusinessDate(businessDate);
			eodLogRepo.save(error);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean eodErrorLog(EodLog eodLog) {
		Boolean result = true;
		try {
			result = false;
			EodLog error = new EodLog();
			error.setComponent("EOD_STATUS");
			error.setErrorMessage(eodLog.getErrorMessage());
			error.setCustomer(eodLog.getCustomer());
			error.setBusinessDate(eodLog.getBusinessDate());
			eodLogRepo.save(error);
		} catch (Exception e) {
			result = false;
			EodLog error = new EodLog();
			error.setComponent("EOD_EXCEPTION");
			error.setErrorMessage(e.getMessage());
			eodLogRepo.save(error);
		}
		return result;
	}

}
