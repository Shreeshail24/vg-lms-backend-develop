package com.samsoft.lms.core.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.batch.services.EODServices;
import com.samsoft.lms.core.dto.RuleDetailsDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.entities.TabOrganization;
import com.samsoft.lms.core.entities.VMstPayRuleManager;
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
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.core.repositories.TabMstPayRuleManagerDtlRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.core.repositories.VMstPayAppRuleSetRepository;
import com.samsoft.lms.core.repositories.VMstPayRuleManagerRepository;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentHistRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.transaction.entities.AgrTrnLimitDtls;
import com.samsoft.lms.transaction.entities.AgrTrnSfLimitDtls;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;
import com.samsoft.lms.transaction.repositories.AgrTrnSfLimitDtlsRepository;
import com.samsoft.lms.transaction.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentApplicationServices {
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
	private PaymentAutoApportionmentService autoApportionment;
	@Autowired
	private AgrCustomerRepository custRepo;


	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public AgrMasterAgreement updateExcess(String mastAgrId, Double excessAmount, String addOrClear) {
		try {
			log.info("Inside Update Excess");
			AgrMasterAgreement masterAgr = agrRepo.findByMastAgrId(mastAgrId);
			if (addOrClear.equalsIgnoreCase("ADD")) {
				masterAgr.setExcessAmount(commonService.numberFormatter(masterAgr.getExcessAmount() + excessAmount));
			} else {
				masterAgr.setExcessAmount(commonService.numberFormatter(masterAgr.getExcessAmount() - excessAmount));
			}
			agrRepo.save(masterAgr);
			return masterAgr;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public AgrMasterAgreement updateTdsExcess(String mastAgrId, Double excessAmount, String addOrClear) {
		try {
			log.info("Inside Update Excess");
			AgrMasterAgreement masterAgr = agrRepo.findByMastAgrId(mastAgrId);
			if (addOrClear.equalsIgnoreCase("ADD")) {
				masterAgr.setTdsExcessAmount(commonService.numberFormatter(masterAgr.getTdsExcessAmount() + excessAmount));
			} else {
				masterAgr.setTdsExcessAmount(commonService.numberFormatter(masterAgr.getTdsExcessAmount() - excessAmount));
			}
			agrRepo.save(masterAgr);
			return masterAgr;
		} catch (Exception e) {
			throw e;
		}

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String getPaymentRule(String mastAgrId, String paymentFor) {
		try {
			AgrMasterAgreement masterAgr = agrRepo.findByMastAgrId(mastAgrId);

			String[] values = new String[5];
			List<VMstPayRuleManager> listOfPayRuleMgr = vMstMgrRepo.findByDecKeyNotNullOrderByColumnName();

			for (VMstPayRuleManager ruleManager : listOfPayRuleMgr) {
				if ((ruleManager.getDecKey().equalsIgnoreCase("PORTFOLIO")) && (masterAgr.getPortfolioCode() != null)) {
					values[0] = masterAgr.getPortfolioCode();
				}
				if ((ruleManager.getDecKey().equalsIgnoreCase("PRODUCT")) && (masterAgr.getProductCode() != null)) {
					values[1] = masterAgr.getProductCode();
				}
				if ((ruleManager.getDecKey().equalsIgnoreCase("NPASTATUS")) && (masterAgr.getNpaStatus() != null)) {
					values[2] = masterAgr.getNpaStatus();
				}
				if ((ruleManager.getDecKey().equalsIgnoreCase("ASSETCLASS")) && (masterAgr.getAssetClass() != null)) {
					values[3] = masterAgr.getAssetClass();
				}
				if ((ruleManager.getDecKey().equalsIgnoreCase("DPD")) && (masterAgr.getDpd() != null)) {
					values[4] = masterAgr.getDpd().toString();
				}
			}
			log.info("values " + values[0] + values[1] + values[2] + values[3] + values[4]);
			String ruleId = ruleMdrDtlRepo.getRuleId(values[0], values[1], values[2], values[3], values[4]);
			if (ruleId == null) {
				ruleId = "DEFAULT_RULE";
			}
			return ruleId;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String manualLoanApportionmentPaymentApplication(String mastAgrId, Integer instrumentId, Date tranDate) {
		String result = "sucess";
		AgrTrnTranHeader tranHdr = null;
		AgrTrnEventDtl eventDtl = null;
		try {
			List<TrnInsInstrumentAlloc> instAllocList = instAllocRepo
					.findByInstrumentInstrumentIdAndInstrumentMasterAgrAndInstrumentInstrumentStatus(instrumentId,
							mastAgrId, "CLR");

			TrnInsInstrument inst = instRepo.findByInstrumentId(instrumentId);

			if (instAllocList.size() <= 0) {
				throw new CoreDataNotFoundException("No instrument found for payment application. ");
			}

			String paymentFor = null;
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(mastAgrId);
			List<TabOrganization> orgData = orgRepo.findAll();
			Date businessDate = null;

			if (orgData.size() <= 0) {
				throw new CoreDataNotFoundException("No Organization details available");
			}

			for (TabOrganization org : orgData) {
				businessDate = org.getDtBusiness();
			}
			for (int i = 0; i < instAllocList.size(); i++) {
				if (i == 0) {
					switch (instAllocList.get(0).getInstrument().getPayType()) {
					case "INSTALLMENT":
						paymentFor = "INSTALLMENT";
						break;

					case "CHARGES":
						paymentFor = "FEE";
						break;

					case "DEBIT_NOTE":
						paymentFor = "DEBIT_NOTE";
						break;
					}

					tranHdr = new AgrTrnTranHeader();
					tranHdr.setMasterAgr(agrRepo.findByMastAgrId(mastAgrId));
					tranHdr.setTranDate(tranDate);
					tranHdr.setTranType("RECEIPT");
					tranHdr.setRemark(
							commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", inst.getInstrumentType())
									+ " Receipt");
					tranHdr.setSource(instAllocList.get(0).getInstrument().getSource());
					tranHdr.setReqId(instAllocList.get(0).getInstrument().getSourceId());
					tranHdr.setIntrumentId(instrumentId);
					tranHdr.setUserID("SYSTEM");

					eventDtl = new AgrTrnEventDtl();
					eventDtl.setTranHeader(tranHdr);
					eventDtl.setTranEvent(paymentFor);
					eventDtl.setTranAmount(instAllocList.get(0).getInstrument().getInstrumentAmount());
					eventDtl.setUserId("SYSTEM");

				}

				String paymentRule = getPaymentRule(mastAgrId, paymentFor);
				List<RuleDetailsDto> ruleDetails = vRuleSetRepo.getRuleDetails(mastAgrId, paymentRule, paymentFor);
				/*
				 * if (ruleDetails.size() <= 0) { throw new
				 * CoreDataNotFoundException("Rule details not found "); }
				 */

				Double amount = 0.0;
				Double totalAmount = commonService.numberFormatter(instAllocList.get(i).getApportionAmount());

				for (RuleDetailsDto ruleDto : ruleDetails) {
					if (commonService.numberFormatter(ruleDto.getDueAmount()) > totalAmount) {
						amount = totalAmount;
					} else {
						amount = commonService.numberFormatter(ruleDto.getDueAmount());
					}
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
						tranDtl.setDtDueDate(tranDate);

						AgrTrnDueDetails dueDtlId = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId());
						dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
						dueDetailRepo.save(dueDtlId);
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
						totalAmount = totalAmount - amount;
						tranDtlRepo.save(tranDtl);
					} else {
						break;
					}
				}

				if (totalAmount > 0) {
					AgrTrnTranDetail tranDtl2 = new AgrTrnTranDetail();
					tranDtl2.setEventDtl(eventDtl);
					tranDtl2.setMasterAgr(masterAgrObj);
					tranDtl2.setTranCategory("EXCESS");
					tranDtl2.setTranHead("EXCESS_AMOUNT");
					tranDtl2.setTranAmount(totalAmount * -1);
					tranDtl2.setTranSide("CR");
					tranDtl2.setDtDueDate(tranDate);

					updateExcess(mastAgrId, totalAmount, "ADD");
					totalAmount = 0.0;
					tranDtlRepo.save(tranDtl2);
				}

			}

			dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtl);

		} catch (CoreDataNotFoundException e) {
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String manualChargesApportionmentPaymentApplication(String mastAgrId, Integer instrumentId, Date tranDate) {
		String result = "sucess";
		AgrTrnTranHeader tranHdr = null;
		AgrTrnEventDtl eventDtl = null;
		Double totalTax = 0d;
		Double totalFee = 0d;
		try {
			List<TrnInsInstrumentAlloc> instAllocList = instAllocRepo
					.findByInstrumentInstrumentIdAndInstrumentMasterAgrAndInstrumentInstrumentStatus(instrumentId,
							mastAgrId, "CLR");

			if (instAllocList.size() <= 0) {
				throw new CoreDataNotFoundException("No instrument found for payment application. ");
			}

			TrnInsInstrument inst = instRepo.findByInstrumentId(instrumentId);

			String paymentFor = null;
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(mastAgrId);
			List<TabOrganization> orgData = orgRepo.findAll();
			Date businessDate = null;

			if (orgData.size() <= 0) {
				throw new CoreDataNotFoundException("No Organization details available");
			}

			for (TabOrganization org : orgData) {
				businessDate = org.getDtBusiness();
			}
			for (int i = 0; i < instAllocList.size(); i++) {
				if (i == 0) {
					switch (instAllocList.get(0).getInstrument().getPayType()) {
					case "INSTALLMENT":
						paymentFor = "INSTALLMENT";
						break;

					case "CHARGES":
						paymentFor = "FEE";
						break;

					case "DEBIT_NOTE":
						paymentFor = "DEBIT_NOTE";
						break;
					}

					tranHdr = new AgrTrnTranHeader();
					tranHdr.setMasterAgr(agrRepo.findByMastAgrId(mastAgrId));
					tranHdr.setTranDate(tranDate);
					tranHdr.setTranType("RECEIPT");
					tranHdr.setRemark(
							commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", inst.getInstrumentType())
									+ " Receipt");
					tranHdr.setSource(instAllocList.get(0).getInstrument().getSource());
					tranHdr.setReqId(instAllocList.get(0).getInstrument().getSourceId());
					tranHdr.setIntrumentId(instrumentId);
					tranHdr.setUserID("SYSTEM");

					eventDtl = new AgrTrnEventDtl();
					eventDtl.setTranHeader(tranHdr);
					eventDtl.setTranEvent(paymentFor);
					eventDtl.setTranAmount(instAllocList.get(0).getInstrument().getInstrumentAmount());
					eventDtl.setUserId("SYSTEM");

				}

				String paymentRule = getPaymentRule(mastAgrId, paymentFor);
				List<RuleDetailsDto> ruleDetails = vRuleSetRepo.getRuleDetailsChargesManual(mastAgrId, paymentRule,
						paymentFor, instAllocList.get(i).getTranCategory(), instAllocList.get(i).getTranHead());
				/*
				 * if (ruleDetails.size() <= 0) { throw new
				 * CoreDataNotFoundException("Rule details not found "); }
				 */

				Double rowAmount = commonService.numberFormatter(instAllocList.get(i).getApportionAmount());
				Double amount = 0.0;
				Double totalAmount = commonService.numberFormatter(instAllocList.get(i).getApportionAmount());
				for (RuleDetailsDto ruleDto : ruleDetails) {
					String onlyTax = null;
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
							tranDtl.setEventDtl(eventDtl);
							tranDtl.setMasterAgr(masterAgrObj);
							tranDtl.setTranCategory(ruleDto.getDueCategory());
							tranDtl.setTranHead(ruleDto.getPayHead());
							tranDtl.setTranAmount(amount * (-1));
							tranDtl.setDtlRemark(ruleDto.getPayHead() + " recovered");
							tranDtl.setTranSide("CR");
							tranDtl.setDtDueDate(tranDate);
							tranDtl.setInstallmentNo(ruleDto.getInstallmentNo());
							List<AgrTrnTaxDueDetails> taxDueList = new ArrayList<AgrTrnTaxDueDetails>();
							if (ruleDto.getDueCategory().equals("FEE")) {

								taxDueList = taxRepo.findByDueDetailDueDtlId(ruleDto.getDueDtlId());
								for (AgrTrnTaxDueDetails taxDue : taxDueList) {
									totalTax += taxDue.getDueTaxAmount();
								}

								totalFee = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId()).getDueAmount();
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
										tranDtlTax.setEventDtl(eventDtl);
										tranDtlTax.setMasterAgr(masterAgrObj);
										tranDtlTax.setTranCategory("TAX");
										tranDtlTax.setTranHead(taxDue.getTaxHead());
										tranDtlTax.setTranAmount(taxAmount * (-1));
										tranDtlTax.setTranSide("CR");
										tranDtlTax.setDtlRemark(taxDue.getTaxHead() + " recovered");
										tranDtlTax.setInstallmentNo(-1);
										tranDtlTax.setDtDueDate(tranDate);
										tranDtlRepo.save(tranDtlTax);

										AgrTrnTaxDueDetails taxDueUpdate = taxRepo.findByTaxDueId(taxDue.getTaxDueId());
										remTaxAmount = remTaxAmount - taxDue.getDueTaxAmount();
										rowAmount = rowAmount - taxDue.getDueTaxAmount();
										taxDueUpdate.setDueTaxAmount(
												taxDueUpdate.getDueTaxAmount() - taxDue.getDueTaxAmount());

										taxRepo.save(taxDueUpdate);

									}

								}

								AgrTrnTranDetail tranDtlUpdate = tranDtlRepo.findByTranDtlId(ruleDto.getTranDtlId());
								tranDtlUpdate.setTranAmount(rowAmount);
								tranDtlRepo.save(tranDtlUpdate);

							}

							AgrTrnDueDetails dueDtlId = dueDetailRepo.findByDueDtlId(ruleDto.getDueDtlId());
							dueDtlId.setDueAmount(commonService.numberFormatter(dueDtlId.getDueAmount() - amount));
							dueDetailRepo.save(dueDtlId);
							/*
							 * if (ruleDto.getDueCategory().equals("INSTALLMENT")) { AgrRepaySchedule
							 * schedule = scheduleRepo.findByMasterAgrIdAndInstallmentNo(mastAgrId,
							 * ruleDto.getInstallmentNo());
							 * 
							 * if (schedule == null) { throw new CoreDataNotFoundException(
							 * "No data available in schedule for master agreement " + mastAgrId + " and " +
							 * ruleDto.getInstallmentNo()); }
							 * 
							 * schedule.setDtPaymentDate(businessDate); scheduleRepo.save(schedule); }
							 */
							rowAmount = rowAmount - amount;
							tranDtlRepo.save(tranDtl);

						}

					}
				}
				if (rowAmount > 0) {
					AgrTrnTranDetail tranDtl2 = new AgrTrnTranDetail();
					tranDtl2.setEventDtl(eventDtl);
					tranDtl2.setMasterAgr(masterAgrObj);
					tranDtl2.setTranCategory("EXCESS");
					tranDtl2.setTranHead("EXCESS_AMOUNT");
					tranDtl2.setTranAmount(totalAmount * -1);
					tranDtl2.setTranSide("CR");
					tranDtl2.setDtDueDate(tranDate);

					updateExcess(mastAgrId, totalAmount, "ADD");
					totalAmount = 0.0;
					tranDtlRepo.save(tranDtl2);
				}

			}

			dueDetailRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(mastAgrId, 0d);
			hdrRepo.save(tranHdr);
			eventRepo.save(eventDtl);

		} catch (CoreDataNotFoundException e) {
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String paymentRelease(String customerId, Date businessDate) throws Exception, CoreDataNotFoundException {
		String result = "success";
		try {
			List<TrnInsInstrument> instList = instRepo
					.findByCustomerIdAndPayAppliedYnAndPayBounceYnAndDtStatusUpdateAndInstrumentStatusIn(customerId,
							"N", "N", businessDate, Arrays.asList("CLR", "BOU"));
			/*
			 * if (instList.size() == 0) { throw new
			 * CoreDataNotFoundException("Instrument data not found for customer " +
			 * customerId); }
			 */
			log.info(Integer.toString(instList.size()));
			for (TrnInsInstrument inst : instList) {
				if (inst.getInstrumentStatus().equalsIgnoreCase("CLR")) {
					List<TrnInsInstrumentAlloc> allocList = instAllocRepo
							.findByInstrumentInstrumentIdAndTranHeadNotNull(inst.getInstrumentId());
					if (allocList.size() > 0) {
						this.manualChargesApportionmentPaymentApplication(inst.getMasterAgr(), inst.getInstrumentId(),
								businessDate);
					} else {
						List<TrnInsInstrumentAlloc> allocLoanList = instAllocRepo
								.findByInstrumentInstrumentIdAndApportionAmountGreaterThanAndTranHeadNotNull(
										inst.getInstrumentId(), 0d);
						log.info(" allocLoanList " + allocLoanList.size() + " Instrument Id " + inst.getInstrumentId());
						if (allocLoanList.size() > 0) {
							log.info(" manualLoanApportionmentPaymentApplication ");
							this.manualLoanApportionmentPaymentApplication(inst.getMasterAgr(), inst.getInstrumentId(),
									businessDate);
						} else {
							log.info(" agreementAutoApportionmentPaymentApplication ");
							String autoApporResult = autoApportionment.agreementAutoApportionmentPaymentApplication(
									inst.getMasterAgr(), inst.getInstrumentId(), businessDate);
						}
					}

					inst.setPayAppliedYn("Y");
					inst.setDtPayApplied(businessDate);

					instRepo.save(inst);
				} else {
					log.info("Payment Reversal Call");
					String paymentReversal = paymentRevService.paymentReversal(inst.getMasterAgr(),
							inst.getInstrumentId(), businessDate);

					/*
					 * inst.setPayBounceYn("Y"); inst.setDtPayBounce(businessDate);
					 * instRepo.save(inst);
					 */
				}
			}

		} catch (CoreDataNotFoundException e) {
			result = "fail";
			throw e;
		} catch (Exception e) {
			result = "fail";
			throw e;
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public void updateLimit(String mastAgrId, Double amount, String method, String purpose, Integer refTranId,
			String userId) {
		try {
			log.info("updateLimit ");
			AgrMasterAgreement masterAgr = agrRepo.findByMastAgrId(mastAgrId);
			AgrCustLimitSetup custLimit = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(mastAgrId, "B");
			if (method.equalsIgnoreCase("ADD")) {
				log.info("updateLimit 1");
				custLimit.setUtilizedLimit(commonService.numberFormatter(custLimit.getUtilizedLimit() - amount));
				custLimit.setAvailableLimit(commonService.numberFormatter(custLimit.getAvailableLimit() + amount));
				custLimit.setDtLastUpdated(LocalDate.now());
			} else {
				if (method.equals("DED") && purpose.equals("DROP")) {
					/*
					 * if (custLimit.getUtilizedLimit() > masterAgr.getCurrentDroppedLimit()) {
					 * custLimit
					 * .setUtilizedLimit(commonService.numberFormatter(custLimit.getUtilizedLimit()
					 * + amount)); }
					 */
				} else {
					custLimit.setUtilizedLimit(commonService.numberFormatter(custLimit.getUtilizedLimit() + amount));
				}
				custLimit.setAvailableLimit(commonService.numberFormatter(custLimit.getAvailableLimit() - amount));
				custLimit.setDtLastUpdated(LocalDate.now());
			}

			log.info("updateLimit 2");
			AgrTrnLimitDtls agrLimit = new AgrTrnLimitDtls();
			agrLimit.setMasterAgreement(masterAgr);
			agrLimit.setTranAmount(amount);
			agrLimit.setTranType(method);
			agrLimit.setPurpose(purpose);
			agrLimit.setRefTranId(refTranId);
			agrLimit.setUserId(userId);
			agrLimit.setAvailableLimit(custLimit.getAvailableLimit());
			agrLimit.setCurrentDroppedLimit(masterAgr.getCurrentDroppedLimit());
			agrLimit.setLimitSanctionAmount(custLimit.getLimitSanctionAmount());
			agrLimit.setUtilizedLimit(custLimit.getUtilizedLimit());
			log.info("updateLimit 3");
			agrLimitRepo.save(agrLimit);
			log.info("updateLimit 4");
			if (purpose.equalsIgnoreCase("BOUNCE")) {
				AgrTrnLimitDtls agrLimitBounce = new AgrTrnLimitDtls();
				agrLimitBounce.setMasterAgreement(masterAgr);
				agrLimitBounce.setTranAmount(0d);
				agrLimitBounce.setTranType(method);
				agrLimitBounce.setPurpose("LIMIT_FREEZED");
				agrLimitBounce.setRefTranId(refTranId);
				agrLimitBounce.setUserId(userId);
				agrLimitBounce.setAvailableLimit(custLimit.getAvailableLimit());
				agrLimitBounce.setCurrentDroppedLimit(masterAgr.getCurrentDroppedLimit());
				agrLimitBounce.setLimitSanctionAmount(custLimit.getLimitSanctionAmount());
				agrLimitBounce.setUtilizedLimit(custLimit.getUtilizedLimit());
				agrLimitRepo.save(agrLimitBounce);

				customer.setLimitFreezYn("Y");
				custRepo.save(customer);
			}
			log.info("updateLimit 5");
			limitRepo.save(custLimit);
			log.info("updateLimit 6");
		} catch (Exception e) {
			throw e;
		}
	}

	
}
