package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrRepayScheduleHist;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleHistRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.request.entities.AgrTrnReqSettlement;
import com.samsoft.lms.request.entities.AgrTrnReqSettlementDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.SettlementGetDto;
import com.samsoft.lms.transaction.dto.SettlementMainGetDto;
import com.samsoft.lms.transaction.entities.AgrTrnSettlement;
import com.samsoft.lms.transaction.entities.AgrTrnSettlementDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnSettlementDtlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SettlementApplicationService {

	@Autowired
	private AgrTrnRequestHdrRepository hdrReqRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnReqSettlementRepository reqSettlementRepo;

	@Autowired
	private AgrTrnReqSettlementDtlRepository reqSettlementDtlRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrTrnSettlementDtlRepository settlementDtlRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private DisbursementService disbService;

	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;

	@Autowired
	private TrnInsInstrumentRepository instRepo;

	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String settlementApplication(String mastAgrId, Integer reqId, Date tranDate) throws Exception {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		try {
			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqHdrOp = hdrReqRepo.findById(reqId);
			if (reqHdrOp.isPresent()) {
				reqHdr = reqHdrOp.get();

				AgrTrnTranHeader hdr = new AgrTrnTranHeader();
				hdr.setMasterAgr(masterObj);
				hdr.setTranDate(tranDate);
				hdr.setTranType(reqHdr.getActivityCode());
				hdr.setRemark(reqHdr.getRemark());
				hdr.setSource("REQ");
				hdr.setReqId(Integer.toString(reqHdr.getReqId()));
				hdr.setUserID(reqHdr.getUserId());

				AgrTrnReqSettlement reqSettlement = reqSettlementRepo.findByMasterAgrIdAndRequestHdrReqId(mastAgrId,
						reqId);

				AgrTrnSettlement settlement = new AgrTrnSettlement();

				settlement.setMasterAgrId(mastAgrId);
				settlement.setHdr(hdr);
				settlement.setLoanId(reqSettlement.getLoanId());
				settlement.setDtTranDate(tranDate);
				settlement.setTotalReceivableAmount(reqSettlement.getTotalReceivableAmount());
				settlement.setSettlementAmount(reqSettlement.getSettlementAmount());
				settlement.setDeficitAmount(reqSettlement.getDeficitAmount());
				settlement.setReasonCode(reqSettlement.getReasonCode());
				settlement.setRemark(reqSettlement.getRemark());
				settlement.setSettlementMode(reqSettlement.getSettlementMode());
				settlement.setUserId(reqSettlement.getUserId());

				List<AgrTrnReqSettlementDtl> reqSettlementDtlList = reqSettlementDtlRepo
						.findBySettlementIdSettlementId(reqSettlement.getSettlementId());
				for (AgrTrnReqSettlementDtl reqSettlementDtl : reqSettlementDtlList) {
					AgrTrnSettlementDtl settlementDtl = new AgrTrnSettlementDtl();

					settlementDtl.setSettlementId(settlement);
					settlementDtl.setLoanId(reqSettlementDtl.getLoanId());
					settlementDtl.setTranCategory(reqSettlementDtl.getTranCategory());
					settlementDtl.setTranHead(reqSettlementDtl.getTranHead());
					settlementDtl.setDueAmount(reqSettlementDtl.getDueAmount());
					settlementDtl.setTaxAmount(reqSettlementDtl.getTaxAmount());
					settlementDtl.setTotalAmount(reqSettlementDtl.getTotalAmount());
					settlementDtl.setPaymentAmount(reqSettlementDtl.getPaymentAmount());
					settlementDtl.setDeficitAmount(reqSettlementDtl.getDeficitAmount());
					settlementDtl.setUserId(reqSettlementDtl.getUserId());

					settlementDtlRepo.save(settlementDtl);

				}

				AgrTrnEventDtl event = new AgrTrnEventDtl();
				// event.setTranAmount(reqSettlement.getDeficitAmount());
				event.setTranAmount(0d);
				event.setTranEvent("SETTLEMENT_RECEIVABLE");
				event.setTranHeader(hdr);
				event.setUserId(reqSettlement.getUserId());

				log.info("Before Loan Amount ");
				AgrTrnTranDetail loanAmountTranDtl = new AgrTrnTranDetail();
				loanAmountTranDtl.setEventDtl(event);
				loanAmountTranDtl.setMasterAgr(masterObj);
				loanAmountTranDtl.setLoan(loan);
				loanAmountTranDtl.setTranCategory("LOAN_AMOUNT");
				loanAmountTranDtl.setTranHead("UNBILLED_PRINCIPAL");
				double loanAmount = commonService.getMasterAgrUnbilledPrincipal(mastAgrId);
				loanAmountTranDtl.setTranAmount(loanAmount);
				loanAmountTranDtl.setTranSide("DR");
				loanAmountTranDtl.setDtlRemark("Principal Dues for Settlement");

				AgrTrnTranDetail loanAmountTranDtlSave = tranDtlRepo.save(loanAmountTranDtl);

				AgrTrnDueDetails loanAmountDue = new AgrTrnDueDetails();
				loanAmountDue.setTranDtlId(loanAmountTranDtlSave.getTranDtlId());
				loanAmountDue.setMastAgrId(mastAgrId);
				loanAmountDue.setLoanId(loan.getLoanId());
				loanAmountDue.setDtDueDate(tranDate);
				loanAmountDue.setDueCategory("LOAN_AMOUNT");
				loanAmountDue.setDueHead("UNBILLED_PRINCIPAL");
				loanAmountDue.setDueAmount(loanAmount);

				masterObj.setUnbilledPrincipal(0d);
				loan.setUnbilledPrincipal(0d);

				dueRepo.save(loanAmountDue);

				log.info("Before BPI Amount ");

				AgrTrnTranDetail bpiAmountTranDtl = new AgrTrnTranDetail();
				bpiAmountTranDtl.setEventDtl(event);
				bpiAmountTranDtl.setMasterAgr(masterObj);
				bpiAmountTranDtl.setLoan(loan);
				bpiAmountTranDtl.setTranCategory("BPI_AMOUNT");
				bpiAmountTranDtl.setTranHead("INTEREST");
				double bpiAmount = commonService.getInterestAccruedTillDateByTranType(mastAgrId,"INTEREST_ACCRUAL");
				bpiAmountTranDtl.setTranAmount(bpiAmount);
				bpiAmountTranDtl.setTranSide("DR");
				bpiAmountTranDtl.setDtlRemark("BPI Dues for Settlement");

				AgrTrnTranDetail bpiAmountTranDtlSave = tranDtlRepo.save(bpiAmountTranDtl);

				AgrTrnDueDetails bpiAmountDue = new AgrTrnDueDetails();
				bpiAmountDue.setTranDtlId(bpiAmountTranDtlSave.getTranDtlId());
				bpiAmountDue.setMastAgrId(mastAgrId);
				bpiAmountDue.setLoanId(loan.getLoanId());
				bpiAmountDue.setDtDueDate(tranDate);
				bpiAmountDue.setDueCategory("BPI_AMOUNT");
				bpiAmountDue.setDueHead("INTEREST");
				bpiAmountDue.setDueAmount(bpiAmount);

				commonService.updateInterestAccrual(mastAgrId);

				dueRepo.save(bpiAmountDue);

				log.info("Before Fee Amount ");

				AgrTrnTranDetail feeAmountTranDtl = new AgrTrnTranDetail();
				feeAmountTranDtl.setEventDtl(event);
				feeAmountTranDtl.setMasterAgr(masterObj);
				feeAmountTranDtl.setLoan(loan);
				feeAmountTranDtl.setTranCategory("FEE");
				feeAmountTranDtl.setTranHead("PENAL");
				double feeAmount = commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL");
				feeAmountTranDtl.setTranAmount(feeAmount);
				feeAmountTranDtl.setTranSide("DR");
				feeAmountTranDtl.setDtlRemark("Penal Dues for Settlement");

				AgrTrnTranDetail feeAmountTranDtlSave = tranDtlRepo.save(feeAmountTranDtl);

				AgrTrnDueDetails feeAmountDue = new AgrTrnDueDetails();
				feeAmountDue.setTranDtlId(feeAmountTranDtlSave.getTranDtlId());
				feeAmountDue.setMastAgrId(mastAgrId);
				feeAmountDue.setLoanId(loan.getLoanId());
				feeAmountDue.setDtDueDate(tranDate);
				feeAmountDue.setDueCategory("FEE");
				feeAmountDue.setDueHead("PENAL");
				feeAmountDue.setDueAmount(feeAmount);

				dueRepo.save(feeAmountDue);

				commonService.updatePenalAccrual(mastAgrId);

				// masterObj.setAgreementStatus("C");
				loan.setLoanAdditionalStatus("SETTLED");

				masterRepo.save(masterObj);
				loanRepo.save(loan);
				List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();

				List<AgrRepaySchedule> scheduleDtl = repayRepo
						.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, tranDate);

				Integer maxSeqNoSch = repayHistRepo.getMaxSeqNo(mastAgrId, tranDate);
				if (maxSeqNoSch == null) {
					maxSeqNoSch = 0;
				}
				maxSeqNoSch++;
				for (AgrRepaySchedule agrRepaySchedule : scheduleDtl) {
					AgrRepayScheduleHist repayHist = new AgrRepayScheduleHist();
					BeanUtils.copyProperties(agrRepaySchedule, repayHist);
					repayHist.setSeqNo(maxSeqNoSch);
					repayHistList.add(repayHist);
				}
				repayHistRepo.saveAll(repayHistList);
				repayRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(mastAgrId, tranDate);

				List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
				List<TrnInsInstrument> insList = instRepo.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId,
						tranDate);
				for (TrnInsInstrument inst : insList) {
					TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
					BeanUtils.copyProperties(inst, insHist);
					insHist.setSeqNo(maxSeqNoSch);
					insHistList.add(insHist);
				}

				insHistRepo.saveAll(insHistList);

				instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, tranDate);

				log.info("Before Instrument posting");

				disbService.postEcsInstruments(mastAgrId);

			}

		} catch (Exception e) {
			throw e;
		}

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public SettlementMainGetDto getSettlementTotalReceivables(String mastAgrId) throws Exception {

		SettlementMainGetDto result = new SettlementMainGetDto();
		List<SettlementGetDto> settlementList = new ArrayList<>();

		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			List<AgrLoans> loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);

			for (AgrLoans loan : loans) {
				List<AgrTrnDueDetails> dueList = dueRepo.findByLoanId(loan.getLoanId());
				for (AgrTrnDueDetails due : dueList) {

					SettlementGetDto settlement = new SettlementGetDto();
					settlement.setDueAmount(due.getDueAmount());
					settlement.setDueCategory(due.getDueCategory());
					settlement.setDueHead(due.getDueHead());
					settlement.setLoanId(due.getLoanId());
					List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailLoanId(loan.getLoanId());
					/*
					 * Double sum = taxList.stream() .mapToDouble(x -> x.getDueTaxAmount())
					 * .reduce(0, Double::sum);
					 */
					double taxSum = 0d;
					for (AgrTrnTaxDueDetails tax : taxList) {
						taxSum += tax.getDueTaxAmount();
					}
					settlement.setTaxAmount(taxSum);
					settlement.setTotalAmount(taxSum + due.getDueAmount());
					settlementList.add(settlement);
				}

				result.setTotalRows(loans.size());

			}

			result.setSettlementList(settlementList);

			return result;

		} catch (Exception e) {
			throw e;
		}
	}
}
