package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.instrument.entities.TrnInsBatchInstruments;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsBatchInstrumentsRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.dto.DreAllocationDto;
import com.samsoft.lms.request.dto.ForclosureCashRequestDto;
import com.samsoft.lms.request.dto.ForclosureChequeRequestDto;
import com.samsoft.lms.request.dto.ForclosureOnlineRequestDto;
import com.samsoft.lms.request.dto.PartPrepaymentDto;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.services.ForclosureApplicationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForclosureRequestService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnRequestHdrRepository requestHeaderRepo;

	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository reqAllocRepo;

	@Autowired
	private AgrMasterAgreementRepository agreementRepo;

	@Autowired
	private ForclosureApplicationService forclosureApplication;

	@Autowired
	private TrnInsInstrumentAllocRepository insAllocRepo;

	@Autowired
	private PaymentApplicationServices payApplService;

	@Autowired
	private TrnInsInstrumentRepository instRepo;

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;

	@Autowired
	private TrnInsBatchInstrumentsRepository batchInsRepo;

	@Autowired
	private CommonServices commService;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;
	
	@Autowired
	private AgrTrnReqInstrumentRepository reqInstRepo;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private ApprovalService approvalService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String forclosureOnlineRequest(ForclosureOnlineRequestDto forclosureDto) throws Exception {

		String result = "success";

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			log.info("Forclosure Request  " + forclosureDto);

			/*
			 * List<TrnInsBatchInstruments> batchInstrumentList =
			 * batchInsRepo.findByBatchHdrBatchStatusNot("C"); List<Integer> instrumentList
			 * = new ArrayList<Integer>(); for (TrnInsBatchInstruments batchInst :
			 * batchInstrumentList) { instrumentList.add(batchInst.getInstrumentId()); }
			 */

			List<TrnInsInstrument> instruments = instRepo.findByMasterAgrAndInstrumentStatusIn(
					forclosureDto.getMasterAgreementId(), Arrays.asList("UNP", "PNT"));
			if (instruments.size() > 0) {
				throw new CoreBadRequestException(
						"Foreclosure can not be done as some instruments are in pending status");
			}

			Date requestDate = new SimpleDateFormat(dateFormat).parse(forclosureDto.getRequestDate());
			Date instrumentDate = new SimpleDateFormat(dateFormat)
					.parse(forclosureDto.getInstrumentDate().equals("") ? forclosureDto.getRequestDate()
							: forclosureDto.getInstrumentDate());

			AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(forclosureDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("FORECLOSURE");
			requestHdr.setReqStatus(forclosureDto.getRequestStatus());
			requestHdr.setFlowType(forclosureDto.getFlowType());
			requestHdr.setReason(forclosureDto.getReason());
			requestHdr.setRemark(forclosureDto.getRemark());
			requestHdr.setUserId(forclosureDto.getUserId());

			AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(forclosureDto.getPaymentType());
			instrument.setPayMode(forclosureDto.getPaymentMode());
			instrument.setInstrumentType(forclosureDto.getInstrumentType());
			// instrument.setInstrumentNo(forclosureDto.get);
			instrument.setBankCode(forclosureDto.getBankCode());
			instrument.setBankBranchCode(forclosureDto.getBankBranchCode());
			instrument.setInstrumentAmount(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDtReceipt(requestDate);
			instrument.setIfscCode(forclosureDto.getIfscCode());
			instrument.setNclStatus("N");
			instrument.setUserId(forclosureDto.getUserId());
			instrument.setCollectedBy(forclosureDto.getCollectionAgent());
			instrument.setCollectionAgency(forclosureDto.getCollectionAgency());
			instrument.setUtrNo(forclosureDto.getUtrNo());

			AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
			instAlloc.setLoanId(forclosureDto.getLoanId());
			instAlloc.setPayMode(forclosureDto.getPaymentMode());
			instAlloc.setAmout(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instAlloc.setUserId(forclosureDto.getUserId());
			instAlloc.setInstrument(instrument);

			reqHdrRepo.save(requestHdr);

			reqAllocRepo.save(instAlloc);

			AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			status.setRequestHdr(requestHdr);
			status.setDtReqChangeDate(sdf.parse(forclosureDto.getRequestDate()));
			status.setReqStatus("PENDING");
			status.setReason(forclosureDto.getReason());
			status.setRemark(forclosureDto.getRemark());

			statusRepo.save(status);

		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String forclosureChequeRequest(ForclosureChequeRequestDto forclosureDto) throws Exception {
		String result = "";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			log.info("Forclosure Request  Cheque" + forclosureDto);

			List<TrnInsBatchInstruments> batchInstrumentList = batchInsRepo.findByBatchHdrBatchStatusNot("C");
			List<Integer> instrumentList = new ArrayList<Integer>();
			for (TrnInsBatchInstruments batchInst : batchInstrumentList) {
				instrumentList.add(batchInst.getInstrumentId());
			}

			List<TrnInsInstrument> instruments = instRepo
					.findByMasterAgrAndInstrumentIdIn(forclosureDto.getMasterAgreementId(), instrumentList);
			if (instruments.size() > 0) {
				throw new CoreBadRequestException(
						"Foreclosure can not be done as some instruments are in pending status");
			}

			AgrMasterAgreement master = agreementRepo.findByMastAgrId(forclosureDto.getMasterAgreementId());
			Date requestDate = new SimpleDateFormat(dateFormat).parse(forclosureDto.getRequestDate());
			Date instrumentDate = new SimpleDateFormat(dateFormat).parse(forclosureDto.getInstrumentDate());

			AgrTrnReqInstrument instrument ;
			AgrTrnReqInstrumentAllocDtl instAlloc ;
		//	AgrTrnRequestStatus status;
			AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(forclosureDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("FORECLOSURE");
			requestHdr.setReqStatus(forclosureDto.getRequestStatus());
			requestHdr.setFlowType(forclosureDto.getFlowType());
			if ("NoFlow".equals(forclosureDto.getFlowType())) {
			    requestHdr.setReqStatus("APPROVED");
			} else {
			    requestHdr.setReqStatus(forclosureDto.getRequestStatus());
			}
			requestHdr.setReason(forclosureDto.getReason());
			requestHdr.setRemark(forclosureDto.getRemark());
			requestHdr.setUserId(forclosureDto.getUserId());
			
			if(forclosureDto.getReqId() != null) {
				requestHdr.setReqId(forclosureDto.getReqId());
			}
			
			reqHdrRepo.save(requestHdr);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnReqInstrument existingTrnReqInstrument = reqInstRepo.
						findByRequestHdrReqId(forclosureDto.getReqId());
				
				if (existingTrnReqInstrument != null) {
					instrument = existingTrnReqInstrument;
				} else {
					instrument = new AgrTrnReqInstrument();
				}
			} else {
				instrument = new AgrTrnReqInstrument();
			}

		//	AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(forclosureDto.getPaymentType());
			instrument.setPayMode(forclosureDto.getPaymentMode());
			instrument.setInstrumentType("CH");
			instrument.setAccountNo(forclosureDto.getBankAccountNo());
			instrument.setAccountType(forclosureDto.getAccountType());
			instrument.setInstrumentNo(forclosureDto.getInstrumentNo());
			instrument.setBankCode(forclosureDto.getBankCode());
			instrument.setBankBranchCode(forclosureDto.getBrachCode());
			instrument.setMicrCode(forclosureDto.getMicrCode());
			instrument.setClearingLocation(forclosureDto.getClearingLocation());
			instrument.setInstrumentAmount(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDepositBank(forclosureDto.getDepositBank());
			instrument.setUtrNo(forclosureDto.getUtrNo());
			instrument.setIfscCode(forclosureDto.getIfscCode());
			instrument.setDtReceipt(requestDate);
			instrument.setDepositRefNo(forclosureDto.getDepositRefNo());
			instrument.setCardHolderName(forclosureDto.getCardHolderName());
			instrument.setIssuingBank(forclosureDto.getIssuingBank());
			instrument.setUpiVpa(forclosureDto.getUpiVpa());
			instrument.setCollectedBy(forclosureDto.getCollectionAgent());
			instrument.setCollectionAgency(forclosureDto.getCollectionAgency());
			instrument.setProvisionalReceipt(forclosureDto.getProvisionalReceiptFlag());
			instrument.setProcLoc(forclosureDto.getProcessingLocation());
			instrument.setCardType(forclosureDto.getCardType());
			instrument.setNclStatus("N");
			instrument.setUserId(forclosureDto.getUserId());
			instrument.setInstrumentLocation(forclosureDto.getInstrumentLocation());
			
            reqInstRepo.save(instrument);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnReqInstrumentAllocDtl existingTrnReqInstrumentAllocDtl = reqAllocRepo.
						findByInstrumentRequestHdrReqId(forclosureDto.getReqId());
				
				if (existingTrnReqInstrumentAllocDtl != null) {
					instAlloc = existingTrnReqInstrumentAllocDtl;
				} else {
					instAlloc = new AgrTrnReqInstrumentAllocDtl();
				}
			} else {
				instAlloc = new AgrTrnReqInstrumentAllocDtl();
			}
			

		//	AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
			instAlloc.setLoanId(forclosureDto.getLoanId());
			instAlloc.setAmout(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instAlloc.setUserId(forclosureDto.getUserId());
			instAlloc.setInstrument(instrument);

		//	reqHdrRepo.save(requestHdr);

			reqAllocRepo.save(instAlloc);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(forclosureDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(requestHdr);
					existingRequestStatus.setDtReqChangeDate(requestDate);
					existingRequestStatus.setReqStatus(forclosureDto.getRequestStatus());
					existingRequestStatus.setReason(forclosureDto.getReason());
					existingRequestStatus.setRemark(forclosureDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = forclosureDto.getAllocatedUserId();	
			if (("NoFlow".equals(forclosureDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(requestHdr);
		    	status.setUserId(forclosureDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(requestDate);
	    	    status.setReason(forclosureDto.getReason());
	    	    status.setRemark(forclosureDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(requestHdr.getReqId(),userId,
		        		forclosureDto.getReason(), forclosureDto.getRemark(),forclosureDto.getRequestStatus(),requestHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
		
			if ("NoFlow".equalsIgnoreCase(forclosureDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(forclosureDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(forclosureDto.getRequestStatus()))) {
				
			forclosureApplication.forclosureApplication(forclosureDto.
					 getMasterAgreementId(),requestDate,forclosureDto.getReqId());
			}
			
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return "Payment Successful";

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String forclosureCashRequest(ForclosureCashRequestDto forclosureDto) throws Exception {
		String result = "";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			log.info("Forclosure Request Cash" + forclosureDto);

			List<TrnInsBatchInstruments> batchInstrumentList = batchInsRepo.findByBatchHdrBatchStatusNot("C");
			List<Integer> instrumentList = new ArrayList<Integer>();
			for (TrnInsBatchInstruments batchInst : batchInstrumentList) {
				instrumentList.add(batchInst.getInstrumentId());
			}

			List<TrnInsInstrument> instruments = instRepo
					.findByMasterAgrAndInstrumentIdIn(forclosureDto.getMasterAgreementId(), instrumentList);
			if (instruments.size() > 0) {
				throw new CoreBadRequestException(
						"Foreclosure can not be done as some instruments are in pending status");
			}

			AgrTrnReqInstrument instrument ;
			AgrTrnReqInstrumentAllocDtl instAlloc ;
		//	AgrTrnRequestStatus status;
			AgrTrnRequestHdr requestHdr = new AgrTrnRequestHdr();
			Date requestDate = new SimpleDateFormat(dateFormat).parse(forclosureDto.getRequestDate());
			if (forclosureDto.getInstrumentDate() == "") {
				forclosureDto.setInstrumentDate(forclosureDto.getRequestDate());
			}
			Date instrumentDate = new SimpleDateFormat(dateFormat)
					.parse(forclosureDto.getInstrumentDate().equals("") ? forclosureDto.getRequestDate()
							: forclosureDto.getInstrumentDate());

			requestHdr.setMasterAgreement(agreementRepo.findByMastAgrId(forclosureDto.getMasterAgreementId()));
			requestHdr.setDtRequest(requestDate);
			requestHdr.setActivityCode("FORECLOSURE");
			requestHdr.setReqStatus(forclosureDto.getRequestStatus());
			requestHdr.setFlowType(forclosureDto.getFlowType());
			if ("NoFlow".equals(forclosureDto.getFlowType())) {
			    requestHdr.setReqStatus("APPROVED");
			} else {
			    requestHdr.setReqStatus(forclosureDto.getRequestStatus());
			}
			requestHdr.setReason(forclosureDto.getReason());
			requestHdr.setRemark(forclosureDto.getRemark());
			requestHdr.setUserId(forclosureDto.getUserId());
			
			if(forclosureDto.getReqId() != null) {
				requestHdr.setReqId(forclosureDto.getReqId());
			}
			
			reqHdrRepo.save(requestHdr);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnReqInstrument existingTrnReqInstrument = reqInstRepo.
						findByRequestHdrReqId(forclosureDto.getReqId());
				
				if (existingTrnReqInstrument != null) {
					instrument = existingTrnReqInstrument;
				} else {
					instrument = new AgrTrnReqInstrument();
				}
			} else {
				instrument = new AgrTrnReqInstrument();
			}

		//	AgrTrnReqInstrument instrument = new AgrTrnReqInstrument();
			instrument.setRequestHdr(requestHdr);
			instrument.setDtInstrumentDate(instrumentDate);
			instrument.setPayType(forclosureDto.getPaymentType());
			instrument.setPayMode(forclosureDto.getPaymentMode());
			instrument.setInstrumentType(forclosureDto.getInstrumentType());
			instrument.setInstrumentAmount(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instrument.setInstrumentStatus("PENDING");
			instrument.setDtReceipt(requestDate);
			instrument.setDepositRefNo(forclosureDto.getDepositRefNo());
			instrument.setCollectedBy(forclosureDto.getCollectionAgent());
			instrument.setCollectionAgency(forclosureDto.getCollectionAgency());
			instrument.setNclStatus("N");
			instrument.setUserId(forclosureDto.getUserId());
			
			reqInstRepo.save(instrument);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnReqInstrumentAllocDtl existingTrnReqInstrumentAllocDtl = reqAllocRepo.
						findByInstrumentRequestHdrReqId(forclosureDto.getReqId());
				
				if (existingTrnReqInstrumentAllocDtl != null) {
					instAlloc = existingTrnReqInstrumentAllocDtl;
				} else {
					instAlloc = new AgrTrnReqInstrumentAllocDtl();
				}
			} else {
				instAlloc = new AgrTrnReqInstrumentAllocDtl();
			}
			

		//	AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
			instAlloc.setLoanId(forclosureDto.getLoanId());
			instAlloc.setAmout(commService.numberFormatter(forclosureDto.getInstrumentAmount()));
			instAlloc.setUserId(forclosureDto.getUserId());
			instAlloc.setInstrument(instrument);

		//	reqHdrRepo.save(requestHdr);

			reqAllocRepo.save(instAlloc);
			
	//		reqInstRepo.save(instrument);
			
			if (forclosureDto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(forclosureDto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(requestHdr);
					existingRequestStatus.setDtReqChangeDate(requestDate);
					existingRequestStatus.setReqStatus(forclosureDto.getRequestStatus());
					existingRequestStatus.setReason(forclosureDto.getReason());
					existingRequestStatus.setRemark(forclosureDto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = forclosureDto.getAllocatedUserId();	
			if (("NoFlow".equals(forclosureDto.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(requestHdr);
		    	status.setUserId(forclosureDto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(requestDate);
	    	    status.setReason(forclosureDto.getReason());
	    	    status.setRemark(forclosureDto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(requestHdr.getReqId(),userId,
		        		forclosureDto.getReason(), forclosureDto.getRemark(),forclosureDto.getRequestStatus(),requestHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
			
			if ("NoFlow".equalsIgnoreCase(forclosureDto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(forclosureDto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(forclosureDto.getRequestStatus()))) {
				
			forclosureApplication.forclosureApplication(forclosureDto.
					 getMasterAgreementId(),requestDate,forclosureDto.getReqId());
			}

		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return "Payment Successful";

	}
	
	
	// ----- Method to get Full-PrePayment Requests by status, date and reqId
			public Page<ForclosureCashRequestDto> getFullPrepaymentReqList(List<String> statusList, String activityCode,
					Integer reqNo, String date, Pageable pageable) throws Exception {

				List<ForclosureCashRequestDto> fullPrepaymentReqDtoList = new ArrayList<>();
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
							ForclosureCashRequestDto fullPrepaymentReqDto = new ForclosureCashRequestDto();
							
							fullPrepaymentReqDto.setReqId(agrTrnRequestHdr.getReqId());
							fullPrepaymentReqDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
							fullPrepaymentReqDto.setFlowType(agrTrnRequestHdr.getFlowType());
							fullPrepaymentReqDto.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
							fullPrepaymentReqDto.setReason(agrTrnRequestHdr.getReason());
							fullPrepaymentReqDto.setRemark(agrTrnRequestHdr.getRemark());
							fullPrepaymentReqDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
							fullPrepaymentReqDto.setUserId(agrTrnRequestHdr.getUserId());

							
							AgrTrnReqInstrument agrTrnReqInstrument = reqInstRepo
									.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
							
						
							if (agrTrnReqInstrument != null) {

								fullPrepaymentReqDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
								fullPrepaymentReqDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());

								AgrTrnRequestStatus agrTrnReqStatus = statusRepo
										.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());

								if (agrTrnReqStatus != null) {
									fullPrepaymentReqDto.setAllocatedUserId(agrTrnReqStatus.getUserId());

								}

							}
							fullPrepaymentReqDtoList.add(fullPrepaymentReqDto);
						}
					}

					// Return a Page object for DTOs using convertToPage
					return pageableUtils.convertToPage(fullPrepaymentReqDtoList, pageable);

				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}

}
