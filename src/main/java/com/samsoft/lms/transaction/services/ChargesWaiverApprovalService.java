package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.request.entities.AgrTrnReqChargesWaiverDtl;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesWaiverDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.transaction.dto.ChargesWaiverApplicationDto;

@Service
public class ChargesWaiverApprovalService {
	@Autowired
	private AgrTrnReqInstrumentRepository instReqRepo;
	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository instReqAlloRepo;
	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;
	@Autowired
	private AgrTrnReqChargesWaiverDtlRepository reqWaiverRepo;
	@Autowired
	private Environment env;
	@Autowired
	private ChargesWaiverApplicationService chargeAppService;
	@Autowired
	private AgrTrnReqChargesWaiverDtlRepository chargesWaiverRepo;

	public String chargesWaiverApproval(Integer reqId, String mastAgrId, String userId, Date tranDate) throws Exception {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			List<AgrTrnReqChargesWaiverDtl> waiverList = chargesWaiverRepo.findByRequestHdrReqId(reqId);

			if (waiverList.size() <= 0) {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			List<AgrTrnReqChargesWaiverDtl> reqWaiverList = reqWaiverRepo.findByRequestHdrReqId(reqId);
			for (AgrTrnReqChargesWaiverDtl param : reqWaiverList) {
				ChargesWaiverApplicationDto waiverApplicationDto = new ChargesWaiverApplicationDto();
				waiverApplicationDto.setMastAgrId(mastAgrId);
				waiverApplicationDto.setChargeBookTranId(param.getChargeBookTranId());
				waiverApplicationDto.setChargeWaiveAmount(param.getChargeWaiveAmount());
				waiverApplicationDto.setChargeWavaieRemark(param.getRemark());
				waiverApplicationDto.setChargeWaiveReason(param.getReasonCode());
				waiverApplicationDto.setInstallmentNo(param.getInstallmentNo());
				waiverApplicationDto.setLoanId(param.getLoanId());
				waiverApplicationDto.setSource("REQ");
				waiverApplicationDto.setSourceId(reqId.toString());
				waiverApplicationDto.setTranDate(sdf.format(tranDate));
				waiverApplicationDto.setTranHead(param.getTranHead());
				waiverApplicationDto.setUserId(userId);

				chargeAppService.chargesWaiverApplication(waiverApplicationDto);

			}

			reqStatusUpdateService.updateRequestStatus(reqId, "APR");
			return result;
		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

}
