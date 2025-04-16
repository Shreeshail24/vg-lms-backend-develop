package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.core.dto.AmortParameter;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.dto.EventFeeOutputDto;
import com.samsoft.lms.core.dto.RuleDetailsDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
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
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleHistRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnEventDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.repositories.TabMstPayRuleManagerDtlRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.core.repositories.VMstPayAppRuleSetRepository;
import com.samsoft.lms.core.repositories.VMstPayRuleManagerRepository;
import com.samsoft.lms.core.services.ColenderDueService;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.CoreAmort;
import com.samsoft.lms.core.services.CoreServices;
import com.samsoft.lms.core.services.OdAmort;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;
import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentHdr;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPrepaymentHdrRepository;
import com.samsoft.lms.request.services.PartPrepaymentReqService;
import com.samsoft.lms.transaction.dto.ForclosureDueDetails;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.entities.AgrTrnPrepaymentDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;
import com.samsoft.lms.transaction.repositories.AgrTrnPrepaymentDtlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PartPrepaymentApplicationService {

	@Autowired
	private AgrMasterAgreementRepository masterRepo;
	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;
	@Autowired
	private CommonServices commonService;
	@Autowired
	private VMstPayRuleManagerRepository vMstMgrRepo;
	@Autowired
	TabMstPayRuleManagerDtlRepository ruleMdrDtlRepo;
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	@Autowired
	private VMstPayAppRuleSetRepository vRuleSetRepo;
	@Autowired
	private AgrRepayScheduleRepository scheduleRepo;
	@Autowired
	private TabOrganizationRepository orgRepo;
	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;
	@Autowired
	private AgrTrnEventDtlRepository eventRepo;
	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;
	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private TrnInsInstrumentAllocRepository instAllocRepo;
	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;
	@Autowired
	private AgrTrnLimitDtlsRepository agrLimitRepo;
	@Autowired
	private TransactionService tranServ;
	@Autowired
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private AgrProductRepository prodRepo;
	@Autowired
	private OdAmort odAmort;
	@Autowired
	private CoreAmort coreAmort;
	@Autowired
	private Environment env;
	@Autowired
	private CoreServices coreService;
	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;
	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;
	@Autowired
	private DisbursementService disbService;
	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;
	@Autowired
	private PaymentApplicationServices paymentService;
	@Autowired
	private PartPrepaymentReqService partPrepaymentService;
	@Autowired
	private ForclosureApplicationService foreclosureAppService;
	@Autowired
	private AgrTrnReqPrepaymentHdrRepository prepayHdrRepo;
	@Autowired
	private AgrTrnPrepaymentDtlRepository prepayDtlRepo;
	@Autowired
	private ColenderDueService conlenderDueService;
	@Autowired
	private AgrTrnReqInstrumentRepository instReqRepo;
	@Autowired
	private AgrTrnReqInstrumentAllocDtlRepository instReqAlloRepo;
	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;
	@Autowired
	private SupplierFinanceUtility suppUtility;
	@Autowired
	private PartPrepaymentApplicationService partPrepaymentAppService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String partPrepaymentApplicationAgrAutoApportion(String mastAgrId, Date tranDate, Integer reqId)
			throws Exception {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		AgrLoans loans = null;
		try {

			AgrTrnReqInstrument reqInst = instReqRepo.findByRequestHdrReqId(reqId);

			TrnInsInstrument insInstrument = new TrnInsInstrument();
			insInstrument.setMasterAgr(mastAgrId);
			insInstrument.setDtInstrumentDate(reqInst.getDtInstrumentDate());
			insInstrument.setPayType(reqInst.getPayType());
			insInstrument.setPayMode(reqInst.getPayMode());
			insInstrument.setInstrumentType(reqInst.getInstrumentType());
			insInstrument.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
			insInstrument.setInstrumentStatus("CLR");
			insInstrument.setDtReceipt(reqInst.getDtReceipt());
			insInstrument.setNclStatus("N");
			insInstrument.setBankName(reqInst.getBankCode());
			insInstrument.setBankBranchName(reqInst.getBankBranchCode());
			insInstrument.setIfscCode(reqInst.getIfscCode());
			insInstrument.setUtrNo(reqInst.getUtrNo());
			insInstrument.setUserId(reqInst.getUserId());
			insInstrument.setDepositRefNo(reqInst.getDepositRefNo());
			insInstrument.setCollectedBy(reqInst.getCollectedBy());
			insInstrument.setCollectionAgency(reqInst.getCollectionAgency());
			insInstrument.setSource("REQ");
			insInstrument.setSourceId(reqId.toString());

			TrnInsInstrument updateInstNo = instRepo.save(insInstrument);
			updateInstNo.setInstrumentNo(Integer.toString(updateInstNo.getInstrumentId()));
			instRepo.save(updateInstNo);

			AgrTrnReqInstrumentAllocDtl reqInstAlloc = instReqAlloRepo.findByInstrumentRequestHdrReqId(reqId);
			TrnInsInstrumentAlloc instAlloc = new TrnInsInstrumentAlloc();
			instAlloc.setLoanId(reqInstAlloc.getLoanId());
			instAlloc.setApportionAmount(commonService.numberFormatter(reqInstAlloc.getAmout()));
			instAlloc.setUserId(reqInstAlloc.getUserId());
			instAlloc.setInstrument(insInstrument);

			instAllocRepo.save(instAlloc);

			AgrMasterAgreement master = masterRepo.findByMastAgrId(mastAgrId);

			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);

			TrnInsInstrument instrument = instRepo.findByMasterAgrAndInstrumentIdAndInstrumentStatus(mastAgrId,
					updateInstNo.getInstrumentId(), "CLR");
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);

			if (instrument == null) {
				throw new CoreDataNotFoundException("No instrument found for payment application. ");
			}

			Date businessDate = orgRepo.findAll().get(0).getDtBusiness();

			String paymentFor = instrument.getPayType();

			AgrTrnTranHeader tranHdr = new AgrTrnTranHeader();
			tranHdr.setMasterAgr(master);
			tranHdr.setTranDate(tranDate);
			tranHdr.setTranType("RECEIPT");
			tranHdr.setRemark("Part Prepayment Receipt");
			tranHdr.setSource(instrument.getSource());
			tranHdr.setReqId(instrument.getSourceId());
			tranHdr.setIntrumentId(updateInstNo.getInstrumentId());
			tranHdr.setUserID(instrument.getUserId());
			if (limitSetup != null) {
				tranHdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			hdrRepo.save(tranHdr);

			AgrTrnReqPrepaymentHdr findByRequestHdrReqId = prepayHdrRepo
					.findByRequestHdrReqId(Integer.parseInt(updateInstNo.getSourceId()));

			AgrTrnPrepaymentDtl prepayDtl = new AgrTrnPrepaymentDtl();

			prepayDtl.setBpiAmount(findByRequestHdrReqId.getBpiAmount());
			prepayDtl.setChangeFactor(findByRequestHdrReqId.getChangeFactor());
			prepayDtl.setDiscountAmount(findByRequestHdrReqId.getDiscountAmount());
			prepayDtl.setDtTranDate(findByRequestHdrReqId.getDtTranDate());
			prepayDtl.setExcessAmount(findByRequestHdrReqId.getExcessAmount());
			prepayDtl.setLoanId(findByRequestHdrReqId.getLoanId());
			prepayDtl.setMastAgrId(mastAgrId);
			prepayDtl.setPrepayReason(findByRequestHdrReqId.getPrepayReason());
			prepayDtl.setStatus(findByRequestHdrReqId.getStatus());
			prepayDtl.setTranHdr(tranHdr);
			prepayDtl.setTranType(findByRequestHdrReqId.getTranType());
			prepayDtl.setUnbilledPrincipal(findByRequestHdrReqId.getUnbilledPrincipal());
			prepayDtl.setUserId(findByRequestHdrReqId.getUserId());

			prepayDtlRepo.save(prepayDtl);

			AgrTrnEventDtl eventDtl = new AgrTrnEventDtl();
			eventDtl.setTranHeader(tranHdr);
			eventDtl.setTranEvent("PREPAY_RECEIVABLE");
			eventDtl.setTranAmount(instrument.getInstrumentAmount());
			eventDtl.setUserId("SYSTEM");

			/*
			 * ForclosureReceivableListDto partPrepaymentReceivables =
			 * partPrepaymentService.getPrepayReceivableList(mastAgrId,
			 * instrument.getInstrumentAmount()); List<ForclosureDueDetails> dueList =
			 * partPrepaymentReceivables.getDueList();
			 * 
			 * double tmpAmout = 0; for (ForclosureDueDetails forEventDue : dueList) { if
			 * (forEventDue.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL")) {
			 * tmpAmout = forEventDue.getDueAmount(); }
			 * 
			 * }
			 */
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);

			ForclosureReceivableListDto partPrepaymentReceivables = partPrepaymentService
					.getPrepayReceivableList(mastAgrId, instrument.getInstrumentAmount());
			List<ForclosureDueDetails> dueList = partPrepaymentReceivables.getDueList();
			double unbilledForChargesCalculation = 0d;
			for (ForclosureDueDetails due : dueList) {
				if (due.getBookYn().equalsIgnoreCase("Y")) {
					AgrTrnTranDetail bookTranDtl = new AgrTrnTranDetail();
					bookTranDtl.setEventDtl(eventDtl);
					bookTranDtl.setMasterAgr(master);
					bookTranDtl.setTranCategory(due.getDueCategory());
					bookTranDtl.setTranHead(due.getDueHead());
					bookTranDtl.setTranAmount(commonService.numberFormatter(due.getDueAmount()));
					bookTranDtl.setDtlRemark(due.getDueHead() + " booked");
					bookTranDtl.setTranSide("DR");
					bookTranDtl.setLoan(loan);
					bookTranDtl.setDtDueDate(sdf.parse(due.getDueDate()));

					tranDtlRepo.save(bookTranDtl);

					AgrTrnDueDetails bookDueDtl = new AgrTrnDueDetails();
					bookDueDtl.setTranDtlId(bookTranDtl.getTranDtlId());
					bookDueDtl.setLoanId(loan.getLoanId());
					bookDueDtl.setMastAgrId(mastAgrId);
					bookDueDtl.setDtDueDate(tranDate);
					bookDueDtl.setDueCategory(due.getDueCategory());
					bookDueDtl.setDueHead(due.getDueHead());
					bookDueDtl.setDueAmount(commonService.numberFormatter(due.getDueAmount()));

					dueRepo.save(bookDueDtl);

					// Supplier Finance Changes Start
					if (!(due.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL"))) {
						if (loan.getLoanType().equalsIgnoreCase("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), product.getProdCode(), "DED",
									commonService.numberFormatter(due.getDueAmount()), tranHdr.getTranId(),
									reqInst.getUserId(), tranDate);

						}
					}
					// Supplier Finance Changes End

					ColenderDueDto colenderDto = new ColenderDueDto();
					colenderDto.setDtDueDate(tranDate);
					colenderDto.setDueAmount(commonService.numberFormatter(due.getDueAmount()));
					colenderDto.setDueCategory(due.getDueCategory());
					colenderDto.setDueHead(due.getDueHead());
					colenderDto.setInstallmentNo(0);
					colenderDto.setLoanId(loan.getLoanId());
					colenderDto.setMastAgrId(master.getMastAgrId());
					colenderDto.setTranDtlId(bookTranDtl.getTranDtlId());
					colenderDto.setUserId("SYSTEM");
					conlenderDueService.generateColenderDues(colenderDto);

					if (due.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL")) {
						loan.setUnbilledPrincipal(
								commonService.numberFormatter(loan.getUnbilledPrincipal() - due.getDueAmount()));
						master.setUnbilledPrincipal(
								commonService.numberFormatter(master.getUnbilledPrincipal() - due.getDueAmount()));
						unbilledForChargesCalculation = commonService.numberFormatter(due.getDueAmount());
					}
				}

			}

			EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
					.getEventBaseChargesCalculation(mastAgrId, updateInstNo.getInstrumentId(), "PREPAYMENT", "N",
							unbilledForChargesCalculation, "N");

			List<EventFeeOutputDto> feeList = eventBaseChargesCalculation.getFeeList();

			for (EventFeeOutputDto fee : feeList) {

				AgrTrnTranDetail feeTranDtl = new AgrTrnTranDetail();
				feeTranDtl.setEventDtl(eventDtl);
				feeTranDtl.setMasterAgr(master);
				feeTranDtl.setTranCategory("FEE");
				feeTranDtl.setDtDueDate(tranDate);
				feeTranDtl.setTranHead(fee.getFeeCode());
				feeTranDtl.setTranAmount(commonService.numberFormatter(fee.getFeeAmount()));
				feeTranDtl.setTranSide("DR");
				feeTranDtl.setDtDueDate(tranDate);
				feeTranDtl.setLoan(loan);

				AgrTrnTranDetail save = tranDtlRepo.save(feeTranDtl);

				AgrTrnDueDetails feeDueDtl = new AgrTrnDueDetails();
				feeDueDtl.setTranDtlId(feeTranDtl.getTranDtlId());
				feeDueDtl.setLoanId(loan.getLoanId());
				feeDueDtl.setMastAgrId(mastAgrId);
				feeDueDtl.setDtDueDate(tranDate);
				feeDueDtl.setDueCategory("FEE");
				feeDueDtl.setDueHead(fee.getFeeCode());
				feeDueDtl.setDueAmount(commonService.numberFormatter(fee.getFeeAmount()));

				dueRepo.save(feeDueDtl);

				// Supplier Finance Changes Start
				if (loan.getLoanType().equalsIgnoreCase("OD")) {
					suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
							master.getCustomerId(), product.getProdCode(), "DED",
							commonService.numberFormatter(fee.getFeeAmount()), tranHdr.getTranId(), reqInst.getUserId(),
							tranDate);

				}
				// Supplier Finance Changes End

				List<GstListDto> gstList = fee.getGstList();
				for (GstListDto gst : gstList) {
					AgrTrnTranDetail gstTranDtl = new AgrTrnTranDetail();
					gstTranDtl.setEventDtl(eventDtl);
					gstTranDtl.setMasterAgr(master);
					gstTranDtl.setLoan(loan);
					feeTranDtl.setTranCategory("TAX");
					gstTranDtl.setTranHead(gst.getTaxCode());
					gstTranDtl.setTranAmount(commonService.numberFormatter(gst.getTaxAmount()));
					gstTranDtl.setTranSide("DR");
					gstTranDtl.setRefTranDtlId(save.getTranDtlId());
					gstTranDtl.setDtDueDate(tranDate);

					tranDtlRepo.save(gstTranDtl);

					AgrTrnTaxDueDetails taxDueDtl = new AgrTrnTaxDueDetails();
					taxDueDtl.setTaxCategory("TAX");
					taxDueDtl.setTaxHead(gst.getTaxCode());
					taxDueDtl.setDueTaxAmount(commonService.numberFormatter(gst.getTaxAmount()));
					taxDueDtl.setDueDetail(feeDueDtl);

					taxRepo.save(taxDueDtl);

					// Supplier Finance Changes Start
					if (loan.getLoanType().equalsIgnoreCase("OD")) {
						suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
								master.getCustomerId(), product.getProdCode(), "DED",
								commonService.numberFormatter(gst.getTaxAmount()), tranHdr.getTranId(),
								reqInst.getUserId(), tranDate);

					}
					// Supplier Finance Changes End
				}

				ColenderDueDto colenderDto = new ColenderDueDto();
				colenderDto.setDtDueDate(tranDate);
				colenderDto.setDueAmount(commonService.numberFormatter(fee.getFeeAmount()));
				colenderDto.setDueCategory("FEE");
				colenderDto.setDueHead(fee.getFeeCode());
				colenderDto.setInstallmentNo(0);
				colenderDto.setLoanId(loan.getLoanId());
				colenderDto.setMastAgrId(master.getMastAgrId());
				colenderDto.setTranDtlId(feeTranDtl.getTranDtlId());
				colenderDto.setUserId("SYSTEM");
				conlenderDueService.generateColenderDues(colenderDto);

			}

			TrnInsInstrument clearInst = instRepo.findByMasterAgrAndInstrumentIdAndInstrumentStatus(mastAgrId,
					updateInstNo.getInstrumentId(), "CLR");

			AgrTrnEventDtl eventDtlReceipt = new AgrTrnEventDtl();
			eventDtlReceipt.setTranHeader(tranHdr);
			eventDtlReceipt.setTranEvent(clearInst.getPayType());
			eventDtlReceipt.setTranAmount(commonService.numberFormatter(clearInst.getInstrumentAmount()));
			eventDtlReceipt.setUserId("SYSTEM");

			log.info("paymentFor " + paymentFor);
			String paymentRule = paymentService.getPaymentRule(mastAgrId, paymentFor);
			log.info("paymentRule " + paymentRule);

			List<RuleDetailsDto> ruleDetailsFee = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRule, "FEE");

			List<RuleDetailsDto> ruleDetailsAcc = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRule,
					"UNBILLED_PRINCIPAL");

			List<RuleDetailsDto> ruleDetails = new ArrayList<RuleDetailsDto>();

			List<RuleDetailsDto> ruleDetailsInst = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRule, "INSTALLMENT");
			Double ruleInstAmount = 0.0d;
			for (RuleDetailsDto ruleInst : ruleDetailsInst) {
				RuleDetailsDto inst = new RuleDetailsDto();
				BeanUtils.copyProperties(ruleInst, inst);
				ruleDetails.add(inst);
				ruleInstAmount += ruleInst.getDueAmount();
			}

			for (RuleDetailsDto ruleAcc : ruleDetailsAcc) {
				RuleDetailsDto inst = new RuleDetailsDto();
				BeanUtils.copyProperties(ruleAcc, inst);
				ruleDetails.add(inst);
				ruleInstAmount += ruleAcc.getDueAmount();
			}

			log.info("ruleDetails" + ruleDetails.size());

			Double amount = 0.0;
			Double rowAmount = 0d;

			Double totalFee = 0d;
			Double totalAmount = 0d;
			if (ruleDetails.size() == 0) {
				totalAmount = instrument.getInstrumentAmount();
			}
			log.info("totalAmount" + totalAmount);
			// if (paymentFor.equalsIgnoreCase("FEE")) {
			for (RuleDetailsDto ruleDto : ruleDetailsFee) {
				String onlyTax = null;
				Double totalTax = 0d;
				totalAmount = ruleDto.getDueAmount();
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(ruleDto.getDueDtlId());
				for (AgrTrnTaxDueDetails tax : taxList) {
					totalAmount += tax.getDueTaxAmount();
				}
				rowAmount = totalAmount;

				if (commonService.numberFormatter(ruleDto.getDueAmount()) > totalAmount) {
					amount = totalAmount;
				} else {
					amount = commonService.numberFormatter(ruleDto.getDueAmount());
				}

				if (ruleDto.getDueCategory().equalsIgnoreCase("FEE") && (ruleDto.getDueAmount() == 0)) {
					onlyTax = "Y";
				} else {
					onlyTax = "N";
				}

				if (rowAmount > 0 && onlyTax.equals("N")) {
					if (amount > 0) {
						AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
						tranDtl.setEventDtl(eventDtlReceipt);
						tranDtl.setMasterAgr(master);
						tranDtl.setTranCategory(ruleDto.getDueCategory());
						tranDtl.setTranHead(ruleDto.getPayHead());
						tranDtl.setTranAmount(amount * (-1));
						tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
						tranDtl.setTranSide("CR");
						tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
						tranDtl.setDtDueDate(ruleDto.getDueDate());
						AgrTrnTranDetail save = tranDtlRepo.save(tranDtl);

						if (limitSetup != null) {
							tranDtl.setAvailableLimit(commonService.numberFormatter(
									limitSetup.getAvailableLimit() + commonService.numberFormatter(amount)));
							tranDtl.setUtilizedLimit(commonService.numberFormatter(
									limitSetup.getUtilizedLimit() - commonService.numberFormatter(amount)));

							paymentService.updateLimit(mastAgrId, commonService.numberFormatter(amount), "ADD",
									"RECEIPT", tranHdr.getTranId(), "PAYAPP");
						}

						// Supplier Finance Changes Start
						if (loan.getLoanType().equalsIgnoreCase("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), product.getProdCode(), "ADD",
									commonService.numberFormatter(amount), tranHdr.getTranId(), reqInst.getUserId(),
									tranDate);

						}
						// Supplier Finance Changes End

						List<AgrTrnTaxDueDetails> taxDueList = new ArrayList<AgrTrnTaxDueDetails>();
						if (ruleDto.getDueCategory().equals("FEE")) {

							taxDueList = taxRepo.findByDueDetailDueDtlId(ruleDto.getDueDtlId());
							for (AgrTrnTaxDueDetails taxDue : taxDueList) {
								totalTax += taxDue.getDueTaxAmount();
							}

							totalFee = dueRepo.findByDueDtlId(ruleDto.getDueDtlId()).getDueAmount();
							Double tempAmount = 0d, newTotalTax = 0d;
							if (rowAmount < (amount + totalTax)) {
								tempAmount = commonService.numberFormatter((amount * rowAmount) / (amount + totalTax));
								newTotalTax = rowAmount - tempAmount;
							} else {
								tempAmount = Math.min(amount, rowAmount);
								newTotalTax = totalTax;
							}
							Double taxAmount = 0d;
							Double remTaxAmount = newTotalTax;
							for (AgrTrnTaxDueDetails taxDue : taxDueList) {
								if (newTotalTax == totalTax) {
									taxAmount = taxDue.getDueTaxAmount();
								} else {
									taxAmount = commonService.numberFormatter(
											(amount + newTotalTax) * taxDue.getDueTaxAmount() / (amount + totalTax));
								}
								if (taxAmount > 0) {
									AgrTrnTranDetail tranDtlTax = new AgrTrnTranDetail();
									tranDtlTax.setEventDtl(eventDtlReceipt);
									tranDtlTax.setMasterAgr(master);
									tranDtlTax.setTranCategory("TAX");
									tranDtlTax.setTranHead(taxDue.getTaxHead());
									tranDtlTax.setTranAmount(commonService.numberFormatter(taxAmount * (-1)));
									tranDtlTax.setTranSide("CR");
									tranDtlTax.setDtlRemark(taxDue.getTaxHead() + " recovered");
									tranDtlTax.setInstallmentNo(-1);
									tranDtlTax.setDtDueDate(ruleDto.getDueDate());
									tranDtlTax.setRefTranDtlId(save.getTranDtlId());
									tranDtlRepo.save(tranDtlTax);

									if (limitSetup != null) {
										tranDtl.setAvailableLimit(
												commonService.numberFormatter(limitSetup.getAvailableLimit()
														+ commonService.numberFormatter(taxAmount)));
										tranDtl.setUtilizedLimit(
												commonService.numberFormatter(limitSetup.getUtilizedLimit()
														- commonService.numberFormatter(taxAmount)));

										paymentService.updateLimit(mastAgrId, commonService.numberFormatter(taxAmount),
												"ADD", "RECEIPT", tranHdr.getTranId(), "PAYAPP");
									}
									// Supplier Finance Changes Start
									if (loan.getLoanType().equalsIgnoreCase("OD")) {
										suppUtility.updateCustomerLimit(master.getMastAgrId(),
												master.getOriginationApplnNo(), master.getCustomerId(),
												product.getProdCode(), "ADD", commonService.numberFormatter(taxAmount),
												tranHdr.getTranId(), reqInst.getUserId(), tranDate);

									}
									// Supplier Finance Changes End

									AgrTrnTaxDueDetails taxDueUpdate = taxRepo.findByTaxDueId(taxDue.getTaxDueId());
									remTaxAmount = commonService
											.numberFormatter(remTaxAmount - taxDue.getDueTaxAmount());
									rowAmount = commonService.numberFormatter(rowAmount - taxDue.getDueTaxAmount());
									taxDueUpdate.setDueTaxAmount(commonService.numberFormatter(
											taxDueUpdate.getDueTaxAmount() - taxDue.getDueTaxAmount()));

									taxRepo.save(taxDueUpdate);

								}

							}

							AgrTrnTranDetail tranDtlUpdate = tranDtlRepo.findByTranDtlId(ruleDto.getTranDtlId());
							tranDtlUpdate.setTranAmount(rowAmount);
							tranDtlRepo.save(tranDtlUpdate);

						}

						AgrTrnDueDetails dueDtlId = dueRepo.findByDueDtlId(ruleDto.getDueDtlId());
						dueDtlId.setDueAmount(commonService
								.numberFormatter(commonService.numberFormatter(dueDtlId.getDueAmount() - amount)));
						dueRepo.save(dueDtlId);
						rowAmount = commonService.numberFormatter((rowAmount - amount));
						tranDtlRepo.save(tranDtl);
						taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(ruleDto.getDueDtlId(), 0d);

					}

				}
			}
			totalAmount = rowAmount;
			dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtl);
			// } else {
			// totalAmount =
			// commonService.numberFormatter(instrument.getInstrumentAmount());
			totalAmount = ruleInstAmount;
			log.info("totalAmount 1 " + totalAmount);
			for (RuleDetailsDto ruleDto : ruleDetails) {
				if (commonService.numberFormatter(ruleDto.getDueAmount()) > totalAmount) {
					amount = totalAmount;
				} else {
					amount = commonService.numberFormatter(ruleDto.getDueAmount());
				}
				log.info("getDueAmount  " + ruleDto.getDueAmount());
				log.info("amount  " + amount);
				if (amount > 0) {
					AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
					tranDtl.setEventDtl(eventDtlReceipt);
					tranDtl.setMasterAgr(master);
					tranDtl.setTranCategory(ruleDto.getDueCategory());
					tranDtl.setTranHead(ruleDto.getPayHead());
					tranDtl.setTranAmount(commonService.numberFormatter(amount * (-1)));
					tranDtl.setTranSide("CR");
					tranDtl.setLoan(loanRepo.findByLoanId(ruleDto.getLoanId()));
					tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
					tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
					tranDtl.setDtDueDate(ruleDto.getDueDate());
					if (limitSetup != null) {
						tranDtl.setAvailableLimit(
								commonService.numberFormatter(limitSetup.getAvailableLimit() + amount));
						tranDtl.setUtilizedLimit(commonService.numberFormatter(limitSetup.getUtilizedLimit() - amount));

						paymentService.updateLimit(mastAgrId, amount, "ADD", "RECEIPT", tranHdr.getTranId(), "PAYAPP");
					}

					// Supplier Finance Changes Start
					if (loan.getLoanType().equalsIgnoreCase("OD")) {
						suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
								master.getCustomerId(), product.getProdCode(), "ADD",
								commonService.numberFormatter(amount), tranHdr.getTranId(), reqInst.getUserId(),
								tranDate);

					}
					// Supplier Finance Changes End

					AgrTrnDueDetails dueDtlId = dueRepo.findByDueDtlId(ruleDto.getDueDtlId());
					dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
					dueRepo.save(dueDtlId);
					if (ruleDto.getDueCategory().equals("INSTALLMENT") && (!((ruleDto.getPayHead().equals("INT_AMT"))
							|| (ruleDto.getPayHead().equals("PRIN_AMT"))))) {
						AgrRepaySchedule schedule = scheduleRepo.findByMasterAgrIdAndInstallmentNo(mastAgrId,
								ruleDto.getInstallmentNo());

						if (schedule == null) {
							throw new CoreDataNotFoundException("No data available in schedule for master agreement "
									+ mastAgrId + " and " + ruleDto.getInstallmentNo());
						}

						schedule.setDtPaymentDate(businessDate);
						scheduleRepo.save(schedule);
					}
					totalAmount = commonService.numberFormatter(totalAmount - amount);
					tranDtlRepo.save(tranDtl);

				} else {
					break;
				}
			}
			// }
			loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			String loanType = loans.getLoanType();

			if (ruleDetails.size() == 0) {
				totalAmount = instrument.getInstrumentAmount();
			}

			if (totalAmount > 0) {
				AgrTrnTranDetail tranDtl2 = new AgrTrnTranDetail();
				tranDtl2.setEventDtl(eventDtlReceipt);
				tranDtl2.setMasterAgr(master);
				tranDtl2.setTranCategory("EXCESS");
				tranDtl2.setTranHead("EXCESS_AMOUNT");
				tranDtl2.setTranAmount(totalAmount * -1);
				tranDtl2.setTranSide("CR");
				tranDtl2.setDtlRemark("Excess amount received");
				tranDtl2.setDtDueDate(tranDate);
				if (limitSetup != null) {
					tranDtl2.setAvailableLimit(
							commonService.numberFormatter(limitSetup.getAvailableLimit() + totalAmount));
					tranDtl2.setUtilizedLimit(
							commonService.numberFormatter(limitSetup.getUtilizedLimit() - totalAmount));
				}

				paymentService.updateExcess(mastAgrId, totalAmount, "ADD");
				tranDtlRepo.save(tranDtl2);
				if (limitSetup != null) {
					paymentService.updateLimit(mastAgrId, totalAmount, "ADD", "EXCESS_REC", tranHdr.getTranId(),
							"PAYAPP");
				}

				// Supplier Finance Changes Start
				if (loan.getLoanType().equalsIgnoreCase("OD")) {
					suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
							master.getCustomerId(), product.getProdCode(), "ADD", totalAmount, tranHdr.getTranId(),
							reqInst.getUserId(), tranDate);

				}
				// Supplier Finance Changes End

				totalAmount = 0.0;

			}
			// loan.setLoanAdditionalStatus("PART-PREPAYMENT");
			dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);

			List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();
			List<AgrRepaySchedule> repayList = scheduleRepo
					.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, businessDate);

			Double disbAmount = commonService.getMasterAgrUnbilledPrincipal(mastAgrId);
			Date nextInstallmentDate = repayList.get(0).getDtInstallment();
			double oldEmiAmount = repayList.get(0).getInstallmentAmount();
			Integer installmentNo = repayList.get(0).getInstallmentNo();
			double emiAmount = 0;
			if (disbAmount > 0) {
				if (prepayDtl.getChangeFactor().equalsIgnoreCase("INSTALLMENT")) {
					emiAmount = coreAmort.getEMI(disbAmount, loan.getInterestRate(),
							(loan.getTenor() - loan.getCurrentInstallmentNo()), loans.getTenorUnit(),
							loan.getRepayFreq(), product.getInterestBasis(), product.getAmortizationMethod());
				} else {
					emiAmount = oldEmiAmount;
				}

				AmortParameter amortParameter = new AmortParameter();

				amortParameter.setDtDisbursement(sdf.format(tranDate));
				amortParameter.setDtInstallmentStart(sdf.format(nextInstallmentDate));
				amortParameter.setLoanAmount(disbAmount);
				amortParameter.setInterestRate(loans.getInterestRate());
				amortParameter.setTenor(commonService.getBalanceTenor(loans.getLoanId(), tranDate));
				amortParameter.setTenorUnit(loans.getTenorUnit());
				amortParameter.setBpiHandling(product.getBpiTreatmentFlag());
				amortParameter.setInterestBasis(product.getInterestBasis());
				amortParameter.setRepaymentFrequency(loans.getRepayFreq());
				amortParameter.setAmortizationType(product.getAmortizationType());
				amortParameter.setAmortizationMethod(product.getAmortizationMethod());
				amortParameter.setLastRowThresholdPercentage(product.getLastRowEMIThreshold());
				amortParameter.setEmiBasis(product.getEmiBasis());
				amortParameter.setEmiRounding("ROUND");
				amortParameter.setNoOfAdvanceEmi(0);
				amortParameter.setEmiAmount(emiAmount);

				List<Amort> amortList = coreAmort.getAmort(amortParameter);

				Integer maxSeqNo = repayHistRepo.getMaxSeqNo(mastAgrId, tranDate);
				if (maxSeqNo == null) {
					maxSeqNo = 0;
				}
				maxSeqNo++;
				for (AgrRepaySchedule agrRepaySchedule : repayList) {
					AgrRepayScheduleHist repayHist = new AgrRepayScheduleHist();
					BeanUtils.copyProperties(agrRepaySchedule, repayHist);
					repayHist.setSeqNo(maxSeqNo);
					repayHistList.add(repayHist);
				}
				repayHistRepo.saveAll(repayHistList);
				scheduleRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(mastAgrId, businessDate);

				List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
				List<TrnInsInstrument> insList = instRepo.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId,
						businessDate);
				for (TrnInsInstrument inst : insList) {
					TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
					BeanUtils.copyProperties(inst, insHist);
					insHist.setSeqNo(maxSeqNo);
					insHistList.add(insHist);
				}

				insHistRepo.saveAll(insHistList);

				instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, businessDate);
				Double nextInstallmentAmount = 0d;
				if (amortList.get(0).getInstallmentNo() == 1) {
					nextInstallmentAmount = amortList.get(0).getInstallmentAmount();
				}

				for (Amort amort : amortList) {

					AgrRepaySchedule repay = new AgrRepaySchedule();
					repay.setLoanId(loans.getLoanId());
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
								.numberFormatter(repay.getInterestAmount() * (master.getTdsRate() / 100)));
					scheduleRepo.save(repay);

				}

				master.setDtNextInstallment(nextInstallmentDate);
				master.setNextInstallmentAmount(nextInstallmentAmount);

				masterRepo.save(master);

			}

			loanRepo.save(loans);

			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtlReceipt);

			log.info("Before Post Ecs Instruments");
			String postEcsInstrumentsResult = disbService.postEcsInstruments(mastAgrId);

			log.info("After Post Ecs Instruments " + postEcsInstrumentsResult);

			Boolean penalReversalForeclosureResult = foreclosureAppService.penalReversalForeclosure(mastAgrId,
					tranDate);

			log.info("penalReversalForeclosureResult " + penalReversalForeclosureResult);

			Boolean interestAccrualReversalForeclosure = foreclosureAppService
					.interestAccrualReversalForeclosure(mastAgrId, tranDate);

			log.info("interestAccrualReversalForeclosure " + interestAccrualReversalForeclosure);

			reqStatusUpdateService.updateRequestStatus(reqId, "APR");

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

		return result;
	}

}
