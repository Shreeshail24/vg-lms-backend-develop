package com.samsoft.lms.transaction.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReqStatusUpdateService {

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private AgrTrnRequestStatusRepository reqStatusRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String updateRequestStatus(Integer reqId, String status) {
		String result = "success";
		try {
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo.findById(reqId);
			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
			}

			reqHdr.setReqStatus(status);

			reqHdrRepo.save(reqHdr);

			AgrTrnRequestStatus reqStatus = reqStatusRepo.findByRequestHdrReqId(reqId);
			reqStatus.setReqStatus(status);
			reqStatus.setRequestHdr(reqHdr);
			reqStatusRepo.save(reqStatus);
			
		} catch (Exception e) {
			throw e;
		}

		return result;
	}
}
