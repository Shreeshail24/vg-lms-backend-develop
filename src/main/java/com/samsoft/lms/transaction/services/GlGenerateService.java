package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnEventDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.transaction.dto.GlGenerateDto;
import com.samsoft.lms.transaction.entities.AgrTrnGlTranDtl;
import com.samsoft.lms.transaction.entities.GlErrorLog;
import com.samsoft.lms.transaction.entities.VLmsGlSysTranIntAccrual;
import com.samsoft.lms.transaction.entities.VLmsGlTranDtl;
import com.samsoft.lms.transaction.entities.VMstGlConfig;
import com.samsoft.lms.transaction.repositories.AgrTrnGlTranDtlRepository;
import com.samsoft.lms.transaction.repositories.GlErrorLogRepository;
import com.samsoft.lms.transaction.repositories.VLmsGlSysTranIntAccrualRepository;
import com.samsoft.lms.transaction.repositories.VLmsGlTranDtlRepository;
import com.samsoft.lms.transaction.repositories.VMstGlConfigRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class GlGenerateService {
	@Autowired
	private AgrTrnGlTranDtlRepository glTranRepo;

	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private VLmsGlTranDtlRepository vLmsGlTranRepo;

	@Autowired
	private VMstGlConfigRepository vMstGlConfigRepo;

	@Autowired
	private GlErrorLogRepository glErrorRepo;

	@Autowired
	private AgrTrnEventDtlRepository eventRepo;

	@Autowired
	private TrnInsInstrumentRepository instRepo;

	@Autowired
	private VLmsGlSysTranIntAccrualRepository glSysTranRepo;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String glGenerate(GlGenerateDto glDto) throws Exception {
		String result = "success";
		boolean cashBankEntry = false;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		AgrTrnGlTranDtl finTranReceipt = null;
		List<AgrTrnGlTranDtl> finTranDebitList = new ArrayList<>();
		List<AgrTrnGlTranDtl> finTranCreditList = new ArrayList<>();
		try {

			if (glDto.getRegenerateYn().equalsIgnoreCase("Y")) {
				List<AgrTrnGlTranDtl> glTranListDuplicateEventId = glTranRepo
						.findByCustomerIdAndDownloadYnAndDtTranDateBetweenAndGlEventNotIn(glDto.getCustomerId(), "N",
								sdf.parse(glDto.getFromDate()), sdf.parse(glDto.getToDate()),
								Arrays.asList(new String[] { "INTEREST_ACCRUAL", "INTEREST_ACCRUAL_REVERSAL",
										"PROVISIONING", "PROVISIONING_REV" }));

				List<AgrTrnGlTranDtl> glTranList = new ArrayList<>();
				for (AgrTrnGlTranDtl glTranSet : glTranListDuplicateEventId) {
					HashSet<Integer> set = new HashSet<Integer>();
					if (!(set.contains(glTranSet.getEventId()))) {
						glTranList.add(glTranSet);
					}

				}

				for (AgrTrnGlTranDtl glTran : glTranList) {
					log.info(glTran.getEventId().toString());
					AgrTrnEventDtl event2 = eventRepo.findByEventIdAndGlGeneratedYnIn(glTran.getEventId(),
							Arrays.asList(new String[] { "Y", "E" }));
					if (event2 != null) {
						event2.setGlGeneratedYn("N");
						eventRepo.save(event2);
					}
					glTranRepo.delete(glTran);

				}
			}

			List<AgrMasterAgreement> listMasterObj = masterRepo.findDistinctByCustomerId(glDto.getCustomerId());

			List<String> listMasterId = listMasterObj.stream().map(t -> t.getMastAgrId()).collect(Collectors.toList());

			List<AgrTrnTranHeader> listTranHdr = hdrRepo
					.findByTranDateBetweenAndMasterAgrMastAgrIdInOrderByMasterAgrMastAgrIdAscTranIdAsc(
							sdf.parse(glDto.getFromDate()), sdf.parse(glDto.getToDate()), listMasterId);

			int srNo = 0;
			for (AgrTrnTranHeader tranHdr : listTranHdr) {
				List<AgrTrnEventDtl> eventList = eventRepo
						.findByTranHeaderTranIdAndGlGeneratedYnOrderByEventId(tranHdr.getTranId(), "N");

				for (AgrTrnEventDtl event : eventList) {
					String glEvent = "";
					event.setGlGeneratedYn("Y");
					finTranDebitList.clear();
					finTranCreditList.clear();
					log.info("{} {} ", tranHdr.getTranId(), event.getEventId());
					List<VLmsGlTranDtl> vLmsGlList = vLmsGlTranRepo
							.findByTranIdAndTranAmountGreaterThanAndEventIdOrderByTranDtlId(tranHdr.getTranId(), 0d,
									event.getEventId());

					if (event.getTranEvent().contains("RECEIPT") || event.getTranEvent().equals("ALLDUES")
							|| event.getTranEvent().equals("CHARGES") || event.getTranEvent().equals("FEE")
							|| event.getTranEvent().equals("INSTALLMENT")) {
						glEvent = "RECEIPT";
					} else {
						glEvent = event.getTranEvent();
					}

					for (VLmsGlTranDtl vLmsGl : vLmsGlList) {

						String glHeadCode = "";
						srNo++;

						log.info("{} {} {} {}", vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
								vLmsGl.getTranHead());
						VMstGlConfig vMstGlConfig = vMstGlConfigRepo
								.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranchAndNbfc(
										vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
										vLmsGl.getTranHead(), vLmsGl.getServBranch(), vLmsGl.getNbfc());

						if (vMstGlConfig == null) {

							vMstGlConfig = vMstGlConfigRepo
									.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranch(
											vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
											vLmsGl.getTranHead(), vLmsGl.getServBranch());

							if (vMstGlConfig == null) {
								vMstGlConfig = vMstGlConfigRepo
										.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndNbfc(
												vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
												vLmsGl.getTranHead(), vLmsGl.getNbfc());

								if (vMstGlConfig == null) {

									vMstGlConfig = vMstGlConfigRepo
											.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHead(
													vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
													vLmsGl.getTranHead());
								}

							}

						}
						if (vMstGlConfig != null) {

							if (glEvent.equalsIgnoreCase("RECEIPT") || glEvent.equalsIgnoreCase("EXCESS_ADJ")) {
								if (cashBankEntry == false) {

									TrnInsInstrument instrument = instRepo.findByInstrumentId(vLmsGl.getInstrumentId());
									if (instrument.getInstrumentType().equals("CASH")
											|| instrument.getInstrumentType().equals("CA")) {
										glHeadCode = "CASH_ACCOUNT";
									} else if (instrument.getInstrumentType().equals("EXCESS")
											|| instrument.getInstrumentType().equals("EX")) {
										glHeadCode = "EXCESS ACCOUNT";
									} else {
										glHeadCode = "BANK_ACCOUNT";
									}

									finTranReceipt = new AgrTrnGlTranDtl();
									finTranReceipt.setCustomerId(glDto.getCustomerId());
									finTranReceipt.setDownloadYn("N");
									finTranReceipt.setDrCr("DR");
									finTranReceipt.setDtTranDate(vLmsGl.getDtTranDate());
									finTranReceipt.setDtValueDate(vLmsGl.getDtTranDate());
									finTranReceipt.setGlEvent(glEvent);
									finTranReceipt.setGlHeadCode(glHeadCode);
									finTranReceipt.setGlTranId(vLmsGl.getTranId());
									finTranReceipt.setEventId(event.getEventId());
									finTranReceipt.setHomeBranch(vLmsGl.getHomeBranch());
									finTranReceipt.setLoanId(vLmsGl.getLoanId());
									finTranReceipt.setMasterAgrId(vLmsGl.getMastAgrId());
									finTranReceipt.setNarration(tranHdr.getRemark());
									finTranReceipt.setGlTranSrNo(srNo);
									finTranReceipt.setPortfolioCode(vLmsGl.getPortfolioCode());
									finTranReceipt.setProdCode(vLmsGl.getProductCode());
									finTranReceipt.setServBranch(vLmsGl.getServBranch());
									finTranReceipt.setTranAmount(instrument.getInstrumentAmount());
									finTranReceipt.setTranDtlId(0);
									finTranReceipt.setUserID(glDto.getUserId());
									finTranReceipt.setVoucherType("JV");

									// glTranRepo.save(finTranReceipt);

									cashBankEntry = true;
								}
							}

							if (vMstGlConfig.getDebitGlHead().length() > 0) {
								AgrTrnGlTranDtl finTranDebit = new AgrTrnGlTranDtl();
								finTranDebit.setCustomerId(glDto.getCustomerId());
								finTranDebit.setDownloadYn("N");
								finTranDebit.setDrCr("DR");
								finTranDebit.setDtTranDate(vLmsGl.getDtTranDate());
								finTranDebit.setDtValueDate(vLmsGl.getDtTranDate());
								finTranDebit.setGlEvent(glEvent);
								finTranDebit.setGlHeadCode(vMstGlConfig.getDebitGlHead());
								finTranDebit.setGlTranId(vLmsGl.getTranId());
								finTranDebit.setEventId(event.getEventId());
								finTranDebit.setHomeBranch(vLmsGl.getHomeBranch());
								finTranDebit.setLoanId(vLmsGl.getLoanId());
								finTranDebit.setMasterAgrId(vLmsGl.getMastAgrId());
								finTranDebit.setNarration(tranHdr.getRemark());
								finTranDebit.setGlTranSrNo(srNo);
								finTranDebit.setPortfolioCode(vLmsGl.getPortfolioCode());
								finTranDebit.setProdCode(vLmsGl.getProductCode());
								finTranDebit.setServBranch(vLmsGl.getServBranch());
								finTranDebit.setTranAmount(vLmsGl.getTranAmount());
								finTranDebit.setTranDtlId(vLmsGl.getTranDtlId());
								finTranDebit.setUserID(glDto.getUserId());
								finTranDebit.setVoucherType("JV");

								finTranDebitList.add(finTranDebit);

							}

							if (vMstGlConfig.getCreditGlHead().length() > 0) {
								AgrTrnGlTranDtl finTranCredit = new AgrTrnGlTranDtl();
								finTranCredit.setCustomerId(glDto.getCustomerId());
								finTranCredit.setDownloadYn("N");
								finTranCredit.setDrCr("CR");
								finTranCredit.setDtTranDate(vLmsGl.getDtTranDate());
								finTranCredit.setDtValueDate(vLmsGl.getDtTranDate());
								finTranCredit.setGlEvent(glEvent);
								finTranCredit.setGlHeadCode(vMstGlConfig.getCreditGlHead());
								finTranCredit.setGlTranId(vLmsGl.getTranId());
								finTranCredit.setEventId(event.getEventId());
								finTranCredit.setHomeBranch(vLmsGl.getHomeBranch());
								finTranCredit.setLoanId(vLmsGl.getLoanId());
								finTranCredit.setMasterAgrId(vLmsGl.getMastAgrId());
								finTranCredit.setNarration(tranHdr.getRemark());
								finTranCredit.setGlTranSrNo(srNo);
								finTranCredit.setPortfolioCode(vLmsGl.getPortfolioCode());
								finTranCredit.setProdCode(vLmsGl.getProductCode());
								finTranCredit.setServBranch(vLmsGl.getServBranch());
								finTranCredit.setTranAmount(vLmsGl.getTranAmount());
								finTranCredit.setTranDtlId(vLmsGl.getTranDtlId());
								finTranCredit.setUserID(glDto.getUserId());
								finTranCredit.setVoucherType("JV");

								finTranCreditList.add(finTranCredit);

							}
						} else {
							GlErrorLog error = new GlErrorLog("No debit head configuration found for Portfolio "
									+ vLmsGl.getPortfolioCode() + " Event " + glEvent + " TranCategory "
									+ vLmsGl.getTranCategory() + " TranType " + vLmsGl.getTranType() + " TranHead "
									+ vLmsGl.getTranHead());
							glErrorRepo.save(error);
							event.setGlGeneratedYn("E");
							eventRepo.save(event);
							break;

						}
					}
					cashBankEntry = false;

					if (!(event.getGlGeneratedYn().equals("E"))) {

						if (finTranReceipt != null) {
							glTranRepo.save(finTranReceipt);
						}

						if (finTranDebitList.size() > 0) {
							glTranRepo.saveAll(finTranDebitList);
						}

						if (finTranCreditList.size() > 0) {
							glTranRepo.saveAll(finTranCreditList);
						}

						event.setGlGeneratedYn("Y");
						eventRepo.save(event);
					}

				}

			}

			if (glDto.getRegenerateYn().equalsIgnoreCase("Y")) {
				List<AgrTrnGlTranDtl> glTranListDuplicateEventId = glTranRepo
						.findByCustomerIdAndDownloadYnAndDtTranDateBetweenAndGlEventIn(glDto.getCustomerId(), "N",
								sdf.parse(glDto.getFromDate()), sdf.parse(glDto.getToDate()),
								Arrays.asList(new String[] { "INTEREST_ACCRUAL", "INTEREST_ACCRUAL_REVERSAL" }));

				List<AgrTrnGlTranDtl> glTranList = new ArrayList<>();
				for (AgrTrnGlTranDtl glTranSet : glTranListDuplicateEventId) {
					HashSet<Integer> set = new HashSet<Integer>();
					if (!(set.contains(glTranSet.getEventId()))) {
						glTranList.add(glTranSet);
					}

				}

				for (AgrTrnGlTranDtl glTran : glTranList) {
					log.info(glTran.getEventId().toString());
					// pending logic for updating sys tran table
					glTranRepo.delete(glTran);

				}

			}

			List<AgrMasterAgreement> listMasterObj1 = masterRepo.findDistinctByCustomerId(glDto.getCustomerId());

			List<String> listMasterId1 = listMasterObj1.stream().map(t -> t.getMastAgrId())
					.collect(Collectors.toList());

			List<VLmsGlSysTranIntAccrual> listTranHdr1 = glSysTranRepo
					.findByGlGeneratedYnAndDtTranDateBetweenAndMastAgrIdInAndTranAmountGreaterThanOrderByMastAgrId(
							"N", sdf.parse(glDto.getFromDate()), sdf.parse(glDto.getToDate()), listMasterId1, 0d);

			int srNo1 = 0;
			for (VLmsGlSysTranIntAccrual sysTran : listTranHdr1) {
				List<AgrTrnEventDtl> eventList = eventRepo
						.findByTranHeaderTranIdAndGlGeneratedYnOrderByEventId(sysTran.getTranId(), "N");

				for (AgrTrnEventDtl event : eventList) {
					String glEvent = "";
					event.setGlGeneratedYn("Y");
					finTranDebitList.clear();
					finTranCreditList.clear();
					log.info("{} {} ", sysTran.getTranId(), event.getEventId());
					List<VLmsGlTranDtl> vLmsGlList = vLmsGlTranRepo
							.findByTranIdAndTranAmountGreaterThanAndEventIdOrderByTranDtlId(sysTran.getTranId(), 0d,
									event.getEventId());

					if (event.getTranEvent().contains("RECEIPT") || event.getTranEvent().equals("ALLDUES")
							|| event.getTranEvent().equals("CHARGES") || event.getTranEvent().equals("FEE")
							|| event.getTranEvent().equals("INSTALLMENT")) {
						glEvent = "RECEIPT";
					} else {
						glEvent = event.getTranEvent();
					}

					for (VLmsGlTranDtl vLmsGl : vLmsGlList) {

						String glHeadCode = "";
						srNo1++;

						log.info("{} {} {} {}", vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
								vLmsGl.getTranHead());
						VMstGlConfig vMstGlConfig = vMstGlConfigRepo
								.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranchAndNbfc(
										vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
										vLmsGl.getTranHead(), vLmsGl.getServBranch(), vLmsGl.getNbfc());

						if (vMstGlConfig == null) {

							vMstGlConfig = vMstGlConfigRepo
									.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranch(
											vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
											vLmsGl.getTranHead(), vLmsGl.getServBranch());

							if (vMstGlConfig == null) {
								vMstGlConfig = vMstGlConfigRepo
										.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndNbfc(
												vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
												vLmsGl.getTranHead(), vLmsGl.getNbfc());

								if (vMstGlConfig == null) {

									vMstGlConfig = vMstGlConfigRepo
											.findByPortfolioCodeAndGlEventAndTranCategoryAndTranHead(
													vLmsGl.getPortfolioCode(), glEvent, vLmsGl.getTranCategory(),
													vLmsGl.getTranHead());
								}

							}

						}
						if (vMstGlConfig != null) {
							TrnInsInstrument instrument = instRepo.findByInstrumentId(vLmsGl.getInstrumentId());
							/*
							 * if (glEvent.equalsIgnoreCase("RECEIPT") ||
							 * glEvent.equalsIgnoreCase("EXCESS_ADJ")) { if (cashBankEntry == false) {
							 * 
							 * TrnInsInstrument instrument =
							 * instRepo.findByInstrumentId(vLmsGl.getInstrumentId()); if
							 * (instrument.getInstrumentType().equals("CASH") ||
							 * instrument.getInstrumentType().equals("CA")) { glHeadCode = "CASH_ACCOUNT"; }
							 * else if (instrument.getInstrumentType().equals("EXCESS") ||
							 * instrument.getInstrumentType().equals("EX")) { glHeadCode = "EXCESS ACCOUNT";
							 * } else { glHeadCode = "BANK_ACCOUNT"; }
							 */

							finTranReceipt = new AgrTrnGlTranDtl();
							finTranReceipt.setCustomerId(glDto.getCustomerId());
							finTranReceipt.setDownloadYn("N");
							finTranReceipt.setDrCr("DR");
							finTranReceipt.setDtTranDate(vLmsGl.getDtTranDate());
							finTranReceipt.setDtValueDate(vLmsGl.getDtTranDate());
							finTranReceipt.setGlEvent(glEvent);
							finTranReceipt.setGlHeadCode(glHeadCode);
							finTranReceipt.setGlTranId(vLmsGl.getTranId());
							finTranReceipt.setEventId(event.getEventId());
							finTranReceipt.setHomeBranch(vLmsGl.getHomeBranch());
							finTranReceipt.setLoanId(vLmsGl.getLoanId());
							finTranReceipt.setMasterAgrId(vLmsGl.getMastAgrId());
							finTranReceipt.setNarration(sysTran.getRemark());
							finTranReceipt.setGlTranSrNo(srNo1);
							finTranReceipt.setPortfolioCode(vLmsGl.getPortfolioCode());
							finTranReceipt.setProdCode(vLmsGl.getProductCode());
							finTranReceipt.setServBranch(vLmsGl.getServBranch());
							finTranReceipt.setTranAmount(instrument.getInstrumentAmount());
							finTranReceipt.setTranDtlId(0);
							finTranReceipt.setUserID(glDto.getUserId());
							finTranReceipt.setVoucherType("JV");

							// glTranRepo.save(finTranReceipt);

							cashBankEntry = true;
							// }
							// }

							if (vMstGlConfig.getDebitGlHead().length() > 0) {
								AgrTrnGlTranDtl finTranDebit = new AgrTrnGlTranDtl();
								finTranDebit.setCustomerId(glDto.getCustomerId());
								finTranDebit.setDownloadYn("N");
								finTranDebit.setDrCr("DR");
								finTranDebit.setDtTranDate(vLmsGl.getDtTranDate());
								finTranDebit.setDtValueDate(vLmsGl.getDtTranDate());
								finTranDebit.setGlEvent(glEvent);
								finTranDebit.setGlHeadCode(vMstGlConfig.getDebitGlHead());
								finTranDebit.setGlTranId(vLmsGl.getTranId());
								finTranDebit.setEventId(event.getEventId());
								finTranDebit.setHomeBranch(vLmsGl.getHomeBranch());
								finTranDebit.setLoanId(vLmsGl.getLoanId());
								finTranDebit.setMasterAgrId(vLmsGl.getMastAgrId());
								finTranDebit.setNarration(sysTran.getRemark());
								finTranDebit.setGlTranSrNo(srNo1);
								finTranDebit.setPortfolioCode(vLmsGl.getPortfolioCode());
								finTranDebit.setProdCode(vLmsGl.getProductCode());
								finTranDebit.setServBranch(vLmsGl.getServBranch());
								finTranDebit.setTranAmount(vLmsGl.getTranAmount());
								finTranDebit.setTranDtlId(vLmsGl.getTranDtlId());
								finTranDebit.setUserID(glDto.getUserId());
								finTranDebit.setVoucherType("JV");

								finTranDebitList.add(finTranDebit);

							}

							if (vMstGlConfig.getCreditGlHead().length() > 0) {
								AgrTrnGlTranDtl finTranCredit = new AgrTrnGlTranDtl();
								finTranCredit.setCustomerId(glDto.getCustomerId());
								finTranCredit.setDownloadYn("N");
								finTranCredit.setDrCr("CR");
								finTranCredit.setDtTranDate(vLmsGl.getDtTranDate());
								finTranCredit.setDtValueDate(vLmsGl.getDtTranDate());
								finTranCredit.setGlEvent(glEvent);
								finTranCredit.setGlHeadCode(vMstGlConfig.getCreditGlHead());
								finTranCredit.setGlTranId(vLmsGl.getTranId());
								finTranCredit.setEventId(event.getEventId());
								finTranCredit.setHomeBranch(vLmsGl.getHomeBranch());
								finTranCredit.setLoanId(vLmsGl.getLoanId());
								finTranCredit.setMasterAgrId(vLmsGl.getMastAgrId());
								finTranCredit.setNarration(sysTran.getRemark());
								finTranCredit.setGlTranSrNo(srNo1);
								finTranCredit.setPortfolioCode(vLmsGl.getPortfolioCode());
								finTranCredit.setProdCode(vLmsGl.getProductCode());
								finTranCredit.setServBranch(vLmsGl.getServBranch());
								finTranCredit.setTranAmount(vLmsGl.getTranAmount());
								finTranCredit.setTranDtlId(vLmsGl.getTranDtlId());
								finTranCredit.setUserID(glDto.getUserId());
								finTranCredit.setVoucherType("JV");

								finTranCreditList.add(finTranCredit);

							}
						} else {
							GlErrorLog error = new GlErrorLog("No debit head configuration found for Portfolio "
									+ vLmsGl.getPortfolioCode() + " Event " + glEvent + " TranCategory "
									+ vLmsGl.getTranCategory() + " TranType " + vLmsGl.getTranType() + " TranHead "
									+ vLmsGl.getTranHead());
							glErrorRepo.save(error);
							event.setGlGeneratedYn("E");
							eventRepo.save(event);
							break;

						}
					}
					cashBankEntry = false;

					if (!(event.getGlGeneratedYn().equals("E"))) {

						if (finTranReceipt != null) {
							glTranRepo.save(finTranReceipt);
						}

						if (finTranDebitList.size() > 0) {
							glTranRepo.saveAll(finTranDebitList);
						}

						if (finTranCreditList.size() > 0) {
							glTranRepo.saveAll(finTranCreditList);
						}

						event.setGlGeneratedYn("Y");
						eventRepo.save(event);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;

	}
}
