package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.request.dto.RecallReqDto;
import com.samsoft.lms.request.dto.RecallReqLoanReceivableListDto;
import com.samsoft.lms.request.entities.AgrTrnReqRecall;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqRecallRepository;

@Service
public class RecallReqService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnReqRecallRepository recallReqRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String recallReq(RecallReqDto recallDto) {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(recallDto.getMastAgrId());
			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setActivityCode("RECALL");
			hdr.setDtRequest(sdf.parse(recallDto.getDtRequest()));
			hdr.setFlowType("NF");
			hdr.setMasterAgreement(masterObj);
			hdr.setReason(recallDto.getReason());
			hdr.setRemark(recallDto.getRemark());
			hdr.setReqStatus(recallDto.getReqStatus());
			hdr.setUserId(recallDto.getUserId());

			for (RecallReqLoanReceivableListDto loanList : recallDto.getLoanReceivables()) {
				AgrTrnReqRecall recall = new AgrTrnReqRecall();

				recall.setRequestHdr(hdr);
				recall.setMasterAgrId(recallDto.getMastAgrId());
				recall.setLoanId(loanList.getLoanId());
				recall.setDtTranDate(sdf.parse(recallDto.getDtRequest()));
				recall.setPortfolioCode(loanList.getPortfolio());
				recall.setRecallStatus(recallDto.getRecallStatus());
				recall.setRemark(recallDto.getRemark());
				recall.setOutstandingAmount(loanList.getOutstandingAmount());
				recall.setBpiAmount(loanList.getBpiAmount());
				recall.setTotalOutstandingAmount(loanList.getTotalOutstanding());
				recall.setUserId(recallDto.getUserId());

				recallReqRepo.save(recall);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
