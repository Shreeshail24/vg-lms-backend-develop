package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.request.dto.ChargesBookingDto;
import com.samsoft.lms.request.entities.AgrTrnReqChargesBookingDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;

@Service

public class ChargesBookingService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository agreementRepo;

	@Autowired
	private AgrTrnRequestHdrRepository requestHeaderRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean chargesBooking(ChargesBookingDto parameters) throws Exception {
		Boolean result = true;
		AgrTrnRequestHdr requestHeader = new AgrTrnRequestHdr();

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.US);
			//LocalDate requestDate = LocalDate.parse(parameters.getRequestDate(), formatter);
			 Date requestDate = new SimpleDateFormat(dateFormat).parse(parameters.getRequestDate());
			requestHeader.setMasterAgreement(agreementRepo.findByMastAgrId(parameters.getMasterAgreementId()));
			requestHeader.setDtRequest(requestDate);
			requestHeader.setActivityCode("CHARGES_BOOKING");
			requestHeader.setReqStatus(parameters.getRequestStatus());
			requestHeader.setFlowType(parameters.getFlowType());
			requestHeader.setReason(parameters.getReason());
			requestHeader.setRemark(parameters.getRemark());
			requestHeader.setUserId(parameters.getUserId());

			AgrTrnReqChargesBookingDtl chargesBooking = new AgrTrnReqChargesBookingDtl();
			chargesBooking.setLoanId(parameters.getLoanId());
			chargesBooking.setDtChargeBook(requestDate);
			chargesBooking.setTranHead(parameters.getTranHead());
			chargesBooking.setChargeAmount(parameters.getChargeAmount());
			chargesBooking.setInstallmentNo(parameters.getInstallmentNo());
			chargesBooking.setReason(parameters.getChargeBookReason());
			chargesBooking.setRemark(parameters.getChargeBookRemark());
			chargesBooking.setUserId(parameters.getUserId());
			chargesBooking.setRequestHdr(requestHeader);
		
			AgrTrnRequestStatus requestStatus = new AgrTrnRequestStatus();
			requestStatus.setReqStatus("PENDING");
			requestStatus.setReason(parameters.getReason());
			requestStatus.setRemark(parameters.getRemark());
			requestStatus.setDtReqChangeDate(requestDate);
			requestStatus.setUserId(parameters.getUserId());
			requestStatus.setRequestHdr(requestHeader);
			ArrayList<AgrTrnRequestStatus> listRequestStatus = new ArrayList<AgrTrnRequestStatus>();
			listRequestStatus.add(requestStatus);
			requestHeader.setRequestStatus(listRequestStatus);

			requestHeaderRepo.save(requestHeader);

		} catch (Exception e) {
			result = false;
			throw new Exception(e.getMessage());
		}

		return result;
	}
}
