package com.samsoft.lms.request.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.entities.ApprovalSetting;
import com.samsoft.lms.approvalSettings.repositories.ApprovalSettingRepository;
import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.DreApplicationService;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.core.services.PaymentAutoApportionmentService;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.document.dto.request.UploadExternalDocumentRequest;
import com.samsoft.lms.document.service.ExternalDocumentUploadService;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.newux.dto.response.RecieptReqDetailsResponseDto;
import com.samsoft.lms.request.dto.DreAllocationDto;
import com.samsoft.lms.request.dto.DreReqCashDto;
import com.samsoft.lms.request.dto.DreReqChequeDto;
import com.samsoft.lms.request.dto.DreRequestOnlineDto;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.services.ForclosureApplicationService;

@Service
@Slf4j
public class DreRequestService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnRequestHdrRepository requestHeaderRepo;
	
	@Autowired
	private AgrMasterAgreementRepository agreementRepo;

	@Autowired
	private PaymentApplicationServices payApplService;

	@Autowired
	private TrnInsInstrumentAllocRepository insAllocRepo;

	@Autowired
	private TrnInsInstrumentRepository instRepo;
	
	@Autowired
	private AgrTrnReqInstrumentRepository agrTrnReqInstrumentRepository;

	@Autowired
	private CommonServices commService;

	@Autowired
	private PaymentAutoApportionmentService autoApportionment;
	
	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository agrTrnReqInstrumentAllocDtlRepository;
	
	@Autowired
	private AgrTrnRequestStatusRepository agrTrnRequestStatus;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;

	@Autowired
	private ExternalDocumentUploadService externalDocumentUploadService;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private ApprovalService approvalService;
	
	@Autowired
	private ApprovalSettingRepository approvalSettingRepository;
	
	@Autowired
	private DreApplicationService dreAppService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String dreReqCheque(DreReqChequeDto dreChequeDto) throws Exception { // File Upload ,
																				// UploadExternalDocumentRequest
																				// uploadDocument
		String result = "";
		AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			// LocalDate requestDate = LocalDate.parse(dreChequeDto.getRequestDate(),
			// formatter);
			// LocalDate instrumentDate = LocalDate.parse(dreChequeDto.getInstrumentDate(),
			// formatter);

			/*
			 * //Upload Document String docUrl = null; if(uploadDocument != null) { docUrl =
			 * this.uploadDocument(uploadDocument); }
			 */

			AgrTrnReqInstrument instrument;
			AgrTrnReqInstrumentAllocDtl instAlloc;
			// List<AgrTrnReqInstrumentAllocDtl> listInstAlloc = new ArrayList<>();

			AgrMasterAgreement master = agreementRepo.findByMastAgrId(dreChequeDto.getMasterAgreementId());
			Date requestDate = sdf.parse(dreChequeDto.getRequestDate());
			Date instrumentDate = sdf.parse(dreChequeDto.getInstrumentDate());
			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(dreChequeDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("RECEIPT");
			requestHdr.setReqStatus(dreChequeDto.getRequestStatus());
		//	requestHdr.setFlowType(dreChequeDto.getFlowType());
			if ("NoFlow".equals(dreChequeDto.getFlowType())) {
			    requestHdr.setReqStatus("APPROVED");
			} else {
			    requestHdr.setReqStatus(dreChequeDto.getRequestStatus());
			}
			requestHdr.setReason(dreChequeDto.getReason());
			requestHdr.setRemark(dreChequeDto.getRemark());
			requestHdr.setUserId(dreChequeDto.getUserId());
			if (dreChequeDto.getReqId() != null) {
				requestHdr.setReqId(dreChequeDto.getReqId());

			}
//			requestHdr.setDocUrl(docUrl);

			if (dreChequeDto.getReqId() != null) {

				AgrTrnReqInstrument existingInstrument = agrTrnReqInstrumentRepository
						.findByRequestHdrReqId(dreChequeDto.getReqId());
				if (existingInstrument != null) {
					instrument = existingInstrument;
				} else {
					instrument = new AgrTrnReqInstrument();
				}
			} else {
				instrument = new AgrTrnReqInstrument();
			}

			// AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(dreChequeDto.getPaymentType());
			instrument.setPayMode(dreChequeDto.getPaymentMode());
			instrument.setInstrumentType("CH");
			instrument.setAccountNo(dreChequeDto.getBankAccountNo());
			instrument.setAccountType(dreChequeDto.getAccountType());
			instrument.setInstrumentNo(dreChequeDto.getInstrumentNo());
			instrument.setBankCode(dreChequeDto.getBankCode());
			instrument.setBankBranchCode(dreChequeDto.getBrachCode());
			instrument.setMicrCode(dreChequeDto.getMicrCode());
			instrument.setClearingLocation(dreChequeDto.getClearingLocation());
			instrument.setInstrumentAmount(commService.numberFormatter(dreChequeDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDepositBank(dreChequeDto.getDepositBank());
			instrument.setUtrNo(dreChequeDto.getUtrNo());
			instrument.setIfscCode(dreChequeDto.getIfscCode());
			instrument.setDtReceipt(requestDate);
			instrument.setDepositRefNo(dreChequeDto.getDepositRefNo());
			instrument.setCardHolderName(dreChequeDto.getCardHolderName());
			instrument.setIssuingBank(dreChequeDto.getIssuingBank());
			instrument.setUpiVpa(dreChequeDto.getUpiVpa());
			instrument.setCollectedBy(dreChequeDto.getCollectedBy());
			instrument.setCollectionAgency(dreChequeDto.getCollectionAgency());
			instrument.setProvisionalReceipt(dreChequeDto.getProvisionalReceiptFlag());
			instrument.setProcLoc(dreChequeDto.getProcessingLocation());
			instrument.setCardType(dreChequeDto.getCardType());
			instrument.setNclStatus("N");
			instrument.setUserId(dreChequeDto.getUserId());
			instrument.setTdsAmount(dreChequeDto.getTdsAmount());
			instrument.setInstrumentLocation(dreChequeDto.getInstrumentLocation());


			if (dreChequeDto.getReqId() != null) {

				AgrTrnReqInstrumentAllocDtl existingInstrumentAllocDtl = agrTrnReqInstrumentAllocDtlRepository
						.findByInstrumentRequestHdrReqId(dreChequeDto.getReqId());
				if (existingInstrumentAllocDtl != null) {
					instAlloc = existingInstrumentAllocDtl;
				} else {
					instAlloc = new AgrTrnReqInstrumentAllocDtl();
				}
			} else {
				instAlloc = new AgrTrnReqInstrumentAllocDtl();
			}

			List<DreAllocationDto> listDreAllocation = dreChequeDto.getDreAllocation();
			List<AgrTrnReqInstrumentAllocDtl> listInstAlloc = new ArrayList<AgrTrnReqInstrumentAllocDtl>();
			for (DreAllocationDto dreAllocation : listDreAllocation) {
				if (dreAllocation.getAllocatedAmount() > 0) {
					instAlloc.setLoanId(dreAllocation.getLoanId());
					instAlloc.setTranCategory(dreAllocation.getTranCategory());
					instAlloc.setTranHead(dreAllocation.getTranHead());
					instAlloc.setPayMode(dreChequeDto.getPaymentMode());
					instAlloc.setAmout(commService.numberFormatter(dreAllocation.getAllocatedAmount()));
					instAlloc.setUserId(dreChequeDto.getUserId());
					instAlloc.setInstrument(instrument);
					listInstAlloc.add(instAlloc);

				}

			}

			instrument.setInstrumentAlloc(listInstAlloc);

			List<AgrTrnReqInstrument> listInstrument = new ArrayList<AgrTrnReqInstrument>();

			listInstrument.add(instrument);
			requestHdr.setInstruments(listInstrument);

			requestHeaderRepo.save(requestHdr);
			
			if (dreChequeDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = agrTrnRequestStatus.findByRequestHdrReqId(dreChequeDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(requestHdr);
					existingRequestStatus.setDtReqChangeDate(requestDate);
					existingRequestStatus.setReqStatus(dreChequeDto.getRequestStatus());
					existingRequestStatus.setReason(dreChequeDto.getReason());
					existingRequestStatus.setRemark(dreChequeDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = dreChequeDto.getAllocatedUserId();	
			if (("NoFlow".equals(dreChequeDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(requestHdr);
		    	status.setUserId(dreChequeDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(requestDate);
	    	    status.setReason(dreChequeDto.getReason());
	    	    status.setRemark(dreChequeDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(requestHdr.getReqId(),userId,
		        		dreChequeDto.getReason(), dreChequeDto.getRemark(),dreChequeDto.getRequestStatus(),requestHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
			if ("NoFlow".equalsIgnoreCase(dreChequeDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(dreChequeDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(dreChequeDto.getRequestStatus()))) {
				
				dreAppService.dreApplication(dreChequeDto.getReqId(), dreChequeDto.getMasterAgreementId(),
						requestDate, dreChequeDto.getUserId());
		
			}

		} catch (Exception e) {
			result = "Payment Failed";
			throw e;
		}
		return "Payment Successful";

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String dreRequestCash(DreReqCashDto dreCashDto) throws Exception { // File Upload , UploadExternalDocumentRequest uploadDocument
		String result = "";
		AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			// LocalDate requestDate = LocalDate.parse(dreCashDto.getRequestDate(),
			// formatter);
			// LocalDate instrumentDate = LocalDate.parse(dreCashDto.getInstrumentDate(),
			// formatter);

			/*//Upload Document
			String docUrl = null;
			if(uploadDocument != null) {
				docUrl = this.uploadDocument(uploadDocument);
			}*/
			
			AgrTrnReqInstrument instrument;
			AgrTrnReqInstrumentAllocDtl instAlloc;

			Date requestDate = sdf.parse(dreCashDto.getRequestDate());
			Date instrumentDate = sdf.parse(dreCashDto.getInstrumentDate());
			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(dreCashDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("RECEIPT");
			//requestHdr.setReqStatus("NoFlow".equals(dreCashDto.getFlowType()) ? "APPROVED" : dreCashDto.getRequestStatus());
			if ("NoFlow".equals(dreCashDto.getFlowType())) {
			    requestHdr.setReqStatus("APPROVED");
			} else {
			    requestHdr.setReqStatus(dreCashDto.getRequestStatus());
			}

			requestHdr.setFlowType(dreCashDto.getFlowType());
			requestHdr.setReason(dreCashDto.getReason());
			requestHdr.setRemark(dreCashDto.getRemark());
			requestHdr.setUserId(dreCashDto.getUserId());
//			requestHdr.setDocUrl(docUrl);
			if(dreCashDto.getReqId() != null) {
				requestHdr.setReqId(dreCashDto.getReqId());
			}
			
			
			if (dreCashDto.getReqId() != null) {

				AgrTrnReqInstrument existingInstrument = agrTrnReqInstrumentRepository
						.findByRequestHdrReqId(dreCashDto.getReqId());
				if (existingInstrument != null) {
					instrument = existingInstrument;
				} else {
					instrument = new AgrTrnReqInstrument();
				}
			} else {
				instrument = new AgrTrnReqInstrument();
			}

		//	AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(dreCashDto.getPaymentType());
			instrument.setPayMode(dreCashDto.getPaymentMode());
			instrument.setInstrumentType("CA");
			instrument.setInstrumentAmount(commService.numberFormatter(dreCashDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDtReceipt(requestDate);
			instrument.setDepositRefNo(dreCashDto.getDepositRefNo());
			instrument.setCollectedBy(dreCashDto.getCollectedBy());
			instrument.setCollectionAgency(dreCashDto.getCollectionAgency());
			instrument.setProvisionalReceipt(dreCashDto.getProvisionalReceiptFlag());
			instrument.setNclStatus("N");
			instrument.setUserId(dreCashDto.getUserId());
			instrument.setTdsAmount(dreCashDto.getTdsAmount());
			
			
			if (dreCashDto.getReqId() != null) {

				AgrTrnReqInstrumentAllocDtl existingInstrumentAllocDtl = agrTrnReqInstrumentAllocDtlRepository
						.findByInstrumentRequestHdrReqId(dreCashDto.getReqId());
				if (existingInstrumentAllocDtl != null) {
					instAlloc = existingInstrumentAllocDtl;
				} else {
					instAlloc = new AgrTrnReqInstrumentAllocDtl();
				}
			} else {
				instAlloc = new AgrTrnReqInstrumentAllocDtl();
			}

			List<DreAllocationDto> listDreAllocation = dreCashDto.getDreAllocation();

			List<AgrTrnReqInstrumentAllocDtl> listReqInstAlloc = new ArrayList<AgrTrnReqInstrumentAllocDtl>();

			for (DreAllocationDto dreAllocation : listDreAllocation) {

			//	AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
				instAlloc.setLoanId(dreAllocation.getLoanId());
				instAlloc.setTranCategory(dreAllocation.getTranCategory());
				instAlloc.setTranHead(dreAllocation.getTranHead());
				instAlloc.setAmout(commService.numberFormatter(dreAllocation.getAllocatedAmount()));
				instAlloc.setUserId(dreCashDto.getUserId());
				instAlloc.setInstrument(instrument);
				instAlloc.setUserId(dreCashDto.getUserId());
				listReqInstAlloc.add(instAlloc);

			}
			instrument.setInstrumentAlloc(listReqInstAlloc);

			List<AgrTrnReqInstrument> listInstrument = new ArrayList<AgrTrnReqInstrument>();

			listInstrument.add(instrument);
			requestHdr.setInstruments(listInstrument);

			requestHdr = requestHeaderRepo.save(requestHdr);
			
			if (dreCashDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = agrTrnRequestStatus.findByRequestHdrReqId(dreCashDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(requestHdr);
					existingRequestStatus.setDtReqChangeDate(requestDate);
					existingRequestStatus.setReqStatus(dreCashDto.getRequestStatus());
					existingRequestStatus.setReason(dreCashDto.getReason());
					existingRequestStatus.setRemark(dreCashDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = dreCashDto.getAllocatedUserId();	
			if (("NoFlow".equals(dreCashDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(requestHdr);
		    	status.setUserId(dreCashDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(requestDate);
	    	    status.setReason(dreCashDto.getReason());
	    	    status.setRemark(dreCashDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(requestHdr.getReqId(),userId,
		                dreCashDto.getReason(), dreCashDto.getRemark(),dreCashDto.getRequestStatus(),requestHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
			
			if ("NoFlow".equalsIgnoreCase(dreCashDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(dreCashDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(dreCashDto.getRequestStatus()))) {
				
				dreAppService.dreApplication(dreCashDto.getReqId(), dreCashDto.getMasterAgreementId(),
						requestDate, dreCashDto.getUserId());
		
			}
			
			//AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			
			/*
			 * if (dreCashDto.getPaymentFor().equalsIgnoreCase("CHARGES")) {
			 * payApplService.manualChargesApportionmentPaymentApplication(dreCashDto.
			 * getMasterAgreementId(),
			 * savedInstAlloc.get(0).getInstrument().getInstrumentId(), requestDate); } else
			 */

		} catch (Exception e) {
			result = "Payment Failed";
			throw e;
		}
		return "Payment Successful";

	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String dreRequestOnline(DreRequestOnlineDto dreOnlineDto) throws Exception { // File Upload , UploadExternalDocumentRequest uploadDocument
		String result = "";
		AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
		try {
			log.info("dreOnlineDto===> " + dreOnlineDto);

			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			// LocalDate requestDate = LocalDate.parse(dreCashDto.getRequestDate(),
			// formatter);
			// LocalDate instrumentDate = LocalDate.parse(dreCashDto.getInstrumentDate(),
			// formatter);

			Date requestDate = sdf.parse(dreOnlineDto.getRequestDate());
			Date instrumentDate = sdf.parse(dreOnlineDto.getInstrumentDate());
			log.info("requestDate===> " + requestDate + "  instrumentDate===> " + instrumentDate);

			/*//Upload Document
			String docUrl = null;
			if(uploadDocument != null) {
				docUrl = this.uploadDocument(uploadDocument);
			}*/

			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(dreOnlineDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("RECEIPT");
			requestHdr.setReqStatus(dreOnlineDto.getRequestStatus());
			requestHdr.setFlowType(dreOnlineDto.getFlowType());
			requestHdr.setReason(dreOnlineDto.getReason());
			requestHdr.setRemark(dreOnlineDto.getRemark());
			requestHdr.setUserId(dreOnlineDto.getUserId());
//			requestHdr.setDocUrl(docUrl);

			AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(dreOnlineDto.getPaymentType());
			instrument.setPayMode(dreOnlineDto.getPaymentMode());
			instrument.setInstrumentType(dreOnlineDto.getPaymentMode());
			instrument.setInstrumentAmount(commService.numberFormatter(dreOnlineDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDtReceipt(requestDate);
			instrument.setDepositRefNo(dreOnlineDto.getDepositRefNo());
			instrument.setCollectedBy(dreOnlineDto.getCollectedBy());
			instrument.setCollectionAgency(dreOnlineDto.getCollectionAgency());
			instrument.setProvisionalReceipt(dreOnlineDto.getProvisionalReceiptFlag());
			instrument.setNclStatus("N");
			instrument.setBankCode(dreOnlineDto.getBankCode());
			instrument.setBankBranchCode(dreOnlineDto.getBankBranchCode());
			instrument.setIfscCode(dreOnlineDto.getIfscCode());
			instrument.setUtrNo(dreOnlineDto.getUtrNo());
			instrument.setUserId(dreOnlineDto.getUserId());
			instrument.setTdsAmount(dreOnlineDto.getTdsAmount());

			List<DreAllocationDto> listDreAllocation = dreOnlineDto.getDreAllocation();
			List<AgrTrnReqInstrumentAllocDtl> listReqInstAlloc = new ArrayList<AgrTrnReqInstrumentAllocDtl>();
			for (DreAllocationDto dreAllocation : listDreAllocation) {

				AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
				instAlloc.setLoanId(dreAllocation.getLoanId());
				instAlloc.setTranCategory(dreAllocation.getTranCategory());
				instAlloc.setTranHead(dreAllocation.getTranHead());
				instAlloc.setAmout(commService.numberFormatter(dreAllocation.getAllocatedAmount()));
				instAlloc.setUserId(dreOnlineDto.getUserId());
				instAlloc.setInstrument(instrument);
				instAlloc.setUserId(dreOnlineDto.getUserId());
				listReqInstAlloc.add(instAlloc);

			}
			instrument.setInstrumentAlloc(listReqInstAlloc);

			List<AgrTrnReqInstrument> listInstrument = new ArrayList<AgrTrnReqInstrument>();

			listInstrument.add(instrument);
			requestHdr.setInstruments(listInstrument);

			requestHdr = requestHeaderRepo.save(requestHdr);

			AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			status.setRequestHdr(requestHdr);
			status.setDtReqChangeDate(requestDate);
			status.setReqStatus("PENDING");
			status.setReason(dreOnlineDto.getReason());
			status.setRemark(dreOnlineDto.getRemark());

			statusRepo.save(status);

		} catch (Exception e) {
			result = "Payment Failed";
			throw e;
		}

		return requestHdr.getReqId().toString();

	}

	@Transactional
	private String uploadDocument(UploadExternalDocumentRequest uploadExternalDocumentRequest) throws Exception {

		try {

			return externalDocumentUploadService.uploadExternalDocument(uploadExternalDocumentRequest);

		} catch (Exception e) {
			e.printStackTrace();

			log.error("DreRequestService :: uploadDocument :: Method: uploadDocument");
			log.error("DreRequestService :: uploadDocument :: Request: {}", uploadExternalDocumentRequest);
			log.error("DreRequestService :: uploadDocument :: Error: {}", e.getMessage());

			throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public Page<DreReqCashDto> getReceiptReqList(List<String> statusList, String activityCode,
			Integer reqNo, String date, Pageable pageable) throws Exception {

		List<DreReqCashDto> recieptReqDtoList = new ArrayList<>();
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
					DreReqCashDto recieptReqDetailsResponseDto = new DreReqCashDto();
					recieptReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
					recieptReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
					recieptReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
					recieptReqDetailsResponseDto.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
					recieptReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
					recieptReqDetailsResponseDto.setReqId(agrTrnRequestHdr.getReqId());
					recieptReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());

					AgrTrnReqInstrument agrTrnReqInstrument = agrTrnReqInstrumentRepository
							.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
				
					
				if (agrTrnReqInstrument != null) {
					recieptReqDetailsResponseDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
					recieptReqDetailsResponseDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());
					recieptReqDetailsResponseDto.setInstrumentLocation(agrTrnReqInstrument.getInstrumentLocation());
					
					}
				
				AgrTrnRequestStatus agrTrnReqStatus = agrTrnRequestStatus
						.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
				
				if (agrTrnReqStatus != null) {
					recieptReqDetailsResponseDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
				
					}	
				recieptReqDtoList.add(recieptReqDetailsResponseDto);
				}
			}

			// Return a Page object for DTOs using convertToPage
			return pageableUtils.convertToPage(recieptReqDtoList, pageable);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	
			

}
