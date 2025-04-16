package com.samsoft.lms.request.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.dto.ChargesReqBookingDto;
import com.samsoft.lms.request.dto.RefundReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqChargesBookingDtl;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesBookingDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.ChargesBookingDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.services.TransactionService;

@Service

public class ChargesBookingRequestService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository agreementRepo;

	@Autowired
	private AgrTrnReqChargesBookingDtlRepository chargesRepo;

	@Autowired
	private TransactionService tranService;

	@Autowired
	private AgrTrnRequestHdrRepository requestHeaderRepo;

	@Autowired
	private CommonServices commService;

	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private AgrTrnRequestStatusRepository agrTrnRequestStatus;
	
	@Autowired
	private ApprovalService approvalService;

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class, Error.class })
	public Boolean chargesReqBooking(ChargesReqBookingDto parameters) throws Exception {
		Boolean result = true;
		AgrTrnRequestHdr requestHeader ;
		if (parameters.getReqId() != null) {
		    requestHeader = requestHeaderRepo.findById(parameters.getReqId())
		                    .orElseThrow(() -> new TransactionDataNotFoundException("Request Header not found for id: " + parameters.getReqId()));
		} else {
		    requestHeader = new AgrTrnRequestHdr();
		}
		AgrTrnReqChargesBookingDtl chargesBooking;
		AgrTrnRequestStatus requestStatus;

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.US);
			// LocalDate requestDate = LocalDate.parse(parameters.getRequestDate(),
			// formatter);
			Date requestDate = new SimpleDateFormat(dateFormat).parse(parameters.getRequestDate());
			requestHeader.setMasterAgreement(agreementRepo.findByMastAgrId(parameters.getMasterAgreementId()));
			requestHeader.setDtRequest(requestDate);
			requestHeader.setActivityCode("CHARGES_BOOKING");
			requestHeader.setReqStatus(parameters.getRequestStatus());
			requestHeader.setFlowType(parameters.getFlowType());
			if ("NoFlow".equals(parameters.getFlowType())) {
				requestHeader.setReqStatus("APPROVED");
			} else {
				requestHeader.setReqStatus(parameters.getRequestStatus());
			}
			requestHeader.setReason(parameters.getReason());
			requestHeader.setRemark(parameters.getRemark());
			requestHeader.setUserId(parameters.getUserId());
			
			if (parameters.getReqId() != null) {

				AgrTrnReqChargesBookingDtl existingChargesBooking = chargesRepo
						.findByRequestHdrReqId(parameters.getReqId());
				if (existingChargesBooking != null) {
					chargesBooking = existingChargesBooking;
				} else {
					chargesBooking = new AgrTrnReqChargesBookingDtl();
				}
			} else {
				chargesBooking = new AgrTrnReqChargesBookingDtl();
			}
			

			chargesBooking.setLoanId(parameters.getLoanId());
			chargesBooking.setDtChargeBook(requestDate);
			chargesBooking.setTranHead(parameters.getTranHead());
			chargesBooking.setChargeAmount(commService.numberFormatter(parameters.getChargeAmount()));
			chargesBooking.setInstallmentNo(parameters.getInstallmentNo());
			chargesBooking.setReason(parameters.getChargeBookReason());
			chargesBooking.setRemark(parameters.getChargeBookRemark());
			chargesBooking.setUserId(parameters.getUserId());
			chargesBooking.setRequestHdr(requestHeader);
			chargesBooking.setTotalAmount(parameters.getTotalAmount());
			
			AgrTrnRequestHdr saveReqHdr = requestHeaderRepo.save(requestHeader);
			
			if (parameters.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = agrTrnRequestStatus.findByRequestHdrReqId(parameters.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(requestHeader);
					existingRequestStatus.setDtReqChangeDate(requestDate);
					existingRequestStatus.setReqStatus(parameters.getRequestStatus());
					existingRequestStatus.setReason(parameters.getReason());
					existingRequestStatus.setRemark(parameters.getRemark());
					
					agrTrnRequestStatus.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = parameters.getAllocatedUserId();	
			if (("NoFlow".equals(parameters.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(requestHeader);
		    	status.setUserId(parameters.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(requestDate);
	    	    status.setReason(parameters.getReason());
	    	    status.setRemark(parameters.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
	    	    agrTrnRequestStatus.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(requestHeader.getReqId(),userId,
		        		parameters.getReason(), parameters.getRemark(),parameters.getRequestStatus(),requestHeader.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }

			
			AgrTrnReqChargesBookingDtl saveCharges = chargesRepo.save(chargesBooking);

			
			if ("NoFlow".equalsIgnoreCase(parameters.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(parameters.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(parameters.getRequestStatus()))) {
			  ChargesBookingDto chargeDto = new ChargesBookingDto();
			  chargeDto.setMastAgrId(saveCharges.getRequestHdr().getMasterAgreement().
			  getMastAgrId()); chargeDto.setLoanId(saveCharges.getLoanId()); String
			  strTranDate = new
			  SimpleDateFormat(dateFormat).format(saveCharges.getDtChargeBook());
			  chargeDto.setTrandDate(strTranDate); chargeDto.setSource("APP");
			  chargeDto.setSourceId(saveReqHdr.getReqId().toString());
			  chargeDto.setTranHead(saveCharges.getTranHead());
			  chargeDto.setChargeAmount(commService.numberFormatter(saveCharges.
			  getChargeAmount())); chargeDto.setReason(saveCharges.getReason());
			  chargeDto.setRemark(saveCharges.getRemark());
			  chargeDto.setInstallmentNo(saveCharges.getInstallmentNo());
			  chargeDto.setUserId(saveCharges.getUserId());
			  
			  tranService.chargeBookingApply(chargeDto);
			}	 

		} catch (TransactionDataNotFoundException e) {
			result = false;
			throw e;
		} catch (Exception e) {
			result = false;
			throw e;
		}

		return result;
	}

	// ----- Method to get BookingCharges requests by status, date and reqId
	public Page<ChargesReqBookingDto> getBookingChargesReqList(List<String> statusList, String activityCode,
			Integer reqNo, String date, Pageable pageable) throws Exception {

		List<ChargesReqBookingDto> chargesReqBookingDtoList = new ArrayList<>();
		try {

			List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

			if (!statusList.isEmpty() || reqNo == 0 && date == null) {
				
				agrTrnRequestHdrList = requestHeaderRepo.getRequestsByStatusesAndActivityCode(statusList, activityCode);
				 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
		                throw new CoreDataNotFoundException("No records found for the provided status list: " + statusList);
		            }

			} else if (reqNo != 0 || statusList == null || date == null) {
				
				AgrTrnRequestHdr agrTrnRequestHdr = requestHeaderRepo.findByReqIdAndActivityCode(reqNo, activityCode);
				 if (agrTrnRequestHdr == null) {
		                throw new CoreDataNotFoundException("No record found for the given request ID: " + reqNo);
		            }
				agrTrnRequestHdrList.add(agrTrnRequestHdr);

			} else if (date != null || statusList == null || reqNo == null) {
				// change string date into Date format
				String dateFormat = env.getProperty("lms.global.date.format");
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				Date requestedDate = sdf.parse(date);
				agrTrnRequestHdrList = requestHeaderRepo.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
				 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
		                throw new CoreDataNotFoundException("No records found for the requested date: " + date);
		            }	
			
			}

			if (!agrTrnRequestHdrList.isEmpty() && agrTrnRequestHdrList != null) {

				for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
					ChargesReqBookingDto chargesReqBookingDto = new ChargesReqBookingDto();
					chargesReqBookingDto.setReason(agrTrnRequestHdr.getReason());
					chargesReqBookingDto.setRemark(agrTrnRequestHdr.getRemark());
					chargesReqBookingDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
					chargesReqBookingDto.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
					chargesReqBookingDto.setUserId(agrTrnRequestHdr.getUserId());
					chargesReqBookingDto.setReqId(agrTrnRequestHdr.getReqId());
					chargesReqBookingDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());

					AgrTrnReqChargesBookingDtl agrTrnReqChargesBookingDtl = chargesRepo
							.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());

					if (agrTrnReqChargesBookingDtl != null) {
						chargesReqBookingDto.setChargeAmount(agrTrnReqChargesBookingDtl.getChargeAmount());
						chargesReqBookingDto.setTranHead(agrTrnReqChargesBookingDtl.getTranHead());
						chargesReqBookingDto.setChargeBookReason(agrTrnReqChargesBookingDtl.getReason());
						chargesReqBookingDto.setChargeBookRemark(agrTrnReqChargesBookingDtl.getRemark());
						chargesReqBookingDto.setTotalAmount(agrTrnReqChargesBookingDtl.getTotalAmount());
					}
					
					AgrTrnRequestStatus agrTrnReqStatus = agrTrnRequestStatus
							.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
					if (agrTrnReqStatus != null) {
						chargesReqBookingDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
					
						}	
					chargesReqBookingDtoList.add(chargesReqBookingDto);
				}
			}

			// Return a Page object for DTOs using convertToPage
			return pageableUtils.convertToPage(chargesReqBookingDtoList, pageable);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
