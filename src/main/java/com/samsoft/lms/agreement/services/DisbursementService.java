package com.samsoft.lms.agreement.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.dto.*;
import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.entities.AgrEpaySetup;
import com.samsoft.lms.agreement.entities.AgrRepayVariation;
import com.samsoft.lms.agreement.exceptions.AgreementBadRequestException;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.repositories.AgrEpaySetupRepository;
import com.samsoft.lms.agreement.repositories.AgrRepayVariationRepository;
import com.samsoft.lms.core.dto.*;
import com.samsoft.lms.core.entities.*;
import com.samsoft.lms.core.entities.Amort;
import com.samsoft.lms.core.repositories.*;
import com.samsoft.lms.core.services.*;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.DrawdownRequest;
import com.samsoft.lms.request.repositories.DrawDownRequestRepository;
import com.samsoft.lms.request.services.DrawdownRequestWorklistService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class DisbursementService {

	@Autowired
	private AgrMasterAgreementRepository masterRepo;
	@Autowired
	private AgrDisbursementRepository disbRepo;
	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private AgrProductRepository prodRepo;
	@Autowired
	private AgrRepayVariationRepository variationRepo;
	@Autowired
	private AgrRepayScheduleRepository repayRepo;
	@Autowired
	private CoreAmort amortService;
	@Autowired
	private TabMstDepositBankRepository depositRepo;
	@Autowired
	private AgrEpaySetupRepository epayRepo;
	@Autowired
	private Environment env;
	@Autowired
	private TrnInsInstrumentAllocRepository allocRepo;
	@Autowired
	private TrnInsInstrumentRepository insRepo;
	@Autowired
	private TabOrganizationRepository orgRepo;
	@Autowired
	private AgrTrnTranDetailsRepository detailRepo;
	@Autowired
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private OdAmort odAmort;
	@Autowired
	private AgrRepayScheduleHistRepository repayHistRepo;
	@Autowired
	private TrnInsInstrumentHistRepository insHistRepo;
	@Autowired
	private CommonServices commonService;
	@Autowired
	private PaymentApplicationServices paymentServ;
	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;
	@Autowired
	private AgrCustomerRepository custRepo;
	@Autowired
	private CoreAmortVariation amortVariationService;
	@Autowired
	private AgrColenderDtlRepository colenderRepo;
	@Autowired
	private DrawDownRequestRepository drawdownRepo;
	@Autowired
	private CustApplLimitSetupRepository custApplLimitRepo;
	@Autowired
	private SupplierFinanceUtility suppUtility;

	@Autowired
	private DrawdownRequestWorklistService drawdownRequestWorklistService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String disbursementApi(DisbursementBoardingDto loanBoardingDto) throws Exception {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		String loanId = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String loanGeneration = "N";
		double previousBpi = 0d;
		int size = 0;
		double previousInterest = 0d;
		List<AgrRepaySchedule> scheduleList = new ArrayList<AgrRepaySchedule>();
		double nextInstallmentAmount = 0d;
		try {

			AgrMasterAgreement master = masterRepo.findByMastAgrId(loanBoardingDto.getMastAgrId());
			if (master == null) {
				throw new AgreementDataNotFoundException("Master Agreement not available in LMS.");

			}
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());

			log.info("disbursementApi master");
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());

			if (limitSetup != null) {
				if ((loanBoardingDto.getDisbursementDtl().getDisbAmount() + limitSetup.getUtilizedLimit()) > limitSetup
						.getLimitSanctionAmount()) {
					throw new AgreementBadRequestException(
							"Sum of Disbursement Amount and Utilized Amount is greater than Sanctioned Amount.");
				}
			}
			
			if (limitSetup != null) {
				if ((loanBoardingDto.getDisbursementDtl().getDisbAmount() + limitSetup.getUtilizedLimit()) > limitSetup
						.getAvailableLimit()) {
					throw new AgreementBadRequestException(
							"Sum of Disbursement Amount and Utilized Amount is greater than Available Amount.");
				}
			}

			// Supplier Finance Changes Start

			if (loanBoardingDto.getLoanDtl().getLoanType().equalsIgnoreCase("OD")) {

				CustApplLimitSetup custAppLimit = custApplLimitRepo.findByOriginationApplnNoAndCustomerIdAndProductCode(
						master.getOriginationApplnNo(), master.getCustomerId(), product.getProdCode());

				if ((loanBoardingDto.getDisbursementDtl().getDisbAmount()
						+ custAppLimit.getUtilizedLimit()) > custAppLimit.getLimitSanctioned()) {
					throw new AgreementBadRequestException(
							"Enough limit not available for further disbursement for " + master.getOriginationApplnNo()
									+ " " + master.getCustomerId() + " " + product.getProdCode());
				}
			}

			// Supplier Finance Changes End

			String date = loanBoardingDto.getDisbursementDtl().getDtInstallmentStartDate().split("-")[0];
			log.info("datedatedate ====== " + date);
			if (loanBoardingDto.getLoanDtl().getCycleDay() != 0) {
				log.info("Cycle DayCycle DayCycle Day ====== " + loanBoardingDto.getLoanDtl().getCycleDay().toString());
				if (Integer.parseInt(date) != loanBoardingDto.getLoanDtl().getCycleDay()) {
					throw new AgreementBadRequestException("Cycle Day is not matching with Installment Start Date");
				}
			}

			if (master.getSanctionedAmount() < (loanBoardingDto.getDisbursementDtl().getDisbAmount()
					+ master.getUnbilledPrincipal())) {
				throw new AgreementBadRequestException(
						"Sum of Disbursement Amount and Unbilled Principal is greater than Sanctioned Amount.");
			}

			List<TabOrganization> findAll = orgRepo.findAll();
			Date businessDate = orgRepo.findAll().get(0).getDtBusiness();
			Date disbDate = sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate());

			// Temporary removed the validation.. enable it on production after migration
			/*
			 * if (businessDate.compareTo(disbDate) != 0) { throw new
			 * AgreementBadRequestException("Disbursement date is not equal to Business Date. "
			 * ); }
			 */
			AgrLoans loan = null;
			if (loanBoardingDto.getLoanDtl().getLoanId() != null) {
				AgrLoans loanCount = loanRepo.findByLoanId(loanBoardingDto.getLoanDtl().getLoanId());
				if (loanCount == null) {
					loanId = "L" + master.getPortfolioCode().substring(0, 2)
							+ Long.toString((long) Math.floor(Math.random() * 9_000_000_00L) + 1_000_000_00L);
					loan = new AgrLoans();
					loanGeneration = "Y";
				} else {
					loan = loanCount;
					loanId = loanBoardingDto.getLoanDtl().getLoanId();
					loanGeneration = "N";
					int currentInstallmentNo = loanRepo.findByLoanId(loanId).getCurrentInstallmentNo();
					AgrRepaySchedule previousBpiScheduleRow = repayRepo
							.findByMasterAgrIdAndInstallmentNo(loanBoardingDto.getMastAgrId(), currentInstallmentNo);
					if (previousBpiScheduleRow != null) {
						previousBpi = previousBpiScheduleRow.getBpiAmount();
						previousInterest = previousBpiScheduleRow.getInterestAmount();
					}

				}
			} else {
				loanId = "L" + master.getPortfolioCode().substring(0, 2)
						+ Long.toString((long) Math.floor(Math.random() * 9_000_000_00L) + 1_000_000_00L);
				loan = new AgrLoans();
				loanGeneration = "Y";
			}

			BeanUtils.copyProperties(loanBoardingDto.getLoanDtl(), loan);
			loan.setDtTenorStartDate(sdf.parse(loanBoardingDto.getLoanDtl().getDtTenorStartDate()));
			loan.setDtTenorEndDate(sdf.parse(loanBoardingDto.getLoanDtl().getDtTenorEndDate()));
			loan.setLoanId(loanId);
			loan.setDtLastDisbDate(sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()));

			loan.setMasterAgreement(master);

			AgrDisbursement disb = new AgrDisbursement();
			BeanUtils.copyProperties(loanBoardingDto.getDisbursementDtl(), disb);
			disb.setUserId("INTERFACE");
			disb.setDtDisbDate(sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()));
			disb.setDtInstallmentStartDate(sdf.parse(loanBoardingDto.getDisbursementDtl().getDtInstallmentStartDate()));
			disb.setMastAgr(master);

			List<AgrRepayVariation> variationList = new ArrayList<AgrRepayVariation>();
			if (loanBoardingDto.getRepaymentVariationDtl() != null) {
				for (AmortVariationListDto variationDto : loanBoardingDto.getRepaymentVariationDtl()) {
					AgrRepayVariation variation = new AgrRepayVariation();
					BeanUtils.copyProperties(variationDto, variation);
					variation.setFromInstallmentNo(variationDto.getFromInstNo());
					variation.setLoans(loan);
					variationList.add(variation);
				}
			}

			if (loanBoardingDto.getLoanDtl().getLoanType().equals("DL")
					|| loanBoardingDto.getLoanDtl().getLoanType().equals("ND")) {
				String odProdYn = "Y";
				// AgrCustLimitSetup limitSetup =
				// limitRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());

				OdAmortDto odDto = new OdAmortDto();
				odDto.setDtLimitSanctioned(sdf.format(limitSetup.getDtLimitSanctioned()));
				odDto.setDtLimitExpired(sdf.format(limitSetup.getDtLimitExpired()));
				odDto.setSanctionedAmount(limitSetup.getLimitSanctionAmount());
				odDto.setDtDrawdown(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
				odDto.setDrawDownAmount(
						(loanGeneration.equals("Y") ? loanBoardingDto.getDisbursementDtl().getDisbAmount()
								: loan.getUnbilledPrincipal() + loanBoardingDto.getDisbursementDtl().getDisbAmount()));
				odDto.setInterestPaymentCycleDay(loanBoardingDto.getLoanDtl().getCycleDay());
				odDto.setPrincipalPaymentCycleDay(product.getDropLineCycleDay());
				odDto.setInterestRate(loanBoardingDto.getLoanDtl().getInterestRate());
				odDto.setInterestBasis(product.getInterestBasis());
				// odDto.setInterestRepaymentFrequency(loanBoardingDto.getLoanDtl().getRepayFreq());
				odDto.setInterestRepaymentFrequency(master.getRepayFreq());
				odDto.setEmiRounding(product.getEmiRounding());
				if (master.getUnbilledPrincipal() <= 0 && previousBpi <= 0) {
					odDto.setBpiAmount(commonService.getInterestAccruedTillDateByTranType(
							loanBoardingDto.getMastAgrId(), "INTEREST_ACCRUAL") + previousInterest);
				} else {
					odDto.setBpiAmount(commonService.getInterestAccruedTillDateByTranType(
							loanBoardingDto.getMastAgrId(), "INTEREST_ACCRUAL") + previousBpi);
				}

				/*
				 * List<AgrTrnSysTranDtl> sysList =
				 * sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(
				 * loanBoardingDto.getMastAgrId(), "N", "INTEREST_ACCRUAL");
				 * log.info("sysList size " + sysList.size()); List<AgrTrnSysTranDtl>
				 * saveSysList = new ArrayList<AgrTrnSysTranDtl>(); for (AgrTrnSysTranDtl
				 * agrTrnSysTranDtl : sysList) { agrTrnSysTranDtl.setAdjustedYn("X");
				 * saveSysList.add(agrTrnSysTranDtl); } log.info("Before sysList save ");
				 * 
				 * sysRepo.saveAll(saveSysList);
				 */

				log.info("After sysList save ");

				odDto.setDropLineODYN(product.getDropLineODYN());
				odDto.setDropLineMode(product.getDropMode());
				odDto.setDropLineAmount(product.getDropLineAmount());
				odDto.setDropLinePerc(product.getDropLinePerc());
				odDto.setDropLineFreq(product.getDropLIneFreq());
				odDto.setDtPrinStart(loanBoardingDto.getDisbursementDtl().getDtPrinStart());
				odDto.setDtInterestStart(loanBoardingDto.getDisbursementDtl().getDtInstallmentStartDate());
				List<OdAmortScheduleDto> odAmortList = odAmort.getOdAmort(odDto);
				log.info("odAmortList size " + odAmortList.size());
				size = odAmortList.size();
				log.info("loanGeneration " + loanGeneration);
				if (loanGeneration.equalsIgnoreCase("Y")) {
					nextInstallmentAmount = odAmortList.get(0).getInstallmentAmount();
					int odInstallmentNo = 0;
					for (OdAmortScheduleDto odAmort : odAmortList) {
						if (odAmort.getInstallmentAmount() == 0) {
							continue;
						}
						AgrRepaySchedule schedule = new AgrRepaySchedule();
						schedule.setMasterAgrId(loanBoardingDto.getMastAgrId());
						schedule.setLoanId(loan.getLoanId());
						schedule.setInstallmentNo(++odInstallmentNo);
						schedule.setDtInstallment(sdf.parse(odAmort.getInstallmentDate()));
						schedule.setOpeningPrincipal(odAmort.getOpeningBalance());
						schedule.setPrincipalAmount(odAmort.getPrincipalAmount());
						schedule.setInterestAmount(odAmort.getInterestAmount());
						schedule.setBpiAmount(odAmort.getBpiAmount());
						schedule.setInstallmentAmount(odAmort.getInstallmentAmount());
						schedule.setClosingPrincipal(odAmort.getClosingBalance());
						schedule.setInterestRate(odAmort.getInterestRate());
						schedule.setInterestBasis(product.getInterestBasis());
						schedule.setUserId("INTERFACE");
						if (master.getTdsRate() != null) {
							if (master.getTdsRate() > 0) {
								schedule.setTdsAmount(commonService
										.numberFormatter(schedule.getInterestAmount() * (master.getTdsRate() / 100)));
							}
						}
						scheduleList.add(schedule);
					}

				} else {
					Integer maxSeqNo = repayHistRepo.getMaxSeqNo(master.getMastAgrId(), businessDate);
					if (maxSeqNo == null) {
						maxSeqNo = 0;
					}

					log.info("maxSeqNo " + maxSeqNo);
					maxSeqNo = maxSeqNo + 1;
					List<AgrRepayScheduleHist> repayHistList = new ArrayList<AgrRepayScheduleHist>();
					List<AgrRepaySchedule> repayList = repayRepo
							.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(master.getMastAgrId(),
									businessDate);
					for (AgrRepaySchedule agrRepaySchedule : repayList) {
						AgrRepayScheduleHist repayHist = new AgrRepayScheduleHist();
						BeanUtils.copyProperties(agrRepaySchedule, repayHist);
						repayHist.setLoan(agrRepaySchedule.getLoanId());
						repayHist.setSeqNo(maxSeqNo);
						repayHistList.add(repayHist);
					}

					repayHistRepo.saveAll(repayHistList);
					repayRepo.deleteByMasterAgrIdAndDtInstallmentGreaterThan(loanBoardingDto.getMastAgrId(),
							businessDate);
					List<TrnInsInstrumentHist> insHistList = new ArrayList<TrnInsInstrumentHist>();
					List<TrnInsInstrument> insList = insRepo
							.findByMasterAgrAndDtInstrumentDateGreaterThan(master.getMastAgrId(), businessDate);
					for (TrnInsInstrument inst : insList) {
						TrnInsInstrumentHist insHist = new TrnInsInstrumentHist();
						BeanUtils.copyProperties(inst, insHist);
						insHist.setMasterAgr(inst.getMasterAgr());
						insHist.setSeqNo(maxSeqNo);
						insHistList.add(insHist);
					}

					insHistRepo.saveAll(insHistList);
					insRepo.deleteByMasterAgrAndDtInstrumentDateGreaterThan(master.getMastAgrId(), businessDate);

					Integer count = 1;

					Integer maxInstallmentNo = repayRepo.getMaxInstallmentNo(master.getMastAgrId(), businessDate);
					if (maxInstallmentNo == null) {
						maxInstallmentNo = 0;
					}
					size = odAmortList.size();
					for (OdAmortScheduleDto odAmort : odAmortList) {
						if (odAmort.getInstallmentAmount() == 0) {
							continue;
						}
						if (count == 1) {
							nextInstallmentAmount = odAmort.getInstallmentAmount();
						}

						// maxInstallmentNo = count + 1;

						AgrRepaySchedule schedule = new AgrRepaySchedule();
						schedule.setMasterAgrId(loanBoardingDto.getMastAgrId());
						schedule.setLoanId(loan.getLoanId());
						schedule.setInstallmentNo(++maxInstallmentNo);
						schedule.setDtInstallment(sdf.parse(odAmort.getInstallmentDate()));
						schedule.setOpeningPrincipal(odAmort.getOpeningBalance());
						schedule.setPrincipalAmount(odAmort.getPrincipalAmount());
						schedule.setInterestAmount(odAmort.getInterestAmount());
						schedule.setBpiAmount(odAmort.getBpiAmount());
						schedule.setInstallmentAmount(odAmort.getInstallmentAmount());
						schedule.setClosingPrincipal(odAmort.getClosingBalance());
						schedule.setInterestRate(odAmort.getInterestRate());
						schedule.setInterestBasis(product.getInterestBasis());
						schedule.setUserId("INTERFACE");
						if (master.getTdsRate() != null) {
							if (master.getTdsRate() > 0) {
								schedule.setTdsAmount(commonService
										.numberFormatter(schedule.getInterestAmount() * (master.getTdsRate() / 100)));

							}
						}
						scheduleList.add(schedule);
					}

				}

			} else {
				List<Amort> amortList = new ArrayList<>();
				if (loanBoardingDto.getRepaymentVariationDtl() != null) {
					AmortVariationDto variationDto = new AmortVariationDto();
					variationDto.setDtDisbursement(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
					variationDto
							.setDtInstallmentStart(loanBoardingDto.getDisbursementDtl().getDtInstallmentStartDate());
					variationDto.setLoanAmount(loan.getLoanAmount());
					variationDto.setInterestRate(loan.getInterestRate());
					variationDto.setTenor(loan.getTenor());
					variationDto.setTenorUnit(loan.getTenorUnit());
					variationDto.setEmiBasis(product.getEmiBasis());
					variationDto.setBpiHandling(product.getBpiTreatmentFlag());
					variationDto.setEmiRounding(product.getEmiRounding());
					variationDto.setInterestBasis(product.getInterestBasis());
					variationDto.setRepaymentFreq(loan.getRepayFreq());
					variationDto.setAmortizationType(product.getAmortizationType());
					variationDto.setAmortizationMethod(product.getAmortizationMethod());
					variationDto.setLastRowThreshold(product.getLastRowEMIThreshold());
					variationDto.setNoOfAdvEmi(loan.getNoOfAdvEmi());
					variationDto.setVariationList(loanBoardingDto.getRepaymentVariationDtl());

					amortList = amortVariationService.getAmortVariation(variationDto);
					size = amortList.size();
				} else {
					AmortParameter amortParam = new AmortParameter();
					amortParam.setDtDisbursement(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
					amortParam.setDtInstallmentStart(loanBoardingDto.getDisbursementDtl().getDtInstallmentStartDate());
					amortParam.setLoanAmount(loan.getLoanAmount());
					amortParam.setInterestRate(loan.getInterestRate());
					amortParam.setTenor(loan.getTenor());
					amortParam.setTenorUnit(loan.getTenorUnit());
					amortParam.setEmiBasis(product.getEmiBasis());
					amortParam.setBpiHandling(product.getBpiTreatmentFlag());
					amortParam.setEmiRounding(product.getEmiRounding());
					amortParam.setInterestBasis(product.getInterestBasis());
					amortParam.setRepaymentFrequency(loan.getRepayFreq());
					amortParam.setAmortizationType(product.getAmortizationType());
					amortParam.setAmortizationMethod(product.getAmortizationMethod());
					amortParam.setLastRowThresholdPercentage(product.getLastRowEMIThreshold());
					amortParam.setNoOfAdvanceEmi(loan.getNoOfAdvEmi());

					amortList = amortService.getAmort(amortParam);
					size = amortList.size();
				}

				if (loanGeneration.equalsIgnoreCase("Y")) {
					nextInstallmentAmount = amortList.get(0).getInstallmentAmount();
					for (Amort amort : amortList) {
						AgrRepaySchedule schedule = new AgrRepaySchedule();
						schedule.setMasterAgrId(loanBoardingDto.getMastAgrId());
						schedule.setLoanId(loan.getLoanId());
						schedule.setInstallmentNo(amort.getInstallmentNo());
						schedule.setDtInstallment(sdf.parse(amort.getDtInstallment()));
						schedule.setOpeningPrincipal(amort.getOpeningBalance());
						schedule.setPrincipalAmount(amort.getPrincipalAmount());
						schedule.setInterestAmount(amort.getInterestAmount());
						schedule.setBpiAmount(amort.getBpiAmount());
						schedule.setInstallmentAmount(amort.getInstallmentAmount());
						schedule.setClosingPrincipal(amort.getClosingBalance());
						schedule.setInterestRate(amort.getInterestRate());
						schedule.setInterestBasis(product.getInterestBasis());
						schedule.setUserId("INTERFACE");
						if (master.getTdsRate() != null) {
							if (master.getTdsRate() > 0) {
								schedule.setTdsAmount(commonService
										.numberFormatter(schedule.getInterestAmount() * (master.getTdsRate() / 100)));

							}
						}
						scheduleList.add(schedule);
					}
				}

			}
			repayRepo.saveAll(scheduleList);

			AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			hdr.setMasterAgr(master);
			hdr.setTranDate(sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()));
			hdr.setTranType("DISBURSEMENT");
			if (loanBoardingDto.getLoanDtl().getLoanType().equals("DL")
					|| loanBoardingDto.getLoanDtl().getLoanType().equals("ND")) {
				hdr.setRemark("Drawdown No#." + loanBoardingDto.getDisbursementDtl().getDisbSrNo());
				hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());

			} else {
				hdr.setRemark("Disbursement No#." + loanBoardingDto.getDisbursementDtl().getDisbSrNo());
			}
			hdr.setSource("INTERFACE");
			hdr.setUserID(loanBoardingDto.getLoanDtl().getUserId());

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(hdr);
			event.setTranEvent("DISBURSEMENT");
			event.setTranAmount(loanBoardingDto.getDisbursementDtl().getDisbAmount());
			event.setUserId(loanBoardingDto.getLoanDtl().getUserId());

			List<AgrTrnTranDetail> detailsList = new ArrayList<AgrTrnTranDetail>();
			for (AgrDisbSoaDtlDto soa : loanBoardingDto.getSoaDtls()) {
				if (soa.getTranAmount() > 0) {
					AgrTrnTranDetail detail = new AgrTrnTranDetail();
					detail.setEventDtl(event);
					detail.setMasterAgr(master);
					detail.setLoan(loan);
					detail.setDtDueDate(sdf.parse(soa.getDtDueDate()));
					detail.setTranCategory(soa.getTranCategory());
					detail.setTranHead(soa.getTranHead());
					if (soa.getTranSide().equalsIgnoreCase("CR")) {
						detail.setTranAmount(soa.getTranAmount() * -1);
					} else {
						detail.setTranAmount(soa.getTranAmount());
					}

					detail.setTranSide(soa.getTranSide());
					detail.setDtlRemark(soa.getDtlRemark());
					detail.setInstallmentNo(loanBoardingDto.getDisbursementDtl().getDisbSrNo());
					if (loanBoardingDto.getLoanDtl().getLoanType().equals("DL")
							|| loanBoardingDto.getLoanDtl().getLoanType().equals("ND")) {
						detail.setAvailableLimit(
								commonService.numberFormatter(limitSetup.getAvailableLimit() - soa.getTranAmount()));
						detail.setUtilizedLimit(
								commonService.numberFormatter(limitSetup.getUtilizedLimit() + soa.getTranAmount()));
					}
					detailsList.add(detail);
				}
			}
			master.setDtNextInstallment(sdf.parse(loanBoardingDto.getLoanDtl().getDtTenorStartDate()));
			master.setNextInstallmentAmount(nextInstallmentAmount);
			master.setLoanAmount(master.getLoanAmount() + loanBoardingDto.getDisbursementDtl().getDisbAmount());
			master.setUnbilledPrincipal(
					master.getUnbilledPrincipal() + loanBoardingDto.getDisbursementDtl().getDisbAmount()
							- (nextInstallmentAmount * loan.getNoOfAdvEmi()));
			loan.setUnbilledPrincipal(master.getUnbilledPrincipal());
			loan.setTenor(size);
			loan.setBalTenor(loan.getTenor());
			loan.setCycleDay(Integer.parseInt(loanBoardingDto.getLoanDtl().getDtTenorStartDate().split("-")[0]));
			List<AgrEpaySetup> ecsList = epayRepo
					.findAllByMastAgreementMastAgrIdAndActive(loanBoardingDto.getMastAgrId(), "Y");

			if (ecsList.size() > 0) {
				this.postEcsInstruments(loanBoardingDto.getMastAgrId());
			}

			if (loanBoardingDto.getLoanDtl().getLoanType().equals("DL")
					|| loanBoardingDto.getLoanDtl().getLoanType().equals("ND")) {
				paymentServ.updateLimit(loanBoardingDto.getMastAgrId(),
						loanBoardingDto.getDisbursementDtl().getDisbAmount(), "DED", "DRAWDOWN", hdr.getTranId(),
						"INTERFACE");
			}

			// Supplier Finance Changes Start
			if (loanBoardingDto.getLoanDtl().getLoanType().equalsIgnoreCase("OD")) {
				suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
						master.getCustomerId(), product.getProdCode(), "DED",
						loanBoardingDto.getDisbursementDtl().getDisbAmount(), hdr.getTranId(), "INTERFACE",
						sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()));

			}
			// Supplier Finance Changes End

			// masterRepo.save(entity)

			// loanRepo.save(loan);
			disbRepo.save(disb);
			variationRepo.saveAll(variationList);
			detailRepo.saveAll(detailsList);

		} catch (AgreementDataNotFoundException e) {
			e.printStackTrace();
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (AgreementBadRequestException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		/*
		 * try {
		 * 
		 * if (epayRepo.findAllByMastAgreementMastAgrIdAndActiveAndMandateStatus(
		 * loanBoardingDto.getMastAgrId(),"Y","RC").size() > 0) {
		 * this.postEcsInstruments(loanBoardingDto.getMastAgrId()); }
		 * 
		 * } catch (AgreementDataNotFoundException e) { throw new
		 * AgreementDataNotFoundException(e.getMessage()); } catch (Exception e) { throw
		 * e; }
		 */

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String disbursementOdFirstApi(DisbursementBoardingOdFirstDto loanBoardingDto) throws Exception {
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String result = "";
			AgrMasterAgreement master = masterRepo.findByMastAgrId(loanBoardingDto.getMastAgrId());
			List<AgrLoans> loanList = loanRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());
			if (master == null) {
				throw new AgreementDataNotFoundException("Master Agreement not available in LMS.");

			}

			AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());
			if (limit == null) {
				throw new AgreementDataNotFoundException("Limit Details not available.");

			}

			AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(loanBoardingDto.getMastAgrId(),
					"B");

			if (customer.getLimitFreezYn().equalsIgnoreCase("Y")) {
				throw new AgreementDataNotFoundException(
						"Limit freezed for master agreement " + customer.getCustomerId());
			}

			List<AgrRepaySchedule> repay = repayRepo.findByMasterAgrIdOrderByRepaySchId(loanBoardingDto.getMastAgrId());

			log.info("disbursementOdFirstApi master=========================>: {}", master);
			DisbursementBoardingDto mainDisb = new DisbursementBoardingDto();

			mainDisb.setMastAgrId(loanBoardingDto.getMastAgrId());
			mainDisb.getLoanDtl().setInterestRate(master.getInterestRate());
			mainDisb.getLoanDtl().setRepayFreq(master.getRepayFreq());
			mainDisb.getLoanDtl().setUserId(loanBoardingDto.getLoanDtl().getUserId());
			log.info("master.getRepayFreq()=========================>: {}", master.getRepayFreq());
			if (repay.size() == 0) {
				mainDisb.getDisbursementDtl().setDtDisbDate(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
				String nextInstallmentDate = amortService.getNextInstallmentDate(
						loanBoardingDto.getDisbursementDtl().getDtDisbDate(), master.getRepayFreq());
				mainDisb.getLoanDtl().setDtTenorStartDate(nextInstallmentDate);

			} else {
				mainDisb.getDisbursementDtl().setDtDisbDate(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
				mainDisb.getLoanDtl().setDtTenorStartDate(sdf.format(master.getDtNextInstallment()));
			}

			mainDisb.getDisbursementDtl().setDisbAmount(loanBoardingDto.getDisbursementDtl().getDisbAmount());
			log.info("loanList.size() " + loanList);
			log.info("loanList.size() " + loanList.size());
			if (loanList.size() > 0) {
				mainDisb.getLoanDtl().setLoanId(loanList.get(0).getLoanId());
			}
			mainDisb.getLoanDtl().setCustomerId(master.getCustomerId());
			mainDisb.getLoanDtl().setCycleDay(0);
			mainDisb.getLoanDtl().setDpd(0);
			mainDisb.getLoanDtl().setTenorUnit("MONTHLY");
			mainDisb.getLoanDtl().setInterestType("FIXED");
			mainDisb.getLoanDtl().setPenalCycleDay(10);
			mainDisb.getLoanDtl().setDtTenorEndDate(sdf.format(limit.getDtLimitExpired()));
			mainDisb.getLoanDtl().setFinalDisbursement("N");
			mainDisb.getLoanDtl().setPreEmiyn("N");
			mainDisb.getLoanDtl().setEmiOnSanctionedAmount("N");
			if (product.getDropLineODYN().equals("Y")) {
				mainDisb.getLoanDtl().setLoanType("DL");
			} else {
				mainDisb.getLoanDtl().setLoanType("ND");
			}
			mainDisb.getLoanDtl().setLoanAmount(master.getSanctionedAmount());
			mainDisb.getLoanDtl().setNoOfAdvEmi(0);

			mainDisb.getDisbursementDtl().setMastAgrId(loanBoardingDto.getMastAgrId());
			mainDisb.getDisbursementDtl().setDisbSrNo(1);
			mainDisb.getDisbursementDtl().setFinalDisbYn("N");

			Date tmpDateInt = null;
			log.info("111111 master.getRepayFreq()=========================>: {}", master.getRepayFreq());
			if (repay.size() == 0) {
				tmpDateInt = sdf.parse(amortService.getNextInstallmentDate(sdf.format(limit.getDtLimitSanctioned()),
						master.getRepayFreq()));
				if (tmpDateInt.before(sdf.parse(mainDisb.getDisbursementDtl().getDtDisbDate()))) {
					tmpDateInt = sdf
							.parse(amortService.getNextInstallmentDate(sdf.format(tmpDateInt), master.getRepayFreq()));
				}
			} else {
				tmpDateInt = sdf.parse(amortService.getNextInstallmentDate(
						loanBoardingDto.getDisbursementDtl().getDtDisbDate(), master.getRepayFreq()));

			}

			log.info(tmpDateInt.toString() + " " + limit.getDtLimitSanctioned());
			Date tmpDatePrin = null;
			log.info("2222222222 product.getDropLIneFreq()=========================>: {}", product.getDropLIneFreq());
			if (product.getDropLIneFreq() != null && !product.getDropLIneFreq().isEmpty()) {
				log.info("product.getDropLIneFreq() is not null=========================>: {}",
						product.getDropLIneFreq());
				if (repay.size() == 0) {
					tmpDatePrin = sdf.parse(amortService.getNextInstallmentDate(
							sdf.format(limit.getDtLimitSanctioned()), product.getDropLIneFreq()));
					if (tmpDatePrin.before(sdf.parse(mainDisb.getDisbursementDtl().getDtDisbDate()))) {
						tmpDatePrin = sdf.parse(
								amortService.getNextInstallmentDate(sdf.format(tmpDatePrin), master.getRepayFreq()));
					}
				} else {
					tmpDatePrin = sdf.parse(amortService.getNextInstallmentDate(
							loanBoardingDto.getDisbursementDtl().getDtDisbDate(), product.getDropLIneFreq()));
				}
			}

			if (master.getCycleDay() == 0) {

				Calendar calendarInt = GregorianCalendar.getInstance();
				calendarInt.setTime(tmpDateInt);
				// calendar.add(Calendar.MONTH, 1);
				calendarInt.set(Calendar.DATE, calendarInt.getActualMinimum(Calendar.DAY_OF_MONTH));
				calendarInt.add(Calendar.DAY_OF_MONTH, -1);
				tmpDateInt = calendarInt.getTime();
				mainDisb.getDisbursementDtl().setDtInstallmentStartDate(sdf.format(tmpDateInt));

				Calendar calendarPrin = GregorianCalendar.getInstance();
				calendarPrin.setTime(tmpDatePrin);
				// calendar.add(Calendar.MONTH, 1);
				calendarPrin.set(Calendar.DATE, calendarPrin.getActualMinimum(Calendar.DAY_OF_MONTH));
				calendarPrin.add(Calendar.DAY_OF_MONTH, -1);
				tmpDatePrin = calendarPrin.getTime();

				if (product.getDropLineODYN().equalsIgnoreCase("N")) {
					mainDisb.getDisbursementDtl().setDtPrinStart(sdf.format(limit.getDtLimitExpired()));
				} else {
					mainDisb.getDisbursementDtl().setDtPrinStart(sdf.format(tmpDatePrin));
				}
			} else {
				Calendar calendarInt = GregorianCalendar.getInstance();
				calendarInt.setTime(tmpDateInt);
				// calendarInt.add(Calendar.MONTH, 1);
				calendarInt.set(calendarInt.get(Calendar.YEAR), calendarInt.get(Calendar.MONTH), master.getCycleDay());

				tmpDateInt = calendarInt.getTime();
				mainDisb.getDisbursementDtl().setDtInstallmentStartDate(sdf.format(tmpDateInt));

				if (tmpDatePrin != null) {
					Calendar calendarPrin = GregorianCalendar.getInstance();
					calendarPrin.setTime(tmpDatePrin);
					// calendarPrin.add(Calendar.MONTH, 1);
					calendarPrin.set(calendarPrin.get(Calendar.YEAR), calendarPrin.get(Calendar.MONTH),
							master.getCycleDay());

					tmpDatePrin = calendarPrin.getTime();
				}

				log.info("tmpDatePrin=======: {}", tmpDatePrin);
				if (product.getDropLineODYN().equalsIgnoreCase("N")) {
					mainDisb.getDisbursementDtl().setDtPrinStart(sdf.format(limit.getDtLimitExpired()));
				} else {
					mainDisb.getDisbursementDtl().setDtPrinStart(sdf.format(tmpDatePrin));
				}

			}

			AgrDisbSoaDtlDto soaDtl = new AgrDisbSoaDtlDto();
			log.info("33333333 master.getRepayFreq()=========================>: {}", master.getRepayFreq());
			soaDtl.setDtDueDate(amortService.getNextInstallmentDate(
					loanBoardingDto.getDisbursementDtl().getDtDisbDate(), master.getRepayFreq()));
			soaDtl.setTranAmount(loanBoardingDto.getDisbursementDtl().getDisbAmount());
			soaDtl.setTranCategory("DISBURSEMENT");
			soaDtl.setTranHead("DISB_AMT");
			soaDtl.setTranSide("DR");
			soaDtl.setDtlRemark("Withdrawal - NEFT");

			mainDisb.getSoaDtls().add(soaDtl);

			log.info("mainDisb ");

			result = this.disbursementApi(mainDisb);

			if (product.getDropLineODYN().equalsIgnoreCase("Y")) {
				int addFactorPrin = 0;

				switch (product.getDropLIneFreq()) {
				case "MONTHLY":
					addFactorPrin = 1;
					break;
				case "BIMONTHLY":
					addFactorPrin = 2;
					break;
				case "QUARTERLY":
					addFactorPrin = 3;
					break;
				case "HALFYEARLY":
					addFactorPrin = 6;
					break;
				case "YEARLY":
					addFactorPrin = 12;
					break;

				}

				/*
				 * Date prinDate = sdf.parse(product.getDropLineCycleDay() + "-" +
				 * loanBoardingDto.getDisbursementDtl().getDtDisbDate().split("-")[1] + "-" +
				 * loanBoardingDto.getDisbursementDtl().getDtDisbDate().split("-")[2]);
				 * 
				 * Calendar c1 = Calendar.getInstance(); c1.setTime(prinDate);
				 * c1.add(Calendar.MONTH, addFactorPrin); prinDate = c1.getTime();
				 * 
				 * master.setDtNextDrop(prinDate);
				 * 
				 * if (product.getDropLinePerc() > 0) { master.setNextDropAmount(commonService
				 * .numberFormatter(master.getSanctionedAmount() * product.getDropLinePerc() /
				 * 100)); } else {
				 * master.setNextDropAmount(commonService.numberFormatter(product.
				 * getDropLineAmount())); }
				 */

				masterRepo.save(master);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("In method disbursementOdFirstApi: {}", e.getMessage());
			log.error("In method disbursementOdFirstApi:{}", e.getLocalizedMessage());
			throw e;
		}
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String disbursementOdSubsequentApi(DisbursementBoardingOdSubsequentDto loanBoardingDto) throws Exception {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String result = "";
		AgrMasterAgreement master = masterRepo.findByMastAgrId(loanBoardingDto.getMastAgrId());
		AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());
		AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(loanBoardingDto.getMastAgrId(), "B");
		if (master == null) {
			throw new AgreementDataNotFoundException("Master Agreement not available in LMS.");

		}

		AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId());
		if (limit == null) {
			throw new AgreementDataNotFoundException("Limit Details not available.");

		}

		if (customer.getLimitFreezYn().equalsIgnoreCase("Y")) {
			throw new AgreementDataNotFoundException("Limit freezed for master agreement " + customer.getCustomerId());

		}

		AgrRepaySchedule repay = repayRepo
				.findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndPrincipalAmountGreaterThanOrderByInstallmentNoAsc(
						loanBoardingDto.getMastAgrId(), sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()),
						0d);
		if (repay == null) {
			throw new AgreementDataNotFoundException("Repayment Details not available.");

		}

		AgrRepaySchedule repayInt = repayRepo
				.findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndInterestAmountGreaterThanOrderByInstallmentNoAsc(
						loanBoardingDto.getMastAgrId(), sdf.parse(loanBoardingDto.getDisbursementDtl().getDtDisbDate()),
						0d);
		if (repayInt == null) {
			throw new AgreementDataNotFoundException("Repayment Details not available.");

		}

		AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(loanBoardingDto.getMastAgrId()).get(0);
		if (loan == null) {
			throw new AgreementDataNotFoundException("First Disbursement Details not found.");

		}

		AgrDisbursement disbDtl = disbRepo.findFirstByMastAgrMastAgrIdOrderByDisbIdDesc(loanBoardingDto.getMastAgrId());
		if (disbRepo == null) {
			throw new AgreementDataNotFoundException("First Disbursement Details not found.");

		}

		DisbursementBoardingDto mainDisb = new DisbursementBoardingDto();

		mainDisb.setMastAgrId(loanBoardingDto.getMastAgrId());
		mainDisb.getLoanDtl().setUserId(loanBoardingDto.getLoanDtl().getUserId());
		mainDisb.getDisbursementDtl().setDtDisbDate(loanBoardingDto.getDisbursementDtl().getDtDisbDate());
		mainDisb.getDisbursementDtl().setDisbAmount(loanBoardingDto.getDisbursementDtl().getDisbAmount());

		mainDisb.getLoanDtl().setLoanId(loan.getLoanId());
		mainDisb.getLoanDtl().setCustomerId(master.getCustomerId());
		mainDisb.getLoanDtl().setCycleDay(0);
		mainDisb.getLoanDtl().setDpd(0);
		mainDisb.getLoanDtl().setInterestRate(loan.getInterestRate());
		mainDisb.getLoanDtl().setRepayFreq(loan.getRepayFreq());
		mainDisb.getLoanDtl().setTenorUnit("MONTHLY");
		mainDisb.getLoanDtl().setTenor(master.getTenor());
		mainDisb.getLoanDtl().setInterestType("FIXED");
		mainDisb.getLoanDtl().setPenalCycleDay(10);
		mainDisb.getLoanDtl().setDtTenorStartDate(sdf.format(master.getDtNextInstallment()));
		mainDisb.getLoanDtl().setDtTenorEndDate(sdf.format(limit.getDtLimitExpired()));
		mainDisb.getLoanDtl().setFinalDisbursement("N");
		mainDisb.getLoanDtl().setPreEmiyn("N");
		mainDisb.getLoanDtl().setEmiOnSanctionedAmount("N");
		if (product.getDropLineODYN().equals("Y")) {
			mainDisb.getLoanDtl().setLoanType("DL");
		} else {
			mainDisb.getLoanDtl().setLoanType("ND");
		}
		mainDisb.getLoanDtl().setLoanAmount(master.getSanctionedAmount());
		mainDisb.getLoanDtl().setNoOfAdvEmi(0);

		mainDisb.getDisbursementDtl().setMastAgrId(loanBoardingDto.getMastAgrId());
		mainDisb.getDisbursementDtl().setDisbSrNo(disbDtl.getDisbSrNo() + 1);
		mainDisb.getDisbursementDtl().setFinalDisbYn("N");
		mainDisb.getDisbursementDtl().setDtInstallmentStartDate(sdf.format(repayInt.getDtInstallment()));
		mainDisb.getDisbursementDtl().setDtPrinStart(sdf.format(repay.getDtInstallment()));

		AgrDisbSoaDtlDto soaDtl = new AgrDisbSoaDtlDto();

		soaDtl.setDtDueDate(amortService.getNextInstallmentDate(loanBoardingDto.getDisbursementDtl().getDtDisbDate(),
				loan.getRepayFreq()));
		soaDtl.setTranAmount(loanBoardingDto.getDisbursementDtl().getDisbAmount());
		soaDtl.setTranCategory("DISBURSEMENT");
		soaDtl.setTranHead("DISB_AMT");
		soaDtl.setTranSide("DR");
		soaDtl.setDtlRemark("Withdrawal - NEFT");

		mainDisb.getSoaDtls().add(soaDtl);
		result = this.disbursementApi(mainDisb);

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String postEcsInstruments(String mastAgrId) {

		String result = "success";
		List<TrnInsInstrument> listIns = new ArrayList<TrnInsInstrument>();
		List<TrnInsInstrumentAlloc> listInsAlloc = new ArrayList<TrnInsInstrumentAlloc>();
		try {
			Date businessDate = orgRepo.findAll().get(0).getDtBusiness();
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			List<AgrEpaySetup> listEpay = epayRepo.findAllByMastAgreementMastAgrIdAndActive(mastAgrId, "Y");
			AgrColenderDtl colender = colenderRepo.findByMasterAgrMastAgrIdAndInstrumentPresenterYn(mastAgrId, "Y");
			for (AgrEpaySetup epay : listEpay) {
				List<AgrRepaySchedule> listRepay = repayRepo
						.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(mastAgrId, businessDate);
				for (AgrRepaySchedule repay : listRepay) {
					if (((repay.getDtInstallment().after(epay.getDtFromDate()))
							|| (repay.getDtInstallment().equals(epay.getDtFromDate())))
							&& ((repay.getDtInstallment().before(epay.getDtToDate()))
									|| ((repay.getDtInstallment().equals(epay.getDtToDate()))))) {
						TrnInsInstrument ins = new TrnInsInstrument();
						ins.setMasterAgr(mastAgrId);
						ins.setCustomerId(masterRepo.findByMastAgrId(mastAgrId).getCustomerId());
						ins.setPayType("INSTALLMENT");
						ins.setDtReceipt(repay.getDtInstallment());
						ins.setAccountNo(epay.getAccountNo());
						ins.setAccountType(epay.getAccountType());
						ins.setBankBranchName(epay.getBankBranchCode());
						ins.setBankName(epay.getBankCode());
						ins.setTdsAmount(repay.getTdsAmount());
						if (colender != null) {
							ins.setColenderId(colender.getColenderCode());
						}

						TabMstDepositBank depositBank = depositRepo.findByBankName(epay.getDepositBank());

						if (depositBank == null) {
							throw new AgreementDataNotFoundException(
									"Agreement deposit bank not matching with master setup.");
						}

						ins.setDepositBankName(epay.getDepositBank());
						ins.setDepositBankIfsc(depositBank.getIfscCode());
						ins.setDepositBankBranch(depositBank.getBranchName());
						ins.setDtInstrumentDate(repay.getDtInstallment());
						ins.setIfscCode(epay.getIfscCode());
						ins.setInstrumentNo(repay.getInstallmentNo().toString());
						ins.setInstrumentStatus("NEW");
						ins.setInstrumentType(epay.getInstrumentType());
						if (product.getProdType() != null) {
							if ((product.getProdType().equalsIgnoreCase("DL"))
									|| product.getProdType().equalsIgnoreCase("ND")) {
								if (repay.getTdsAmount() > 0) {
									ins.setInstrumentAmount(commonService.numberFormatter(repay.getInstallmentAmount()
											+ repay.getBpiAmount() - repay.getTdsAmount()));
								} else {
									ins.setInstrumentAmount(commonService
											.numberFormatter(repay.getInstallmentAmount() + repay.getBpiAmount()));
								}

							} else {
								if (repay.getTdsAmount() > 0) {
									ins.setInstrumentAmount(commonService
											.numberFormatter(repay.getInstallmentAmount() - repay.getTdsAmount()));
								} else {
									ins.setInstrumentAmount(
											commonService.numberFormatter(repay.getInstallmentAmount()));
								}

							}
						} else {
							if (repay.getTdsAmount() > 0) {
								ins.setInstrumentAmount(commonService
										.numberFormatter(repay.getInstallmentAmount() - repay.getTdsAmount()));
							} else {
								ins.setInstrumentAmount(commonService.numberFormatter(repay.getInstallmentAmount()));
							}
						}
						ins.setNclStatus("N");
						ins.setSource("EPAY");
						ins.setSourceId(epay.getEpayId().toString());
						ins.setUmrn(epay.getMandateRefNo());
						ins.setUserId(epay.getUserId());

						TrnInsInstrumentAlloc instAlloc = new TrnInsInstrumentAlloc();
						if (product.getProdType() != null) {
							if ((product.getProdType().equalsIgnoreCase("DL"))
									|| product.getProdType().equalsIgnoreCase("ND")) {
								instAlloc.setApportionAmount(repay.getInstallmentAmount() + repay.getBpiAmount());
							} else {
								instAlloc.setApportionAmount(repay.getInstallmentAmount());
							}
						} else {
							instAlloc.setApportionAmount(repay.getInstallmentAmount());
						}

						instAlloc.setLoanId(repay.getLoanId());
						instAlloc.setUserId(epay.getUserId());
						instAlloc.setInstrument(ins);
						listIns.add(ins);
						listInsAlloc.add(instAlloc);

					}
				}
			}

			allocRepo.saveAll(listInsAlloc);

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	public String executeOdDisbursementApi(ExecuteDisbursementBoardingOdFirstDto executeDisbursementBoardingOdFirstDto)
			throws Exception {
		try {
			log.info("=============>>>>>>>>>>>>>executeDisbursementBoardingOdFirstDto dto: {}",
					executeDisbursementBoardingOdFirstDto);
			String result = "";
			List<DrawdownRequest> requestsList = drawdownRequestWorklistService
					.getDrawdownRequestListByMasterAgrIdAndStatus(
							executeDisbursementBoardingOdFirstDto.getDisbursementBoardingOdFirstDto().getMastAgrId(),
							'D');
			log.info("requestsList size: {}", requestsList.size());
			if (requestsList == null || requestsList.isEmpty() || requestsList.size() == 0) {
				log.info("call disbursementOdFirstApi : {}",
						executeDisbursementBoardingOdFirstDto.getDisbursementBoardingOdFirstDto());
				result = disbursementOdFirstApi(
						executeDisbursementBoardingOdFirstDto.getDisbursementBoardingOdFirstDto());
				DrawdownRequest drawDown = drawdownRepo
						.findByRequestId(executeDisbursementBoardingOdFirstDto.getRequestId());
				drawDown.setStatus('D');
				drawdownRepo.save(drawDown);
			} else {
				log.info("call disbursementOdSubsequentApi");
				DisbursementBoardingOdSubsequentDto disbursementBoardingOdSubsequent = prepareDisbursementBoardingOdSubsequentDto(
						executeDisbursementBoardingOdFirstDto.getDisbursementBoardingOdFirstDto());
				log.info("call disbursementOdSubsequentApi : {}", disbursementBoardingOdSubsequent);
				result = disbursementOdSubsequentApi(disbursementBoardingOdSubsequent);
				DrawdownRequest drawDown = drawdownRepo
						.findByRequestId(executeDisbursementBoardingOdFirstDto.getRequestId());
				drawDown.setStatus('D');
				drawdownRepo.save(drawDown);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("In method executeOdDisbursementApi: {}", e.getMessage());
			throw e;
		}
	}

	private DisbursementBoardingOdSubsequentDto prepareDisbursementBoardingOdSubsequentDto(
			DisbursementBoardingOdFirstDto disbursementBoarding) throws Exception {
		try {
			DisbursementBoardingOdSubsequentDto disbursementBoardingOdSubsequent = new DisbursementBoardingOdSubsequentDto();
			disbursementBoardingOdSubsequent.setMastAgrId(disbursementBoarding.getMastAgrId());

			LoanDisbursementOdSubsequentDto loanDtl = new LoanDisbursementOdSubsequentDto();
			loanDtl.setUserId(disbursementBoarding.getLoanDtl().getUserId());
			disbursementBoardingOdSubsequent.setLoanDtl(loanDtl);

			DisbursementOdSubsequentDto disbursementDtl = new DisbursementOdSubsequentDto();
			disbursementDtl.setDtDisbDate(disbursementBoarding.getDisbursementDtl().getDtDisbDate());
			disbursementDtl.setDisbAmount(disbursementBoarding.getDisbursementDtl().getDisbAmount());
			disbursementDtl.setIfscCode(disbursementBoarding.getDisbursementDtl().getIfscCode());
			disbursementDtl.setAccountNo(disbursementBoarding.getDisbursementDtl().getAccountNo());
			disbursementDtl.setPaymentMode(disbursementBoarding.getDisbursementDtl().getPaymentMode());
			disbursementDtl.setUtrNo(disbursementBoarding.getDisbursementDtl().getUtrNo());
			disbursementBoardingOdSubsequent.setDisbursementDtl(disbursementDtl);

			return disbursementBoardingOdSubsequent;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("IN method prepareDisbursementBoardingOdSubsequentDto:{}", e.getMessage());
			throw e;
		}
	}
}
