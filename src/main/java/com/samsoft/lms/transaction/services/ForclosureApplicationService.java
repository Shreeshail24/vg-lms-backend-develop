package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.batch.services.EODServices;
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
import com.samsoft.lms.core.entities.AgrTrnSysTranDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.TabOrganization;
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
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentAllocDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.transaction.dto.ForclosureDueDetails;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForclosureApplicationService {

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
	private EODServices edoServ;
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

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public ForclosureReceivableListDto getForclosureReceivable(String mastAgrId) throws Exception {
		ForclosureReceivableListDto receivable = new ForclosureReceivableListDto();

		AgrMasterAgreement master = masterRepo.findByMastAgrId(mastAgrId);
		AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
		Double totalAmount = 0d;
		List<ForclosureDueDetails> forclosureDueList = new ArrayList<ForclosureDueDetails>();
		List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdOrderByDtDueDate(mastAgrId);
		for (AgrTrnDueDetails due : dueList) {
			ForclosureDueDetails dueDetails = new ForclosureDueDetails();
			dueDetails.setDueCategory(due.getDueCategory());
			dueDetails.setDueHead(due.getDueHead());
			dueDetails.setDueAmount(due.getDueAmount());
			dueDetails.setBookYn("N");
			double tempTax = 0;
			if (due.getDueCategory().equalsIgnoreCase("FEE")) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(due.getDueDtlId());
				for (AgrTrnTaxDueDetails tax : taxList) {
					totalAmount += tax.getDueTaxAmount();
					tempTax += tax.getDueTaxAmount();
				}
			}

			dueDetails.setTaxAmount(tempTax);
			forclosureDueList.add(dueDetails);
			totalAmount += due.getDueAmount();
		}

		ForclosureDueDetails dueDetailsIntAccured = new ForclosureDueDetails();
		dueDetailsIntAccured
				.setDueAmount(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));
		dueDetailsIntAccured.setDueCategory("INSTALLMENT");
		dueDetailsIntAccured.setDueHead("BPI");
		dueDetailsIntAccured.setBookYn("Y");
		if (dueDetailsIntAccured.getDueAmount() > 0) {
			totalAmount += dueDetailsIntAccured.getDueAmount();
			forclosureDueList.add(dueDetailsIntAccured);
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
			ForclosureDueDetails dueDetailsPenalAcc = new ForclosureDueDetails();
			dueDetailsPenalAcc
					.setDueAmount(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL"));
			dueDetailsPenalAcc.setDueCategory("FEE");
			dueDetailsPenalAcc.setDueHead("PENAL");
			dueDetailsPenalAcc.setBookYn("Y");
			if (dueDetailsPenalAcc.getDueAmount() > 0) {
				totalAmount += dueDetailsPenalAcc.getDueAmount();
				forclosureDueList.add(dueDetailsPenalAcc);
			}
		}

		ForclosureDueDetails unbilledDetails = new ForclosureDueDetails();
		unbilledDetails.setDueAmount(master.getUnbilledPrincipal());
		unbilledDetails.setDueCategory("UNBILLED_PRINCIPAL");
		unbilledDetails.setDueHead("FUTURE_PRINCIPAL");
		unbilledDetails.setBookYn("Y");

		forclosureDueList.add(unbilledDetails);

		EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
				.getEventBaseChargesCalculation(mastAgrId, 0, "FORECLOSURE", "N", master.getUnbilledPrincipal(), "N");

		List<EventFeeOutputDto> feeList = eventBaseChargesCalculation.getFeeList();
		Double amount = 0d;

		for (EventFeeOutputDto fee : feeList) {
			amount = fee.getFeeAmount();
			List<GstListDto> gstList = fee.getGstList();
			for (GstListDto gst : gstList) {
				amount += gst.getTaxAmount();
			}

			ForclosureDueDetails dueDetailsForclosureCharges = new ForclosureDueDetails();
			dueDetailsForclosureCharges.setDueAmount(amount);
			dueDetailsForclosureCharges.setDueCategory("FEE");
			dueDetailsForclosureCharges.setDueHead(fee.getFeeCode());
			dueDetailsForclosureCharges.setBookYn("N");

			if (dueDetailsForclosureCharges.getDueAmount() > 0) {
				forclosureDueList.add(dueDetailsForclosureCharges);
			}

			totalAmount += amount;
		}

		receivable.setTotalReceivables(commonService.numberFormatter(master.getUnbilledPrincipal() + totalAmount));

		receivable.setExcess(commonService.numberFormatter(master.getExcessAmount()));
		receivable.setNetReceivables(
				commonService.numberFormatter(receivable.getTotalReceivables() - receivable.getExcess()));
		receivable.setDueList(forclosureDueList);
		return receivable;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String forclosureApplication(String mastAgrId, Date tranDate, Integer reqId) throws Exception {
		String result = "sucess";
		AgrLoans loans = null;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrRepaySchedule> scheduleList = new ArrayList<AgrRepaySchedule>();
		double nextInstallmentAmount = 0d;
		TrnInsInstrument updateInstNo = null;
		try {
			log.info("forclosureApplication :: mastAgrId: {}, tranDate: {}, reqId: {}", mastAgrId, tranDate, reqId);
			AgrTrnReqInstrument reqInst = instReqRepo.findByRequestHdrReqId(reqId);

			AgrTrnReqInstrumentAllocDtl reqInstAlloc = instReqAlloRepo.findByInstrumentRequestHdrReqId(reqId);

			if (reqInst.getInstrumentType().equalsIgnoreCase("CH")) {
				TrnInsInstrument inst = new TrnInsInstrument();
				inst.setDtInstrumentDate(reqInst.getDtInstrumentDate());
				inst.setPayType(reqInst.getPayType());
				inst.setPayMode(reqInst.getPayMode());
				inst.setInstrumentType("CH");
				inst.setAccountNo(reqInst.getAccountNo());
				inst.setAccountType(reqInst.getAccountType());
				inst.setInstrumentNo(reqInst.getInstrumentNo());
				inst.setBankName(reqInst.getBankCode());
				inst.setBankBranchName(reqInst.getBankBranchCode());
				inst.setMicrCode(reqInst.getMicrCode());
				inst.setClearingLocation(reqInst.getClearingLocation());
				inst.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
				inst.setInstrumentStatus("NEW");
				inst.setDepositBankName(reqInst.getDepositBank());
				inst.setUtrNo(reqInst.getUtrNo());
				inst.setIfscCode(reqInst.getIfscCode());
				inst.setDtReceipt(reqInst.getDtReceipt());
				inst.setDepositRefNo(reqInst.getDepositRefNo());
				inst.setCardHolderName(reqInst.getCardHolderName());
				inst.setIssuingBank(reqInst.getIssuingBank());
				inst.setUpiVpa(reqInst.getUpiVpa());
				inst.setCollectedBy(reqInst.getCollectedBy());
				inst.setCollectionAgency(reqInst.getCollectionAgency());
				inst.setProvisionalReceipt(reqInst.getProvisionalReceipt());
				inst.setProcLoc(reqInst.getProcLoc());
				inst.setCardType(reqInst.getCardType());
				inst.setNclStatus("N");
				inst.setUserId(reqInst.getUserId());
				inst.setMasterAgr(mastAgrId);
				inst.setDepositBank("ICICI_HL_AC");
				inst.setDepositBankBranch("PUNE");
				inst.setDepositBankIfsc("ICICI000001");
				inst.setCustomerId(masterRepo.findByMastAgrId(mastAgrId).getCustomerId());

				updateInstNo = instRepo.save(inst);
				updateInstNo.setInstrumentNo(Integer.toString(updateInstNo.getInstrumentId()));
				instRepo.save(updateInstNo);

				TrnInsInstrumentAlloc instAl = new TrnInsInstrumentAlloc();
				instAl.setLoanId(reqInstAlloc.getLoanId());
				instAl.setApportionAmount(commonService.numberFormatter(reqInstAlloc.getAmout()));
				instAl.setUserId(reqInstAlloc.getUserId());
				instAl.setInstrument(inst);

				instAllocRepo.save(instAl);

			} else if (reqInst.getInstrumentType().equalsIgnoreCase("CA")) {

				TrnInsInstrument insInstrument = new TrnInsInstrument();
				insInstrument.setMasterAgr(mastAgrId);
				insInstrument.setDtInstrumentDate(reqInst.getDtInstrumentDate());
				insInstrument.setPayType(reqInst.getPayType());
				insInstrument.setPayMode(reqInst.getPayMode());
				insInstrument.setInstrumentType(reqInst.getInstrumentType());
				insInstrument.setInstrumentAmount(commonService.numberFormatter(reqInst.getInstrumentAmount()));
				insInstrument.setInstrumentStatus("CLR");
				insInstrument.setDtReceipt(reqInst.getDtReceipt());
				insInstrument.setDepositRefNo(reqInst.getDepositRefNo());
				insInstrument.setNclStatus("N");
				insInstrument.setUserId(reqInst.getUserId());

				updateInstNo = instRepo.save(insInstrument);
				updateInstNo.setInstrumentNo(Integer.toString(updateInstNo.getInstrumentId()));
				instRepo.save(updateInstNo);

				TrnInsInstrumentAlloc instAl = new TrnInsInstrumentAlloc();
				instAl.setLoanId(reqInstAlloc.getLoanId());
				instAl.setApportionAmount(commonService.numberFormatter(reqInstAlloc.getAmout()));
				instAl.setUserId(reqInstAlloc.getUserId());
				instAl.setInstrument(insInstrument);

				instAllocRepo.save(instAl);

			} else {

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

				updateInstNo = instRepo.save(insInstrument);
				updateInstNo.setInstrumentNo(Integer.toString(updateInstNo.getInstrumentId()));
				instRepo.save(updateInstNo);

				TrnInsInstrumentAlloc instAlloc = new TrnInsInstrumentAlloc();
				instAlloc.setLoanId(reqInstAlloc.getLoanId());
				instAlloc.setApportionAmount(commonService.numberFormatter(reqInstAlloc.getAmout()));
				instAlloc.setUserId(reqInstAlloc.getUserId());
				instAlloc.setInstrument(insInstrument);

				instAllocRepo.save(instAlloc);
			}

			log.info("Forclosure Application input " + mastAgrId + " " + updateInstNo.getInstrumentId() + " "
					+ tranDate);

			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			TrnInsInstrument instrument = instRepo.findByMasterAgrAndInstrumentIdAndInstrumentStatus(mastAgrId,
					updateInstNo.getInstrumentId(), "CLR");

			if (instrument == null) {
				throw new CoreDataNotFoundException(
						"No instrument found for payment application or instrument is not CLR ");
			}

			String paymentFor = null;
			AgrMasterAgreement masterAgrObj = masterRepo.findByMastAgrId(mastAgrId);
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			List<TabOrganization> orgData = orgRepo.findAll();
			Date businessDate = null;

			if (orgData.size() <= 0) {
				throw new CoreDataNotFoundException("No Organization details available");
			}

			for (TabOrganization org : orgData) {
				businessDate = org.getDtBusiness();
			}

			paymentFor = instrument.getPayType();

			AgrTrnTranHeader tranHdr = new AgrTrnTranHeader();
			tranHdr.setMasterAgr(masterRepo.findByMastAgrId(mastAgrId));
			tranHdr.setTranDate(tranDate);
			tranHdr.setTranType("RECEIPT");
			tranHdr.setRemark("ForeClosure Receipt");
			tranHdr.setSource(instrument.getSource());
			tranHdr.setReqId(instrument.getSourceId());
			tranHdr.setIntrumentId(updateInstNo.getInstrumentId());
			tranHdr.setUserID(instrument.getUserId());

			if (limitSetup != null) {
				tranHdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl eventDtl = new AgrTrnEventDtl();
			eventDtl.setTranHeader(tranHdr);
			eventDtl.setTranEvent("FORECLOSURE_RECEIVABLE");
			eventDtl.setTranAmount(instrument.getInstrumentAmount());
			eventDtl.setUserId(instrument.getUserId());

			ForclosureReceivableListDto forclosureReceivable = this.getForclosureReceivable(mastAgrId);
			List<ForclosureDueDetails> dueList = forclosureReceivable.getDueList();

			double tmpAmout = 0;
			for (ForclosureDueDetails forEventDue : dueList) {
				if (forEventDue.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL")) {
					tmpAmout = forEventDue.getDueAmount();
				}

			}

			EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
					.getEventBaseChargesCalculation(mastAgrId, 0, "FORECLOSURE", "N", tmpAmout, "N");

			List<EventFeeOutputDto> feeList = eventBaseChargesCalculation.getFeeList();
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			for (EventFeeOutputDto fee : feeList) {

				AgrTrnTranDetail feeTranDtl = new AgrTrnTranDetail();
				feeTranDtl.setEventDtl(eventDtl);
				feeTranDtl.setMasterAgr(masterAgrObj);
				feeTranDtl.setTranCategory("FEE");
				feeTranDtl.setTranHead(fee.getFeeCode());
				feeTranDtl.setTranAmount(fee.getFeeAmount());
				feeTranDtl.setTranSide("DR");
				feeTranDtl.setLoan(loan);
				feeTranDtl.setDtDueDate(tranDate);

				AgrTrnTranDetail save = tranDtlRepo.save(feeTranDtl);

				AgrTrnDueDetails feeDueDtl = new AgrTrnDueDetails();
				feeDueDtl.setTranDtlId(feeTranDtl.getTranDtlId());
				feeDueDtl.setLoanId(loan.getLoanId());
				feeDueDtl.setMastAgrId(mastAgrId);
				feeDueDtl.setDtDueDate(tranDate);
				feeDueDtl.setDueCategory("FEE");
				feeDueDtl.setDueHead(fee.getFeeCode());
				feeDueDtl.setDueAmount(fee.getFeeAmount());

				dueRepo.save(feeDueDtl);

				// Supplier Finance Changes Start
				if (loan.getLoanType().equalsIgnoreCase("OD")) {
					suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(), masterAgrObj.getOriginationApplnNo(),
							masterAgrObj.getCustomerId(), product.getProdCode(), "DED",
							commonService.numberFormatter(fee.getFeeAmount()), tranHdr.getTranId(), reqInst.getUserId(),
							tranDate);

				}
				// Supplier Finance Changes End

				List<GstListDto> gstList = fee.getGstList();
				for (GstListDto gst : gstList) {
					AgrTrnTranDetail gstTranDtl = new AgrTrnTranDetail();
					gstTranDtl.setEventDtl(eventDtl);
					gstTranDtl.setMasterAgr(masterAgrObj);
					gstTranDtl.setLoan(loan);
					gstTranDtl.setTranCategory("TAX");
					gstTranDtl.setTranHead(gst.getTaxCode());
					gstTranDtl.setTranAmount(gst.getTaxAmount());
					gstTranDtl.setTranSide("DR");
					gstTranDtl.setDtDueDate(tranDate);
					gstTranDtl.setRefTranDtlId(save.getTranDtlId());
					tranDtlRepo.save(gstTranDtl);

					AgrTrnTaxDueDetails taxDueDtl = new AgrTrnTaxDueDetails();
					taxDueDtl.setTaxCategory("TAX");
					taxDueDtl.setTaxHead(gst.getTaxCode());
					taxDueDtl.setDueTaxAmount(gst.getTaxAmount());
					taxDueDtl.setDueDetail(feeDueDtl);

					taxRepo.save(taxDueDtl);

					// Supplier Finance Changes Start
					if (loan.getLoanType().equalsIgnoreCase("OD")) {
						suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
								masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
								product.getProdCode(), "DED", commonService.numberFormatter(gst.getTaxAmount()),
								tranHdr.getTranId(), reqInst.getUserId(), tranDate);

					}
					// Supplier Finance Changes End
				}

				ColenderDueDto colenderDto = new ColenderDueDto();
				colenderDto.setDtDueDate(tranDate);
				colenderDto.setDueAmount(fee.getFeeAmount());
				colenderDto.setDueCategory("FEE");
				colenderDto.setDueHead(fee.getFeeCode());
				colenderDto.setInstallmentNo(0);
				colenderDto.setLoanId(loan.getLoanId());
				colenderDto.setMastAgrId(mastAgrId);
				colenderDto.setTranDtlId(feeTranDtl.getTranDtlId());
				colenderDto.setUserId(instrument.getUserId());
				conlenderDueService.generateColenderDues(colenderDto);

			}

			for (ForclosureDueDetails due : dueList) {
				if (due.getBookYn().equalsIgnoreCase("Y")) {
					AgrTrnTranDetail bookTranDtl = new AgrTrnTranDetail();
					bookTranDtl.setEventDtl(eventDtl);
					bookTranDtl.setMasterAgr(masterAgrObj);
					bookTranDtl.setTranCategory(due.getDueCategory());
					bookTranDtl.setTranHead(due.getDueHead());
					bookTranDtl.setTranAmount(due.getDueAmount());
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
					bookDueDtl.setDueAmount(due.getDueAmount());

					dueRepo.save(bookDueDtl);

					// Supplier Finance Changes Start
					if (!(due.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL"))) {
						if (loan.getLoanType().equalsIgnoreCase("OD")) {
							suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
									masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
									product.getProdCode(), "DED", commonService.numberFormatter(due.getDueAmount()),
									tranHdr.getTranId(), reqInst.getUserId(), tranDate);

						}
					}
					// Supplier Finance Changes End

					if (due.getDueCategory().equalsIgnoreCase("UNBILLED_PRINCIPAL")) {
						loan.setUnbilledPrincipal(loan.getUnbilledPrincipal() - due.getDueAmount());
						masterAgrObj.setUnbilledPrincipal(masterAgrObj.getUnbilledPrincipal() - due.getDueAmount());
					}

					ColenderDueDto colenderDto = new ColenderDueDto();
					colenderDto.setDtDueDate(tranDate);
					colenderDto.setDueAmount(due.getDueAmount());
					colenderDto.setDueCategory(due.getDueCategory());
					colenderDto.setDueHead(due.getDueHead());
					colenderDto.setInstallmentNo(0);
					colenderDto.setLoanId(loan.getLoanId());
					colenderDto.setMastAgrId(mastAgrId);
					colenderDto.setTranDtlId(bookTranDtl.getTranDtlId());
					colenderDto.setUserId(instrument.getUserId());
					conlenderDueService.generateColenderDues(colenderDto);
				}

			}

			AgrTrnEventDtl eventDtlReceipt = new AgrTrnEventDtl();
			eventDtlReceipt.setTranHeader(tranHdr);
			eventDtlReceipt.setTranEvent("FORECLOSURE_RECEIPT");
			eventDtlReceipt.setTranAmount(instrument.getInstrumentAmount());
			eventDtlReceipt.setUserId(instrument.getUserId());

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
			Double instrumentAmount = instrument.getInstrumentAmount();
			if (ruleDetails.size() == 0) {
				totalAmount = instrument.getInstrumentAmount();
			}
			log.info("totalAmount" + totalAmount);
			// if (paymentFor.equalsIgnoreCase("FEE")) {
			for (RuleDetailsDto ruleDto : ruleDetailsFee) {
				if (instrumentAmount > 0) {
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
							tranDtl.setMasterAgr(masterAgrObj);
							tranDtl.setTranCategory(ruleDto.getDueCategory());
							tranDtl.setTranHead(ruleDto.getPayHead());
							tranDtl.setTranAmount(amount * (-1));
							tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
							tranDtl.setTranSide("CR");
							tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
							tranDtl.setDtDueDate(ruleDto.getDueDate());
							AgrTrnTranDetail save = tranDtlRepo.save(tranDtl);

							// Supplier Finance Changes Start
							if (loan.getLoanType().equalsIgnoreCase("OD")) {
								suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
										masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
										product.getProdCode(), "ADD", commonService.numberFormatter(amount),
										tranHdr.getTranId(), reqInst.getUserId(), tranDate);

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
									tempAmount = commonService
											.numberFormatter((amount * rowAmount) / (amount + totalTax));
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
										taxAmount = commonService.numberFormatter((amount + newTotalTax)
												* taxDue.getDueTaxAmount() / (amount + totalTax));
									}
									if (taxAmount > 0) {
										AgrTrnTranDetail tranDtlTax = new AgrTrnTranDetail();
										tranDtlTax.setEventDtl(eventDtlReceipt);
										tranDtlTax.setMasterAgr(masterAgrObj);
										tranDtlTax.setTranCategory("TAX");
										tranDtlTax.setTranHead(taxDue.getTaxHead());
										tranDtlTax.setTranAmount(taxAmount * (-1));
										tranDtlTax.setTranSide("CR");
										tranDtlTax.setDtlRemark(taxDue.getTaxHead() + " recovered");
										tranDtlTax.setInstallmentNo(-1);
										tranDtlTax.setDtDueDate(ruleDto.getDueDate());
										tranDtlTax.setRefTranDtlId(save.getTranDtlId());
										tranDtlRepo.save(tranDtlTax);

										// Supplier Finance Changes Start
										if (loan.getLoanType().equalsIgnoreCase("OD")) {
											suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
													masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
													product.getProdCode(), "DED",
													commonService.numberFormatter(taxAmount), tranHdr.getTranId(),
													reqInst.getUserId(), tranDate);

										}
										// Supplier Finance Changes End

										AgrTrnTaxDueDetails taxDueUpdate = taxRepo.findByTaxDueId(taxDue.getTaxDueId());
										remTaxAmount = remTaxAmount - taxDue.getDueTaxAmount();
										rowAmount = rowAmount - taxDue.getDueTaxAmount();
										taxDueUpdate.setDueTaxAmount(
												taxDueUpdate.getDueTaxAmount() - taxDue.getDueTaxAmount());

										taxRepo.save(taxDueUpdate);
										instrumentAmount = commonService.numberFormatter(instrumentAmount - taxAmount);

									}

								}

								AgrTrnTranDetail tranDtlUpdate = tranDtlRepo.findByTranDtlId(ruleDto.getTranDtlId());
								tranDtlUpdate.setTranAmount(rowAmount);
								tranDtlRepo.save(tranDtlUpdate);

							}

							AgrTrnDueDetails dueDtlId = dueRepo.findByDueDtlId(ruleDto.getDueDtlId());
							dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
							dueRepo.save(dueDtlId);
							rowAmount = rowAmount - amount;
							instrumentAmount = commonService.numberFormatter(instrumentAmount - amount);
							tranDtlRepo.save(tranDtl);

							taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(ruleDto.getDueDtlId(), 0d);

						}

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
				if (instrumentAmount > 0) {
					if (commonService.numberFormatter(ruleDto.getDueAmount()) > instrumentAmount) {
						amount = instrumentAmount;
					} else {
						amount = commonService.numberFormatter(ruleDto.getDueAmount());
					}
					log.info("getDueAmount  " + ruleDto.getDueAmount());
					log.info("amount  " + amount);
					if (amount > 0) {
						AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
						tranDtl.setEventDtl(eventDtlReceipt);
						tranDtl.setMasterAgr(masterAgrObj);
						tranDtl.setTranCategory(ruleDto.getDueCategory());
						tranDtl.setTranHead(ruleDto.getPayHead());
						tranDtl.setTranAmount(amount * (-1));
						tranDtl.setTranSide("CR");
						tranDtl.setLoan(loanRepo.findByLoanId(ruleDto.getLoanId()));
						tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
						tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
						tranDtl.setDtDueDate(ruleDto.getDueDate());
						if (limitSetup != null) {
							tranDtl.setAvailableLimit(
									commonService.numberFormatter(limitSetup.getAvailableLimit() + amount));
							tranDtl.setUtilizedLimit(
									commonService.numberFormatter(limitSetup.getUtilizedLimit() - amount));

							paymentService.updateLimit(mastAgrId, amount, "ADD", "RECEIPT", tranHdr.getTranId(),
									"PAYAPP");
						}

						// Supplier Finance Changes Start
						if (loan.getLoanType().equalsIgnoreCase("OD")) {
							suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
									masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
									product.getProdCode(), "ADD", commonService.numberFormatter(amount),
									tranHdr.getTranId(), reqInst.getUserId(), tranDate);

						}
						// Supplier Finance Changes End

						AgrTrnDueDetails dueDtlId = dueRepo.findByDueDtlId(ruleDto.getDueDtlId());
						dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
						instrumentAmount = commonService.numberFormatter(instrumentAmount - amount);
						dueRepo.save(dueDtlId);
						taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(ruleDto.getDueDtlId(), 0d);

						if (ruleDto.getDueCategory().equals("INSTALLMENT")
								&& (((ruleDto.getPayHead().equals("INTEREST"))
										|| (ruleDto.getPayHead().equals("PRINCIPAL"))))) {
							AgrRepaySchedule schedule = scheduleRepo.findByMasterAgrIdAndInstallmentNo(mastAgrId,
									ruleDto.getInstallmentNo());

							if (schedule == null) {
								throw new CoreDataNotFoundException(
										"No data available in schedule for master agreement " + mastAgrId + " and "
												+ ruleDto.getInstallmentNo());
							}

							schedule.setDtPaymentDate(businessDate);
							scheduleRepo.save(schedule);
						}
						totalAmount = totalAmount - amount;
						tranDtlRepo.save(tranDtl);

					} else {
						break;
					}
				}
			}
			// }
			loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			String loanType = loans.getLoanType();

			if (ruleDetails.size() == 0) {
				instrumentAmount = instrument.getInstrumentAmount();
			}

			if (instrumentAmount > 0) {
				AgrTrnTranDetail tranDtl2 = new AgrTrnTranDetail();
				tranDtl2.setEventDtl(eventDtlReceipt);
				tranDtl2.setMasterAgr(masterAgrObj);
				tranDtl2.setTranCategory("EXCESS");
				tranDtl2.setTranHead("EXCESS_AMOUNT");
				tranDtl2.setTranAmount(instrumentAmount * -1);
				tranDtl2.setTranSide("CR");
				tranDtl2.setDtlRemark("Excess amount received");
				tranDtl2.setDtDueDate(tranDate);
				if (limitSetup != null) {
					tranDtl2.setAvailableLimit(
							commonService.numberFormatter(limitSetup.getAvailableLimit() + instrumentAmount));
					tranDtl2.setUtilizedLimit(
							commonService.numberFormatter(limitSetup.getUtilizedLimit() - instrumentAmount));
				}

				paymentService.updateExcess(mastAgrId, instrumentAmount, "ADD");
				tranDtlRepo.save(tranDtl2);
				if (limitSetup != null) {
					paymentService.updateLimit(mastAgrId, instrumentAmount, "ADD", "EXCESS_REC", tranHdr.getTranId(),
							"PAYAPP");
				}

				// Supplier Finance Changes Start
				if (loan.getLoanType().equalsIgnoreCase("OD")) {
					suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(), masterAgrObj.getOriginationApplnNo(),
							masterAgrObj.getCustomerId(), product.getProdCode(), "ADD",
							commonService.numberFormatter(instrumentAmount), tranHdr.getTranId(), reqInst.getUserId(),
							tranDate);

				}
				// Supplier Finance Changes End

				totalAmount = 0.0;

			}

			dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);

			Boolean excessAdj = edoServ.excessAdjustmentApi(masterAgrObj.getCustomerId(), businessDate);
			log.info("excessAdj " + excessAdj);

			loan.setLoanAdditionalStatus("FORECLOSED");
			if (masterAgrObj.getUnbilledPrincipal() == 0 && masterAgrObj.getTotalDues() == 0
					&& masterAgrObj.getExcessAmount() == 0) {
				masterAgrObj.setAgreementStatus("C");
				masterAgrObj.setDtLastStatusUpdated(tranDate);
			}

			Integer maxSeqNo = repayHistRepo.getMaxSeqNo(mastAgrId, tranDate);
			if (maxSeqNo == null) {
				maxSeqNo = 0;
			}
			maxSeqNo++;
			List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();
			List<AgrRepaySchedule> repayList = scheduleRepo
					.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, businessDate);
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

			loanRepo.save(loans);
			dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtlReceipt);

			Boolean penalReversalForeclosureResult = penalReversalForeclosure(mastAgrId, tranDate);

			log.info("penalReversalForeclosureResult " + penalReversalForeclosureResult);

			Boolean interestAccrualReversalForeclosure = interestAccrualReversalForeclosure(mastAgrId, tranDate);

			log.info("interestAccrualReversalForeclosure " + interestAccrualReversalForeclosure);

			reqStatusUpdateService.updateRequestStatus(reqId, "APR");

		} catch (CoreDataNotFoundException e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean penalReversalForeclosure(String mastAgrId, Date tranDate) {
		Boolean result = true;
		try {
			List<AgrLoans> listLoanDetails = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			for (AgrLoans loanDetail : listLoanDetails) {
				Double totalDue;
				if (loanDetail.getTotalDues() == null) {
					totalDue = 0.0d;
				} else {
					totalDue = loanDetail.getTotalDues();
				}
				if (totalDue == 0) {
					Integer maxDpd = sysRepo.getMaxDpd("PENAL_ACCRUAL", loanDetail.getLoanId());
					Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(mastAgrId).getGraceDays();
					if (maxDpd == null) {
						maxDpd = 0;
					}
					if (graceDays == null) {
						graceDays = 0;
					}
					// if (maxDpd < graceDays) {
					Double sumOfDues = this.commonService.numberFormatter(
							sysRepo.getSumOfTranAmountOfPenal("PENAL_ACCRUAL", loanDetail.getLoanId(), tranDate));
					if (sumOfDues != null) {
						if (sumOfDues > 0) {
							AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
							sysTran.setCustomerId(loanDetail.getCustomerId());
							sysTran.setMastAgrId(mastAgrId);
							sysTran.setLoanId(loanDetail.getLoanId());
							sysTran.setDtTranDate(tranDate);
							sysTran.setTranType("PENAL_ACCRUAL_REVERSAL");
							sysTran.setTranAmount(sumOfDues * -1);

							sysTran.setDpd(maxDpd);
							sysTran.setRemark("Penal Accrual Reversal");
							sysTran.setUserId("SYSTEM");

							sysRepo.save(sysTran);

							List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
									.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanEqualOrderByInstallmentNo(
											"PENAL_ACCRUAL", loanDetail.getLoanId(), "N", tranDate);
							for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
								sysLoan.setAdjustedYn("Y");
								sysRepo.save(sysLoan);
							}
						}

						AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(mastAgrId);
						masterObj.setDpd(0);
						masterObj.setAssetClass("STD");
						masterRepo.save(masterObj);

						AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);

						loan.setDpd(0);
						loan.setAssetClass("STD");
						loanRepo.save(loan);

					}
					// }
				}

			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean interestAccrualReversalForeclosure(String mastAgrId, Date tranDate) {
		Boolean result = true;
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			Boolean odReversalRun = false;
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
			LocalDate date1 = LocalDate.parse(new SimpleDateFormat(dateFormat).format(tranDate), formatter);
			// date1 = date1.plusDays(1);
			List<AgrLoans> listLoanDetails = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			for (AgrLoans loanDetail : listLoanDetails) {

				Calendar c = Calendar.getInstance();
				c.setTime(tranDate);
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

				Calendar tmp = Calendar.getInstance();
				tmp.setTime(tranDate);

				if ((loanDetail.getLoanType() == "DL") || (loanDetail.getLoanType() == "ND")) {
					if (tmp.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
						odReversalRun = true;
					}
				}

				if (odReversalRun) {
					List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
							.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanEqualOrderByInstallmentNo(
									"INTEREST_ACCRUAL", loanDetail.getLoanId(), "N", tranDate);
					for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
						sysLoan.setAdjustedYn("X");
						sysRepo.save(sysLoan);
					}
				} else {
					// if ((loanDetail.getCycleDay() == date1.getDayOfMonth())) {

					Calendar backupDatePlus1Cal = Calendar.getInstance();
					backupDatePlus1Cal.setTime(tranDate);
					// backupDatePlus1Cal.add(Calendar.DAY_OF_MONTH, 1);

					Date backupDatePlus1Day = backupDatePlus1Cal.getTime();

					List<AgrTrnDueDetails> checkRowInDueTable = dueRepo.findByLoanIdAndDtDueDateAndDueCategory(
							loanDetail.getLoanId(), backupDatePlus1Day, "INSTALLMENT");
					// if (checkRowInDueTable.size() > 0) {
					Double sumOfIntAccrual = this.commonService.numberFormatter(sysRepo
							.getSumOfTranAmountOfPenal("INTEREST_ACCRUAL", loanDetail.getLoanId(), backupDatePlus1Day));
					if (sumOfIntAccrual != null) {
						if (sumOfIntAccrual > 0) {
							AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
							sysTran.setCustomerId(loanDetail.getCustomerId());
							sysTran.setMastAgrId(mastAgrId);
							sysTran.setLoanId(loanDetail.getLoanId());
							sysTran.setDtTranDate(backupDatePlus1Day);
							sysTran.setInstallmentNo(loanDetail.getCurrentInstallmentNo());
							sysTran.setTranType("INTEREST_ACCRUAL_REVERSAL");
							sysTran.setTranAmount(sumOfIntAccrual * -1);

							sysTran.setRemark("Interest Accrual Reversal");
							sysTran.setUserId("SYSTEM");

							sysRepo.save(sysTran);

							List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
									.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanEqualOrderByInstallmentNo(
											"INTEREST_ACCRUAL", loanDetail.getLoanId(), "N", backupDatePlus1Day);
							for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
								sysLoan.setAdjustedYn("Y");
								sysRepo.save(sysLoan);
							}
						}
					}
					// }
					// }
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return result;
	}

}
