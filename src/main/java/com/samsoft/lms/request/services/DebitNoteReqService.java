package com.samsoft.lms.request.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.request.dto.DebitNoteReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqChargesBookingDtl;
import com.samsoft.lms.request.entities.AgrTrnReqDebitNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqDebitNoteDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.DebitNoteApplicationDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.services.DebitNoteService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DebitNoteReqService {

	@Autowired
	private AgrMasterAgreementRepository masterAgrRepo;
	@Autowired
	private Environment env;
	@Autowired
	private AgrTrnReqDebitNoteDtlRepository debitRepo;
	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;
	@Autowired
	private DebitNoteService debitService;
	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private ApprovalService approvalService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean debitNoteRequest(DebitNoteReqDto debitNoteDto) throws Exception {
		Boolean result = true;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		log.info("Debit Request Req " + debitNoteDto);
		try {
			AgrMasterAgreement master = masterAgrRepo.findByMastAgrId(debitNoteDto.getMastAgrId());
			log.info("AgrMasterAgreement===> " + master);
			
			AgrTrnRequestHdr hdr ;
			AgrTrnReqDebitNoteDtl debitNote;
			if (debitNoteDto.getReqId() != null) {
				hdr = reqHdrRepo.findById(debitNoteDto.getReqId())
			                    .orElseThrow(() -> new TransactionDataNotFoundException("Request Header not found for id: " + debitNoteDto.getReqId()));
			} else {
				hdr = new AgrTrnRequestHdr();
			}

//			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setMasterAgreement(master);
			hdr.setDtRequest(sdf.parse(debitNoteDto.getDtRequestDate()));
			hdr.setActivityCode("DEBIT_NOTE");
			hdr.setReqStatus(debitNoteDto.getRequestStatus());
		//	hdr.setFlowType(debitNoteDto.getFlowType());
			if ("NoFlow".equals(debitNoteDto.getFlowType())) {
				hdr.setReqStatus("APPROVED");
			} else {
				hdr.setReqStatus(debitNoteDto.getRequestStatus());
			}
			hdr.setFlowType(debitNoteDto.getFlowType());
			hdr.setReason(debitNoteDto.getReason());
			hdr.setRemark(debitNoteDto.getRemark());
			hdr.setUserId(debitNoteDto.getUserId());
			
			if (debitNoteDto.getReqId() != null) {

				AgrTrnReqDebitNoteDtl	existingDebitNote = debitRepo
						.findByRequestHdrReqId(debitNoteDto.getReqId());
				if (existingDebitNote != null) {
					debitNote = existingDebitNote;
				} else {
					debitNote = new AgrTrnReqDebitNoteDtl();
				}
			} else {
				debitNote = new AgrTrnReqDebitNoteDtl();
			}

//			AgrTrnReqDebitNoteDtl debitNote = new AgrTrnReqDebitNoteDtl();
			debitNote.setRequestHdr(hdr);
			debitNote.setMastAgrId(master.getMastAgrId());
			debitNote.setLoanId(debitNoteDto.getLoanId());
			debitNote.setDtDebitNote(sdf.parse(debitNoteDto.getDtRequestDate()));
			debitNote.setDebitNoteAmount(debitNoteDto.getDrNoteAmount());
			debitNote.setTranCategory("DEBIT_NOTE");
			debitNote.setDebitNoteHead(debitNoteDto.getTranHead());
			debitNote.setReasonCode(debitNoteDto.getDrNoteReason());
			debitNote.setRemark(debitNoteDto.getDrNoteRemark());
			debitNote.setUserId(debitNoteDto.getUserId());
			debitNote.setInstallmentNo(debitNoteDto.getInstallmentNo());

			reqHdrRepo.save(hdr);
			
			debitRepo.save(debitNote);
			log.info("Debit Request Hdr save");
				
				if (debitNoteDto.getReqId() != null) {

					AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(debitNoteDto.getReqId());
							
					if (existingRequestStatus != null) {
						existingRequestStatus.setRequestHdr(hdr);
						existingRequestStatus.setDtReqChangeDate(sdf.parse(debitNoteDto.getDtRequestDate()));
						existingRequestStatus.setReqStatus(debitNoteDto.getRequestStatus());
						existingRequestStatus.setReason(debitNoteDto.getReason());
						existingRequestStatus.setRemark(debitNoteDto.getRemark());
						
						statusRepo.save(existingRequestStatus);
					}
						
				} 
			

			 String userId = debitNoteDto.getAllocatedUserId();	
				if (("NoFlow".equals(debitNoteDto.getFlowType()))) {
					
					AgrTrnRequestStatus status = new AgrTrnRequestStatus();
					status.setRequestHdr(hdr);
			    	status.setUserId(debitNoteDto.getUserId());
		    	    status.setReqStatus("APPROVED");
		    	    status.setDtReqChangeDate(sdf.parse(debitNoteDto.getDtRequestDate()));
		    	    status.setReason(debitNoteDto.getReason());
		    	    status.setRemark(debitNoteDto.getRemark());
			     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
			       statusRepo.save(status);
					
				}
				
				else {
			        String allocationResult = approvalService.allocateRequest(hdr.getReqId(),userId,
			        		debitNoteDto.getReason(), debitNoteDto.getRemark(),debitNoteDto.getRequestStatus(),hdr.getActivityCode());
		
			        if (!"Success".equals(allocationResult)) {
			            throw new Exception("Request Allocation Failed");
			        }
			      }
		
			log.info("Debit Note save");
			
			if ("NoFlow".equalsIgnoreCase(debitNoteDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(debitNoteDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(debitNoteDto.getRequestStatus()))) {
			
		    DebitNoteApplicationDto debitApp = new DebitNoteApplicationDto();
			debitApp.setAmount(debitNoteDto.getDrNoteAmount());
			debitApp.setInstallmentNo(debitNoteDto.getInstallmentNo());
			debitApp.setLoanId(debitNoteDto.getLoanId());
			debitApp.setMastAgrId(debitNoteDto.getMastAgrId());
			debitApp.setReason(debitNoteDto.getDrNoteReason());
			debitApp.setRemark(debitNoteDto.getDrNoteRemark());
			debitApp.setSource("REQ");
			debitApp.setSourceId(Integer.toString(hdr.getReqId()));
			debitApp.setTranDate(debitNoteDto.getDtRequestDate());
			debitApp.setTranHead(debitNoteDto.getTranHead());
			debitApp.setUserId(debitNoteDto.getUserId());

			log.info("Debit Application Req " + debitNoteDto);

			debitService.debitNoteApplication(debitApp); 
			
		}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}
		return result;

	}

}
