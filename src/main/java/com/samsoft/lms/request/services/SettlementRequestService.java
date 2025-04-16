package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.request.dto.RestructureJpaDto;
import com.samsoft.lms.request.dto.SettlementDtlDto;
import com.samsoft.lms.request.dto.SettlementPaymentScheduleListDto;
import com.samsoft.lms.request.dto.SettlementReceivableListDto;
import com.samsoft.lms.request.dto.SettlementReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqSettlement;
import com.samsoft.lms.request.entities.AgrTrnReqSettlementDtl;
import com.samsoft.lms.request.entities.AgrTrnReqSettlementPaySch;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementPaySchRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;

@Service
public class SettlementRequestService {

	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnReqSettlementDtlRepository settlementDtlRepo;

	@Autowired
	private AgrTrnReqSettlementPaySchRepository paymentScheduleRepo;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;

	@Autowired
	private AgrTrnRequestHdrRepository hdrRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String settlementRequest(SettlementReqDto settlementDto) throws Exception {
		String result = "sucess";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(settlementDto.getMastAgrId());
			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setActivityCode("SETTLEMENT");
			hdr.setDtRequest(sdf.parse(settlementDto.getDtRequest()));
			hdr.setFlowType("NF");
			hdr.setMasterAgreement(masterObj);
			hdr.setReason(settlementDto.getReason());
			hdr.setRemark(settlementDto.getRemark());
			hdr.setReqStatus(settlementDto.getReqStatus());
			hdr.setUserId(settlementDto.getUserId());

			hdrRepo.save(hdr);

			AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			status.setRequestHdr(hdr);
			status.setDtReqChangeDate(sdf.parse(settlementDto.getDtRequest()));
			status.setReqStatus("PENDING");
			status.setReason(settlementDto.getReason());
			status.setRemark(settlementDto.getRemark());

			statusRepo.save(status);

			AgrTrnReqSettlement settlement = new AgrTrnReqSettlement();

			settlement.setDeficitAmount(settlementDto.getDeficitAmount());
			settlement.setDtTranDate(sdf.parse(settlementDto.getDtRequest()));
			settlement.setExcessAmount(settlementDto.getExcessAmount());
			settlement.setLoanId(settlementDto.getLoanId());
			settlement.setMasterAgrId(settlementDto.getMastAgrId());
			settlement.setReasonCode(settlementDto.getReason());
			settlement.setRemark(settlementDto.getRemark());
			settlement.setRequestHdr(hdr);
			settlement.setSettlementAmount(settlementDto.getSettlementAmount());
			settlement.setSettlementMode(settlementDto.getSettlementMode());
			settlement.setTotalReceivableAmount(settlementDto.getReceivableAmount());
			settlement.setUserId(settlementDto.getUserId());

			for (SettlementDtlDto settlementDtlDto : settlementDto.getSettlementDtl()) {

				AgrTrnReqSettlementDtl settlementDtl = new AgrTrnReqSettlementDtl();

				settlementDtl.setDeficitAmount(settlementDtlDto.getDeficitAmount());
				settlementDtl.setDueAmount(settlementDtlDto.getDueAmount());
				settlementDtl.setLoanId(settlementDto.getLoanId());
				settlementDtl.setMasterAgrId(settlementDto.getMastAgrId());
				settlementDtl.setPaymentAmount(settlementDtlDto.getPaymentAmount());
				settlementDtl.setSettlementId(settlement);
				settlementDtl.setTaxAmount(settlementDtlDto.getTaxAmount());
				settlementDtl.setTotalAmount(settlementDtlDto.getTotalAmount());
				settlementDtl.setTranCategory(settlementDtlDto.getTranType());
				settlementDtl.setTranHead(settlementDtlDto.getTranHead());
				settlementDtl.setUserId(settlementDto.getUserId());

				settlementDtlRepo.save(settlementDtl);

			}

			for (SettlementPaymentScheduleListDto paymentScheduleList : settlementDto.getPaymentSchedule()) {
				AgrTrnReqSettlementPaySch paymentSchedule = new AgrTrnReqSettlementPaySch();
				paymentSchedule.setDtPromiseDate(sdf.parse(paymentScheduleList.getDtPromise()));
				paymentSchedule.setMasterAgrId(settlementDto.getMastAgrId());
				paymentSchedule.setPromiseAmount(paymentScheduleList.getPromiseAmount());
				paymentSchedule.setSettlementId(settlement);
				paymentSchedule.setUserId(settlementDto.getUserId());

				paymentScheduleRepo.save(paymentSchedule);
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<SettlementReceivableListDto> getSettlementReceivableList(String mastAgrId) throws Exception {
		List<SettlementReceivableListDto> result = new ArrayList<>();

		try {
			List<RestructureJpaDto> dueList = dueRepo.restructureReceivables(mastAgrId);
			AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			for (RestructureJpaDto due : dueList) {
				SettlementReceivableListDto dueDetails = new SettlementReceivableListDto();
				dueDetails.setTranType(due.getDueCategory());
				dueDetails.setTranHead(due.getDueHead());
				dueDetails.setDueAmount(commonService.numberFormatter(due.getDueAmount()));
				dueDetails.setTaxAmount(commonService.numberFormatter(due.getDueTaxAmount()));
				dueDetails.setTotalAmount(commonService.numberFormatter(
						due.getDueAmount() + (due.getDueTaxAmount() == null ? 0 : due.getDueTaxAmount())));
				dueDetails.setDeficitAmount(dueDetails.getTotalAmount());

				/*
				 * if (due.getDueCategory().equalsIgnoreCase("FEE")) { List<AgrTrnTaxDueDetails>
				 * taxList = taxRepo.findByDueDetailDueDtlId(due.getDueDtlId()); for
				 * (AgrTrnTaxDueDetails tax : taxList) { totalAmount += tax.getDueTaxAmount();
				 * totalReceivable += tax.getDueTaxAmount(); } }
				 */

				result.add(dueDetails);

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
					SettlementReceivableListDto penalAccrualDue = new SettlementReceivableListDto();
					penalAccrualDue.setTranType("FEE");
					penalAccrualDue.setTranHead("PENAL");
					penalAccrualDue.setDueAmount(penalAccrual);
					penalAccrualDue.setDeficitAmount(penalAccrual);
					penalAccrualDue.setTotalAmount(penalAccrual);
					result.add(penalAccrualDue);

				}
			}

			double interestAccrual = commonService
					.numberFormatter(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));

			if (interestAccrual > 0) {
				SettlementReceivableListDto interestAccrualDue = new SettlementReceivableListDto();
				interestAccrualDue.setTranType("BPI");
				interestAccrualDue.setTranHead("INTEREST");
				interestAccrualDue.setDueAmount(interestAccrual);
				interestAccrualDue.setDeficitAmount(interestAccrual);
				interestAccrualDue.setTotalAmount(interestAccrual);
				result.add(interestAccrualDue);

			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
}
