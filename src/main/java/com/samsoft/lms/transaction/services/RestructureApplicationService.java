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

import com.samsoft.lms.agreement.entities.AgrRepayVariation;
import com.samsoft.lms.agreement.entities.AgrRepayVariationHist;
import com.samsoft.lms.agreement.repositories.AgrRepayVariationHistRepository;
import com.samsoft.lms.agreement.repositories.AgrRepayVariationRepository;
import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.core.dto.AmortParameter;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrRepayScheduleHist;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.Amort;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleHistRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.services.ColenderDueService;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.CoreAmort;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.request.entities.AgrTrnReqRepayVariation;
import com.samsoft.lms.request.entities.AgrTrnReqRescheduleDtl;
import com.samsoft.lms.request.entities.AgrTrnReqRestructureDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqRepayVariationRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRescheduleDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRestructureDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.RestructureApplicationDto;
import com.samsoft.lms.transaction.entities.AgrTrnRescheduleDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnRescheduleDtlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestructureApplicationService {

	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrTrnReqRestructureDtlRepository restructureReqRepo;

	@Autowired
	private AgrTrnReqRescheduleDtlRepository rescheduleReqRepo;

	@Autowired
	private AgrTrnRescheduleDtlRepository rescheduleRepo;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private AgrRepayVariationRepository variationRepo;

	@Autowired
	private AgrRepayVariationHistRepository variationHistRepo;

	@Autowired
	private AgrTrnReqRepayVariationRepository variationReqRepo;

	@Autowired
	private CoreAmort coreAmort;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;

	@Autowired
	private TrnInsInstrumentRepository instRepo;

	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;

	@Autowired
	private DisbursementService disbService;

	@Autowired
	private ColenderDueService conlenderDueService;

	@Transactional
	public String restructureApplication(String mastAgrId, int reqId, String tranDate) throws Exception {
		String result = "sucess";

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String changeFactor = "";

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			Optional<AgrTrnRequestHdr> reqHdrOptObj = reqHdrRepo.findById(reqId);
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrTrnRequestHdr reqHdrObj = null;
			List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();

			if (reqHdrOptObj.isPresent()) {
				reqHdrObj = reqHdrOptObj.get();
			}

			AgrTrnTranHeader hdr = new AgrTrnTranHeader();

			hdr.setMasterAgr(masterObj);
			hdr.setTranDate(sdf.parse(tranDate));
			hdr.setTranType(reqHdrObj.getActivityCode());
			hdr.setRemark(reqHdrObj.getRemark());
			hdr.setSource("REQ");
			hdr.setReqId(reqHdrObj.getReqId().toString());
			hdr.setUserID(reqHdrObj.getUserId());

			log.info(" ReqId {} and Master Agreement {} and tranDate  " + reqHdrObj.getReqId().toString(), mastAgrId,
					tranDate);

			List<AgrRepaySchedule> scheduleDtl = repayRepo
					.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, sdf.parse(tranDate));

			int installmentNo = scheduleDtl.get(0).getInstallmentNo();

			log.info("installmentNo {}", installmentNo);

			AgrTrnReqRescheduleDtl rescheduleReqObj = rescheduleReqRepo.findByMasterAgrIdAndRequestHdrReqId(mastAgrId,
					reqHdrObj.getReqId());
			changeFactor = rescheduleReqObj.getChangeFactor();
			log.info("before saving reschedule ");

			AgrTrnRescheduleDtl reschedule = new AgrTrnRescheduleDtl();

			reschedule.setBpiAmount(rescheduleReqObj.getBpiAmount());
			reschedule.setChangeFactor(rescheduleReqObj.getChangeFactor());
			reschedule.setDreAmount(rescheduleReqObj.getDreAmount());
			reschedule.setDtNewInstStartDate(rescheduleReqObj.getDtNewInstStartDate());
			reschedule.setDtTranDate(sdf.parse(tranDate));
			reschedule.setLoanId(rescheduleReqObj.getLoanId());
			reschedule.setMasterAgrId(mastAgrId);
			reschedule.setNetReceivable(rescheduleReqObj.getNetReceivable());
			reschedule.setNewAssetClass(rescheduleReqObj.getNewAssetClass());
			reschedule.setNewCycleDay(rescheduleReqObj.getNewCycleDay());
			reschedule.setNewFinanceAmt(rescheduleReqObj.getNewFinanceAmt());
			reschedule.setNewIndexRate(rescheduleReqObj.getNewIndexRate());
			reschedule.setNewInstallmentAmount(rescheduleReqObj.getNewInstallmentAmount());
			reschedule.setNewOffsetRate(rescheduleReqObj.getNewOffsetRate());
			reschedule.setNewRepayFrequency(rescheduleReqObj.getNewRepayFrequency());
			reschedule.setNewSpreadRate(rescheduleReqObj.getNewSpreadRate());
			reschedule.setNewTenor(rescheduleReqObj.getNewTenor());
			reschedule.setNewInterestRate(rescheduleReqObj.getNewInterestRate());
			reschedule.setOldAssetClass(loan.getAssetClass());
			reschedule.setOldCycleDay(loan.getCycleDay());
			reschedule.setOldIndexRate(loan.getIndexRate() == null ? 0 : loan.getIndexRate());
			reschedule.setOldInstallmentAmount(scheduleDtl.get(0).getInstallmentAmount());
			reschedule.setOldInterestRate(loan.getInterestRate());
			reschedule.setOldOffsetRate(loan.getOffsetRate() == null ? 0 : loan.getOffsetRate());
			reschedule.setOldRepayFrequency(loan.getRepayFreq());
			reschedule.setOldSpreadRate(loan.getSpreadRate() == null ? 0 : loan.getSpreadRate());
			reschedule.setOldTenor(loan.getTenor());
			reschedule.setOldUnbilledPrincipal(loan.getUnbilledPrincipal());
			reschedule.setRestructureReason(rescheduleReqObj.getRestructureReason());
			reschedule.setRestructureRemark(rescheduleReqObj.getRestructureRemark());
			reschedule.setTranHdr(hdr);
			reschedule.setTranType(rescheduleReqObj.getTranType());
			reschedule.setUserId(rescheduleReqObj.getUserId());

			rescheduleRepo.save(reschedule);

			log.info("after saving reschedule ");

			if (rescheduleReqObj.getTranType().equals("RESTRUCTURE")) {
				AgrTrnEventDtl eventReceivable = new AgrTrnEventDtl();
				// event.setTranAmount(reqSettlement.getDeficitAmount());
				eventReceivable.setTranAmount(0d);
				eventReceivable.setTranEvent("RESTRUCTURE_RECEIVABLE");
				eventReceivable.setTranHeader(hdr);
				eventReceivable.setUserId(rescheduleReqObj.getUserId());

				log.info("Before BPI Amount ");

				AgrTrnTranDetail bpiAmountTranDtl = new AgrTrnTranDetail();
				bpiAmountTranDtl.setEventDtl(eventReceivable);
				bpiAmountTranDtl.setMasterAgr(masterObj);
				bpiAmountTranDtl.setLoan(loan);
				bpiAmountTranDtl.setTranCategory("BPI_AMOUNT");
				bpiAmountTranDtl.setTranHead("INTEREST");
				double bpiAmount = commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL");
				bpiAmountTranDtl.setTranAmount(bpiAmount);
				bpiAmountTranDtl.setTranSide("DR");
				bpiAmountTranDtl.setDtlRemark("BPI Dues for Restructure");

				AgrTrnTranDetail bpiAmountTranDtlSave = tranDtlRepo.save(bpiAmountTranDtl);

				AgrTrnDueDetails bpiAmountDue = new AgrTrnDueDetails();
				bpiAmountDue.setTranDtlId(bpiAmountTranDtlSave.getTranDtlId());
				bpiAmountDue.setMastAgrId(mastAgrId);
				bpiAmountDue.setLoanId(loan.getLoanId());
				bpiAmountDue.setDtDueDate(sdf.parse(tranDate));
				bpiAmountDue.setDueCategory("BPI_AMOUNT");
				bpiAmountDue.setDueHead("INTEREST");
				bpiAmountDue.setDueAmount(bpiAmount);

				commonService.updateInterestAccrual(mastAgrId);

				dueRepo.save(bpiAmountDue);

				log.info("Before Fee Amount ");

				AgrTrnTranDetail feeAmountTranDtl = new AgrTrnTranDetail();
				feeAmountTranDtl.setEventDtl(eventReceivable);
				feeAmountTranDtl.setMasterAgr(masterObj);
				feeAmountTranDtl.setLoan(loan);
				feeAmountTranDtl.setTranCategory("FEE");
				feeAmountTranDtl.setTranHead("PENAL");
				double feeAmount = commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL");
				feeAmountTranDtl.setTranAmount(feeAmount);
				feeAmountTranDtl.setTranSide("DR");
				feeAmountTranDtl.setDtlRemark("Penal Dues for Restructure");

				AgrTrnTranDetail feeAmountTranDtlSave = tranDtlRepo.save(feeAmountTranDtl);

				AgrTrnDueDetails feeAmountDue = new AgrTrnDueDetails();
				feeAmountDue.setTranDtlId(feeAmountTranDtlSave.getTranDtlId());
				feeAmountDue.setMastAgrId(mastAgrId);
				feeAmountDue.setLoanId(loan.getLoanId());
				feeAmountDue.setDtDueDate(sdf.parse(tranDate));
				feeAmountDue.setDueCategory("FEE");
				feeAmountDue.setDueHead("PENAL");
				feeAmountDue.setDueAmount(feeAmount);

				dueRepo.save(feeAmountDue);

				commonService.updatePenalAccrual(mastAgrId);

			}

			List<AgrTrnReqRestructureDtl> restructureReqCapList = restructureReqRepo
					.findByRequestHdrReqIdAndCapitalizeAmountGreaterThan(reqHdrObj.getReqId(), 0);

			log.info(" restructureReqCapList size {}", restructureReqCapList.size());

			double capitalizeAmount = 0;
			AgrTrnEventDtl event = new AgrTrnEventDtl();
			for (AgrTrnReqRestructureDtl rest : restructureReqCapList) {
				capitalizeAmount += rest.getCapitalizeAmount();
			}

			log.info(" capitalizeAmount ", capitalizeAmount);

			log.info(" restructureReqCapList size {} ", restructureReqCapList.size());

			masterObj.setUnbilledPrincipal(
					commonService.numberFormatter(masterObj.getUnbilledPrincipal() + capitalizeAmount));
			loan.setUnbilledPrincipal(commonService.numberFormatter(masterObj.getUnbilledPrincipal()));
			int count = 0;
			for (AgrTrnReqRestructureDtl restCap : restructureReqCapList) {
				count++;
				if (count == 1) {
					event.setTranHeader(hdr);
					event.setTranEvent("RESTRUCTURE_CAPITALIZE");
					event.setUserId(rescheduleReqObj.getUserId());
					event.setTranAmount(commonService.numberFormatter(capitalizeAmount));
				}
				AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
				tranDtl.setEventDtl(event);
				tranDtl.setMasterAgr(masterObj);
				tranDtl.setLoan(loan);
				tranDtl.setTranCategory(restCap.getTranHead());
				tranDtl.setTranHead(restCap.getTranHead());
				tranDtl.setTranSide("DR");
				tranDtl.setTranAmount(restCap.getCapitalizeAmount());
				tranDtl.setDtlRemark("Due " + restCap.getTranHead() + " Capitalized");

				log.info("before tranDtl Save");
				tranDtlRepo.save(tranDtl);
				log.info("after tranDtl Save");

				List<AgrTrnDueDetails> capDueList = dueRepo.findByMastAgrIdAndDueCategoryAndDueHead(mastAgrId,
						restCap.getTranType(), restCap.getTranHead());

				log.info("capDueList {}, and TranTye {} and TranHead {}  ", capDueList.size(), restCap.getTranType(),
						restCap.getTranHead());

				for (AgrTrnDueDetails capDue : capDueList) {

					capDue.setDueAmount(capDue.getDueAmount() - restCap.getCapitalizeAmount());

					log.info("before due Save");
					dueRepo.save(capDue);
					log.info("after due Save");

					log.info("Tax Amount " + restCap.getTaxAmount());

					if (capDue.getDueCategory().equalsIgnoreCase("FEE") && restCap.getTaxAmount() > 0) {

						List<AgrTrnTaxDueDetails> taxCapList = taxRepo.findByDueDetailDueDtlId(capDue.getDueDtlId());

						log.info(" taxCapList {} and dueId {} ", taxCapList.size(), capDue.getDueDtlId());

						double totalTaxAmount = restCap.getTaxAmount();
						for (AgrTrnTaxDueDetails tax : taxCapList) {
							totalTaxAmount = totalTaxAmount - tax.getDueTaxAmount();
							tax.setDueTaxAmount(0.0);
							taxRepo.save(tax);

						}
					}
				}
			}

			List<AgrTrnReqRestructureDtl> restructureReqWaiveList = restructureReqRepo
					.findByRequestHdrReqIdAndWaiveAmountGreaterThan(reqHdrObj.getReqId(), 0);
			log.info("restructureReqWaiveList {} " + restructureReqWaiveList.size());
			double waiveAmount = 0;
			for (AgrTrnReqRestructureDtl restWaive : restructureReqWaiveList) {
				waiveAmount += restWaive.getWaiveAmount();
			}
			log.info(" Waiver Amount {} ", waiveAmount);
			int countWaive = 0;
			AgrTrnEventDtl eventWaive = new AgrTrnEventDtl();
			for (AgrTrnReqRestructureDtl restWaive : restructureReqWaiveList) {
				countWaive++;
				if (countWaive == 1) {
					eventWaive.setTranHeader(hdr);
					eventWaive.setTranEvent("RESTRUCTURE_WAIVER");
					eventWaive.setUserId(rescheduleReqObj.getUserId());
					eventWaive.setTranAmount(commonService.numberFormatter(waiveAmount));
				}
				AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
				tranDtl.setEventDtl(eventWaive);
				tranDtl.setMasterAgr(masterObj);
				tranDtl.setLoan(loan);
				tranDtl.setTranCategory(restWaive.getTranHead());
				tranDtl.setTranHead(restWaive.getTranHead());
				tranDtl.setTranSide("CR");
				tranDtl.setTranAmount(restWaive.getWaiveAmount() * -1);
				tranDtl.setDtlRemark("Due " + restWaive.getTranHead() + " waived");

				log.info(" before tranDtl waive ");
				tranDtlRepo.save(tranDtl);
				log.info(" After tranDtl waive ");
				List<AgrTrnDueDetails> waiveDueList = dueRepo.findByMastAgrIdAndDueCategoryAndDueHead(mastAgrId,
						restWaive.getTranType(), restWaive.getTranHead());

				log.info(" waiveDueList " + waiveDueList.size());

				for (AgrTrnDueDetails waiveDue : waiveDueList) {

					waiveDue.setDueAmount(waiveDue.getDueAmount() - restWaive.getWaiveAmount());

					log.info(" before waive due ");

					dueRepo.save(waiveDue);

					log.info(" after waive due ");

					if (waiveDue.getDueCategory().equalsIgnoreCase("FEE") && restWaive.getTaxAmount() > 0) {

						List<AgrTrnTaxDueDetails> taxCapList = taxRepo.findByDueDetailDueDtlId(waiveDue.getDueDtlId());

						double totalTaxAmount = restWaive.getTaxAmount();
						for (AgrTrnTaxDueDetails tax : taxCapList) {
							totalTaxAmount = totalTaxAmount - tax.getDueTaxAmount();
							tax.setDueTaxAmount(0.0);
							taxRepo.save(tax);

						}
					}
				}
			}

			int previousInstallmentNo = repayRepo
					.findFirstByMasterAgrIdAndDtInstallmentLessThanOrderByInstallmentNoDesc(mastAgrId,
							sdf.parse(tranDate))
					.getInstallmentNo();

			Integer maxSeqNo = variationHistRepo.getMaxSeqNo(loan.getLoanId());
			log.info("maxSeqNo " + maxSeqNo);

			List<AgrRepayVariation> variationList = variationRepo
					.findByLoansLoanIdAndFromInstallmentNoGreaterThan(loan.getLoanId(), previousInstallmentNo);
			log.info(" variationList {} ", variationList.size());
			for (AgrRepayVariation variation : variationList) {

				AgrRepayVariationHist variationHist = new AgrRepayVariationHist();

				variationHist.setAdjustmentFactor(variation.getAdjustmentFactor());
				variationHist.setFromInstallmentNo(variation.getFromInstallmentNo());
				variationHist.setLoanId(variation.getLoans().getLoanId());
				variationHist.setNoOfInstallments(variation.getNoOfInstallments());
				variationHist.setSrNo(variation.getSrNo());
				variationHist.setRepayVarId(variation.getRepayVarId());
				variationHist.setSeqNo(++maxSeqNo);
				variationHist.setUserId(rescheduleReqObj.getUserId());
				variationHist.setVariationOption(variation.getVariationOption());
				variationHist.setVariationType(variation.getVariationType());
				variationHist.setVariationValue(variation.getVariationValue());
				log.info("before variationHist");
				variationHistRepo.save(variationHist);
				log.info("after variationHist");

			}

			log.info("before delete schedule");
			repayRepo.deleteByMasterAgrIdAndInstallmentNoGreaterThan(mastAgrId, previousInstallmentNo);
			log.info("after delete schedule");

			List<AgrTrnReqRepayVariation> variationReqList = variationReqRepo
					.findByMasterAgrIdAndRequestHdrReqId(mastAgrId, reqHdrObj.getReqId());
			log.info(" variationReqList " + variationReqList.size());
			for (AgrTrnReqRepayVariation variationReq : variationReqList) {
				AgrRepayVariation repayVar = new AgrRepayVariation();
				repayVar.setAdjustmentFactor(variationReq.getAdjustmentFactor());
				repayVar.setFromInstallmentNo(variationReq.getFromInstallmentNo());
				repayVar.setLoans(loan);
				repayVar.setNoOfInstallments(variationReq.getNoofInstallments());
				repayVar.setRepayVarId(variationReq.getVarId());
				repayVar.setSrNo(variationReq.getSrNo());
				repayVar.setUserId(variationReq.getUserId());
				repayVar.setVariationOption(variationReq.getVariationOption());
				repayVar.setVariationType(variationReq.getVariationType());
				repayVar.setVariationValue(variationReq.getVariationValue());

				log.info("before save repayVar ");
				variationRepo.save(repayVar);

				log.info("after save repayVar ");

			}

			log.info("before delete due");
			dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			log.info("after delete due");

			double disbAmount = masterObj.getUnbilledPrincipal();
			Date nextInstallmentDate = scheduleDtl.get(0).getDtInstallment();
			double oldEmiAmount = scheduleDtl.get(0).getInstallmentAmount();
			double emiAmount = 0;
			log.info("disbAmont {} , oldEmi {} nextInstallmentatDate {}, changeFactor {} ", disbAmount, oldEmiAmount,
					nextInstallmentDate, changeFactor);
			if (disbAmount > 0) {
				if (changeFactor.equalsIgnoreCase("INSTALLMENT")) {
					if (rescheduleReqObj.getTranType().equals("RESCHEDULE")) {
						emiAmount = rescheduleReqObj.getNewInstallmentAmount();
					} else {
						emiAmount = coreAmort.getEMI(disbAmount, loan.getInterestRate(),
								(loan.getTenor() - loan.getCurrentInstallmentNo()), loan.getTenorUnit(),
								loan.getRepayFreq(), product.getInterestBasis(), product.getAmortizationMethod());
					}

				} else {
					emiAmount = oldEmiAmount;
				}

				AmortParameter amortParam = new AmortParameter();
				amortParam.setAmortizationMethod(product.getAmortizationMethod());
				amortParam.setAmortizationType(product.getAmortizationType());
				amortParam.setBpiHandling(product.getBpiTreatmentFlag());
				amortParam.setDtDisbursement(tranDate);
				amortParam.setDtInstallmentStart(sdf.format(rescheduleReqObj.getDtNewInstStartDate()));
				amortParam.setEmiAmount(emiAmount);
				amortParam.setEmiBasis(product.getEmiBasis());
				amortParam.setEmiRounding(product.getEmiRounding());
				amortParam.setInterestBasis(product.getInterestBasis());
				amortParam.setInterestRate(rescheduleReqObj.getNewInterestRate());
				amortParam.setLastRowThresholdPercentage(product.getLastRowEMIThreshold());
				amortParam.setLoanAmount(disbAmount);
				amortParam.setNoOfAdvanceEmi(0);
				amortParam.setRepaymentFrequency(rescheduleReqObj.getNewRepayFrequency());
				amortParam.setTenor(
						changeFactor.equals("INSTALLMENT") ? (loan.getTenor() - loan.getCurrentInstallmentNo())
								: rescheduleReqObj.getNewTenor());
				amortParam.setTenorUnit(loan.getTenorUnit());

				log.info(" Amort Parameter " + amortParam.toString());

				List<Amort> amortList = coreAmort.getAmort(amortParam);

				Integer maxSeqNoSch = repayHistRepo.getMaxSeqNo(mastAgrId, sdf.parse(tranDate));
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
				repayRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(mastAgrId, sdf.parse(tranDate));

				List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
				List<TrnInsInstrument> insList = instRepo.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId,
						sdf.parse(tranDate));
				for (TrnInsInstrument inst : insList) {
					TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
					BeanUtils.copyProperties(inst, insHist);
					insHist.setSeqNo(maxSeqNoSch);
					insHistList.add(insHist);
				}

				insHistRepo.saveAll(insHistList);

				instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, sdf.parse(tranDate));
				Double nextInstallmentAmount = 0d;
				if (amortList.get(0).getInstallmentNo() == 1) {
					nextInstallmentAmount = amortList.get(0).getInstallmentAmount();
				}

				for (Amort amort : amortList) {

					AgrRepaySchedule repay = new AgrRepaySchedule();
					repay.setLoanId(loan.getLoanId());
					repay.setMasterAgrId(mastAgrId);
					repay.setInstallmentNo(installmentNo++);
					repay.setDtInstallment(sdf.parse(amort.getDtInstallment()));
					repay.setOpeningPrincipal(amort.getOpeningBalance());
					repay.setPrincipalAmount(amort.getPrincipalAmount());
					repay.setInterestAmount(amort.getInterestAmount());
					repay.setBpiAmount(amort.getBpiAmount());
					repay.setInstallmentAmount(amort.getInstallmentAmount());
					repay.setClosingPrincipal(amort.getClosingBalance());
					repay.setInterestRate(amort.getInterestRate());
					repay.setInterestBasis(product.getInterestBasis());
					repay.setUserId("SYSTEM");
					repay.setTdsAmount(commonService
								.numberFormatter(repay.getInterestAmount() * (masterObj.getTdsRate() / 100)));
					repayRepo.save(repay);

				}

				if (restructureReqWaiveList.size() == 0 && restructureReqCapList.size() == 0) {
					AgrTrnEventDtl eventDefault = new AgrTrnEventDtl();

					eventDefault.setTranHeader(hdr);
					eventDefault.setTranEvent("RESCHEDULE");
					eventDefault.setUserId(rescheduleReqObj.getUserId());
					eventDefault.setTranAmount(commonService.numberFormatter(capitalizeAmount));

					AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
					tranDtl.setEventDtl(eventDefault);
					tranDtl.setMasterAgr(masterObj);
					tranDtl.setLoan(loan);
					tranDtl.setTranCategory("RESCHEDULE");
					tranDtl.setTranHead(changeFactor);
					tranDtl.setTranSide("DR");
					tranDtl.setTranAmount(0d);
					tranDtl.setDtlRemark("Default Reschedule Entry");

					tranDtlRepo.save(tranDtl);

				}

				masterObj.setDtNextInstallment(nextInstallmentDate);
				masterObj.setNextInstallmentAmount(nextInstallmentAmount);

				masterRepo.save(masterObj);

				loan.setTenor(amortList.size() + loan.getCurrentInstallmentNo());
				loan.setInterestRate(rescheduleReqObj.getNewInterestRate());
				loan.setRepayFreq(rescheduleReqObj.getNewRepayFrequency());
				loanRepo.save(loan);

				log.info("before postEcsInstruments");
				String ecsInstResult = disbService.postEcsInstruments(mastAgrId);
				log.info("After postEcsInstruments");

			}

		} catch (Exception e) {
			throw e;
		}

		return result;

	}
}
