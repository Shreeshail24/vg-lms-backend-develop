package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleHistRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.request.entities.AgrTrnReqRecall;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqRecallRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.RecallGetDto;
import com.samsoft.lms.transaction.dto.RecallMainGetDto;
import com.samsoft.lms.transaction.entities.AgrTrnRecall;
import com.samsoft.lms.transaction.repositories.AgrTrnRecallRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecallApplicationService {

	@Autowired
	private AgrTrnRequestHdrRepository hdrReqRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnReqRecallRepository reqRecallRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private DisbursementService disbService;

	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;

	@Autowired
	private AgrTrnRecallRepository recallRepo;
	
	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;
	
	@Autowired
	private AgrRepayScheduleRepository scheduleRepo;
	
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	
	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String recallApplication(String mastAgrId, Integer reqId, Date tranDate) throws Exception {
		String result = "sucess";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();

			List<AgrTrnReqRecall> reqRecallList = reqRecallRepo.findByMasterAgrIdAndRequestHdrReqId(mastAgrId, reqId);

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

				AgrTrnReqRecall reqRecall = reqRecallRepo.findByMasterAgrIdAndRequestHdrReqId(mastAgrId, reqId).get(0);

				AgrTrnEventDtl event = new AgrTrnEventDtl();
				event.setTranAmount(0d);
				event.setTranEvent("RECALL_RECEIVABLES");
				event.setTranHeader(hdr);
				event.setUserId(reqHdr.getUserId());

				for (AgrTrnReqRecall recallList : reqRecallList) {
					AgrTrnRecall recall = new AgrTrnRecall();

					recall.setRequestHdr(hdr);
					recall.setMasterAgrId(mastAgrId);
					recall.setLoanId(recallList.getLoanId());
					recall.setDtTranDate(tranDate);
					recall.setPortfolioCode(recallList.getPortfolioCode());
					recall.setRecallStatus(recallList.getRecallStatus());
					recall.setRemark(recallList.getRemark());
					recall.setOutstandingAmount(recallList.getOutstandingAmount());
					recall.setBpiAmount(recallList.getBpiAmount());
					recall.setTotalOutstandingAmount(recallList.getTotalOutstandingAmount());
					recall.setUserId(reqRecall.getUserId());

					recallRepo.save(recall);

					AgrLoans loan = loanRepo.findByLoanId(recallList.getLoanId());

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

					commonService.updateInterestAccrual(mastAgrId);

					loan.setLoanAdditionalStatus("RECALLED");
					loanRepo.save(loan);
				}

				log.info("Before Event Recall ");

				masterRepo.save(masterObj);

				log.info("Before Instrument posting");
				
				Integer maxSeqNo = repayHistRepo.getMaxSeqNo(mastAgrId, tranDate);
				if (maxSeqNo == null) {
					maxSeqNo = 0;
				}
				maxSeqNo++;
				List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();
				List<AgrRepaySchedule> repayList = scheduleRepo
						.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, tranDate);
				for (AgrRepaySchedule agrRepaySchedule : repayList) {
					AgrRepayScheduleHist repayHist = new AgrRepayScheduleHist();
					BeanUtils.copyProperties(agrRepaySchedule, repayHist);
					repayHist.setSeqNo(maxSeqNo);
					repayHistList.add(repayHist);
				}
				repayHistRepo.saveAll(repayHistList);
				scheduleRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(mastAgrId, tranDate);

				List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
				List<TrnInsInstrument> insList = instRepo.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId,
						tranDate);
				for (TrnInsInstrument inst : insList) {
					TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
					BeanUtils.copyProperties(inst, insHist);
					insHist.setSeqNo(maxSeqNo);
					insHistList.add(insHist);
				}

				insHistRepo.saveAll(insHistList);

				instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, tranDate);
				

				disbService.postEcsInstruments(mastAgrId);

				hdrRepo.save(hdr);
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public RecallMainGetDto getRecallTotalReceivables(String mastAgrId) throws Exception {

		RecallMainGetDto result = new RecallMainGetDto();
		List<RecallGetDto> recallList = new ArrayList<>();

		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			List<AgrLoans> loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			for (AgrLoans loan : loans) {
				RecallGetDto recall = new RecallGetDto();

				recall.setBpiAmount(commonService.getInterestAccruedTillDateByTranTypeAndByLoanId(mastAgrId,
						"INTEREST_ACCRUAL", loan.getLoanId()));
				recall.setLoanId(loan.getLoanId());
				recall.setMastAgrId(mastAgrId);
				recall.setOutstandingAmount(commonService.getLoanUnbilledPrincipal(loan.getLoanId()));
				recall.setPortfolioCode(masterObj.getPortfolioCode());
				recall.setTotalOutstanding(commonService.getLoanTotalDues(loan.getLoanId())
						+ commonService.getLoanUnbilledPrincipal(loan.getLoanId()));

				recallList.add(recall);

			}

			result.setRecallList(recallList);
			result.setTotalRows(loans.size());

			return result;

		} catch (Exception e) {
			throw e;
		}

	}

}
