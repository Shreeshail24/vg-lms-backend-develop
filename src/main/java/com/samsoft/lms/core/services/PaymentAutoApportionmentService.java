package com.samsoft.lms.core.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.batch.services.EODServices;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.dto.OdAmortDto;
import com.samsoft.lms.core.dto.OdAmortScheduleDto;
import com.samsoft.lms.core.dto.RuleDetailsDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProdSlabwiseInterest;
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
import com.samsoft.lms.core.repositories.AgrProdSlabwiseInterestRepository;
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
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;
import com.samsoft.lms.transaction.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentAutoApportionmentService {
	@Autowired
	private PaymentApplicationServices paymentService;
	@Autowired
	private AgrMasterAgreementRepository agrRepo;
	@Autowired
	private VMstPayRuleManagerRepository vMstMgrRepo;
	@Autowired
	TabMstPayRuleManagerDtlRepository ruleMdrDtlRepo;
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	@Autowired
	private VMstPayAppRuleSetRepository vRuleSetRepo;
	@Autowired
	private AgrTrnDueDetailsRepository dueDetailRepo;
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
	private CommonServices commonService;
	@Autowired
	private OdAmort odAmort;
	@Autowired
	private Environment env;
	@Autowired
	private CoreServices coreService;
	@Autowired
	private CoreAmort amortService;
	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;
	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;
	@Autowired
	private DisbursementService disbService;
	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;
	@Autowired
	private EODServices edoServ;
	@Autowired
	private PaymentReversalService paymentRevService;
	@Autowired
	private AgrCustomerRepository custRepo;
	@Autowired
	private ColenderDueService conlenderDueService;
	@Autowired
	private SupplierFinanceUtility suppUtility;
	@Autowired
	private AgrProdSlabwiseInterestRepository slabRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String agreementAutoApportionmentPaymentApplication(String mastAgrId, Integer instrumentId, Date tranDate)
			throws CoreDataNotFoundException, Exception {
		String result = "sucess";
		AgrLoans loans = null;
		log.info("Inside AutoApportion {}  {} {}", mastAgrId, instrumentId, tranDate);
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrRepaySchedule> scheduleList = new ArrayList<AgrRepaySchedule>();
		AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(mastAgrId, "B");
		double previousBpi = 0d;
		double nextInstallmentAmount = 0d;
		try {

			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(mastAgrId);

			if (masterAgrObj == null) {
				throw new CoreDataNotFoundException("Master Agreement not found " + mastAgrId);
			}

			log.info("Inside try");
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);

			log.info("limitSetup ");
			TrnInsInstrument instrument = instRepo.findByMasterAgrAndInstrumentIdAndInstrumentStatus(mastAgrId,
					instrumentId, "CLR");

			log.info("instrument");

			if (instrument == null) {
				throw new CoreDataNotFoundException("No instrument found for payment application. ");
			}

			String paymentFor = null;

			log.info("instrument 2");
			List<TabOrganization> orgData = orgRepo.findAll();
			log.info("instrument 3");
			Date businessDate = null;

			if (orgData.size() <= 0) {
				throw new CoreDataNotFoundException("No Organization details available");
			}

			for (TabOrganization org : orgData) {
				businessDate = org.getDtBusiness();
			}
			log.info(" Paytype " + instrument.getPayType());
			switch (instrument.getPayType()) {
			case "INSTALLMENT":
				paymentFor = "INSTALLMENT";
				break;

			case "CHARGES":
				paymentFor = "FEE";
				break;

			case "DEBIT_NOTE":
				paymentFor = "DEBIT_NOTE";
				break;

			case "EXCESS_ADJ":
				paymentFor = "ALL_DUES";
				break;

			case "ALLDUES":
				paymentFor = "ALL_DUES";
				break;

			case "SETTLEMENT":
				paymentFor = "ALL_DUES";
				break;
			}

			AgrTrnTranHeader tranHdr = new AgrTrnTranHeader();
			tranHdr.setMasterAgr(agrRepo.findByMastAgrId(mastAgrId));
			tranHdr.setTranDate(tranDate);
			tranHdr.setTranType("RECEIPT");
			log.info(" INSTRUMENT_TYPES ");
			tranHdr.setRemark(
					commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", instrument.getInstrumentType())
							+ " Receipt");
			log.info(" INSTRUMENT_TYPES 2 ");
			tranHdr.setSource(instrument.getSource());
			tranHdr.setReqId(instrument.getSourceId());
			tranHdr.setIntrumentId(instrumentId);
			tranHdr.setUserID(instrument.getUserId());
			log.info(" INSTRUMENT_TYPES 3 ");
			if (limitSetup != null) {
				tranHdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}
			log.info(" INSTRUMENT_TYPES 4 ");
			loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			String loanType = loans.getLoanType();
			log.info("loanType " + loanType);

			AgrProdSlabwiseInterest slabData = slabRepo.findFirstByMasterAgrIdOrderByIntSlabIdDesc(mastAgrId);
			Double addIntAccrued = commonService.getInterestAccruedTillDateByTranType(mastAgrId,
					"ADD_INTEREST_ACCRUAL");

			Double addPenalAccrued = commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL");

			Integer maxDpd = loans.getDpd();
			Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(mastAgrId).getGraceDays();
			if (maxDpd == null) {
				maxDpd = 0;
			}
			if (graceDays == null) {
				graceDays = 0;
			}
			if (maxDpd > graceDays) {

				if (loanType.equals("OD") && slabData != null) {
					if (addIntAccrued > 0) {
						AgrTrnEventDtl slabEventDtl = new AgrTrnEventDtl();
						slabEventDtl.setTranHeader(tranHdr);
						slabEventDtl.setTranEvent("ADD_INTEREST_BOOKING");
						slabEventDtl.setTranAmount(commonService.numberFormatter(addIntAccrued));
						slabEventDtl.setUserId(instrument.getUserId());

						hdrRepo.save(tranHdr);
						eventRepo.save(slabEventDtl);

						AgrTrnTranDetail tranDtlAddInt = new AgrTrnTranDetail();
						tranDtlAddInt.setEventDtl(slabEventDtl);
						tranDtlAddInt.setMasterAgr(masterAgrObj);
						tranDtlAddInt.setTranCategory("ADD_INSTALLMENT");
						tranDtlAddInt.setTranHead("ADD_INTEREST");
						tranDtlAddInt.setDtlRemark("Additional Interest Dues");
						tranDtlAddInt.setTranSide("DR");
						tranDtlAddInt.setInstallmentNo(0);
						tranDtlAddInt.setTranAmount(addIntAccrued);
						tranDtlAddInt.setDtDueDate(tranDate);

						tranDtlAddInt.setAvailableLimit(tranDtlAddInt.getAvailableLimit() - addIntAccrued);
						tranDtlAddInt.setUtilizedLimit(tranDtlAddInt.getUtilizedLimit() + addIntAccrued);

						AgrTrnTranDetail tranAddInt = tranDtlRepo.save(tranDtlAddInt);

						AgrTrnDueDetails dueAddInt = new AgrTrnDueDetails();
						dueAddInt.setDtDueDate(tranDate);
						dueAddInt.setDueAmount(addIntAccrued);
						dueAddInt.setDueCategory("ADD_INSTALLMENT");
						dueAddInt.setDueHead("ADD_INTEREST");
						dueAddInt.setInstallmentNo(0);
						dueAddInt.setLoanId(loans.getLoanId());
						dueAddInt.setMastAgrId(mastAgrId);
						dueAddInt.setTranDtlId(tranAddInt.getTranDtlId());

						dueDetailRepo.save(dueAddInt);

						// Supplier Finance Changes Start

						suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
								masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
								product.getProdCode(), "DED", addIntAccrued, tranHdr.getTranId(),
								instrument.getUserId(), tranDate);

						// Supplier Finance Changes End

						List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(mastAgrId, "N",
								"ADD_INTEREST_ACCRUAL");
						List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
						for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
							agrTrnSysTranDtl.setAdjustedYn("Y");
							saveSysList.add(agrTrnSysTranDtl);
						}

						sysRepo.saveAll(saveSysList);

					}

					if (addPenalAccrued > 0) {
						AgrTrnEventDtl penalEventDtl = new AgrTrnEventDtl();
						penalEventDtl.setTranHeader(tranHdr);
						penalEventDtl.setTranEvent("CHARGES_BOOKING");
						penalEventDtl.setTranAmount(commonService.numberFormatter(addPenalAccrued));
						penalEventDtl.setUserId(instrument.getUserId());

						hdrRepo.save(tranHdr);
						eventRepo.save(penalEventDtl);

						AgrTrnTranDetail tranDtlPenal = new AgrTrnTranDetail();
						tranDtlPenal.setEventDtl(penalEventDtl);
						tranDtlPenal.setMasterAgr(masterAgrObj);
						tranDtlPenal.setTranCategory("FEE");
						tranDtlPenal.setTranHead("PENAL");
						tranDtlPenal.setDtlRemark("Penal Dues");
						tranDtlPenal.setTranSide("DR");
						tranDtlPenal.setInstallmentNo(0);
						tranDtlPenal.setTranAmount(addPenalAccrued);
						tranDtlPenal.setDtDueDate(tranDate);

						tranDtlPenal.setAvailableLimit(tranDtlPenal.getAvailableLimit() - addPenalAccrued);
						tranDtlPenal.setUtilizedLimit(tranDtlPenal.getUtilizedLimit() + addPenalAccrued);

						AgrTrnTranDetail tranAddInt = tranDtlRepo.save(tranDtlPenal);

						AgrTrnDueDetails duePenal = new AgrTrnDueDetails();
						duePenal.setDtDueDate(tranDate);
						duePenal.setDueAmount(addPenalAccrued);
						duePenal.setDueCategory("FEE");
						duePenal.setDueHead("PENAL");
						duePenal.setInstallmentNo(0);
						duePenal.setLoanId(loans.getLoanId());
						duePenal.setMastAgrId(mastAgrId);
						duePenal.setTranDtlId(tranAddInt.getTranDtlId());

						dueDetailRepo.save(duePenal);

						// Supplier Finance Changes Start

						suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
								masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
								product.getProdCode(), "DED", addPenalAccrued, tranHdr.getTranId(),
								instrument.getUserId(), tranDate);

						// Supplier Finance Changes End

						List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(mastAgrId, "N",
								"PENAL_ACCRUAL");
						List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
						for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
							agrTrnSysTranDtl.setAdjustedYn("Y");
							saveSysList.add(agrTrnSysTranDtl);
						}

						sysRepo.saveAll(saveSysList);

					}
				}

			}

			AgrTrnEventDtl eventDtl = new AgrTrnEventDtl();
			eventDtl.setTranHeader(tranHdr);
			eventDtl.setTranEvent(instrument.getPayType());
			eventDtl.setTranAmount(commonService.numberFormatter(instrument.getInstrumentAmount()));
			eventDtl.setUserId(instrument.getUserId());

			Double amount = 0.0;
			Double amountTds = 0.0;
			Double rowAmount = 0d;
			Double totalTax = 0d;
			Double totalFee = 0d;
			Double instrumentAmount = commonService.numberFormatter(instrument.getInstrumentAmount());
			Double totalAmount = commonService.numberFormatter(instrument.getInstrumentAmount());
			Double totalAmountTds = 0d;
			Double excessPaymentAmount = 0d;
			// OD Excess payment to be applied to unbilled principal

			if (loanType.equalsIgnoreCase("DL") || loanType.equalsIgnoreCase("ND")) {
				Double masterTotalDues = commonService.getMasterTotalDues(mastAgrId);
				log.info("masterTotalDues " + masterTotalDues);
				excessPaymentAmount = commonService.numberFormatter(instrumentAmount - masterTotalDues);
				log.info("excessPaymentAmount " + excessPaymentAmount);
				if (excessPaymentAmount <= 0) {
					excessPaymentAmount = 0d;
				} else {
					{
						if (masterAgrObj.getUnbilledPrincipal() > 0) {

							excessPaymentAmount = Math.min(excessPaymentAmount, masterAgrObj.getUnbilledPrincipal());

							AgrTrnEventDtl eventDtlExcess = new AgrTrnEventDtl();
							eventDtlExcess.setTranHeader(tranHdr);
							eventDtlExcess.setTranEvent("PREPAYMENT_RECEIVABLE");
							eventDtlExcess.setTranAmount(excessPaymentAmount);
							eventDtlExcess.setUserId(instrument.getUserId());

							AgrTrnTranDetail tranDtlExccess = new AgrTrnTranDetail();
							tranDtlExccess.setEventDtl(eventDtlExcess);
							tranDtlExccess.setMasterAgr(masterAgrObj);
							tranDtlExccess.setTranCategory("UNBILLED_PRINCIPAL");
							tranDtlExccess.setTranHead("FUTURE_PRINCIPAL");
							tranDtlExccess.setDtlRemark("Future Principal Dues");
							tranDtlExccess.setTranSide("DR");
							tranDtlExccess.setInstallmentNo(0);
							tranDtlExccess.setTranAmount(excessPaymentAmount);
							tranDtlExccess.setDtDueDate(tranDate);

							tranDtlExccess.setAvailableLimit(limitSetup.getAvailableLimit());
							tranDtlExccess.setUtilizedLimit(limitSetup.getUtilizedLimit());

							AgrTrnTranDetail tranExcess = tranDtlRepo.save(tranDtlExccess);

							AgrTrnDueDetails dueExcess = new AgrTrnDueDetails();
							dueExcess.setDtDueDate(tranDate);
							dueExcess.setDueAmount(excessPaymentAmount);
							dueExcess.setDueCategory("UNBILLED_PRINCIPAL");
							dueExcess.setDueHead("FUTURE_PRINCIPAL");
							dueExcess.setInstallmentNo(0);
							dueExcess.setLoanId(loans.getLoanId());
							dueExcess.setMastAgrId(mastAgrId);
							dueExcess.setTranDtlId(tranExcess.getTranDtlId());

							dueDetailRepo.save(dueExcess);

							ColenderDueDto colenderDto = new ColenderDueDto();
							colenderDto.setDtDueDate(tranDate);
							colenderDto.setDueAmount(excessPaymentAmount);
							colenderDto.setDueCategory("UNBILLED_PRINCIPAL");
							colenderDto.setDueHead("FUTURE_PRINCIPAL");
							colenderDto.setInstallmentNo(0);
							colenderDto.setLoanId(loans.getLoanId());
							colenderDto.setMastAgrId(mastAgrId);
							colenderDto.setTranDtlId(tranExcess.getTranDtlId());
							colenderDto.setUserId(instrument.getUserId());
							conlenderDueService.generateColenderDues(colenderDto);

							masterAgrObj
									.setUnbilledPrincipal(masterAgrObj.getUnbilledPrincipal() - excessPaymentAmount);
							loans.setUnbilledPrincipal(loans.getUnbilledPrincipal() - excessPaymentAmount);

							int currentInstallmentNo = loanRepo.findByLoanId(loans.getLoanId())
									.getCurrentInstallmentNo();
							AgrRepaySchedule previousBpiScheduleRow = scheduleRepo
									.findByMasterAgrIdAndInstallmentNo(mastAgrId, currentInstallmentNo);
							previousBpi = previousBpiScheduleRow.getBpiAmount();

							if (masterAgrObj.getUnbilledPrincipal() <= 0) {
								scheduleRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(mastAgrId, businessDate);

								double bpi = commonService.getInterestAccruedTillDateByTranType(mastAgrId,
										"INTEREST_ACCRUAL") + previousBpi;
								if (bpi > 0) {

									AgrRepaySchedule bpiRow = new AgrRepaySchedule();
									bpiRow.setBpiAmount(0d);
									bpiRow.setClosingPrincipal(0d);
									bpiRow.setDtInstallment(masterAgrObj.getDtNextInstallment());
									bpiRow.setInstallmentAmount(bpi);
									bpiRow.setInstallmentNo(loans.getCurrentInstallmentNo());
									bpiRow.setInterestAmount(bpi);
									// bpiRow.setInterestBasis(interestBasis);
									bpiRow.setInterestRate(masterAgrObj.getInterestRate());
									bpiRow.setLoanId(loans.getLoanId());
									bpiRow.setMasterAgrId(masterAgrObj.getMastAgrId());
									bpiRow.setOpeningPrincipal(0d);
									bpiRow.setPrincipalAmount(0d);
									bpiRow.setTdsAmount(commonService.numberFormatter(
											bpiRow.getInterestAmount() * (masterAgrObj.getTdsRate() / 100)));

									scheduleRepo.save(bpiRow);

									List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(
											mastAgrId, "N", "INTEREST_ACCRUAL");
									List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
									for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
										agrTrnSysTranDtl.setAdjustedYn("Y");
										saveSysList.add(agrTrnSysTranDtl);
									}

									sysRepo.saveAll(saveSysList);

								}
								Integer maxSeqNo = repayHistRepo.getMaxSeqNo(mastAgrId, tranDate);
								if (maxSeqNo == null) {
									maxSeqNo = 0;
								}
								maxSeqNo++;

								List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
								List<TrnInsInstrument> insList = instRepo
										.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, businessDate);
								for (TrnInsInstrument inst : insList) {
									TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
									BeanUtils.copyProperties(inst, insHist);
									insHist.setSeqNo(maxSeqNo);
									insHistList.add(insHist);
								}

								insHistRepo.saveAll(insHistList);

								instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, businessDate);

								masterAgrObj.setDtNextInstallment(masterAgrObj.getDtNextInstallment());
								masterAgrObj.setNextInstallmentAmount(bpi);

								disbService.postEcsInstruments(mastAgrId);

							} else {

								AgrRepaySchedule NextInstallmentRowPrin = scheduleRepo
										.findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndPrincipalAmountGreaterThanOrderByInstallmentNoAsc(
												mastAgrId, tranDate, 0d);

								AgrRepaySchedule NextInstallmentRowInt = scheduleRepo
										.findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndInterestAmountGreaterThanOrderByInstallmentNoAsc(
												mastAgrId, tranDate, 0d);

								String prinCycleDate = sdf.format(NextInstallmentRowPrin.getDtInstallment())
										.split("-")[0];
								Date tmpDatePrin = null;
								if (product.getDropLineODYN().equals("Y")) {

									tmpDatePrin = sdf.parse(amortService.getNextInstallmentDate(sdf.format(tranDate),
											product.getDropLIneFreq()));

									if (product.getDropLineCycleDay() == 0) {

										Calendar calendarPrin = GregorianCalendar.getInstance();
										calendarPrin.setTime(tmpDatePrin);
										// calendar.add(Calendar.MONTH, 1);
										calendarPrin.set(Calendar.DATE,
												calendarPrin.getActualMinimum(Calendar.DAY_OF_MONTH));
										calendarPrin.add(Calendar.DAY_OF_MONTH, -1);
										tmpDatePrin = calendarPrin.getTime();

									} else {

										Calendar calendarPrin = GregorianCalendar.getInstance();
										calendarPrin.setTime(tmpDatePrin);
										// calendarPrin.add(Calendar.MONTH, 1);
										calendarPrin.set(calendarPrin.get(Calendar.YEAR),
												calendarPrin.get(Calendar.MONTH), product.getDropLineCycleDay());

										tmpDatePrin = calendarPrin.getTime();

									}
								} else {
									tmpDatePrin = NextInstallmentRowPrin.getDtInstallment();
								}

								List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();
								List<AgrRepaySchedule> repayList = scheduleRepo
										.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId,
												businessDate);

								Date nextInstallmentDate = repayList.get(0).getDtInstallment();
								Integer installmentNo = repayList.get(0).getInstallmentNo();

								OdAmortDto odDto = new OdAmortDto();
								odDto.setDtLimitSanctioned(sdf.format(limitSetup.getDtLimitSanctioned()));
								odDto.setDtLimitExpired(sdf.format(limitSetup.getDtLimitExpired()));
								odDto.setSanctionedAmount(limitSetup.getLimitSanctionAmount());
								odDto.setDtDrawdown(sdf.format(tranDate));
								if (masterTotalDues > 0) {
									odDto.setDrawDownAmount(masterAgrObj.getUnbilledPrincipal() - excessPaymentAmount);
								} else {
									odDto.setDrawDownAmount(masterAgrObj.getUnbilledPrincipal());
								}

								odDto.setInterestPaymentCycleDay(masterAgrObj.getCycleDay());
								odDto.setPrincipalPaymentCycleDay(Integer.parseInt(prinCycleDate));
								odDto.setInterestRate(loans.getInterestRate());
								odDto.setInterestBasis(product.getInterestBasis());
								odDto.setInterestRepaymentFrequency(loans.getRepayFreq());
								odDto.setEmiRounding(product.getEmiRounding());
								odDto.setBpiAmount(commonService.getInterestAccruedTillDateByTranType(mastAgrId,
										"INTEREST_ACCRUAL") + previousBpi);

								List<AgrTrnSysTranDtl> sysList = sysRepo
										.findByMastAgrIdAndAdjustedYnAndTranType(mastAgrId, "N", "INTEREST_ACCRUAL");
								List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
								for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
									agrTrnSysTranDtl.setAdjustedYn("X");
									saveSysList.add(agrTrnSysTranDtl);
								}
								sysRepo.saveAll(saveSysList);

								odDto.setDropLineODYN(product.getDropLineODYN());
								odDto.setDropLineMode(product.getDropMode());
								odDto.setDropLineAmount(product.getDropLineAmount());
								odDto.setDropLinePerc(product.getDropLinePerc());
								odDto.setDropLineFreq(product.getDropLIneFreq());
								odDto.setDtPrinStart(sdf.format(tmpDatePrin));
								odDto.setDtInterestStart(sdf.format(NextInstallmentRowInt.getDtInstallment()));

								List<OdAmortScheduleDto> odAmortList = odAmort.getOdAmort(odDto);

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
								List<TrnInsInstrument> insList = instRepo
										.findByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, businessDate);
								for (TrnInsInstrument inst : insList) {
									TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
									BeanUtils.copyProperties(inst, insHist);
									insHist.setSeqNo(maxSeqNo);
									insHistList.add(insHist);
								}

								insHistRepo.saveAll(insHistList);

								instRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(mastAgrId, businessDate);
								if (odAmortList.get(0).getInstallmentNo() == 1) {
									nextInstallmentAmount = odAmortList.get(0).getInstallmentAmount();
								}

								for (OdAmortScheduleDto amort : odAmortList) {

									AgrRepaySchedule repay = new AgrRepaySchedule();
									repay.setLoanId(loans.getLoanId());
									repay.setMasterAgrId(mastAgrId);
									repay.setInstallmentNo(installmentNo++);
									repay.setDtInstallment(sdf.parse(amort.getInstallmentDate()));
									repay.setOpeningPrincipal(amort.getOpeningBalance());
									repay.setPrincipalAmount(amort.getPrincipalAmount());
									repay.setInterestAmount(amort.getInterestAmount());
									repay.setBpiAmount(amort.getBpiAmount());
									repay.setInstallmentAmount(amort.getInstallmentAmount());
									repay.setClosingPrincipal(amort.getClosingBalance());
									repay.setInterestRate(amort.getInterestRate());
									repay.setInterestBasis(product.getInterestBasis());
									repay.setUserId(instrument.getUserId());
									repay.setTdsAmount(commonService.numberFormatter(
											repay.getInterestAmount() * (masterAgrObj.getTdsRate() / 100)));
									scheduleRepo.save(repay);

								}

								masterAgrObj.setDtNextInstallment(nextInstallmentDate);
								masterAgrObj.setNextInstallmentAmount(nextInstallmentAmount);

								disbService.postEcsInstruments(mastAgrId);
							}

							paymentFor = "ALL_DUES";
						}
					}
				}

			}

			if (instrument.getTdsAmount() != null && instrument.getTdsAmount() > 0) {
				log.info("Tds Amount" + instrument.getTdsAmount());

				totalAmountTds = instrument.getTdsAmount();

				String paymentRuleTds = "TDS_RULE";

				log.info("paymentRule TDS " + paymentRuleTds);
				List<RuleDetailsDto> ruleDetailsTds = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRuleTds,
						paymentFor);

				AgrTrnEventDtl eventDtlTds = new AgrTrnEventDtl();
				eventDtlTds.setTranHeader(tranHdr);
				eventDtlTds.setTranEvent("TDS_RECEIPT");
				eventDtlTds.setTranAmount(commonService.numberFormatter(instrument.getTdsAmount()));
				eventDtlTds.setUserId(instrument.getUserId());

				for (RuleDetailsDto ruleDtoTds : ruleDetailsTds) {

					if (commonService.numberFormatter(ruleDtoTds.getDueAmount()) > totalAmountTds) {
						amountTds = totalAmountTds;
					} else {
						amountTds = commonService.numberFormatter(ruleDtoTds.getDueAmount());
					}
					if (totalAmountTds > 0) {
						AgrTrnTranDetail tranDtlTds = new AgrTrnTranDetail();
						tranDtlTds.setEventDtl(eventDtlTds);
						tranDtlTds.setMasterAgr(masterAgrObj);
						tranDtlTds.setTranCategory(ruleDtoTds.getDueCategory());
						tranDtlTds.setTranHead(ruleDtoTds.getPayHead());
						tranDtlTds.setTranAmount(amountTds * (-1));
						tranDtlTds.setTranSide("CR");
						tranDtlTds.setLoan(loanRepo.findByLoanId(ruleDtoTds.getLoanId()));
						tranDtlTds.setInstallmentNo(ruleDtoTds.getInstallmentNo());
						tranDtlTds.setDtlRemark("TDS recovered");
						tranDtlTds.setDtDueDate(ruleDtoTds.getDueDate());

						log.info("Before dueDtl Tds");
						AgrTrnDueDetails dueDtlIdTds = dueDetailRepo.findByDueDtlId(ruleDtoTds.getDueDtlId());
						log.info("After dueDtl Tds");
						dueDtlIdTds.setDueAmount(commonService.numberFormatter(dueDtlIdTds.getDueAmount() - amountTds));
						dueDetailRepo.save(dueDtlIdTds);

						totalAmountTds = totalAmountTds - amountTds;

						if (limitSetup != null) {
							tranDtlTds.setAvailableLimit(
									commonService.numberFormatter(limitSetup.getAvailableLimit() + amountTds));
							tranDtlTds.setUtilizedLimit(
									commonService.numberFormatter(limitSetup.getUtilizedLimit() - amountTds));
							log.info("Update Limit before ");
							paymentService.updateLimit(mastAgrId, amountTds, "ADD", "RECEIPT", tranHdr.getTranId(),
									"PAYAPP");

							log.info("Update Limit After ");
						}

						// Supplier Finance Changes Start
						if (!instrument.getPayType().equals("EXCESS_ADJ")) {
							if (loans.getLoanType().equalsIgnoreCase("OD")) {
								suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
										masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
										product.getProdCode(), "ADD", amountTds, tranHdr.getTranId(),
										instrument.getUserId(), tranDate);

							}
						}
						// Supplier Finance Changes End
						tranDtlRepo.save(tranDtlTds);
					}
				}

				if (totalAmountTds > 0) {

					log.info("Excess Call " + totalAmountTds);
					AgrTrnTranDetail tranDtl2Tds = new AgrTrnTranDetail();
					tranDtl2Tds.setEventDtl(eventDtlTds);
					tranDtl2Tds.setMasterAgr(masterAgrObj);
					tranDtl2Tds.setTranCategory("EXCESS");
					tranDtl2Tds.setTranHead("TDS_EXCESS_AMOUNT");
					tranDtl2Tds.setTranAmount(totalAmountTds * -1);
					tranDtl2Tds.setTranSide("CR");
					tranDtl2Tds.setDtlRemark("Tds Excess amount received");
					tranDtl2Tds.setDtDueDate(tranDate);
					if (limitSetup != null) {
						tranDtl2Tds.setAvailableLimit(
								commonService.numberFormatter(limitSetup.getAvailableLimit() + totalAmountTds));
						tranDtl2Tds.setUtilizedLimit(
								commonService.numberFormatter(limitSetup.getUtilizedLimit() - totalAmountTds));
					}
					log.info("Before Update Tds Excess");
					paymentService.updateTdsExcess(mastAgrId, totalAmountTds, "ADD");
					log.info("After Update Tds Excess");
					if (limitSetup != null) {
						log.info("Before Update Limit");
						paymentService.updateLimit(mastAgrId, totalAmountTds, "ADD", "TDS_EXCESS_REC",
								tranHdr.getTranId(), "PAYAPP");
						log.info("After Update Limit");

					}

					// Supplier Finance Changes Start
					if (!instrument.getPayType().equals("EXCESS_ADJ")) {
						if (loans.getLoanType().equalsIgnoreCase("OD")) {
							suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
									masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
									product.getProdCode(), "ADD", totalAmountTds, tranHdr.getTranId(), "TDS_EXCESS_REC",
									tranDate);

						}
					}
					// Supplier Finance Changes End

					totalAmountTds = 0.0;
					// eventRepo.save(eventDtl);
					tranDtlRepo.save(tranDtl2Tds);

				}

				dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			}

			log.info("paymentFor " + paymentFor);
			String paymentRule = paymentService.getPaymentRule(mastAgrId, paymentFor);
			log.info("paymentRule " + paymentRule);
			List<RuleDetailsDto> ruleDetails = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRule, paymentFor);

			log.info("ruleDetails" + ruleDetails.size());

			/*
			 * if (ruleDetails.size() <= 0) { throw new
			 * CoreDataNotFoundException("Rule details not found "); }
			 */

			log.info("totalAmount" + totalAmount);
			for (RuleDetailsDto ruleDto : ruleDetails) {
				if (ruleDto.getDueCategory().equalsIgnoreCase("FEE")) {
					if (instrumentAmount > 0) {
						Boolean add = false;
						String onlyTax = null;
						rowAmount = totalAmount;
						Double chargeBreakUp = 0.0;
						Double totalApportTax = 0.0;
						if (commonService.numberFormatter(ruleDto.getDueAmount()) > totalAmount) {
							amount = totalAmount;
							add = true;
						} else {
							amount = commonService.numberFormatter(ruleDto.getDueAmount());
							add = false;
						}

						if (ruleDto.getDueCategory().equalsIgnoreCase("FEE") && (ruleDto.getDueAmount() == 0)) {
							onlyTax = "Y";
						} else {
							onlyTax = "N";
						}

						if (rowAmount > 0 && onlyTax.equals("N")) {
							if (amount > 0) {
								AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
								tranDtl.setEventDtl(eventDtl);
								tranDtl.setMasterAgr(masterAgrObj);
								tranDtl.setTranCategory(ruleDto.getDueCategory());
								tranDtl.setTranHead(ruleDto.getPayHead());
								tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
								tranDtl.setTranSide("CR");
								tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
								tranDtl.setDtDueDate(ruleDto.getDueDate());
								tranDtl = tranDtlRepo.save(tranDtl);
								List<AgrTrnTaxDueDetails> taxDueList = new ArrayList<AgrTrnTaxDueDetails>();
								if (ruleDto.getDueCategory().equals("FEE")) {

									taxDueList = taxRepo.findByDueDetailDueDtlId(ruleDto.getDueDtlId());
									for (AgrTrnTaxDueDetails taxDue : taxDueList) {
										totalTax += taxDue.getDueTaxAmount();
									}

									totalFee = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId()).getDueAmount();
									if (totalAmount >= (totalTax + totalFee)) {
										amount = totalTax + totalFee;
									} else {
										amount = amount;
									}
									Double taxBreakUpAmount = commonService
											.numberFormatter(amount * totalTax / (totalTax + totalFee));
									chargeBreakUp = commonService.numberFormatter(amount - taxBreakUpAmount);

									Double taxAmount = 0d;
									Double remTaxAmount = totalTax;
									for (AgrTrnTaxDueDetails taxDue : taxDueList) {
										taxAmount = commonService.numberFormatter(
												taxDue.getDueTaxAmount() * taxBreakUpAmount / totalTax);
										if (taxAmount > 0) {
											AgrTrnTranDetail tranDtlTax = new AgrTrnTranDetail();
											tranDtlTax.setEventDtl(eventDtl);
											tranDtlTax.setMasterAgr(masterAgrObj);
											tranDtlTax.setTranCategory("TAX");
											tranDtlTax.setTranHead(taxDue.getTaxHead());
											tranDtlTax.setTranAmount(taxAmount * (-1));
											tranDtlTax.setTranSide("CR");
											tranDtlTax.setDtlRemark(taxDue.getTaxHead() + " recovered");
											tranDtlTax.setInstallmentNo(-1);
											tranDtlTax.setDtDueDate(ruleDto.getDueDate());
											tranDtlTax.setRefTranDtlId(tranDtl.getTranDtlId());
											tranDtlRepo.save(tranDtlTax);

											AgrTrnTaxDueDetails taxDueUpdate = taxRepo
													.findByTaxDueId(taxDue.getTaxDueId());
											remTaxAmount = taxAmount - taxDue.getDueTaxAmount();
											taxDueUpdate.setDueTaxAmount(commonService
													.numberFormatter(taxDueUpdate.getDueTaxAmount() - taxAmount));

											taxRepo.save(taxDueUpdate);
											instrumentAmount = commonService
													.numberFormatter(instrumentAmount - taxAmount);

											if (limitSetup != null) {
												tranDtl.setAvailableLimit(commonService
														.numberFormatter(limitSetup.getAvailableLimit() + taxAmount));
												tranDtl.setUtilizedLimit(commonService
														.numberFormatter(limitSetup.getUtilizedLimit() - taxAmount));
												log.info("Update Limit before ");
												paymentService.updateLimit(mastAgrId, taxAmount, "ADD", "RECEIPT",
														tranHdr.getTranId(), "PAYAPP");

												log.info("Update Limit After ");
											}

											// Supplier Finance Changes Start
											if (!instrument.getPayType().equals("EXCESS_ADJ")) {
												if (loans.getLoanType().equalsIgnoreCase("OD")) {
													suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
															masterAgrObj.getOriginationApplnNo(),
															masterAgrObj.getCustomerId(), product.getProdCode(), "ADD",
															taxAmount, tranHdr.getTranId(), instrument.getUserId(),
															tranDate);

												}
											}
											// Supplier Finance Changes End

										}

									}

									totalAmount = commonService.numberFormatter(totalAmount - (amount));

								}

								AgrTrnDueDetails dueDtlId = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId());

								dueDtlId.setDueAmount(
										commonService.numberFormatter(dueDtlId.getDueAmount() - (chargeBreakUp)));
								tranDtl.setTranAmount(commonService.numberFormatter(chargeBreakUp) * (-1));

								dueDetailRepo.save(dueDtlId);

								if (limitSetup != null) {
									tranDtl.setAvailableLimit(
											commonService.numberFormatter(limitSetup.getAvailableLimit()
													+ commonService.numberFormatter(chargeBreakUp)));
									tranDtl.setUtilizedLimit(commonService.numberFormatter(limitSetup.getUtilizedLimit()
											- commonService.numberFormatter(chargeBreakUp)));
									log.info("Update Limit before ");
									paymentService.updateLimit(mastAgrId, commonService.numberFormatter(chargeBreakUp),
											"ADD", "RECEIPT", tranHdr.getTranId(), "PAYAPP");

									log.info("Update Limit After ");
								}

								// Supplier Finance Changes Start
								if (!instrument.getPayType().equals("EXCESS_ADJ")) {
									if (loans.getLoanType().equalsIgnoreCase("OD")) {
										suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
												masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
												product.getProdCode(), "ADD",
												commonService.numberFormatter(chargeBreakUp), tranHdr.getTranId(),
												instrument.getUserId(), tranDate);

									}
								}
								// Supplier Finance Changes End

								rowAmount = commonService.numberFormatter(totalAmount);
								// tranDtl.setTranAmount(numberFormatter(totalAmount-totalApportTax)*(-1));
								instrumentAmount = commonService.numberFormatter(instrumentAmount - amount);
								tranDtlRepo.save(tranDtl);
								taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(ruleDto.getDueDtlId(),
										0d);

							}

						}
					}
					totalAmount = rowAmount;
					dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
					hdrRepo.save(tranHdr);
					eventRepo.save(eventDtl);

				} else {
					totalAmount = commonService.numberFormatter(instrument.getInstrumentAmount());
					log.info("totalAmount 1 " + totalAmount);
					// for (RuleDetailsDto ruleDto : ruleDetails) {
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
							tranDtl.setEventDtl(eventDtl);
							tranDtl.setMasterAgr(masterAgrObj);
							tranDtl.setTranCategory(ruleDto.getDueCategory());
							tranDtl.setTranHead(ruleDto.getPayHead());
							tranDtl.setTranAmount(amount * (-1));
							tranDtl.setTranSide("CR");
							tranDtl.setLoan(loanRepo.findByLoanId(ruleDto.getLoanId()));
							tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
							tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
							tranDtl.setDtDueDate(ruleDto.getDueDate());

							log.info("tranDtl Saved  ");
							if (limitSetup != null) {
								tranDtl.setAvailableLimit(
										commonService.numberFormatter(limitSetup.getAvailableLimit() + amount));
								tranDtl.setUtilizedLimit(
										commonService.numberFormatter(limitSetup.getUtilizedLimit() - amount));
								log.info("Update Limit before ");
								paymentService.updateLimit(mastAgrId, amount, "ADD", "RECEIPT", tranHdr.getTranId(),
										"PAYAPP");

								log.info("Update Limit After ");
							}

							// Supplier Finance Changes Start
							if (!instrument.getPayType().equals("EXCESS_ADJ")) {
								if (loans.getLoanType().equalsIgnoreCase("OD")) {
									suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
											masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
											product.getProdCode(), "ADD", amount, tranHdr.getTranId(),
											instrument.getUserId(), tranDate);

								}
							}
							// Supplier Finance Changes End

							log.info("Before dueDtl");
							AgrTrnDueDetails dueDtlId = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId());
							log.info("After dueDtl");
							dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
							instrumentAmount = commonService.numberFormatter(instrumentAmount - amount);
							dueDetailRepo.save(dueDtlId);
							taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(ruleDto.getDueDtlId(), 0d);
							if (ruleDto.getDueCategory().equals("INSTALLMENT")) {
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

							log.info("before totalAmount");
							totalAmount = totalAmount - amount;
							tranDtlRepo.save(tranDtl);

						} else {
							break;
						}
					}
					// }
				}
			}

			if (ruleDetails.size() == 0) {
				instrumentAmount = instrument.getInstrumentAmount();
			}

			if (instrumentAmount > 0) {
				log.info("Excess Call " + instrumentAmount);
				AgrTrnTranDetail tranDtl2 = new AgrTrnTranDetail();
				tranDtl2.setEventDtl(eventDtl);
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
				log.info("Before Update Excess");
				paymentService.updateExcess(mastAgrId, instrumentAmount, "ADD");
				log.info("After Update Excess");
				if (limitSetup != null) {
					log.info("Before Update Limit");
					paymentService.updateLimit(mastAgrId, instrumentAmount, "ADD", "EXCESS_REC", tranHdr.getTranId(),
							"PAYAPP");
					log.info("After Update Limit");

				}

				// Supplier Finance Changes Start
				if (!instrument.getPayType().equals("EXCESS_ADJ")) {
					if (loans.getLoanType().equalsIgnoreCase("OD")) {
						suppUtility.updateCustomerLimit(masterAgrObj.getMastAgrId(),
								masterAgrObj.getOriginationApplnNo(), masterAgrObj.getCustomerId(),
								product.getProdCode(), "ADD", instrumentAmount, tranHdr.getTranId(), "EXCESS_REC",
								tranDate);

					}
				}
				// Supplier Finance Changes End

				instrumentAmount = 0.0;
				// eventRepo.save(eventDtl);
				tranDtlRepo.save(tranDtl2);
			}
			dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			log.info("Before excessAdj");
			// Boolean excessAdj = edoServ.excessAdjustmentApi(masterAgrObj.getCustomerId(),
			// businessDate);
			// log.info("excessAdj " + excessAdj);

			loanRepo.save(loans);
			dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);

			List<AgrMasterAgreement> customerList = agrRepo.findAllByCustomerId(customer.getCustomerId());

			double totalDues = 0.0;
			for (AgrMasterAgreement cust : customerList) {
				totalDues += commonService.getMasterTotalDues(cust.getMastAgrId());
			}

			if (totalDues <= 0) {
				customer.setLimitFreezYn("N");
				custRepo.save(customer);
			}
			Integer dueDtlCount = dueDetailRepo.findByMastAgrIdOrderByDtDueDate(mastAgrId).size();
			if (loanType.equals("OD") && slabData != null && dueDtlCount <= 0) {

				Double sumOfAddDues = this.commonService.numberFormatter(
						sysRepo.getSumOfTranAmountOfPenal("ADD_INTEREST_ACCRUAL", loans.getLoanId(), tranDate));
				if (sumOfAddDues != null) {
					if (sumOfAddDues > 0) {
						AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
						sysTran.setCustomerId(loans.getCustomerId());
						sysTran.setMastAgrId(loans.getMasterAgreement().getMastAgrId());
						sysTran.setLoanId(loans.getLoanId());
						sysTran.setDtTranDate(tranDate);
						sysTran.setTranType("ADD_INTEREST_ACCRUAL_REVERSAL");
						sysTran.setTranAmount(sumOfAddDues * -1);

						sysTran.setDpd(maxDpd);
						sysTran.setRemark("Additional Interest Accrual Reversal");
						sysTran.setUserId("SYSTEM");

						sysRepo.save(sysTran);

						List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
								.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
										"ADD_INTEREST_ACCRUAL", loans.getLoanId(), "N", tranDate);
						for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
							sysLoan.setAdjustedYn("Y");
							sysRepo.save(sysLoan);
						}
					}

				}
			}

			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtl);

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

}
