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
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleHistRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnEventDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoff;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoffDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqWriteoffDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqWriteoffRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.RecallGetDto;
import com.samsoft.lms.transaction.dto.WriteoffGetDto;
import com.samsoft.lms.transaction.dto.WriteoffMainGetDto;
import com.samsoft.lms.transaction.entities.AgrTrnWriteoff;
import com.samsoft.lms.transaction.entities.AgrTrnWriteoffDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnWriteoffDtlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WriteoffApplicationService {

	@Autowired
	private AgrTrnRequestHdrRepository hdrReqRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnReqWriteoffRepository reqWriteoffRepo;

	@Autowired
	private AgrTrnReqWriteoffDtlRepository reqWriteoffDtlRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrTrnWriteoffDtlRepository writeoffDtlRepo;

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
	
	@Autowired
	private AgrTrnEventDtlRepository eventRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String writeoffApplication(String mastAgrId, Integer reqId, Date tranDate) throws Exception {
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

				AgrTrnReqWriteoff reqWriteoff = reqWriteoffRepo.findByMasterAgrIdAndRequestHdrReqId(mastAgrId, reqId);

				AgrTrnEventDtl event = new AgrTrnEventDtl();
				event.setTranAmount(reqWriteoff.getWriteoffAmount());
				event.setTranEvent("WRITEOFF_RECEIVABLES");
				event.setTranHeader(hdr);
				event.setUserId(reqWriteoff.getUserId());
				
				eventRepo.save(event);

				AgrTrnWriteoff writeoff = new AgrTrnWriteoff();

				writeoff.setMasterAgrId(mastAgrId);
				writeoff.setHdr(hdr);
				writeoff.setLoanId(reqWriteoff.getLoanId());
				writeoff.setDtTranDate(tranDate);
				writeoff.setRemark(reqWriteoff.getRemark());
				writeoff.setReasonCode(reqWriteoff.getReasonCode());
				writeoff.setWriteoffAmount(reqWriteoff.getWriteoffAmount());
				writeoff.setSystemorManual("M");
				writeoff.setUserId(reqWriteoff.getUserId());

				List<AgrTrnReqWriteoffDtl> reqWriteoffDtlList = reqWriteoffDtlRepo
						.findByWriteoffIdWriteoffId(reqWriteoff.getWriteoffId());
				for (AgrTrnReqWriteoffDtl reqWriteoffDtl : reqWriteoffDtlList) {
					AgrTrnWriteoffDtl writeoffDtl = new AgrTrnWriteoffDtl();

					writeoffDtl.setWriteoffId(writeoff);
					writeoffDtl.setLoanId(reqWriteoffDtl.getLoanId());
					writeoffDtl.setTranCategory(reqWriteoffDtl.getTranCategory());
					writeoffDtl.setTranHead(reqWriteoffDtl.getTranHead());
					writeoffDtl.setAmount(reqWriteoffDtl.getAmount());
					writeoffDtl.setTaxAmount(reqWriteoffDtl.getTaxAmount());
					writeoffDtl.setTotalAmount(reqWriteoffDtl.getTotalAmount());
					writeoffDtl.setUserId(reqWriteoffDtl.getUserId());

					writeoffDtlRepo.save(writeoffDtl);

				}

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
				loanAmountTranDtl.setDtlRemark("Principal due for writeoff");

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
				double bpiAmount = commonService.getInterestAccruedTillDate(mastAgrId);
				bpiAmountTranDtl.setTranAmount(loanAmount);
				bpiAmountTranDtl.setTranSide("DR");
				bpiAmountTranDtl.setDtlRemark("BPI due for writeoff");

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
				feeAmountTranDtl.setDtlRemark("Penal due for writeoff");

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

				log.info("Before Event Writeoff ");

				AgrTrnEventDtl eventWriteoff = new AgrTrnEventDtl();
				eventWriteoff.setTranAmount(loanAmount + bpiAmount + feeAmount);
				eventWriteoff.setTranEvent("WRITEOFF");
				eventWriteoff.setTranHeader(hdr);
				eventWriteoff.setUserId(reqWriteoff.getUserId());
				eventRepo.save(eventWriteoff);


				List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdOrderByDtDueDate(mastAgrId);
				for (AgrTrnDueDetails due : dueList) {

					AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
					tranDtl.setEventDtl(eventWriteoff);
					tranDtl.setMasterAgr(masterObj);
					tranDtl.setTranCategory(due.getDueCategory());
					tranDtl.setTranHead(due.getDueHead());
					tranDtl.setTranAmount(due.getDueAmount() * -1);
					tranDtl.setTranSide("CR");
					tranDtl.setDtlRemark(due.getDueHead() + " writeoff");

					due.setDueAmount(0d);

					dueRepo.save(due);
					tranDtlRepo.save(tranDtl);
				}

				dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);

				masterObj.setAgreementStatus("C");
				loan.setLoanAdditionalStatus("WRITEOFF");

				masterRepo.save(masterObj);
				loanRepo.save(loan);

				log.info("Before Instrument posting");
				
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

				disbService.postEcsInstruments(mastAgrId);

				hdrRepo.save(hdr);

			}

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public WriteoffMainGetDto getWriteoffTotalReceivables(String mastAgrId) throws Exception {

		WriteoffMainGetDto result = new WriteoffMainGetDto();
		List<WriteoffGetDto> writeoffList = new ArrayList<>();

		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			List<AgrLoans> loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);

			for (AgrLoans loan : loans) {
				List<AgrTrnDueDetails> dueList = dueRepo.findByLoanId(loan.getLoanId());
				for (AgrTrnDueDetails due : dueList) {

					WriteoffGetDto writefoff = new WriteoffGetDto();
					writefoff.setDueAmount(due.getDueAmount());
					writefoff.setDueCategory(due.getDueCategory());
					writefoff.setDueHead(due.getDueHead());
					writefoff.setLoanId(due.getLoanId());
					List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailLoanId(loan.getLoanId());
					/*
					 * Double sum = taxList.stream() .mapToDouble(x -> x.getDueTaxAmount())
					 * .reduce(0, Double::sum);
					 */
					double taxSum = 0d;
					for (AgrTrnTaxDueDetails tax : taxList) {
						taxSum += tax.getDueTaxAmount();
					}
					writefoff.setTaxAmount(taxSum);
					writefoff.setTotalAmount(taxSum + due.getDueAmount());
					writeoffList.add(writefoff);
				}

				result.setTotalRows(loans.size());

			}

			result.setWriteoffList(writeoffList);

			return result;

		} catch (Exception e) {
			throw e;
		}
	}
}
