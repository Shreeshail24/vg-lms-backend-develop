package com.samsoft.lms.newux.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.newux.dto.response.ChargesReqBookingReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.ChargesWaiverParamListResponseDto;
import com.samsoft.lms.newux.dto.response.ChargesWaverReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.CreditNoteReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DebitNoteReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownDisbUpdateReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.DrawDownUpdateReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.ForClosureOnlineReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.PartPaymentDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.RecieptReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.RefundReqDetailsResponseDto;
import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeAndStatusResponseDto;
import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeResponseDto;
import com.samsoft.lms.newux.dto.response.getloans.AllLoanDetailsResDto;
import com.samsoft.lms.newux.dto.response.getloans.AllSRWithActCodeAndStatusResDto;
import com.samsoft.lms.newux.dto.response.getloans.AllSRWithActCodeResDto;
import com.samsoft.lms.newux.dto.response.getloans.GetAllLoansResDto;
import com.samsoft.lms.newux.dto.response.getloans.SrPendingCountResDto;
import com.samsoft.lms.newux.exceptions.LoansNotFoundException;
import com.samsoft.lms.newux.exceptions.SRNotFoundException;
import com.samsoft.lms.request.dto.ChargesReqBookingDto;
import com.samsoft.lms.request.dto.ChargesWaiverParamListDto;
import com.samsoft.lms.request.dto.DreAllocationDto;
import com.samsoft.lms.request.entities.AgrTrnReqChargesBookingDtl;
import com.samsoft.lms.request.entities.AgrTrnReqChargesWaiverDtl;
import com.samsoft.lms.request.entities.AgrTrnReqCreditNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnReqDebitNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentHdr;
import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.entities.DrawdownRequest;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesBookingDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesWaiverDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqCreditNoteDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqDebitNoteDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPayoutInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPrepaymentDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPrepaymentHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRefundDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.request.repositories.DrawDownRequestRepository;
import com.samsoft.lms.newux.dto.response.getloans.RequestedStatusCountDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SRService {

	@Autowired
	private AgrTrnRequestHdrRepository agrTrnRequestHdrRepository;

	@Autowired
	private AgrTrnReqInstrumentRepository agrTrnReqInstrumentRepository;

	@Autowired
	private AgrTrnReqRefundDtlRepository agrTrnReqRefundDtlRepository;

	@Autowired
	private AgrTrnRequestStatusRepository agrTrnRequestStatusRepository;

	@Autowired
	private AgrTrnReqPayoutInstrumentRepository agrTrnReqPayoutInstrumentRepository;

	@Autowired
	private AgrTrnReqDebitNoteDtlRepository agrTrnReqDebitNoteDtlRepository;

	@Autowired
	private AgrTrnReqCreditNoteDtlRepository agrTrnReqCreditNoteDtlRepository;

	@Autowired
	private AgrTrnReqChargesBookingDtlRepository agrTrnReqChargesBookingDtlRepository;

	@Autowired
	private AgrTrnReqChargesWaiverDtlRepository agrTrnReqChargesWaiverDtlRepository;

	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository agrTrnReqInstrumentAllocDtlRepository;

	@Autowired
	private AgrTrnReqPrepaymentDtlRepository agrTrnReqPrepaymentDtlRepository;

	@Autowired
	private AgrTrnReqPrepaymentHdrRepository agrTrnReqPrepaymentHdrRepository;

	@Autowired
	private DrawDownRequestRepository downRequestRepository;

	@Autowired
	private CustomerServices customerServices;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PageableUtils pageableUtils;

	// Get All Service Requests Pending Count
	public SrPendingCountResDto getSRPendingCount() throws Exception {

		SrPendingCountResDto srPendingCountResDto = new SrPendingCountResDto();

		try {

			Integer debitSrPendingCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus("DEBIT_NOTE",
					"PENDING", "PND");
			srPendingCountResDto.setDebitSrPendingCount(debitSrPendingCount);

			Integer creditSrPendingCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus("CREDIT_NOTE",
					"PENDING", "PND");
			srPendingCountResDto.setCreditSrPendingCount(creditSrPendingCount);

			Integer recepitSrPendingCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus("RECEIPT",
					"PENDING", "PND");
			srPendingCountResDto.setRecepitSrPendingCount(recepitSrPendingCount);

			Integer refundSrPendingCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus("REFUND",
					"PENDING", "PND");
			srPendingCountResDto.setRefundSrPendingCount(refundSrPendingCount);

			Integer forclousreSrPendingCount = agrTrnRequestHdrRepository
					.getSrCountByActivityCodeANdStatus("FORECLOSURE", "PENDING", "PND");
			srPendingCountResDto.setForclousreSrPendingCount(forclousreSrPendingCount);

			Integer overdraftSrPendingCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus("DRAWDOWN",
					"PENDING", "PND");
			srPendingCountResDto.setOverdraftSrPendingCount(overdraftSrPendingCount);

			Integer chargesBookingSrPendingCount = agrTrnRequestHdrRepository
					.getSrCountByActivityCodeANdStatus("CHARGES_BOOKING", "PENDING", "PND");
			srPendingCountResDto.setChargesBookingSrPendingCount(chargesBookingSrPendingCount);

			Integer chargesWaverSrPendingCount = agrTrnRequestHdrRepository
					.getSrCountByActivityCodeANdStatus("CHARGES_WAIVER", "PENDING", "PND");
			srPendingCountResDto.setChargesWaverSrPendingCount(chargesWaverSrPendingCount);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return srPendingCountResDto;
	}

	// Get All Service Requests by ActivityCode
	public AllSRWithActCodeResDto getAllSRByActivityCode(String activityCode) throws Exception {

		AllSRWithActCodeResDto allSRResDto = new AllSRWithActCodeResDto();
		Integer allSRCount = 0;
		Integer pendingSRCount = 0;
		Integer approvedSRCount = 0;
		Integer rejectedSRCount = 0;
		try {
			if (activityCode == null) {
				throw new SRNotFoundException("Required Type.");
			}

			allSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCode(activityCode);
			allSRResDto.setAllSRCount(allSRCount);

			pendingSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "PENDING",
					"PND");
			allSRResDto.setPendingSRCount(pendingSRCount);

			approvedSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "APPROVED",
					"APR");
			allSRResDto.setApprovedSRCount(approvedSRCount);

			rejectedSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "REJECTED",
					"REJ");
			allSRResDto.setRejectedSRCount(rejectedSRCount);
			List<SrWorklistByActivityCodeResponseDto> allSRByActivityCode = agrTrnRequestHdrRepository
					.getAllSRByActivityCode(activityCode);
			allSRResDto.setType("ALL");
			allSRResDto.setServiceRequests(allSRByActivityCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return allSRResDto;
	}

	// Get All Service Requests by ActivityCode and Request Status
	public AllSRWithActCodeAndStatusResDto getAllSRByActivityCodeAndStatus(String activityCode, String status)
			throws Exception {

		AllSRWithActCodeAndStatusResDto allSRResDto = new AllSRWithActCodeAndStatusResDto();
		Integer allSRCount = 0;
		Integer pendingSRCount = 0;
		Integer approvedSRCount = 0;
		Integer rejectedSRCount = 0;
		try {
			if (activityCode == null) {
				throw new SRNotFoundException("Required Type.");
			}

			String otherStatus = status;
			switch (status) {
				case "APR":
					otherStatus = "APPROVED";
					break;
				case "REJ":
					otherStatus = "REJECTED";
					break;
				case "PND":
					otherStatus = "PENDING";
					break;
			}

			allSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCode(activityCode);
			allSRResDto.setAllSRCount(allSRCount);

			pendingSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "PENDING",
					"PND");
			allSRResDto.setPendingSRCount(pendingSRCount);

			approvedSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "APPROVED",
					"APR");
			allSRResDto.setApprovedSRCount(approvedSRCount);

			rejectedSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCodeANdStatus(activityCode, "REJ",
					"REJECTED");
			allSRResDto.setRejectedSRCount(rejectedSRCount);

			List<SrWorklistByActivityCodeAndStatusResponseDto> allSRByActivityCodeAndStatus = agrTrnRequestHdrRepository
					.getAllSRByActivityCodeAndStatus(activityCode, status, otherStatus);
			allSRResDto.setType(status);
			allSRResDto.setServiceRequests(allSRByActivityCodeAndStatus);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return allSRResDto;
	}

	// Get Reciept Request Details
	public RecieptReqDetailsResponseDto getRecieptReqDetails(String customerId, String mastAgrId, String reqId)
			throws Exception {

		RecieptReqDetailsResponseDto recieptReqDetailsResponseDto = new RecieptReqDetailsResponseDto();
		try {

			log.info("IN getRecieptReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnReqInstrument agrTrnReqInstrument = agrTrnReqInstrumentRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			
			if (agrTrnRequestStatus != null) {
				recieptReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}
			if (agrTrnRequestHdr != null) {
				recieptReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				recieptReqDetailsResponseDto.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				recieptReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				recieptReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				recieptReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				recieptReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				recieptReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				recieptReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				recieptReqDetailsResponseDto.setClearingLocation(agrTrnReqInstrument.getClearingLocation());
				recieptReqDetailsResponseDto.setIfscCode(agrTrnReqInstrument.getIfscCode());
				recieptReqDetailsResponseDto.setIssuingBank(agrTrnReqInstrument.getIssuingBank());
				recieptReqDetailsResponseDto.setAccountType(agrTrnReqInstrument.getAccountType());
				recieptReqDetailsResponseDto.setAccountNo(agrTrnReqInstrument.getAccountNo());
				recieptReqDetailsResponseDto.setDtReceipt(agrTrnReqInstrument.getDtReceipt());
				recieptReqDetailsResponseDto.setDepositBank(agrTrnReqInstrument.getDepositBank());
				recieptReqDetailsResponseDto.setBankBranchCode(agrTrnReqInstrument.getBankBranchCode());
				recieptReqDetailsResponseDto.setBankCode(agrTrnReqInstrument.getBankCode());
				recieptReqDetailsResponseDto.setCardHolderName(agrTrnReqInstrument.getCardHolderName());
				recieptReqDetailsResponseDto.setCardType(agrTrnReqInstrument.getCardType());
				recieptReqDetailsResponseDto.setInstrumentNo(agrTrnReqInstrument.getInstrumentNo());
				recieptReqDetailsResponseDto.setMICRCode(agrTrnReqInstrument.getMicrCode());
				recieptReqDetailsResponseDto.setProcLoc(agrTrnReqInstrument.getProcLoc());
				recieptReqDetailsResponseDto.setTdsAmount(agrTrnReqInstrument.getTdsAmount());
				recieptReqDetailsResponseDto.setUPIVPA(agrTrnReqInstrument.getUpiVpa());
				recieptReqDetailsResponseDto.setUTRNo(agrTrnReqInstrument.getUtrNo());

				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					recieptReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + (customerBasicInfoDto.getMiddleName() != null ? customerBasicInfoDto.getMiddleName() : "") + " " + (customerBasicInfoDto.getLastName() != null ? customerBasicInfoDto.getLastName() : ""));
				}
			}
			if (agrTrnReqInstrument != null) {
				recieptReqDetailsResponseDto.setCollectedBy(agrTrnReqInstrument.getCollectedBy());
				recieptReqDetailsResponseDto.setCollectionAgency(agrTrnReqInstrument.getCollectionAgency());
				recieptReqDetailsResponseDto.setDepositRefNo(agrTrnReqInstrument.getDepositRefNo());
				recieptReqDetailsResponseDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
				recieptReqDetailsResponseDto.setInstrumentDate(agrTrnReqInstrument.getDtInstrumentDate().toString());
				recieptReqDetailsResponseDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());
				recieptReqDetailsResponseDto.setPaymentMode(agrTrnReqInstrument.getPayMode());
				recieptReqDetailsResponseDto.setPaymentType(agrTrnReqInstrument.getPayType());
				recieptReqDetailsResponseDto.setProvisionalReceiptFlag(agrTrnReqInstrument.getProvisionalReceipt());
				recieptReqDetailsResponseDto.setInstrumentLocation(agrTrnReqInstrument.getInstrumentLocation());

				List<AgrTrnReqInstrumentAllocDtl> instrumentAllocList = agrTrnReqInstrument.getInstrumentAlloc();
				if (instrumentAllocList != null && !instrumentAllocList.isEmpty()) {
					List<DreAllocationDto> dreAllocationList = new ArrayList<DreAllocationDto>();
					for (AgrTrnReqInstrumentAllocDtl instrumentAlloc : instrumentAllocList) {

						DreAllocationDto dreAllocationDto = new DreAllocationDto();
						dreAllocationDto.setLoanId(instrumentAlloc.getLoanId());
						dreAllocationDto.setAllocatedAmount(instrumentAlloc.getAmout());
						dreAllocationDto.setTranCategory(instrumentAlloc.getTranCategory());
						dreAllocationDto.setTranHead(instrumentAlloc.getTranHead());

						dreAllocationList.add(dreAllocationDto);
					}
					recieptReqDetailsResponseDto.setDreAllocation(dreAllocationList);
				}
			}

			return recieptReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getRecieptReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get Refund Request Details
	public RefundReqDetailsResponseDto getRefundReqDetails(String customerId, String mastAgrId, String reqId)
			throws Exception {
		RefundReqDetailsResponseDto refundReqDetailsResponseDto = new RefundReqDetailsResponseDto();
		try {
			log.info("IN getRefundReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			AgrTrnReqRefundDtl agrTrnReqRefundDtl = agrTrnReqRefundDtlRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnReqPayoutInstrument agrTrnReqPayoutInstrument = agrTrnReqPayoutInstrumentRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			if (agrTrnRequestHdr != null) {

				refundReqDetailsResponseDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				refundReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				refundReqDetailsResponseDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				refundReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				refundReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				refundReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					refundReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}
			}

			if (agrTrnReqRefundDtl != null) {
				refundReqDetailsResponseDto.setRefundAmount(agrTrnReqRefundDtl.getRefundAmount());
				refundReqDetailsResponseDto.setRefundReason(agrTrnReqRefundDtl.getReasonCode());
				refundReqDetailsResponseDto.setRefundRemark(agrTrnReqRefundDtl.getRemark());
			}

			if (agrTrnRequestStatus != null) {
				refundReqDetailsResponseDto.setRemark(agrTrnRequestStatus.getRemark());
				refundReqDetailsResponseDto.setRequestStatus(agrTrnRequestStatus.getReqStatus());
				refundReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}

			if (agrTrnReqPayoutInstrument != null) {
				refundReqDetailsResponseDto.setAccountType(agrTrnReqPayoutInstrument.getAccountType());
				refundReqDetailsResponseDto.setBankAccountNo(agrTrnReqPayoutInstrument.getAccountNo());
				refundReqDetailsResponseDto.setBankCode(agrTrnReqPayoutInstrument.getBankCode());
				refundReqDetailsResponseDto.setBranchCode(agrTrnReqPayoutInstrument.getBankBranchCode());
				refundReqDetailsResponseDto.setDtInstrument(agrTrnReqPayoutInstrument.getDtInstrumentDate().toString());
				refundReqDetailsResponseDto.setIfscCode(agrTrnReqPayoutInstrument.getIfscCode());
				refundReqDetailsResponseDto.setInstrumentAmount(agrTrnReqPayoutInstrument.getInstrumentAmount());
				refundReqDetailsResponseDto.setInstrumentNo(agrTrnReqPayoutInstrument.getInstrumentNo());
				refundReqDetailsResponseDto.setInstrumentType(agrTrnReqPayoutInstrument.getInstrumentType());
				refundReqDetailsResponseDto.setPaymentMode(agrTrnReqPayoutInstrument.getPayMode());
				refundReqDetailsResponseDto.setPaymentType(agrTrnReqPayoutInstrument.getPayType());
				refundReqDetailsResponseDto.setUtrNo(agrTrnReqPayoutInstrument.getUtrNo());
				refundReqDetailsResponseDto.setInstrumentLocation(agrTrnReqPayoutInstrument.getInstrumentLocation());	
			}

			return refundReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getRefundReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get Debit Note Request Details
	public DebitNoteReqDetailsResponseDto getDebitNoteReqDetails(String customerId, String mastAgrId, String reqId)
			throws Exception {
		DebitNoteReqDetailsResponseDto debitNoteReqDetailsResponseDto = new DebitNoteReqDetailsResponseDto();
		try {
			log.info("IN getDebitNoteReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			log.info("done agrTrnRequestHdr");
			AgrTrnReqDebitNoteDtl agrTrnReqDebitNoteDtl = agrTrnReqDebitNoteDtlRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(Integer.parseInt(reqId));
			log.info("done agrTrnReqDebitNoteDtl");

			if (agrTrnRequestHdr != null) {
				debitNoteReqDetailsResponseDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				debitNoteReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				debitNoteReqDetailsResponseDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				debitNoteReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				debitNoteReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				debitNoteReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				debitNoteReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				debitNoteReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());

				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					debitNoteReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}
			}

			if (agrTrnReqDebitNoteDtl != null) {
				debitNoteReqDetailsResponseDto.setDrNoteReason(agrTrnReqDebitNoteDtl.getReasonCode());
				debitNoteReqDetailsResponseDto.setDrNoteRemark(agrTrnReqDebitNoteDtl.getRemark());
				debitNoteReqDetailsResponseDto.setLoanId(agrTrnReqDebitNoteDtl.getLoanId());
				debitNoteReqDetailsResponseDto.setInstallmentNo(agrTrnReqDebitNoteDtl.getInstallmentNo());
				debitNoteReqDetailsResponseDto.setTranHead(agrTrnReqDebitNoteDtl.getDebitNoteHead());
				debitNoteReqDetailsResponseDto.setDrNoteAmount(agrTrnReqDebitNoteDtl.getDebitNoteAmount());
			}
			
			if (agrTrnRequestStatus != null) {
				debitNoteReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}
			
			return debitNoteReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getDebitNoteReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get Credit Note Request Details
	public CreditNoteReqDetailsResponseDto getCreditNoteReqDetails(String customerId, String mastAgrId, String reqId)
			throws Exception {

		CreditNoteReqDetailsResponseDto creditNoteReqDetailsResponseDto = new CreditNoteReqDetailsResponseDto();
		try {

			log.info("IN getCreditNoteReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			log.info("done agrTrnRequestHdr");
			AgrTrnReqCreditNoteDtl agrTrnReqCreditNoteDtl = agrTrnReqCreditNoteDtlRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			log.info("done agrTrnReqCreditNoteDtl");
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(Integer.parseInt(reqId));

			if (agrTrnRequestHdr != null) {
				creditNoteReqDetailsResponseDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				creditNoteReqDetailsResponseDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				creditNoteReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				creditNoteReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				creditNoteReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				creditNoteReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				creditNoteReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());

				creditNoteReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					creditNoteReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}
			}
			if (agrTrnReqCreditNoteDtl != null) {
				creditNoteReqDetailsResponseDto.setLoanId(agrTrnReqCreditNoteDtl.getLoanId());
				creditNoteReqDetailsResponseDto.setTranHead(agrTrnReqCreditNoteDtl.getCreditNoteHead());
				creditNoteReqDetailsResponseDto.setCrNoteAmount(agrTrnReqCreditNoteDtl.getCreditNoteAmount());
				creditNoteReqDetailsResponseDto.setCrNoteReason(agrTrnReqCreditNoteDtl.getReasonCode());
				creditNoteReqDetailsResponseDto.setCrNoteRemark(agrTrnReqCreditNoteDtl.getRemark());
				creditNoteReqDetailsResponseDto.setInstallmentNo(agrTrnReqCreditNoteDtl.getInstallmentNo());
			}
			
			if (agrTrnRequestStatus != null) {
				creditNoteReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}

			return creditNoteReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getCreditNoteReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get Charges Request Booking Request Details
	public ChargesReqBookingReqDetailsResponseDto getChargesReqBookingReqDetails(String customerId, String mastAgrId,
			String reqId) throws Exception {

		ChargesReqBookingReqDetailsResponseDto chargesReqBookingReqDetailsResponseDto = new ChargesReqBookingReqDetailsResponseDto();
		try {

			log.info("IN getChargesReqBookingReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			AgrTrnReqChargesBookingDtl agrTrnReqChargesBookingDtl = agrTrnReqChargesBookingDtlRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(Integer.parseInt(reqId));

			if (agrTrnRequestHdr != null) {
				chargesReqBookingReqDetailsResponseDto
						.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				chargesReqBookingReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				chargesReqBookingReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				chargesReqBookingReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				chargesReqBookingReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				chargesReqBookingReqDetailsResponseDto
						.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					chargesReqBookingReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}

			}
			if (agrTrnReqChargesBookingDtl != null) {
				chargesReqBookingReqDetailsResponseDto.setLoanId(agrTrnReqChargesBookingDtl.getLoanId());
				chargesReqBookingReqDetailsResponseDto.setTranHead(agrTrnReqChargesBookingDtl.getTranHead());
				chargesReqBookingReqDetailsResponseDto.setChargeAmount(agrTrnReqChargesBookingDtl.getChargeAmount());
				chargesReqBookingReqDetailsResponseDto.setChargeBookReason(agrTrnReqChargesBookingDtl.getReason());
				chargesReqBookingReqDetailsResponseDto.setChargeBookRemark(agrTrnReqChargesBookingDtl.getRemark());
				chargesReqBookingReqDetailsResponseDto.setInstallmentNo(agrTrnReqChargesBookingDtl.getInstallmentNo());
				chargesReqBookingReqDetailsResponseDto.setTotalAmount(agrTrnReqChargesBookingDtl.getTotalAmount());
			}
			if (agrTrnRequestStatus != null) {
				chargesReqBookingReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			
			}
			
			return chargesReqBookingReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getChargesReqBookingReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get Charges Waver Request Details
	public ChargesWaverReqDetailsResponseDto getChargesWaverReqDetails(String customerId, String mastAgrId,
			String reqId) throws Exception {

		ChargesWaverReqDetailsResponseDto chargesWaverReqDetailsResponseDto = new ChargesWaverReqDetailsResponseDto();
		try {

			log.info("IN getChargesWaverReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			List<AgrTrnReqChargesWaiverDtl> agrTrnReqChargesBookingDtl = agrTrnReqChargesWaiverDtlRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));

			if (agrTrnRequestHdr != null) {
				chargesWaverReqDetailsResponseDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				chargesWaverReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				chargesWaverReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				chargesWaverReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				chargesWaverReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				chargesWaverReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				chargesWaverReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				chargesWaverReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				
				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					chargesWaverReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}

			}
			if (agrTrnReqChargesBookingDtl != null && !agrTrnReqChargesBookingDtl.isEmpty()) {
				List<ChargesWaiverParamListResponseDto> waiverList = new ArrayList<ChargesWaiverParamListResponseDto>();

				for (AgrTrnReqChargesWaiverDtl objj : agrTrnReqChargesBookingDtl) {

					ChargesWaiverParamListResponseDto chargesWaiverParamListResponseDto = new ChargesWaiverParamListResponseDto();
					chargesWaiverParamListResponseDto.setLoanId(objj.getLoanId());
					chargesWaiverParamListResponseDto.setChargeTranId(objj.getChargeBookTranId());
					chargesWaiverParamListResponseDto.setTranHead(objj.getTranHead());
					chargesWaiverParamListResponseDto.setWaiverAmount(objj.getChargeWaiveAmount());
					chargesWaiverParamListResponseDto.setWaiverReason(objj.getReasonCode());
					chargesWaiverParamListResponseDto.setWaiverRemark(objj.getRemark());
					chargesWaiverParamListResponseDto.setInstallmentNo(objj.getInstallmentNo());
					chargesWaiverParamListResponseDto.setReqDate(objj.getDtLastUpdated().toString());
					waiverList.add(chargesWaiverParamListResponseDto);

				}
				
				AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(Integer.parseInt(reqId));
				
				if (agrTrnRequestStatus != null) {
					chargesWaverReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
				
				}
				

				chargesWaverReqDetailsResponseDto.setWaiverParamList(waiverList);
			}

			return chargesWaverReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getChargesWaverReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get ForClosure Request Details
	public ForClosureOnlineReqDetailsResponseDto getForClosureOnlineReqDetails(String customerId, String mastAgrId,
			String reqId) throws Exception {

		ForClosureOnlineReqDetailsResponseDto forClosureOnlineReqDetailsResponseDto = new ForClosureOnlineReqDetailsResponseDto();
		try {

			log.info("IN getForClosureOnlineReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			AgrTrnReqInstrument agrTrnReqInstrument = agrTrnReqInstrumentRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnReqInstrumentAllocDtl agrTrnReqInstrumentAllocDtl = agrTrnReqInstrumentAllocDtlRepository
					.findByInstrumentRequestHdrReqId(Integer.parseInt(reqId));
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository
					.findByRequestHdrReqId(Integer.parseInt(reqId));

			if (agrTrnRequestHdr != null) {
				forClosureOnlineReqDetailsResponseDto.setReqId(agrTrnRequestHdr.getReqId());
				forClosureOnlineReqDetailsResponseDto
						.setMasterAgreementId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
				forClosureOnlineReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				forClosureOnlineReqDetailsResponseDto.setFlowType(agrTrnRequestHdr.getFlowType());
				forClosureOnlineReqDetailsResponseDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
				forClosureOnlineReqDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				forClosureOnlineReqDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				forClosureOnlineReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				forClosureOnlineReqDetailsResponseDto
						.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				CustomerBasicInfoDto customerBasicInfoDto = customerServices.getCustomerByCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				if(customerBasicInfoDto != null){
					forClosureOnlineReqDetailsResponseDto.setCustomerName(customerBasicInfoDto.getFirstName() + " " + customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
				}

			}
			if (agrTrnReqInstrument != null) {
				forClosureOnlineReqDetailsResponseDto.setCollectionAgency(agrTrnReqInstrument.getCollectionAgency());
				forClosureOnlineReqDetailsResponseDto.setCollectionAgent(agrTrnReqInstrument.getCollectedBy());
				forClosureOnlineReqDetailsResponseDto
						.setInstrumentDate(agrTrnReqInstrument.getDtInstrumentDate().toString());
				forClosureOnlineReqDetailsResponseDto.setReceiptDate(agrTrnReqInstrument.getDtReceipt().toString());
				forClosureOnlineReqDetailsResponseDto.setPaymentType(agrTrnReqInstrument.getPayType());
				forClosureOnlineReqDetailsResponseDto.setPaymentMode(agrTrnReqInstrument.getPayMode());
				forClosureOnlineReqDetailsResponseDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());
				forClosureOnlineReqDetailsResponseDto.setIfscCode(agrTrnReqInstrument.getIfscCode());
				forClosureOnlineReqDetailsResponseDto.setBankCode(agrTrnReqInstrument.getBankCode());
				forClosureOnlineReqDetailsResponseDto.setBankBranchCode(agrTrnReqInstrument.getBankBranchCode());
				forClosureOnlineReqDetailsResponseDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
				forClosureOnlineReqDetailsResponseDto.setUtrNo(agrTrnReqInstrument.getUtrNo());
				forClosureOnlineReqDetailsResponseDto.setAccountNo(agrTrnReqInstrument.getAccountNo());
				forClosureOnlineReqDetailsResponseDto.setAccountType(agrTrnReqInstrument.getAccountType());
				forClosureOnlineReqDetailsResponseDto.setInstrumentLocation(agrTrnReqInstrument.getInstrumentLocation());
				forClosureOnlineReqDetailsResponseDto.setInstrumentNo(agrTrnReqInstrument.getInstrumentNo());
			}
			if (agrTrnReqInstrumentAllocDtl != null) {
				forClosureOnlineReqDetailsResponseDto.setLoanId(agrTrnReqInstrumentAllocDtl.getLoanId().toString());
			}
			if (agrTrnRequestStatus != null) {
				forClosureOnlineReqDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}

			return forClosureOnlineReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getForClosureOnlineReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get DrawDown Request Details
	public DrawDownReqDetailsResponseDto getDrawDownReqDetails(String customerId, String mastAgrId, String reqId)
			throws Exception {

		DrawDownReqDetailsResponseDto drawDownReqDetailsResponseDto = new DrawDownReqDetailsResponseDto();
		try {

			log.info("IN getDrawDownReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));

			if (agrTrnRequestHdr != null) {
				drawDownReqDetailsResponseDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				drawDownReqDetailsResponseDto.setUserId(agrTrnRequestHdr.getUserId());
				drawDownReqDetailsResponseDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
				DrawdownRequest drawdownRequest = downRequestRepository.findByRequestId(agrTrnRequestHdr.getReqId());
				if (drawdownRequest != null) {
					drawDownReqDetailsResponseDto.setMasterAggrId(drawdownRequest.getMastAgrId());
					drawDownReqDetailsResponseDto.setLimitSanAmount(drawdownRequest.getLimitSanctionAmount());
					drawDownReqDetailsResponseDto.setUtilizedLimit(drawdownRequest.getUtilizedLimit());
					drawDownReqDetailsResponseDto.setAvailableLimit(drawdownRequest.getAvailableLimit());
					drawDownReqDetailsResponseDto.setTotalDues(drawdownRequest.getTotalDues());
					drawDownReqDetailsResponseDto.setTotalOverDues(drawdownRequest.getTotalOverDues());
					drawDownReqDetailsResponseDto.setRequestedAmount(drawdownRequest.getRequestedAmount());
					drawDownReqDetailsResponseDto.setApprovedAmount(drawdownRequest.getApprovedAmount());
					drawDownReqDetailsResponseDto.setRemarksRequest(drawdownRequest.getRemarksRequest());
					drawDownReqDetailsResponseDto.setUserIdRequest(drawdownRequest.getUseridRequest());
					drawDownReqDetailsResponseDto.setEndUse(drawdownRequest.getEndUse());
				}
			}

			return drawDownReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getDrawDownReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get DrawDown Update Request Details
	public DrawDownUpdateReqDetailsResponseDto getDrawDownUpdateReqDetails(String customerId, String mastAgrId,
			String reqId) throws Exception {

		DrawDownUpdateReqDetailsResponseDto drawDownUpdateReqDetailsResponseDto = new DrawDownUpdateReqDetailsResponseDto();
		try {

			log.info("IN getDrawDownUpdateReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			if (agrTrnRequestHdr != null) {
				drawDownUpdateReqDetailsResponseDto
						.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
				DrawdownRequest drawdownRequest = downRequestRepository.findByRequestId(agrTrnRequestHdr.getReqId());
				if (drawdownRequest != null) {
					drawDownUpdateReqDetailsResponseDto.setRequestId(drawdownRequest.getRequestId());
					drawDownUpdateReqDetailsResponseDto.setMastAgrId(drawdownRequest.getMastAgrId());
					drawDownUpdateReqDetailsResponseDto.setRejectReasonCode(drawdownRequest.getRejectReasonCode());
					drawDownUpdateReqDetailsResponseDto.setRemarksApproval(drawdownRequest.getRemarksApproval());
					drawDownUpdateReqDetailsResponseDto.setUserIdDecision(drawdownRequest.getUserIdDecision());
					drawDownUpdateReqDetailsResponseDto.setStatus(drawdownRequest.getStatus());
					drawDownUpdateReqDetailsResponseDto.setRepayFreq(drawdownRequest.getRepayFreq());
					drawDownUpdateReqDetailsResponseDto.setDtPrinStart(drawdownRequest.getDtPrinStart().toString());
					drawDownUpdateReqDetailsResponseDto.setApprovedAmount(drawdownRequest.getApprovedAmount());
					drawDownUpdateReqDetailsResponseDto.setDisbifscCode(drawdownRequest.getIfsc());
					drawDownUpdateReqDetailsResponseDto.setDisbaccountNo(drawdownRequest.getDisbAccNo());
				}
			}

			return drawDownUpdateReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getDrawDownUpdateReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// Get DrawDown Disbursement Update Request Details
	public DrawDownDisbUpdateReqDetailsResponseDto getDrawDownDisbUpdateReqDetails(String customerId, String mastAgrId,
			String reqId) throws Exception {

		DrawDownDisbUpdateReqDetailsResponseDto drawDownDisbUpdateReqDetailsResponseDto = new DrawDownDisbUpdateReqDetailsResponseDto();
		try {

			log.info("IN getDrawDownDisbUpdateReqDetails SERVICE");
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(Integer.parseInt(reqId));
			if (agrTrnRequestHdr != null) {
				drawDownDisbUpdateReqDetailsResponseDto
						.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
			}
			return drawDownDisbUpdateReqDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getDrawDownDisbUpdateReqDetails SERVICE" + e.toString());
			throw e;
		}
	}

	// get part payment Details
	public PartPaymentDetailsResponseDto getPartPaymentOnlineReqDetails(Integer reqId) throws Exception {
		PartPaymentDetailsResponseDto partPaymentDetailsResponseDto = new PartPaymentDetailsResponseDto();
		try {
			log.info("IN getPartPaymentOnlineReqDetails SERVICE"); 
 
			AgrTrnReqPrepaymentHdr agrTrnReqPrepaymentHdr = agrTrnReqPrepaymentHdrRepository
					.findByRequestHdrReqId(reqId);
			if (agrTrnReqPrepaymentHdr != null) { 
				partPaymentDetailsResponseDto.setUserId(agrTrnReqPrepaymentHdr.getUserId());
				partPaymentDetailsResponseDto.setUnbilledPrincipal(agrTrnReqPrepaymentHdr.getUnbilledPrincipal());
				partPaymentDetailsResponseDto.setTranType(agrTrnReqPrepaymentHdr.getTranType());
				partPaymentDetailsResponseDto.setMasterAgreementId(agrTrnReqPrepaymentHdr.getMastAgrId());
				partPaymentDetailsResponseDto.setLoanID(agrTrnReqPrepaymentHdr.getLoanId());
				partPaymentDetailsResponseDto.setTranDate(new SimpleDateFormat("dd-MM-yyyy").format(agrTrnReqPrepaymentHdr.getDtTranDate()));
//				partPaymentDetailsResponseDto.setUserDate(new SimpleDateFormat("dd-MM-yyyy").format(agrTrnReqPrepaymentHdr.getDtUserDate()));
//				partPaymentDetailsResponseDto.setUserDate((agrTrnReqPrepaymentHdr.getDtUserDate());
				// Added new response fields for edit
				partPaymentDetailsResponseDto.setDiscountAmount(agrTrnReqPrepaymentHdr.getDiscountAmount());
				partPaymentDetailsResponseDto.setChangeFactor(agrTrnReqPrepaymentHdr.getChangeFactor());
			}
			AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqId(reqId);
			if(agrTrnRequestHdr != null){
				partPaymentDetailsResponseDto.setRemark(agrTrnRequestHdr.getRemark());
				partPaymentDetailsResponseDto.setReason(agrTrnRequestHdr.getReason());
				partPaymentDetailsResponseDto.setReqID(agrTrnRequestHdr.getReqId());
				partPaymentDetailsResponseDto.setReqStatus(agrTrnRequestHdr.getReqStatus());
			}

			AgrTrnReqInstrument agrTrnReqInstrument = agrTrnReqInstrumentRepository.findByRequestHdrReqId(reqId);
			if(agrTrnReqInstrument != null){
				partPaymentDetailsResponseDto.setInstrumentAmount(agrTrnReqInstrument.getInstrumentAmount());
				partPaymentDetailsResponseDto.setUTRN(agrTrnReqInstrument.getUtrNo());
				partPaymentDetailsResponseDto.setRefNo(agrTrnReqInstrument.getDepositRefNo());
				partPaymentDetailsResponseDto.setPaymode(agrTrnReqInstrument.getPayMode());
				partPaymentDetailsResponseDto.setPayType(agrTrnReqInstrument.getPayType());
				// Added new response fields for edit
				partPaymentDetailsResponseDto.setInstrumentType(agrTrnReqInstrument.getInstrumentType());
				partPaymentDetailsResponseDto.setReceiptDate(new SimpleDateFormat("dd-MM-yyyy").format(agrTrnReqInstrument.getDtReceipt()));
				partPaymentDetailsResponseDto.setAccountType(agrTrnReqInstrument.getAccountType());
				partPaymentDetailsResponseDto.setAccountNo(agrTrnReqInstrument.getAccountNo());
				partPaymentDetailsResponseDto.setInstrumentLocation(agrTrnReqInstrument.getInstrumentLocation());
				partPaymentDetailsResponseDto.setIfscCode(agrTrnReqInstrument.getIfscCode());
				partPaymentDetailsResponseDto.setBankCode(agrTrnReqInstrument.getBankCode());
				partPaymentDetailsResponseDto.setBranchCode(agrTrnReqInstrument.getBankBranchCode());
				partPaymentDetailsResponseDto.setInstrumentNo(agrTrnReqInstrument.getInstrumentNo());
			} 
			
			AgrTrnRequestStatus agrTrnRequestStatus = agrTrnRequestStatusRepository.findByRequestHdrReqId(reqId);
			if (agrTrnRequestStatus != null) {
				partPaymentDetailsResponseDto.setAllocatedUserId(agrTrnRequestStatus.getUserId());
			}
			

			return partPaymentDetailsResponseDto;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ERROR getPartPaymetOnlineReqDetails SERVICE" + e.toString());
			throw e;
		}
	}
	
	
	public RequestedStatusCountDto getAllTransactionsByActivityCode(String activityCode) throws Exception {

		RequestedStatusCountDto allSRResDto = new RequestedStatusCountDto();
		Integer allSRCount = 0;
		Integer pendingTransactionCount = 0;
		Integer approvedTransactionCount = 0;
		Integer rejectedTransactionCount = 0;
		Integer verifiedTransactionCount = 0;
		try {
			if (activityCode == null) {
				throw new SRNotFoundException("Required Type.");
			}

			allSRCount = agrTrnRequestHdrRepository.getSrCountByActivityCode(activityCode);
			allSRResDto.setAllSRCount(allSRCount);

			pendingTransactionCount = agrTrnRequestHdrRepository.getTransactionsCountByActivityCodeANdStatus(activityCode, "PENDING");
			allSRResDto.setPendingSRCount(pendingTransactionCount);
			
			approvedTransactionCount = agrTrnRequestHdrRepository.getTransactionsCountByActivityCodeANdStatus(activityCode, "APPROVED");
			allSRResDto.setApprovedSRCount(approvedTransactionCount);
			
			rejectedTransactionCount = agrTrnRequestHdrRepository.getTransactionsCountByActivityCodeANdStatus(activityCode, "REJECTED");
			allSRResDto.setRejectedSRCount(rejectedTransactionCount);
			
			
			verifiedTransactionCount = agrTrnRequestHdrRepository.getTransactionsCountByActivityCodeANdStatus(activityCode, "VERIFIED");
			allSRResDto.setVerifiedSRCount(verifiedTransactionCount);
			

//			List<SrWorklistByActivityCodeResponseDto> allSRByActivityCode = agrTrnRequestHdrRepository
//					.getAllSRByActivityCode(activityCode);
			allSRResDto.setType("ALL");
			allSRResDto.setAllSRCount(allSRCount);
//			allSRResDto.setServiceRequests(allSRByActivityCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return allSRResDto;
	}
	
	
	// ----- Method to get Debit note requests by status, date and reqId
		public Page<DebitNoteReqDetailsResponseDto> getDebitNoteReqList(List<String> statusList, String activityCode,
				Integer reqNo, String date, Pageable pageable) throws Exception {

			List<DebitNoteReqDetailsResponseDto> debitNoteReqDtoList = new ArrayList<>();
			try {

				List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

				if (!statusList.isEmpty() || reqNo == 0 && date == null) {
					
					agrTrnRequestHdrList = agrTrnRequestHdrRepository.getRequestsByStatusesAndActivityCode(statusList, activityCode);

				} else if (reqNo != 0 || statusList == null || date == null) {
					
					AgrTrnRequestHdr agrTrnRequestHdr = agrTrnRequestHdrRepository.findByReqIdAndActivityCode(reqNo, activityCode);
					if (agrTrnRequestHdr == null) {
						throw new CoreBadRequestException("Record not found for reqNo: " + reqNo);
					}
					agrTrnRequestHdrList.add(agrTrnRequestHdr);

				} else if (date != null || statusList == null || reqNo == null) {
					// change string date into Date format
					String dateFormat = env.getProperty("lms.global.date.format");
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					Date requestedDate = sdf.parse(date);
					agrTrnRequestHdrList = agrTrnRequestHdrRepository.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
				
				}

				if (!agrTrnRequestHdrList.isEmpty() && agrTrnRequestHdrList != null) {

					for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
						DebitNoteReqDetailsResponseDto debitNoteReqDto = new DebitNoteReqDetailsResponseDto();

						debitNoteReqDto.setReqId(agrTrnRequestHdr.getReqId());
						debitNoteReqDto.setDtRequestDate(agrTrnRequestHdr.getDtRequest().toString());
						debitNoteReqDto.setFlowType(agrTrnRequestHdr.getFlowType());
						debitNoteReqDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
						debitNoteReqDto.setReason(agrTrnRequestHdr.getReason());
						debitNoteReqDto.setRemark(agrTrnRequestHdr.getRemark());
						debitNoteReqDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
						debitNoteReqDto.setUserId(agrTrnRequestHdr.getUserId());
						debitNoteReqDto.setCustomerId(agrTrnRequestHdr.getMasterAgreement().getCustomerId());
						
						AgrTrnReqDebitNoteDtl agrTrnReqDebitNoteDtl = agrTrnReqDebitNoteDtlRepository
								.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());

						if (agrTrnReqDebitNoteDtl != null) {
							debitNoteReqDto.setDrNoteReason(agrTrnReqDebitNoteDtl.getReasonCode());
							debitNoteReqDto.setDrNoteRemark(agrTrnReqDebitNoteDtl.getRemark());
							debitNoteReqDto.setLoanId(agrTrnReqDebitNoteDtl.getLoanId());
							debitNoteReqDto.setInstallmentNo(agrTrnReqDebitNoteDtl.getInstallmentNo());
							debitNoteReqDto.setTranHead(agrTrnReqDebitNoteDtl.getDebitNoteHead());
							debitNoteReqDto.setDrNoteAmount(agrTrnReqDebitNoteDtl.getDebitNoteAmount());
						}
						
						AgrTrnRequestStatus agrTrnReqStatus = agrTrnRequestStatusRepository
								.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
						if (agrTrnReqStatus != null) {
							debitNoteReqDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
						
							}
						
						debitNoteReqDtoList.add(debitNoteReqDto);
					}
				}

				// Return a Page object for DTOs using convertToPage
				return pageableUtils.convertToPage(debitNoteReqDtoList, pageable);

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	
}
