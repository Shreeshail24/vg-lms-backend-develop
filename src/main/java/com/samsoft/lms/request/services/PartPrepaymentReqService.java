package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.dto.EventFeeOutputDto;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.CoreServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.dto.PartPrepaymentDto;
import com.samsoft.lms.request.dto.PrepaymentDuesList;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPrepaymentDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPrepaymentHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.ForclosureDueDetails;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.services.PartPrepaymentApplicationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartPrepaymentReqService {

	@Autowired
	private AgrTrnRequestHdrRepository reHdrRepo;

	@Autowired
	private AgrTrnReqInstrumentRepository reqInstRepo;

	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository reqInstAllocRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private CoreServices coreService;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private PartPrepaymentApplicationService prepayApplicationService;

	@Autowired
	private TrnInsInstrumentRepository insRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrTrnReqPrepaymentDtlRepository prepayDtlRepo;

	@Autowired
	private AgrTrnReqPrepaymentHdrRepository prepayHdrRepo;

	@Autowired
	private TrnInsInstrumentAllocRepository instAllocRepo;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private ApprovalService approvalService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String partPrepaymentOnlineRequest(PartPrepaymentDto partPrepayment) throws Exception {

		String result = "success";

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		try {

			List<TrnInsInstrument> instruments = insRepo
					.findByMasterAgrAndInstrumentStatusIn(partPrepayment.getMastAgrId(), Arrays.asList("UNP", "PNT"));
			if (instruments.size() > 0) {
				throw new CoreBadRequestException(
						"Prepayment can not be done as some instruments are in pending status");
			}

			AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(partPrepayment.getMastAgrId()).get(0);

			AgrMasterAgreement master = masterRepo.findByMastAgrId(partPrepayment.getMastAgrId());
			AgrTrnRequestHdr reqHdr ;
		//	AgrTrnRequestStatus status;
			
			if (partPrepayment.getReqId() != null) {
				reqHdr = reHdrRepo.findById(partPrepayment.getReqId())
			                    .orElseThrow(() -> new TransactionDataNotFoundException("Request Header not found for id: " + partPrepayment.getReqId()));
			} else {
				reqHdr = new AgrTrnRequestHdr();
			}
			
//			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			reqHdr.setMasterAgreement(master);
			reqHdr.setDtRequest(sdf.parse(partPrepayment.getDtRequest()));
			reqHdr.setActivityCode("PART_PREPAYMENT");
			reqHdr.setReqStatus(partPrepayment.getRequestStatus());
	//		reqHdr.setFlowType(partPrepayment.getFlowType());
			if ("NoFlow".equals(partPrepayment.getFlowType())) {
				reqHdr.setReqStatus("APPROVED");
			} else {
				reqHdr.setReqStatus(partPrepayment.getRequestStatus());
			}
			reqHdr.setReason(partPrepayment.getReason());
			reqHdr.setRemark(partPrepayment.getRemark());
			reqHdr.setUserId(partPrepayment.getUserId());

			reHdrRepo.save(reqHdr);
			
			if (partPrepayment.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(partPrepayment.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(reqHdr);
					existingRequestStatus.setDtReqChangeDate(sdf.parse(partPrepayment.getDtRequest()));
					existingRequestStatus.setReqStatus(partPrepayment.getRequestStatus());
					existingRequestStatus.setReason(partPrepayment.getReason());
					existingRequestStatus.setRemark(partPrepayment.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = partPrepayment.getAllocatedUserId();	
			if (("NoFlow".equals(partPrepayment.getFlowType()))) {
				
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(reqHdr);
		    	status.setUserId(partPrepayment.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(sdf.parse(partPrepayment.getDtRequest()));
	    	    status.setReason(partPrepayment.getReason());
	    	    status.setRemark(partPrepayment.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(reqHdr.getReqId(),userId,
		        		partPrepayment.getReason(), partPrepayment.getRemark(),partPrepayment.getRequestStatus(),reqHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }

			Date dtRecipt = sdf.parse(partPrepayment.getDtReceipt());
			Date dtInstrument = sdf
					.parse(partPrepayment.getDtInstrument().equalsIgnoreCase("") ? partPrepayment.getDtReceipt()
							: partPrepayment.getDtInstrument());
			Date dtRequest = sdf.parse(partPrepayment.getDtRequest());

			EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
					.getEventBaseChargesCalculation(partPrepayment.getMastAgrId(), 0, "PREPAYMENT", "N",
							partPrepayment.getPrepayAmount(), "Y");

			int i = 0;
			AgrTrnReqPrepaymentHdr prepayHdr;
			
			if (partPrepayment.getReqId() != null) {

				AgrTrnReqPrepaymentHdr existingPrePayHdr = prepayHdrRepo.findByRequestHdrReqId(partPrepayment.getReqId());
						
				if (existingPrePayHdr != null) {
					prepayHdr = existingPrePayHdr;
				} else {
					prepayHdr = new  AgrTrnReqPrepaymentHdr();
				}
			} else {
				prepayHdr = new  AgrTrnReqPrepaymentHdr();
			}
			
//			AgrTrnReqPrepaymentHdr prepayHdr = new AgrTrnReqPrepaymentHdr();
			for (PrepaymentDuesList dueList : partPrepayment.getPrepayDueList()) {
				if (i == 0) {
					prepayHdr.setBpiAmount(commonService
							.getInterestAccruedTillDateByTranType(partPrepayment.getMastAgrId(), "INTEREST_ACCRUAL"));
					prepayHdr.setChangeFactor(partPrepayment.getChangeFactor());
					prepayHdr.setDiscountAmount(partPrepayment.getDiscountAmount());
					prepayHdr.setDtTranDate(dtRequest);
					prepayHdr.setExcessAmount(partPrepayment.getExcessAmount());
					prepayHdr.setLoanId(agrLoans.getLoanId());
					prepayHdr.setMastAgrId(partPrepayment.getMastAgrId());
					if (eventBaseChargesCalculation.getFeeList().size() > 0) {
						prepayHdr.setPrepayCharge(eventBaseChargesCalculation.getFeeList().get(0).getFeeAmount());
					}
					prepayHdr.setPrepayReason(partPrepayment.getReason());
					prepayHdr.setRequestHdr(reqHdr);
					prepayHdr.setStatus("APR");
					prepayHdr.setTranType(partPrepayment.getPaymentType());
					prepayHdr.setUnbilledPrincipal(master.getUnbilledPrincipal());
					prepayHdr.setUserId(partPrepayment.getUserId());

					prepayHdrRepo.save(prepayHdr);

				} else {

					AgrTrnReqPrepaymentDtl prepayDtl = new AgrTrnReqPrepaymentDtl();

					prepayDtl.setDtDueDate(sdf.parse(dueList.getDueDate()));
					prepayDtl.setDueAmount(dueList.getDueAmount());
					prepayDtl.setDueCategory(dueList.getDueCategory());
					prepayDtl.setDueHead(dueList.getDueHead());
					prepayDtl.setInstallmentNo(dueList.getInstallmentNo());
					prepayDtl.setLoanId(agrLoans.getLoanId());
					prepayDtl.setMastAgrId(partPrepayment.getMastAgrId());
					prepayDtl.setPrepaymentHdr(prepayHdr);
					prepayDtl.setUserId(partPrepayment.getUserId());

					prepayDtlRepo.save(prepayDtl);

				}

			}
			
			AgrTrnReqInstrument instReq;
			
			if (partPrepayment.getReqId() != null) {

				AgrTrnReqInstrument existingInstruReq = reqInstRepo.findByRequestHdrReqId(partPrepayment.getReqId());
						
				if (existingInstruReq != null) {
					instReq = existingInstruReq;
				} else {
					instReq = new AgrTrnReqInstrument();
				}
			} else {
				instReq = new AgrTrnReqInstrument();
			}
			

//			AgrTrnReqInstrument instReq = new AgrTrnReqInstrument();
			instReq.setRequestHdr(reqHdr);
			instReq.setDtInstrumentDate(dtInstrument);
			instReq.setPayMode(partPrepayment.getPaymentMode());
			instReq.setPayType(partPrepayment.getPaymentType());
			instReq.setInstrumentType(partPrepayment.getInstrumentType());
			// instReq.setInstrumentNo(instrumentNo);
			instReq.setBankCode(partPrepayment.getBankCode());
			instReq.setBankBranchCode(partPrepayment.getBranchCode());
			instReq.setInstrumentAmount(partPrepayment.getInstrumentAmount());
			instReq.setInstrumentStatus("CLR");
			instReq.setIfscCode(partPrepayment.getIfscCode());
			instReq.setDtReceipt(dtRecipt);
			instReq.setDepositRefNo(partPrepayment.getDepositRefNo());
			instReq.setNclStatus("N");
			instReq.setCollectedBy(partPrepayment.getCollectionAgent());
			instReq.setCollectionAgency(partPrepayment.getCollectionAgency());
			instReq.setUtrNo(partPrepayment.getUtrNo());
			instReq.setAccountType(partPrepayment.getAccountType());
			instReq.setAccountNo(partPrepayment.getAccountNo());
			instReq.setInstrumentLocation(partPrepayment.getInstrumentLocation());
			instReq.setInstrumentNo(partPrepayment.getInstrumentNo());
			AgrTrnReqInstrument updateInstrumentNo = reqInstRepo.save(instReq);
			updateInstrumentNo.setInstrumentNo(Integer.toString(updateInstrumentNo.getInstrumetSrNo()));
			reqInstRepo.save(updateInstrumentNo);

			/*
			 * TrnInsInstrument insInstrument = new TrnInsInstrument();
			 * insInstrument.setMasterAgr(partPrepayment.getMastAgrId());
			 * insInstrument.setDtInstrumentDate(dtInstrument);
			 * insInstrument.setPayType(partPrepayment.getPaymentType());
			 * insInstrument.setPayMode(partPrepayment.getPaymentMode());
			 * insInstrument.setInstrumentType(partPrepayment.getInstrumentType());
			 * insInstrument.setInstrumentAmount(commonService.numberFormatter(
			 * partPrepayment.getInstrumentAmount()));
			 * insInstrument.setInstrumentStatus("CLR");
			 * insInstrument.setDtReceipt(dtRequest); insInstrument.setNclStatus("N");
			 * insInstrument.setBankName(partPrepayment.getBankCode());
			 * insInstrument.setBankBranchName(partPrepayment.getBranchCode());
			 * insInstrument.setIfscCode(partPrepayment.getIfscCode());
			 * insInstrument.setUtrNo(partPrepayment.getUtrNo());
			 * insInstrument.setUserId(partPrepayment.getUserId());
			 * insInstrument.setDepositRefNo(partPrepayment.getDepositRefNo());
			 * insInstrument.setCollectedBy(partPrepayment.getCollectionAgent());
			 * insInstrument.setCollectionAgency(partPrepayment.getCollectionAgency());
			 * 
			 * TrnInsInstrument updateInstNo = insRepo.save(insInstrument);
			 * updateInstNo.setInstrumentNo(Integer.toString(updateInstNo.getInstrumentId())
			 * ); insRepo.save(updateInstNo);
			 */

			AgrTrnReqInstrumentAllocDtl instAlloc;
			if (partPrepayment.getReqId() != null) {

				AgrTrnReqInstrumentAllocDtl existingInstAlloc = reqInstAllocRepo.findByInstrumentRequestHdrReqId(partPrepayment.getReqId());
						
				if (existingInstAlloc != null) {
					instAlloc = existingInstAlloc;
				} else {
					instAlloc = new AgrTrnReqInstrumentAllocDtl();
				}
			} else {
				instAlloc = new AgrTrnReqInstrumentAllocDtl();
			}
			
//			AgrTrnReqInstrumentAllocDtl instAlloc = new AgrTrnReqInstrumentAllocDtl();
			instAlloc.setInstrument(instReq);
			instAlloc.setLoanId(partPrepayment.getLoanId());
			instAlloc.setPayMode(partPrepayment.getPaymentMode());
			instAlloc.setAmout(partPrepayment.getAllocatedAmount());
			instAlloc.setUserId(partPrepayment.getUserId());

			reqInstAllocRepo.save(instAlloc);

			/*
			 * TrnInsInstrumentAlloc inst = new TrnInsInstrumentAlloc();
			 * inst.setLoanId(partPrepayment.getLoanId());
			 * inst.setApportionAmount(commonService.numberFormatter(partPrepayment.
			 * getInstrumentAmount())); inst.setUserId(partPrepayment.getUserId());
			 * inst.setInstrument(insInstrument);
			 * 
			 * instAllocRepo.save(inst);
			 * 
			 * log.info("Before Payment Application ");
			 * 
			 * prepayApplicationService.partPrepaymentApplicationAgrAutoApportion(
			 * partPrepayment.getMastAgrId(), updateInstNo.getInstrumentId(), dtRecipt);
			 * 
			 * log.info("After payment application");
			 */
			
			if ("NoFlow".equalsIgnoreCase(partPrepayment.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(partPrepayment.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(partPrepayment.getRequestStatus()))) {
			prepayApplicationService.partPrepaymentApplicationAgrAutoApportion(partPrepayment.getUserId(),
					dtRequest, partPrepayment.getReqId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public ForclosureReceivableListDto getPrepayReceivableList(String mastAgrId, Double dreAmount) throws Exception {
		ForclosureReceivableListDto result = new ForclosureReceivableListDto();
		Double totalReceivable = 0.0;
		Double netReceivables = 0.0;
		Double totalAmount = 0.0;
		Double excessAmount;
		Double prepayAmount = 0.0;
		try {
			excessAmount = commonService.getMasterAgrExcessAmount(mastAgrId);
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdOrderByDtDueDate(mastAgrId);
			List<ForclosureDueDetails> forclosureDueList = new ArrayList<ForclosureDueDetails>();
			AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			for (AgrTrnDueDetails due : dueList) {
				ForclosureDueDetails dueDetails = new ForclosureDueDetails();
				dueDetails.setDueCategory(due.getDueCategory());
				dueDetails.setDueHead(due.getDueHead());
				dueDetails.setDueAmount(due.getDueAmount());
				dueDetails.setDueDate(
						new SimpleDateFormat(env.getProperty("lms.global.date.format")).format(due.getDtDueDate()));
				dueDetails.setBookYn("N");

				if (due.getDueCategory().equalsIgnoreCase("FEE")) {
					List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(due.getDueDtlId());
					for (AgrTrnTaxDueDetails tax : taxList) {
						totalAmount += tax.getDueTaxAmount();
						totalReceivable += tax.getDueTaxAmount();
					}
				}

				forclosureDueList.add(dueDetails);
				totalReceivable += due.getDueAmount();
				totalAmount += due.getDueAmount();
			}

			Integer maxDpd = sysRepo.getMaxDpd("PENAL_ACCRUAL", agrLoans.getLoanId());
			Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(mastAgrId).getGraceDays();
			if (maxDpd == null) {
				maxDpd = 0;
			}
			if (graceDays == null) {
				graceDays = 0;
			}
			if (maxDpd > graceDays) {
				double penalAccrual = commonService.numberFormatter(
						commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL"));
				if (penalAccrual > 0) {
					ForclosureDueDetails penalAccrualDue = new ForclosureDueDetails();
					penalAccrualDue.setDueCategory("FEE");
					penalAccrualDue.setDueHead("PENAL");
					penalAccrualDue.setDueAmount(penalAccrual);
					penalAccrualDue.setBookYn("N");
					penalAccrualDue.setDueDate(commonService.getBusinessDateInString());
					forclosureDueList.add(penalAccrualDue);

					totalAmount += penalAccrual;
					totalReceivable += penalAccrual;
				}
			}

			double interestAccrual = commonService
					.numberFormatter(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));

			if (interestAccrual > 0) {
				ForclosureDueDetails interestAccrualDue = new ForclosureDueDetails();
				interestAccrualDue.setDueCategory("BPI");
				interestAccrualDue.setDueHead("INTEREST");
				interestAccrualDue.setDueAmount(interestAccrual);
				interestAccrualDue.setBookYn("N");
				interestAccrualDue.setDueDate(commonService.getBusinessDateInString());
				forclosureDueList.add(interestAccrualDue);

				totalAmount += interestAccrual;
				totalReceivable += interestAccrual;
			}
			prepayAmount = commonService.numberFormatter((dreAmount + excessAmount) - totalAmount);

			EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
					.getEventBaseChargesCalculation(mastAgrId, 0, "PREPAYMENT", "N", prepayAmount, "Y");

			Double prepayCharge = 0d;

			for (EventFeeOutputDto fees : eventBaseChargesCalculation.getFeeList()) {
				prepayCharge += fees.getFeeAmount();
				Double gstAmont = 0d;

				prepayCharge += gstAmont;

				ForclosureDueDetails prepayChargeDue = new ForclosureDueDetails();
				prepayChargeDue.setDueCategory("FEE");
				prepayChargeDue.setDueHead(fees.getFeeCode());
				prepayChargeDue.setDueAmount(commonService.numberFormatter(prepayCharge));
				prepayChargeDue.setBookYn("N");
				prepayChargeDue.setDueDate(commonService.getBusinessDateInString());
				forclosureDueList.add(prepayChargeDue);
			}

			totalAmount += commonService.numberFormatter(prepayCharge);
			totalReceivable += commonService.numberFormatter(prepayCharge);

			prepayAmount = commonService.numberFormatter((dreAmount + excessAmount) - totalAmount);

			ForclosureDueDetails unbilledDue = new ForclosureDueDetails();
			unbilledDue.setDueCategory("UNBILLED_PRINCIPAL");
			unbilledDue.setDueHead("FUTURE_PRINCIPAL");
			unbilledDue.setDueAmount(prepayAmount);
			unbilledDue.setBookYn("Y");
			unbilledDue.setDueDate(commonService.getBusinessDateInString());
			forclosureDueList.add(unbilledDue);

			totalReceivable += prepayAmount;

			result.setDueList(forclosureDueList);
			result.setExcess(commonService.numberFormatter(excessAmount));
			result.setNetReceivables(commonService.numberFormatter(totalReceivable - excessAmount));
			result.setTotalReceivables(commonService.numberFormatter(totalReceivable));

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	
	// ----- Method to get Credit note requests by status, date and reqId
		public Page<PartPrepaymentDto> getPartPrepaymentReqList(List<String> statusList, String activityCode,
				Integer reqNo, String date, Pageable pageable) throws Exception {

			List<PartPrepaymentDto> partPrepaymentReqDtoList = new ArrayList<>();
			try {

				List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

				if (!statusList.isEmpty() || reqNo == 0 && date == null) {
					
					agrTrnRequestHdrList = reHdrRepo.getRequestsByStatusesAndActivityCode(statusList, activityCode);
					 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
			                throw new CoreDataNotFoundException("No records found for the provided status : " + statusList);
			            }

				} else if (reqNo != 0 || statusList == null || date == null) {
					
					AgrTrnRequestHdr agrTrnRequestHdr = reHdrRepo.findByReqIdAndActivityCode(reqNo, activityCode);
					 if (agrTrnRequestHdr == null) {
			                throw new CoreDataNotFoundException("No record found for the given request ID: " + reqNo);
			            }
					agrTrnRequestHdrList.add(agrTrnRequestHdr);

				} else if (date != null || statusList == null || reqNo == null) {
					// change string date into Date format
					String dateFormat = env.getProperty("lms.global.date.format");
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					Date requestedDate = sdf.parse(date);
					agrTrnRequestHdrList = reHdrRepo.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
					
					 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
			                throw new CoreDataNotFoundException("No records found for the requested date: " + date);
			            }	
				
				}

				if (!agrTrnRequestHdrList.isEmpty() && agrTrnRequestHdrList != null) {

					for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
						PartPrepaymentDto partPrepaymentReqDto = new PartPrepaymentDto();
						
						partPrepaymentReqDto.setReqId(agrTrnRequestHdr.getReqId());
						partPrepaymentReqDto.setDtRequest(agrTrnRequestHdr.getDtRequest().toString());
						partPrepaymentReqDto.setFlowType(agrTrnRequestHdr.getFlowType());
						partPrepaymentReqDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
						partPrepaymentReqDto.setReason(agrTrnRequestHdr.getReason());
						partPrepaymentReqDto.setRemark(agrTrnRequestHdr.getRemark());
						partPrepaymentReqDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
						partPrepaymentReqDto.setUserId(agrTrnRequestHdr.getUserId());

						
						AgrTrnReqInstrument agrTrnReqInstrument = reqInstRepo
								.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
						
					
						if (agrTrnReqInstrument != null) {
							
							partPrepaymentReqDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
							partPrepaymentReqDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());
						}
						
						AgrTrnRequestStatus agrTrnReqStatus = statusRepo
								.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
						
						if (agrTrnReqStatus != null) {
							partPrepaymentReqDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
						
							}	
						partPrepaymentReqDtoList.add(partPrepaymentReqDto);
					}
				}

				// Return a Page object for DTOs using convertToPage
				return pageableUtils.convertToPage(partPrepaymentReqDtoList, pageable);

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

}
