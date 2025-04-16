package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.newux.dto.response.DebitNoteReqDetailsResponseDto;
import com.samsoft.lms.request.dto.CreditNoteReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqCreditNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnReqDebitNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqCreditNoteDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.CreditNoteApplicationDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.services.CreditNoteService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreditNoteReqService {

	@Autowired
	private AgrMasterAgreementRepository masterAgrRepo;
	@Autowired
	private Environment env;
	@Autowired
	private AgrTrnReqCreditNoteDtlRepository debitRepo;
	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;
	@Autowired
	private CreditNoteService creditNoteService;
	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private PageableUtils pageableUtils;
	@Autowired
	private ApprovalService approvalService;

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class, Error.class })
	public Boolean creditNoteRequest(CreditNoteReqDto creditNoteDto) throws Exception {
		Boolean result = true;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		log.info("Credit Request Req "+creditNoteDto);
		try {
			AgrMasterAgreement master = masterAgrRepo.findByMastAgrId(creditNoteDto.getMastAgrId());
		//	AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			
			AgrTrnRequestHdr hdr ;
			AgrTrnReqCreditNoteDtl creditNote;
			//AgrTrnRequestStatus status;
			if (creditNoteDto.getReqId() != null) {
				hdr = reqHdrRepo.findById(creditNoteDto.getReqId())
			                    .orElseThrow(() -> new TransactionDataNotFoundException("Request Header not found for id: " + creditNoteDto.getReqId()));
			} else {
				hdr = new AgrTrnRequestHdr();
			}
			
			hdr.setMasterAgreement(master);
			hdr.setDtRequest(sdf.parse(creditNoteDto.getDtRequestDate()));
			hdr.setActivityCode("CREDIT_NOTE");
			hdr.setReqStatus(creditNoteDto.getRequestStatus());
		
			if ("NoFlow".equals(creditNoteDto.getFlowType())) {
				hdr.setReqStatus("APPROVED");
			} else {
				hdr.setReqStatus(creditNoteDto.getRequestStatus());
			}
			hdr.setFlowType(creditNoteDto.getFlowType());
			hdr.setReason(creditNoteDto.getReason());
			hdr.setRemark(creditNoteDto.getRemark());
			hdr.setUserId(creditNoteDto.getUserId());
			
			
			if (creditNoteDto.getReqId() != null) {

				AgrTrnReqCreditNoteDtl	existingCreditNote = debitRepo
						.findByRequestHdrReqId(creditNoteDto.getReqId());
				if (existingCreditNote != null) {
					creditNote = existingCreditNote;
				} else {
					creditNote = new AgrTrnReqCreditNoteDtl();
				}
			} else {
				creditNote = new AgrTrnReqCreditNoteDtl();
			}

			//AgrTrnReqCreditNoteDtl creditNote = new AgrTrnReqCreditNoteDtl();
			creditNote.setRequestHdr(hdr);
			creditNote.setMastAgrId(master.getMastAgrId());
			creditNote.setLoanId(creditNoteDto.getLoanId());
			creditNote.setDtCreditNote(sdf.parse(creditNoteDto.getDtRequestDate()));
			creditNote.setCreditNoteAmount(creditNoteDto.getCrNoteAmount());
			creditNote.setCreditNoteHead(creditNoteDto.getTranHead());
			creditNote.setReasonCode(creditNoteDto.getCrNoteReason());
			creditNote.setRemark(creditNoteDto.getCrNoteRemark());
			creditNote.setUserId(creditNoteDto.getUserId());
			creditNote.setInstallmentNo(creditNoteDto.getInstallmentNo());
			reqHdrRepo.save(hdr);
			debitRepo.save(creditNote);
			
			if (creditNoteDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(creditNoteDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(hdr);
					existingRequestStatus.setDtReqChangeDate(sdf.parse(creditNoteDto.getDtRequestDate()));
					existingRequestStatus.setReqStatus(creditNoteDto.getRequestStatus());
					existingRequestStatus.setReason(creditNoteDto.getReason());
					existingRequestStatus.setRemark(creditNoteDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
		

		 String userId = creditNoteDto.getAllocatedUserId();	
			if (("NoFlow".equals(creditNoteDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(hdr);
		    	status.setUserId(creditNoteDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(sdf.parse(creditNoteDto.getDtRequestDate()));
	    	    status.setReason(creditNoteDto.getReason());
	    	    status.setRemark(creditNoteDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(hdr.getReqId(),userId,
		        		creditNoteDto.getReason(), creditNoteDto.getRemark(),creditNoteDto.getRequestStatus(),hdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }


			if ("NoFlow".equalsIgnoreCase(creditNoteDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(creditNoteDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(creditNoteDto.getRequestStatus()))) {
			CreditNoteApplicationDto creditApp = new CreditNoteApplicationDto();

			creditApp.setCrNoteAmount(creditNoteDto.getCrNoteAmount());
			creditApp.setCrNoteReasonCode(creditNoteDto.getCrNoteReason());
			creditApp.setCrNoteRemark(creditNoteDto.getCrNoteRemark());
			creditApp.setInstallmentNo(creditNoteDto.getInstallmentNo());
			creditApp.setLoanId(creditNoteDto.getLoanId());
			creditApp.setMastAgrId(creditNoteDto.getMastAgrId());
			creditApp.setSource("REQ");
			creditApp.setSourceId(Integer.toString(hdr.getReqId()));
			creditApp.setTranDate(creditNoteDto.getDtRequestDate());
			creditApp.setTranHead(creditNoteDto.getTranHead());
			creditApp.setUserId(creditNoteDto.getUserId());

			log.info("Credit Application Req "+creditApp);
			creditNoteService.creditNoteApplication(creditApp);

			}
		} catch (Exception e) {
			throw e;
		}
		return result;

	}
	
	// ----- Method to get Credit note requests by status, date and reqId
			public Page<CreditNoteReqDto> getCreditNoteReqList(List<String> statusList, String activityCode,
					Integer reqNo, String date, Pageable pageable) throws Exception {

				List<CreditNoteReqDto> creditNoteReqDtoList = new ArrayList<>();
				try {

					List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

					if (!statusList.isEmpty() || reqNo == 0 && date == null) {
						
						agrTrnRequestHdrList = reqHdrRepo.getRequestsByStatusesAndActivityCode(statusList, activityCode);
						 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
				                throw new CoreDataNotFoundException("No records found for the provided status : " + statusList);
				            }

					} else if (reqNo != 0 || statusList == null || date == null) {
						
						AgrTrnRequestHdr agrTrnRequestHdr = reqHdrRepo.findByReqIdAndActivityCode(reqNo, activityCode);
						 if (agrTrnRequestHdr == null) {
				                throw new CoreDataNotFoundException("No record found for the given request ID: " + reqNo);
				            }
						agrTrnRequestHdrList.add(agrTrnRequestHdr);

					} else if (date != null || statusList == null || reqNo == null) {
						// change string date into Date format
						String dateFormat = env.getProperty("lms.global.date.format");
						SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
						Date requestedDate = sdf.parse(date);
						agrTrnRequestHdrList = reqHdrRepo.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
						
						 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
				                throw new CoreDataNotFoundException("No records found for the requested date: " + date);
				            }	
					
					}

					if (!agrTrnRequestHdrList.isEmpty() && agrTrnRequestHdrList != null) {

						for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
							CreditNoteReqDto creditNoteReqDto = new CreditNoteReqDto();

							creditNoteReqDto.setReqId(agrTrnRequestHdr.getReqId());
							creditNoteReqDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
							creditNoteReqDto.setFlowType(agrTrnRequestHdr.getFlowType());
							creditNoteReqDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
							creditNoteReqDto.setReason(agrTrnRequestHdr.getReason());
							creditNoteReqDto.setRemark(agrTrnRequestHdr.getRemark());
							creditNoteReqDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
							creditNoteReqDto.setUserId(agrTrnRequestHdr.getUserId());
						
							AgrTrnReqCreditNoteDtl agrTrnReqCreditNoteDtl = debitRepo
									.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());

							if (agrTrnReqCreditNoteDtl != null) {
								creditNoteReqDto.setCrNoteReason(agrTrnReqCreditNoteDtl.getReasonCode());
								creditNoteReqDto.setCrNoteRemark(agrTrnReqCreditNoteDtl.getRemark());
								creditNoteReqDto.setLoanId(agrTrnReqCreditNoteDtl.getLoanId());
								creditNoteReqDto.setInstallmentNo(agrTrnReqCreditNoteDtl.getInstallmentNo());
								creditNoteReqDto.setTranHead(agrTrnReqCreditNoteDtl.getCreditNoteHead());
								creditNoteReqDto.setCrNoteAmount(agrTrnReqCreditNoteDtl.getCreditNoteAmount());
							}
							
							AgrTrnRequestStatus agrTrnReqStatus = statusRepo
									.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
							
							if (agrTrnReqStatus != null) {
								creditNoteReqDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
							
								}	
							creditNoteReqDtoList.add(creditNoteReqDto);
						}
					}

					// Return a Page object for DTOs using convertToPage
					return pageableUtils.convertToPage(creditNoteReqDtoList, pageable);

				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		

}
