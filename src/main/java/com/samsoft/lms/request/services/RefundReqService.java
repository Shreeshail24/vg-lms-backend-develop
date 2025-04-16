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
import com.samsoft.lms.request.dto.DreReqCashDto;
import com.samsoft.lms.request.dto.RefundReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqPayoutInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRefundDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.RefundApplicationDto;
import com.samsoft.lms.transaction.services.RefundService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefundReqService {

	@Autowired
	private AgrMasterAgreementRepository masterAgrRepo;
	@Autowired
	private Environment env;
	@Autowired
	private AgrTrnReqRefundDtlRepository refundRepo;
	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;
	@Autowired
	private AgrTrnReqPayoutInstrumentRepository payoutRepo;
	@Autowired
	private AgrTrnReqRefundDtlRepository agrTrnReqRefundDtlRepository;
	@Autowired
	private AgrTrnReqPayoutInstrumentRepository agrPayoutInstrumentRepository;
	@Autowired
	private RefundService refundService;
	@Autowired
	private AgrTrnRequestStatusRepository agrTrnRequestStatus;
	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private PageableUtils pageableUtils;
	@Autowired
	private ApprovalService approvalService;
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean refundRequest(RefundReqDto refundDto) throws Exception {
		Boolean result = true;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		log.info("Refund Request Dto " + refundDto);
		try {
			AgrMasterAgreement master = masterAgrRepo.findByMastAgrId(refundDto.getMastAgrId());
			
			if(master.getExcessAmount() < refundDto.getRefundAmount()) {
				log.info("Refund Amount is greater than Excess Amount.");
				throw new CoreBadRequestException("Refund Amount is greater than Excess Amount.");
				
			}
			
			if(refundDto.getRefundAmount() <= 0) {
				log.info("Refund Amount should be grater than 0");
				throw new CoreBadRequestException("Refund Amount should be grater than 0");
				
			}
			
			AgrTrnReqRefundDtl refund ;
			AgrTrnReqPayoutInstrument payout;
		//	AgrTrnRequestStatus status;
			
			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setMasterAgreement(master);
			hdr.setDtRequest(sdf.parse(refundDto.getDtRequestDate()));
			hdr.setActivityCode("REFUND");
			hdr.setReqStatus(refundDto.getRequestStatus());
		//	hdr.setFlowType(refundDto.getFlowType());
			if ("NoFlow".equals(refundDto.getFlowType())) {
				hdr.setReqStatus("APPROVED");
			} else {
				hdr.setReqStatus(refundDto.getRequestStatus());
			}
			hdr.setFlowType(refundDto.getFlowType());
			hdr.setReason(refundDto.getReason());
			hdr.setRemark(refundDto.getRemark());
			hdr.setUserId(refundDto.getUserId());
			
			if(refundDto.getReqId() != null) {
				hdr.setReqId(refundDto.getReqId());
			}
		
			
			reqHdrRepo.save(hdr);

			
			if (refundDto.getReqId() != null) {

				AgrTrnReqRefundDtl existingTrnReqRefundcDtl = agrTrnReqRefundDtlRepository.
						findByRequestHdrReqId(refundDto.getReqId());
				if (existingTrnReqRefundcDtl != null) {
					refund = existingTrnReqRefundcDtl;
				} else {
					refund = new AgrTrnReqRefundDtl();
				}
			} else {
				refund = new AgrTrnReqRefundDtl();
			}
			
			
			//AgrTrnReqRefundDtl refund = new AgrTrnReqRefundDtl();
			refund.setRequestHdr(hdr);
			refund.setMastAgrId(master.getMastAgrId());
			refund.setDtRefund(sdf.parse(refundDto.getDtRequestDate()));
			refund.setRefundAmount(refundDto.getRefundAmount());
			refund.setReasonCode(refundDto.getRefundReason());
			refund.setRemark(refundDto.getRefundRemark());
			refund.setUserId(refundDto.getUserId());

			if(refundDto.getDtInstrument()== "") {
				refundDto.setDtInstrument(refundDto.getDtRequestDate());
			}

			refundRepo.save(refund);
			
			if (refundDto.getReqId() != null) {

				AgrTrnReqPayoutInstrument existingTrnReqPayoutInstrument = agrPayoutInstrumentRepository.
						findByRequestHdrReqId(refundDto.getReqId());
				if (existingTrnReqPayoutInstrument != null) {
					payout = existingTrnReqPayoutInstrument;
				} else {
					payout = new AgrTrnReqPayoutInstrument();
				}
			} else {
				payout = new AgrTrnReqPayoutInstrument();
			}
			
			//AgrTrnReqPayoutInstrument payout = new AgrTrnReqPayoutInstrument();
			payout.setRequestHdr(hdr);
			payout.setDtInstrumentDate(sdf.parse(refundDto.getDtInstrument()));
			payout.setPayType(refundDto.getPaymentType());
			payout.setPayMode(refundDto.getPaymentMode());
			payout.setInstrumentType(refundDto.getInstrumentType());
			payout.setAccountNo(refundDto.getBankAccountNo());
			payout.setAccountType(refundDto.getAccountType());
			payout.setInstrumentNo(refundDto.getInstrumentNo());
			payout.setBankBranchCode(refundDto.getBranchCode());
			payout.setBankCode(refundDto.getBankCode());
			payout.setInstrumentAmount(refundDto.getRefundAmount());
			if (refundDto.getReqId() != null) {
				payout.setInstrumentStatus(refundDto.getRequestStatus());
			}
			payout.setInstrumentStatus("PENDING");
			payout.setUtrNo(refundDto.getUtrNo());
			payout.setIfscCode(refundDto.getIfscCode());

			payout.setDtPayment(sdf.parse(refundDto.getDtRequestDate()));
			// payout.setDepositRefNo(refundDto.get);
			payout.setUserId(refundDto.getUserId());
			payout.setInstrumentLocation(refundDto.getInstrumentLocation());

			payoutRepo.save(payout);
			
			if (refundDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = agrTrnRequestStatus.findByRequestHdrReqId(refundDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(hdr);
					existingRequestStatus.setDtReqChangeDate(sdf.parse(refundDto.getDtRequestDate()));
					existingRequestStatus.setReqStatus(refundDto.getRequestStatus());
					existingRequestStatus.setReason(refundDto.getReason());
					existingRequestStatus.setRemark(refundDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = refundDto.getAllocatedUserId();	
			if (("NoFlow".equals(refundDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(hdr);
		    	status.setUserId(refundDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(sdf.parse(refundDto.getDtRequestDate()));
	    	    status.setReason(refundDto.getReason());
	    	    status.setRemark(refundDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(hdr.getReqId(),userId,
		        		refundDto.getReason(), refundDto.getRemark(),refundDto.getRequestStatus(),hdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
			
			if ("NoFlow".equalsIgnoreCase(refundDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(refundDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(refundDto.getRequestStatus()))) {
		
			RefundApplicationDto refundApp = new RefundApplicationDto();
			refundApp.setAccountNo(refundDto.getBankAccountNo());
			refundApp.setAccountType(refundDto.getAccountType());
			refundApp.setBankCode(refundDto.getBankCode());
			refundApp.setBranchCode(refundDto.getBranchCode());
			refundApp.setDtInstrument(refundDto.getDtInstrument());
			refundApp.setDtTranDate(refundDto.getDtRequestDate());
			refundApp.setIfscCode(refundDto.getIfscCode());
			refundApp.setInstrumentNo(refundDto.getInstrumentNo());
			refundApp.setInstrumentType(refundDto.getInstrumentType());
			refundApp.setMastAgrId(refundDto.getMastAgrId());
			refundApp.setPaymentRefNo(refundDto.getUtrNo());
			refundApp.setRefundAmount(refundDto.getRefundAmount());
			refundApp.setRefundReason(refundDto.getRefundReason());
			refundApp.setRefundRemark(refundDto.getRefundRemark());
			refundApp.setSource("REQ");
			refundApp.setSourceId(Integer.toString(hdr.getReqId()));
			refundApp.setUserId(refundDto.getUserId());
			refundApp.setUtrNo(refundDto.getUtrNo());

			log.info("Refund Request Dto " + refundApp);

			refundService.refundApplication(refundApp);
		}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}



	// ----- Method to get Refund requests by status, date and reqId
	public Page<RefundReqDto> getRefundReqList(List<String> statusList, String activityCode, Integer reqNo, String date, Pageable pageable) throws Exception {
	    List<RefundReqDto> RefundReqDtoList = new ArrayList<>();
	    try {
	        List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

	        if (statusList != null && !statusList.isEmpty()) {
	            // Case 1: Filter by statuses and activity code
	            agrTrnRequestHdrList = reqHdrRepo.getRequestsByStatusesAndActivityCode(statusList, activityCode);
	            if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
	                throw new CoreDataNotFoundException("No records found for the provided status list: " + statusList);
	            }
	        } else if (reqNo != null && reqNo != 0) {
	            // Case 2: Filter by request number and activity code
	            AgrTrnRequestHdr agrTrnRequestHdr = reqHdrRepo.findByReqIdAndActivityCode(reqNo, activityCode);
	            if (agrTrnRequestHdr == null) {
	                throw new CoreDataNotFoundException("No record found for the given request ID: " + reqNo);
	            }
	                agrTrnRequestHdrList.add(agrTrnRequestHdr);
	            
	         
	        } else if (date != null && !date.equalsIgnoreCase("null")) {
	            // Case 3: Filter by date and activity code
	            String dateFormat = env.getProperty("lms.global.date.format");
	            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	            Date requestedDate = sdf.parse(date);
	            agrTrnRequestHdrList = reqHdrRepo.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
	            
	            if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
	                throw new CoreDataNotFoundException("No records found for the requested date: " + date);
	            }		
	        }

	        // Process the retrieved data
	        if (!agrTrnRequestHdrList.isEmpty()) {
	            for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
	                RefundReqDto refundReqDetailsResponseDto = new RefundReqDto();
	                refundReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
	                refundReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
	                refundReqDetailsResponseDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
	                refundReqDetailsResponseDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
	                refundReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
	                refundReqDetailsResponseDto.setReqId(agrTrnRequestHdr.getReqId());
	                refundReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());

	                AgrTrnReqPayoutInstrument agrTrnReqPayoutInstrument = payoutRepo.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
	                if (agrTrnReqPayoutInstrument != null) {
	                    refundReqDetailsResponseDto.setRefundAmount(agrTrnReqPayoutInstrument.getInstrumentAmount());
	                    refundReqDetailsResponseDto.setInstrumentType(agrTrnReqPayoutInstrument.getInstrumentType());
	                    refundReqDetailsResponseDto.setInstrumentLocation(agrTrnReqPayoutInstrument.getInstrumentLocation());
	                }
	                
	                AgrTrnRequestStatus agrTrnReqStatus = agrTrnRequestStatus
							.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
					
					if (agrTrnReqStatus != null) {
						refundReqDetailsResponseDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
					
						}	

	                RefundReqDtoList.add(refundReqDetailsResponseDto);
	            }
	        }

	        // Convert to Page object
	        return pageableUtils.convertToPage(RefundReqDtoList, pageable);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}

	
	
	
}
