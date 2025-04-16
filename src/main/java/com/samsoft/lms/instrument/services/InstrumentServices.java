package com.samsoft.lms.instrument.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.entities.TabMstColender;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.repositories.TabMstColenderRepository;
import com.samsoft.lms.batch.entities.BatchErrorLog;
import com.samsoft.lms.batch.repositories.BatchErrorLogRepository;
import com.samsoft.lms.core.dto.AgreementDueListDto;
import com.samsoft.lms.core.dto.AgreementFeeListDto;
import com.samsoft.lms.core.dto.ReceiptMisDto;
import com.samsoft.lms.core.entities.AgrFeeParam;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.TabMstDepositBank;
import com.samsoft.lms.core.entities.TabMstLookups;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.TabMstDepositBankRepository;
import com.samsoft.lms.core.repositories.TabMstLookupsRepository;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.instrument.dto.BatchCreateResDto;
import com.samsoft.lms.instrument.dto.BatchDetailsListDto;
import com.samsoft.lms.instrument.dto.BatchListDto;
import com.samsoft.lms.instrument.dto.FutureInstrumentsListDto;
import com.samsoft.lms.instrument.entities.TrnInsBatchHdr;
import com.samsoft.lms.instrument.entities.TrnInsBatchInstruments;
import com.samsoft.lms.instrument.entities.TrnInsBatchStatus;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.VLmsHcInstrumentVsPayapplied;
import com.samsoft.lms.instrument.entities.VTrnInsBatchInstruments;
import com.samsoft.lms.instrument.exceptions.InstrumentDataNotFoundException;
import com.samsoft.lms.instrument.repositories.TrnInsBatchHdrRepository;
import com.samsoft.lms.instrument.repositories.TrnInsBatchInstrumentsRepository;
import com.samsoft.lms.instrument.repositories.TrnInsBatchStatusRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.instrument.repositories.VLmsHcInstrumentVsPayappliedRepository;
import com.samsoft.lms.instrument.repositories.VTrnInsBatchInstrumentsRepository;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.services.GstDetailsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InstrumentServices {

	@Autowired
	private Environment env;

	@Autowired
	private TabMstDepositBankRepository depositRepo;

	@Autowired
	private TabMstLookupsRepository lookupRepo;

	@Autowired
	private TrnInsInstrumentRepository instRepo;

	@Autowired
	private TrnInsBatchInstrumentsRepository batchInstRepo;

	@Autowired
	private TrnInsBatchStatusRepository batchStatusRepo;

	@Autowired
	private TrnInsBatchHdrRepository hdrRepo;

	@Autowired
	private VTrnInsBatchInstrumentsRepository vBatchInstRepo;

	@Autowired
	private AgrMasterAgreementRepository mastRepo;

	@Autowired
	private TrnInsBatchHdrRepository batchHdrRepo;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private PaymentApplicationServices paymentService;

	@Autowired
	private AgrCustomerRepository custRepo;

	@Autowired
	private AgrColenderDtlRepository colenderRepo;

	@Autowired
	private AgrDisbursementRepository disbRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private GstDetailsService gstService;

	@Autowired
	private TabMstColenderRepository colenderMastRepo;

	@Autowired
	private VLmsHcInstrumentVsPayappliedRepository batchHeathViewRepo;

	@Autowired
	private BatchErrorLogRepository batchErrorRepo;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private TrnInsInstrumentRepository trnInsInstrumentRepository;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Page<BatchListDto> getBatchList(String instrumentType, String batchStatus, Date fromDate, Date toDate, Pageable pageable )
			throws Exception {

		List<BatchListDto> batchListDto = new ArrayList<BatchListDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			if (!(fromDate == null)) {
				List<TrnInsBatchHdr> batchHdrList = batchHdrRepo
						.findByInstrumentTypeAndBatchStatusAndDtBatchDateBetween(instrumentType, batchStatus, fromDate,
								toDate);
				if (batchHdrList.size() == 0) {
					throw new InstrumentDataNotFoundException("Batch details not found.");
				}
				for (TrnInsBatchHdr batchHdr : batchHdrList) {
					BatchListDto batchDto = new BatchListDto();
					BeanUtils.copyProperties(batchHdr, batchDto);
					batchDto.setDtBatchDate(sdf.format(batchHdr.getDtBatchDate()));
					batchListDto.add(batchDto);
				}
			} else {
				List<TrnInsBatchHdr> batchHdrList;
				if(instrumentType == null) {
					 batchHdrList = batchHdrRepo.findByBatchStatus(
							batchStatus);
				}else {
					 batchHdrList = batchHdrRepo.findByInstrumentTypeAndBatchStatus(instrumentType,
							batchStatus);
					
				}
				
				if (batchHdrList.size() == 0) {
					throw new InstrumentDataNotFoundException("Batch details not found.");
				}
				for (TrnInsBatchHdr batchHdr : batchHdrList) {
					BatchListDto batchDto = new BatchListDto();
					BeanUtils.copyProperties(batchHdr, batchDto);
					batchDto.setDtBatchDate(sdf.format(batchHdr.getDtBatchDate()));
					batchListDto.add(batchDto);
				}
			}
			
	        return pageableUtils.convertToPage(batchListDto, pageable);

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<AgreementDueListDto> getFutureDues(Date fromDate, Date toDate) throws Exception {

		List<AgreementDueListDto> dueListDto = new ArrayList<AgreementDueListDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			List<AgrRepaySchedule> dueList = repayRepo.findByDtInstallmentBetween(fromDate, toDate);
			if (dueList.size() == 0) {
				throw new InstrumentDataNotFoundException("Due details not found.");
			}
			for (AgrRepaySchedule repay : dueList) {
				AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(repay.getMasterAgrId(), "B");
				AgrMasterAgreement master = mastRepo.findByMastAgrId(repay.getMasterAgrId());
				List<AgrColenderDtl> colenderList = colenderRepo.findByMasterAgrMastAgrId(master.getMastAgrId());
				String colenderString = "";
				for (AgrColenderDtl colender : colenderList) {
					TabMstColender colen = colenderMastRepo
							.findByColenderId(Integer.parseInt(colender.getColenderCode()));
					if (colen != null) {
						colenderString += colen.getColenderName() + " ,";
					}

				}
				AgreementDueListDto repayDto = new AgreementDueListDto();
				BeanUtils.copyProperties(repay, repayDto);
				repayDto.setMastAgrId(repay.getMasterAgrId());
				repayDto.setDtDueDate(sdf.format(repay.getDtInstallment()));
				repayDto.setDueAmount(repay.getInstallmentAmount());
				log.info("Master Agreement {}  ", master.getMastAgrId());

				TrnInsInstrument instrument = instRepo.findByMasterAgrAndInstrumentNo(master.getMastAgrId(), "1");
				if (instrument != null) {
					repayDto.setUmrn(instrument.getUmrn());
					log.info("Umrn NO {} ", instrument.getUmrn());
				}

				// log.info("Customer Name {} {} {} ", customer.getFirstName(),
				// customer.getMiddleName(), customer.getLastName());
				String name = "";
				if (customer != null) {
					if (customer.getFirstName() != null) {
						name = customer.getFirstName();
					}
					if (customer.getMiddleName() != null) {
						name = name + " " + customer.getMiddleName();
					}
					if (customer.getLastName() != null) {
						name = name + " " + customer.getLastName();
					}

					repayDto.setCustomerName(name);
					repayDto.setMobile(customer.getMobile());
					repayDto.setGender(customer.getGender());
					repayDto.setLoanAmount(master.getLoanAmount());
					repayDto.setLosApplicationNo(master.getOriginationApplnNo());
					repayDto.setColender(colenderString);

				}

				AgrDisbursement agrDisbursement = disbRepo.findByMastAgrMastAgrId(master.getMastAgrId());

				if (agrDisbursement != null) {
					repayDto.setDisbDate(sdf.format(agrDisbursement.getDtDisbDate()));
				}

				dueListDto.add(repayDto);
			}

		} catch (Exception e) {
			throw e;
		}
		return dueListDto;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<ReceiptMisDto> getReceiptMis(Date fromDate, Date toDate) throws Exception {

		List<ReceiptMisDto> dueListDto = new ArrayList<ReceiptMisDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			List<AgrRepaySchedule> dueList = repayRepo.findByDtInstallmentBetween(fromDate, toDate);
			if (dueList.size() == 0) {
				throw new InstrumentDataNotFoundException("Due details not found.");
			}
			for (AgrRepaySchedule repay : dueList) {
				{

					AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(repay.getMasterAgrId(),
							"B");
					AgrColenderDtl colender = colenderRepo.findByMasterAgrMastAgrId(repay.getMasterAgrId()).get(0);
					AgrMasterAgreement master = mastRepo.findByMastAgrId(repay.getMasterAgrId());
					AgrDisbursement disb = disbRepo.findByMastAgrMastAgrId(repay.getMasterAgrId());
					AgrLoans loans = loanRepo.findByMasterAgreementMastAgrId(repay.getMasterAgrId()).get(0);

					ReceiptMisDto repayDto = new ReceiptMisDto();
					BeanUtils.copyProperties(repay, repayDto);

					repayDto.setColenderId(colender.getCoLendId());
					repayDto.setCustomerId(customer.getCustomerId());
					repayDto.setDtDisb(sdf.format(disb.getDtDisbDate()));
					repayDto.setEmiDue(repay.getInstallmentAmount());
					repayDto.setEmiDueDate(sdf.format(repay.getDtInstallment()));
					repayDto.setInterestAmountDue(repay.getInterestAmount());
					repayDto.setPrincipalAmountDue(repay.getPrincipalAmount());
					repayDto.setLoanAmount(master.getLoanAmount());
					repayDto.setLoanId(loans.getLoanId());

					List<AgrTrnDueDetails> chargesDuesList = dueRepo.findByMastAgrIdAndDueCategoryAndDtDueDateBetween(
							repay.getMasterAgrId(), "FEE", fromDate, toDate);
					Double totalCharges = 0d;

					for (AgrTrnDueDetails chargesDues : chargesDuesList) {
						totalCharges += chargesDues.getDueAmount();
						List<AgrTrnTaxDueDetails> taxDueList = taxRepo
								.findByDueDetailDueDtlId(chargesDues.getDueDtlId());
						for (AgrTrnTaxDueDetails tax : taxDueList) {
							totalCharges += tax.getDueTaxAmount();
						}
					}

					repayDto.setOtherCharges(totalCharges);
					if (repay.getDtPaymentDate() != null) {
						repayDto.setPaymentDate(sdf.format(repay.getDtPaymentDate()));
						repayDto.setInterestAmountPaid(repay.getInterestAmount());
						repayDto.setAmountPaid(repay.getInstallmentAmount());
						repayDto.setPrincipalAmountPaid(repay.getPrincipalAmount());
						repayDto.setPaymentStatus("CLR");
					} else {
						repayDto.setPaymentStatus("PNT");
					}
					repayDto.setProduct(master.getProductCode());
					String name = "";
					if (customer.getFirstName() != null) {
						name = customer.getFirstName();
					}
					if (customer.getMiddleName() != null) {
						name = name + " " + customer.getMiddleName();
					}
					if (customer.getLastName() != null) {
						name = name + " " + customer.getLastName();
					}
					repayDto.setCustomerName(name);
					repayDto.setLoanAmount(master.getLoanAmount());
					dueListDto.add(repayDto);

				}
			}

		} catch (Exception e) {
			throw e;
		}
		return dueListDto;
	}
	
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Page<BatchDetailsListDto> getBatchDetailList(Integer batchId,Pageable pageable) throws Exception {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<BatchDetailsListDto> batchDtlDtoList = new ArrayList<BatchDetailsListDto>();
		try {
			List<TrnInsBatchInstruments> batchHdrList = batchInstRepo.findByBatchHdrBatchId(batchId);
			if (batchHdrList.size() == 0) {
				throw new InstrumentDataNotFoundException("Batch details not found.");
			}
			for (TrnInsBatchInstruments batchIns : batchHdrList) {
				BatchDetailsListDto detailDto = new BatchDetailsListDto();

				TrnInsInstrument insInst = instRepo.findByInstrumentId(batchIns.getInstrumentId());
//				if (insInst == null) {
//					throw new InstrumentDataNotFoundException("Instrument details not found.");
//				}
				if(insInst != null) {
					detailDto.setInstrumentStatus(insInst.getInstrumentStatus());
					detailDto.setBounceReason(insInst.getBounceReason());
					detailDto.setInstrumentAmount(insInst.getInstrumentAmount());
					detailDto.setMastAgrId(insInst.getMasterAgr());
					detailDto.setDtInstrumentDate(sdf.format(insInst.getDtInstrumentDate()));
					detailDto.setInstrumentType(insInst.getInstrumentType());
					detailDto.setInstrumentId(insInst.getInstrumentId());
					detailDto.setUmrn(insInst.getUmrn());
					batchDtlDtoList.add(detailDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getBatchDetailList :: Method: getBatchDetailList");
			log.error("getBatchDetailList :: Request batchId: {}", batchId);
			log.error("getBatchDetailList :: Error: {}", e.getMessage());

			throw e;
		}

		return pageableUtils.convertToPage(batchDtlDtoList, pageable);

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public BatchCreateResDto batchCreate(Date businessDate) throws Exception {

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			TrnInsBatchHdr batchHdr = null;
			List<TrnInsBatchInstruments> listBatchInst = null;
			String prevInstrumentType = "X";
			int totalInstrumentCount = 0;
			Date instrumentDate = null;

			Calendar c = Calendar.getInstance();
			c.setTime(businessDate);
			c.add(Calendar.DATE, Integer.parseInt(env.getProperty("lms.advance.batch.days")));
			Date batchDaysDate = c.getTime();

			List<TabMstDepositBank> listActiveDeposit = depositRepo.findAllByActive("Y");
			if (listActiveDeposit.size() < 1) {
				throw new InstrumentDataNotFoundException("No active deposit bank available.");
			}

			for (TabMstDepositBank depositBank : listActiveDeposit) {

				List<TabMstLookups> listLookupValues = lookupRepo.findAllByLookupType("BATCH_INSTRUMENT");
				if (listLookupValues.size() < 1) {
					throw new InstrumentDataNotFoundException("No lookup values for BATCH_INSTRUMENT found.");
				}

				List<TabMstColender> colenderList = colenderMastRepo.findAll();

				for (TabMstColender colender : colenderList) {

					for (TabMstLookups lookupValue : listLookupValues) {
						List<TrnInsInstrument> listInstruments = instRepo
								.findByDepositBankIfscAndInstrumentTypeAndInstrumentStatusAndDtInstrumentDateLessThanAndColenderIdOrderByInstrumentId(
										depositBank.getIfscCode(), lookupValue.getCode(), "NEW", batchDaysDate,
										colender.getColenderId().toString());
						// List<TrnInsInstrument> updateInstList = new ArrayList<TrnInsInstrument>();
						List<TrnInsBatchInstruments> batchInstList = new ArrayList<TrnInsBatchInstruments>();

						/*
						 * if(listInstruments.size() < 1) { throw new
						 * InstrumentDataNotFoundException("No instruments found for Ifsc code "
						 * +depositBank.getIfscCode() + " and for lookup code "+lookupValue.getCode());
						 * }
						 */
						listBatchInst = new ArrayList<TrnInsBatchInstruments>();
						for (TrnInsInstrument instrument : listInstruments) {
							instrumentDate = instrument.getDtInstrumentDate();
							totalInstrumentCount += listInstruments.size();
							// TrnInsInstrument updateInst = new TrnInsInstrument();
							if (!(prevInstrumentType.equals(lookupValue.getCode()))) {
								batchHdr = new TrnInsBatchHdr();
								batchHdr.setDtBatchDate(businessDate);
								batchHdr.setInstrumentType(lookupValue.getCode());
								batchHdr.setDepositBankIfsc(depositBank.getIfscCode());
								batchHdr.setDepositBankName(depositBank.getBankName());
								batchHdr.setDepositBankBranch(depositBank.getBranchName());
								batchHdr.setTotalInstruments(totalInstrumentCount);
								batchHdr.setTotalClearedInstruments(0);
								batchHdr.setTotalBounceInstruments(0);
								batchHdr.setBatchStatus("O");
								batchHdr.setUserId("SYSTEM");
								batchHdr.setColenderId(colender.getColenderId().toString());
								prevInstrumentType = lookupValue.getCode();
							}

							AgrMasterAgreement mastAgrId = mastRepo.findByMastAgrId(instrument.getMasterAgr());

							if (((mastAgrId.getPortfolioCode().equals("DL"))
									|| (mastAgrId.getPortfolioCode().equals("ND")))
									&& (businessDate.equals(instrument.getDtInstrumentDate()))) {
								TrnInsBatchInstruments batchInst = new TrnInsBatchInstruments();
								batchInst.setInstrumentId(instrument.getInstrumentId());
								batchInst.setInstrumentStatus("UNP");
								batchInst.setUserId("SYSTEM");
								batchInst.setBatchHdr(batchHdr);
								// BeanUtils.copyProperties(instrument, updateInst);
								instrument.setInstrumentStatus("UNP");
								batchInstList.add(batchInst);
								// updateInstList.add(updateInst);
							} else {
								if (!((mastAgrId.getPortfolioCode().equals("DL"))
										|| (mastAgrId.getPortfolioCode().equals("ND")))) {
									TrnInsBatchInstruments batchInst = new TrnInsBatchInstruments();
									batchInst.setInstrumentId(instrument.getInstrumentId());
									batchInst.setInstrumentStatus("UNP");
									batchInst.setUserId("SYSTEM");
									batchInst.setBatchHdr(batchHdr);
									// BeanUtils.copyProperties(instrument, updateInst);
									instrument.setInstrumentStatus("UNP");
									batchInstList.add(batchInst);
									// updateInstList.add(updateInst);
								}
							}

						}

						if (listInstruments.size() > 0 && batchInstList.size() > 0) {
							TrnInsBatchStatus batchStatus = new TrnInsBatchStatus();
							batchStatus.setBatchHdr(batchHdr);
							batchStatus.setBatchStatus("O");
							batchStatus.setBatchStatusChangeDt(businessDate);
							batchStatus.setUserId("SYSTEM");

							// batchHdr.setBatchSts(Arrays.asList(new TrnInsBatchStatus[] { batchStatus }));

							// batchHdr.setBatchInstrument(batchInst);
							// batchStatusRepo.save(batchStatus);

							instRepo.saveAll(listInstruments);
							batchInstRepo.saveAll(batchInstList);
							batchStatusRepo.save(batchStatus);
							// hdrRepo.save(batchHdr);
						}

					}
				}
				prevInstrumentType = "X";
			}

			if(batchHdr != null) {
				return BatchCreateResDto.builder()
						.batchId(batchHdr.getBatchId().toString())
						.instrumentDate(sdf.format(instrumentDate))
						.build();

			}

		} catch (InstrumentDataNotFoundException e) {
			throw new InstrumentDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return null;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String batchDownloadInCsv(Integer batchId, Date businessDate, Writer writer, String fileName)
			throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
					Arrays.asList(new String[] { "O" }));
			String date = sdf.format(businessDate).replaceAll("-", "");
			if (batchHdr == null) {
				throw new InstrumentDataNotFoundException("Batch Id not found or Batch is closed.");
			}

			List<VTrnInsBatchInstruments> listBatchInstrument = vBatchInstRepo
					.findAllByBatchIdAndInstrumentStatusOrderByInstrumentNo(batchId, "UNP");
			if (listBatchInstrument.size() == 0) {
				throw new InstrumentDataNotFoundException("No instrument with UNP status is found.");
			}
			List<TrnInsInstrument> listInstruments = new ArrayList<TrnInsInstrument>();
			TrnInsInstrument trnInsInstrument = null;
			try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
				csvPrinter.printRecord(batchHdr.getInstrumentType(), Integer.toString(batchId),
						sdf.format(businessDate), batchHdr.getTotalInstruments().toString());
				csvPrinter.printRecord("Master Agreement", "Instrument Date", "Instrument No", "Ifsc Code",
						"Account No", "Account Type", "Instrument Amount", "Instrument Status", "Bounce Reason", "NBFC Name", "UMRN", "UtrNo");
				for (VTrnInsBatchInstruments instrument : listBatchInstrument) {
					csvPrinter.printRecord(instrument.getMastAgrId(), sdf.format(instrument.getDtInstrumentDate()),
							Integer.toString(instrument.getInstrumentNo()), instrument.getIfscCode(),
							instrument.getAccountNo(), instrument.getAccountType(),
							Double.toString(instrument.getInstrumentAmount()), instrument.getInstrumentStatus(),
							instrument.getBounceReason(), instrument.getColenderName(),instrument.getUmrn(),instrument.getUtrNo());
					Optional<TrnInsInstrument> instId = instRepo.findById(instrument.getInstrumentId());
					if (instId.isPresent()) {
						trnInsInstrument = instId.get();
						trnInsInstrument.setInstrumentStatus("PNT");
						listInstruments.add(trnInsInstrument);
					}
				}

				csvPrinter.printRecord(Integer.toString(batchId), batchHdr.getTotalInstruments().toString());
				List<TrnInsBatchInstruments> listForUpdatingStatus = batchInstRepo.findByBatchHdrBatchId(batchId);
				for (TrnInsBatchInstruments updateInst : listForUpdatingStatus) {
					updateInst.setInstrumentStatus("PNT");
				}

				TrnInsBatchStatus batchStatus = new TrnInsBatchStatus();
				batchStatus.setBatchStatus("D");
				batchStatus.setBatchStatusChangeDt(businessDate);
				batchStatus.setFileName(fileName);
				batchStatus.setFileLocation("");
				batchStatus.setUserId("SYSTEM");
				batchStatus.setBatchHdr(batchHdr);

				batchHdr.setBatchStatus("D");
				hdrRepo.save(batchHdr);
				batchStatusRepo.save(batchStatus);
				instRepo.save(trnInsInstrument);
				batchInstRepo.saveAll(listForUpdatingStatus);

			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			result = "fail";
			throw e;
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<String> batchDownloadInBulk(Integer[] arrBatchId, Date businessDate) throws Exception {
		String result = "success";
		String zipFileName = "";
		List<String> sourceFilesForZip = new ArrayList<String>();

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			SimpleDateFormat sdfIst = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(businessDate).replaceAll("-", "");
			String fileDownloadPath = env.getProperty("lms.batch.file.download.path");

			File dir = null;
			try {
				dir = new File(fileDownloadPath);
				deleteFiles(dir);
				dir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String fileName = null;
			for (int i = 0; i < arrBatchId.length; i++) {

				TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(arrBatchId[i],
						Arrays.asList(new String[] { "O" }));

				if (batchHdr == null) {
					throw new InstrumentDataNotFoundException("Batch Id not found or Batch is closed.");
				}

				fileName = batchHdr.getInstrumentType() + "_" + Integer.toString(arrBatchId[i]) + "_" + date + ".csv";

				File tmp = new File(dir, fileName);
				tmp.createNewFile();

				File file = new File(fileDownloadPath + fileName);

				CSVWriter writer = new CSVWriter(new FileWriter(file));

				List<VTrnInsBatchInstruments> listBatchInstrument = vBatchInstRepo
						.findAllByBatchIdAndInstrumentStatusOrderByInstrumentNo(arrBatchId[i], "UNP");

				if (listBatchInstrument.size() == 0) {
					throw new InstrumentDataNotFoundException("No data with UNP status found in Batch Instrument. ");
				}

				List<TrnInsInstrument> listInstruments = new ArrayList<TrnInsInstrument>();
				TrnInsInstrument trnInsInstrument = null;
				List<String[]> listCsvRows = new ArrayList<String[]>();

				String[] header = new String[] { batchHdr.getInstrumentType(), Integer.toString(arrBatchId[i]),
						sdf.format(businessDate), batchHdr.getTotalInstruments().toString() };

				String[] columnHeader = new String[17];
				/*
				 * columnHeader[0] = "Master Agreement"; columnHeader[1] = "Instrument Date";
				 * columnHeader[2] = "Instrument No"; columnHeader[3] = "Ifsc Code";
				 * columnHeader[4] = "Account No"; columnHeader[5] = "Account Type";
				 * columnHeader[6] = "Instrument Amount"; columnHeader[7] = "Instrument Status";
				 * columnHeader[8] = "Bounce Reason";
				 */

				columnHeader[0] = "umrn";
				columnHeader[1] = "amount";
				columnHeader[2] = "settlement_date";
				columnHeader[3] = "unique_key";
				columnHeader[4] = "corporate_account_number";
				columnHeader[5] = "corporate_config_id";
				columnHeader[6] = "destination_bank_id";
				columnHeader[7] = "customer_account_number";
				columnHeader[8] = "destination_account_type";
				columnHeader[9] = "customer_name";
				columnHeader[10] = "frequency";
				columnHeader[11] = "present_before_days";
				columnHeader[12] = "narration";

				// Supplier Finance Changes Start

				columnHeader[13] = "Master Agreement";

				columnHeader[14] = "NBFC Name";
				// Supplier Finance Changes End

				// columnHeader[14] = "bounce_reason";
				// columnHeader[15] = "settlement_date";
				// columnHeader[16] = "utrno";

				// listCsvRows.add(header);
				listCsvRows.add(columnHeader);

				for (VTrnInsBatchInstruments instrument : listBatchInstrument) {

					AgrCustomer customer = null;
					AgrMasterAgreement masterObj = null;

					if(instrument.getCustomerId() != null) {
						customer = custRepo.findFirstByCustomerId(instrument.getCustomerId());
					}

					if(instrument.getMastAgrId() != null) {
						masterObj = mastRepo.findByMastAgrId(instrument.getMastAgrId());
					}

					String[] row = new String[15];
					row[0] = instrument.getUmrn();
					row[1] = Double.toString(instrument.getInstrumentAmount());
					row[2] = sdfIst.format(instrument.getDtInstrumentDate());
					if(masterObj != null){
						row[3] = (instrument.getInstrumentNo() != null ? instrument.getInstrumentNo() : " ") + "_" + (masterObj.getOriginationApplnNo() != null ? masterObj.getOriginationApplnNo() : " ");
					}
					row[4] = "50200054499342";
					row[5] = "TSE210403121944093DSA38U3WNX47H1";
					row[6] = instrument.getIfscCode();
					row[7] = instrument.getAccountNo();
					row[8] = instrument.getAccountType();
					if(customer != null){
						row[9] = customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName();
					}
					row[10] = "ONETIME";
					row[11] = "1";
					row[12] = "Loan agr no " + instrument.getMastAgrId();
					// Supplier Finance Changes Start

					row[13] = masterObj.getMastAgrId();

					row[14] = instrument.getColenderName();
					// Supplier Finance Changes End

					// row[14] = instrument.getBounceReason();

					/*
					 * row[0] = instrument.getMastAgrId(); row[1] =
					 * sdf.format(instrument.getDtInstrumentDate()); row[2] =
					 * Integer.toString(instrument.getInstrumentNo()); row[3] =
					 * instrument.getIfscCode(); row[4] = instrument.getAccountNo(); row[5] =
					 * instrument.getAccountType(); row[6] =
					 * Double.toString(instrument.getInstrumentAmount()); row[7] =
					 * instrument.getInstrumentStatus(); row[8] = instrument.getBounceReason();
					 */

					listCsvRows.add(row);

					Optional<TrnInsInstrument> instId = instRepo.findById(instrument.getInstrumentId());
					if (instId.isPresent()) {
						trnInsInstrument = instId.get();
						trnInsInstrument.setInstrumentStatus("PNT");
						listInstruments.add(trnInsInstrument);
					}
				}

				String[] footer = new String[] { Integer.toString(arrBatchId[i]),
						batchHdr.getTotalInstruments().toString() };
				// listCsvRows.add(footer);
				List<TrnInsBatchInstruments> listForUpdatingStatus = batchInstRepo.findByBatchHdrBatchId(arrBatchId[i]);
				for (TrnInsBatchInstruments updateInst : listForUpdatingStatus) {
					updateInst.setInstrumentStatus("PNT");
				}

				TrnInsBatchStatus batchStatus = new TrnInsBatchStatus();
				batchStatus.setBatchStatus("D");
				batchStatus.setBatchStatusChangeDt(businessDate);
				batchStatus.setFileName(fileName);
				batchStatus.setFileLocation(fileDownloadPath);
				batchStatus.setUserId("SYSTEM");
				batchStatus.setBatchHdr(batchHdr);

				writer.writeAll(listCsvRows);
				writer.close();
				batchHdr.setBatchStatus("D");
				hdrRepo.save(batchHdr);
				batchStatusRepo.save(batchStatus);
				instRepo.save(trnInsInstrument);
				batchInstRepo.saveAll(listForUpdatingStatus);
				sourceFilesForZip.add(file.getAbsolutePath());
			}

			byte[] buffer = new byte[1024];
			ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(fileDownloadPath + date + ".zip"));

			for (String sourceFile : sourceFilesForZip) {
				Path source = Paths.get(sourceFile);
				FileInputStream fin = new FileInputStream(source.toFile());
				zout.putNextEntry(new ZipEntry(source.getFileName().toString()));
				int length;

				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();

			}
			zout.close();

		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
			throw e;
		} finally {
			// outWriter.close();
		}
		return sourceFilesForZip;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<String> downloadDownloadedFilesInBulk(Integer[] arrBatchId, Date businessDate) throws Exception {
		String result = "success";
		String zipFileName = "";
		List<String> sourceFilesForZip = new ArrayList<String>();

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			SimpleDateFormat sdfIst = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(businessDate).replaceAll("-", "");
			String fileDownloadPath = env.getProperty("lms.batch.file.download.path");
			File dir = new File(fileDownloadPath);
			deleteFiles(dir);
			dir.mkdirs();
			String fileName = null;
			for (int i = 0; i < arrBatchId.length; i++) {

				TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(arrBatchId[i],
						Arrays.asList(new String[] { "D" }));

				if (batchHdr == null) {
					throw new InstrumentDataNotFoundException("Batch Id not found or Batch is closed.");
				}

				fileName = batchHdr.getInstrumentType() + "_" + Integer.toString(arrBatchId[i]) + "_" + date + ".csv";

				File tmp = new File(dir, fileName);
				tmp.createNewFile();

				File file = new File(fileDownloadPath + fileName);

				CSVWriter writer = new CSVWriter(new FileWriter(file));

				List<VTrnInsBatchInstruments> listBatchInstrument = vBatchInstRepo
						.findAllByBatchIdAndInstrumentStatusOrderByInstrumentNo(arrBatchId[i], "PNT");

				List<TrnInsInstrument> listInstruments = new ArrayList<TrnInsInstrument>();
				TrnInsInstrument trnInsInstrument = null;
				List<String[]> listCsvRows = new ArrayList<String[]>();

				String[] header = new String[] { batchHdr.getInstrumentType(), Integer.toString(arrBatchId[i]),
						sdf.format(businessDate), batchHdr.getTotalInstruments().toString() };

				String[] columnHeader = new String[17];
				/*
				 * columnHeader[0] = "Master Agreement"; columnHeader[1] = "Instrument Date";
				 * columnHeader[2] = "Instrument No"; columnHeader[3] = "Ifsc Code";
				 * columnHeader[4] = "Account No"; columnHeader[5] = "Account Type";
				 * columnHeader[6] = "Instrument Amount"; columnHeader[7] = "Instrument Status";
				 * columnHeader[8] = "Bounce Reason";
				 */

				columnHeader[0] = "umrn";
				columnHeader[1] = "amount";
				columnHeader[2] = "instrument_date";
				columnHeader[3] = "unique_key";
				columnHeader[4] = "corporate_account_number";
				columnHeader[5] = "corporate_config_id";
				columnHeader[6] = "destination_bank_id";
				columnHeader[7] = "customer_account_number";
				columnHeader[8] = "destination_account_type";
				columnHeader[9] = "customer_name";
				columnHeader[10] = "frequency";
				columnHeader[11] = "present_before_days";
				columnHeader[12] = "narration";

				// Supplier Finance Changes Start

				columnHeader[13] = "Master Agreement";

				columnHeader[14] = "NBFC Name";
				// Supplier Finance Changes End

				// columnHeader[13] = "instrument_status";
				// columnHeader[14] = "bounce_reason";
				// columnHeader[15] = "settlement_date";
				// columnHeader[16] = "utrno";

				// listCsvRows.add(header);
				listCsvRows.add(columnHeader);

				for (VTrnInsBatchInstruments instrument : listBatchInstrument) {
					log.info("Customer id is {} and instrument id is {} ", instrument.getCustomerId(),
							instrument.getInstrumentId());
					AgrCustomer customer = null;
					AgrMasterAgreement masterObj = null;

					if(instrument.getCustomerId() != null) {
						customer = custRepo.findFirstByCustomerId(instrument.getCustomerId());
					}

					if(instrument.getMastAgrId() != null) {
						masterObj = mastRepo.findByMastAgrId(instrument.getMastAgrId());
					}

					String[] row = new String[15];
					row[0] = instrument.getUmrn();
					row[1] = Double.toString(instrument.getInstrumentAmount());
					row[2] = sdfIst.format(instrument.getDtInstrumentDate());
					if(masterObj != null){
						row[3] = (instrument.getInstrumentNo() != null ? instrument.getInstrumentNo() : " ") + "_" + (masterObj.getOriginationApplnNo() != null ? masterObj.getOriginationApplnNo() : " ");
					}
					row[4] = "50200054499342";
					row[5] = "TSE210403121944093DSA38U3WNX47H1";
					row[6] = instrument.getIfscCode();
					row[7] = instrument.getAccountNo();
					row[8] = instrument.getAccountType();
					if(customer != null){
						row[9] = customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName();
					}
					row[10] = "ONETIME";
					row[11] = "1";
					row[12] = "Loan agr no " + instrument.getMastAgrId();
					// Supplier Finance Changes Start

					row[13] = masterObj.getMastAgrId();

					row[14] = instrument.getColenderName();
					// Supplier Finance Changes End

					// row[13] = instrument.getInstrumentStatus();
					// row[14] = instrument.getBounceReason();
					log.info("After all setters");
					/*
					 * row[0] = instrument.getMastAgrId(); row[1] =
					 * sdf.format(instrument.getDtInstrumentDate()); row[2] =
					 * Integer.toString(instrument.getInstrumentNo()); row[3] =
					 * instrument.getIfscCode(); row[4] = instrument.getAccountNo(); row[5] =
					 * instrument.getAccountType(); row[6] =
					 * Double.toString(instrument.getInstrumentAmount()); row[7] =
					 * instrument.getInstrumentStatus(); row[8] = instrument.getBounceReason();
					 */

					listCsvRows.add(row);

				}

				String[] footer = new String[] { Integer.toString(arrBatchId[i]),
						batchHdr.getTotalInstruments().toString() };
				// listCsvRows.add(footer);
				log.info("Adding footer");
				writer.writeAll(listCsvRows);
				log.info("Write all list");
				writer.close();
				log.info("Close writer");
				sourceFilesForZip.add(file.getAbsolutePath());

			}

			byte[] buffer = new byte[1024];
			ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(fileDownloadPath + date + ".zip"));

			for (String sourceFile : sourceFilesForZip) {
				Path source = Paths.get(sourceFile);
				FileInputStream fin = new FileInputStream(source.toFile());
				zout.putNextEntry(new ZipEntry(source.getFileName().toString()));
				int length;

				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				zout.closeEntry();
				fin.close();

			}
			zout.close();

		} catch (Exception e) {
			log.info(e.getMessage());
			result = "fail";
			throw e;
		} finally {
			// outWriter.close();
		}
		return sourceFilesForZip;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String batchDownloadInCsvInServerLocation(Integer batchId, Date businessDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String fileDownloadPath = env.getProperty("lms.batch.file.download.path");
			String fileName = null;
			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
					Arrays.asList(new String[] { "O" }));
			String date = sdf.format(businessDate).replaceAll("-", "");
			if (batchHdr == null) {
				throw new InstrumentDataNotFoundException("Batch Id not found or Batch is closed.");
			}

			fileName = batchHdr.getInstrumentType() + "_" + Integer.toString(batchId) + "_" + date + ".csv";
			File dir = new File(fileDownloadPath);
			dir.mkdirs();
			File tmp = new File(dir, fileName);
			tmp.createNewFile();

			File file = new File(env.getProperty("lms.batch.file.download.path") + fileName);

			// file.createNewFile();

			CSVWriter writer = new CSVWriter(new FileWriter(file));

			List<VTrnInsBatchInstruments> listBatchInstrument = vBatchInstRepo
					.findAllByBatchIdAndInstrumentStatusOrderByInstrumentNo(batchId, "UNP");

			List<TrnInsInstrument> listInstruments = new ArrayList<TrnInsInstrument>();
			TrnInsInstrument trnInsInstrument = null;
			List<String[]> listCsvRows = new ArrayList<String[]>();

			String[] header = new String[] { batchHdr.getInstrumentType(), Integer.toString(batchId),
					sdf.format(businessDate), batchHdr.getTotalInstruments().toString() };

			String[] columnHeader = new String[9];
			columnHeader[0] = "Master Agreement";
			columnHeader[1] = "Instrument Date";
			columnHeader[2] = "Instrument No";
			columnHeader[3] = "Ifsc Code";
			columnHeader[4] = "Account No";
			columnHeader[5] = "Account Type";
			columnHeader[6] = "Instrument Amount";
			columnHeader[7] = "Instrument Status";
			columnHeader[8] = "Bounce Reason";
			listCsvRows.add(header);
			listCsvRows.add(columnHeader);

			for (VTrnInsBatchInstruments instrument : listBatchInstrument) {

				String[] row = new String[9];
				row[0] = instrument.getMastAgrId();
				row[1] = sdf.format(instrument.getDtInstrumentDate());
				row[2] = Integer.toString(instrument.getInstrumentNo());
				row[3] = instrument.getIfscCode();
				row[4] = instrument.getAccountNo();
				row[5] = instrument.getAccountType();
				row[6] = Double.toString(instrument.getInstrumentAmount());
				row[7] = instrument.getInstrumentStatus();
				row[8] = instrument.getBounceReason();

				listCsvRows.add(row);

				Optional<TrnInsInstrument> instId = instRepo.findById(instrument.getInstrumentId());
				if (instId.isPresent()) {
					trnInsInstrument = instId.get();
					trnInsInstrument.setInstrumentStatus("PNT");
					listInstruments.add(trnInsInstrument);
				}
			}

			String[] footer = new String[] { Integer.toString(batchId), batchHdr.getTotalInstruments().toString() };
			listCsvRows.add(footer);
			List<TrnInsBatchInstruments> listForUpdatingStatus = batchInstRepo.findByBatchHdrBatchId(batchId);
			for (TrnInsBatchInstruments updateInst : listForUpdatingStatus) {
				updateInst.setInstrumentStatus("PNT");
			}

			TrnInsBatchStatus batchStatus = new TrnInsBatchStatus();
			batchStatus.setBatchStatus("D");
			batchStatus.setBatchStatusChangeDt(businessDate);
			batchStatus.setFileName(fileName);
			batchStatus.setFileLocation(fileDownloadPath);
			batchStatus.setUserId("SYSTEM");
			batchStatus.setBatchHdr(batchHdr);

			writer.writeAll(listCsvRows);
			writer.close();
			batchHdr.setBatchStatus("D");
			hdrRepo.save(batchHdr);
			batchStatusRepo.save(batchStatus);
			instRepo.save(trnInsInstrument);
			batchInstRepo.saveAll(listForUpdatingStatus);

		} catch (Exception e) {
			result = "fail";
			throw e;
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String batchUploadCsvInServerLocation(Integer batchId, String fileName, Date businessDate) throws Exception {
		String result = "success";
		String batchStatus = "C";
		Set<String> customerSet = new HashSet<String>();
		try {
			String fileUploadPath = env.getProperty("lms.batch.file.upload.path");

			File file = new File(env.getProperty("lms.batch.file.upload.path") + fileName);

			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
					Arrays.asList(new String[] { "D", "PU" }));
			if (batchHdr == null) {
				result = "Batch Status not Open/Partially Uploaded";
			}

			FileReader filereader = new FileReader(file);

			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(0).build();

			List<String[]> allData = csvReader.readAll();
			List<TrnInsInstrument> listInst = new ArrayList<TrnInsInstrument>();

			for (int i = 0; i < allData.size() - 1; i++) {
				TrnInsInstrument instrument = null;
				if (i == 0) {
					String[] header = allData.get(0);
					if (!(header[1].equals(Integer.toString(batchId)))) {
						return "Input Batch No and File Batch no not matching";
					}
				}
				if (i > 1) {
					String[] details = allData.get(i);
					String dateFormat = env.getProperty("lms.global.date.format");
					DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					instrument = instRepo.findByMasterAgrAndInstrumentNo(details[0], details[2]);
					if (instrument == null) {
						batchStatus = "PU";
					} else {
						instrument.setInstrumentStatus(details[7]);
						instrument.setBounceReason(details[8]);
						instrument.setDtStatusUpdate(businessDate);
						listInst.add(instrument);
					}
					customerSet.add(instrument.getCustomerId());
				}

			}

			TrnInsBatchStatus batchSts = new TrnInsBatchStatus();
			batchSts.setBatchStatus(batchStatus);
			batchSts.setBatchStatusChangeDt(businessDate);
			batchSts.setFileName(fileName);
			batchSts.setFileLocation(fileUploadPath);
			batchSts.setUserId("SYSTEM");
			batchSts.setBatchHdr(batchHdr);
			batchHdr.setBatchStatus(batchStatus);

			for (String customer : customerSet) {
				result = paymentService.paymentRelease(customer, businessDate);
			}

			batchStatusRepo.save(batchSts);
			instRepo.saveAll(listInst);
			hdrRepo.save(batchHdr);

		} catch (Exception e) {
			result = "fail";
			throw e;
		}
		return result;
	}
	
//	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
//	public String batchUploadCsv(Integer batchId, String fileData, String fileName, Date businessDate)
//			throws Exception {
//		String result = "success";
//		String batchStatus = "C";
//		Set<String> customerSet = new LinkedHashSet<String>();
//		try {
//			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
//					Arrays.asList(new String[] { "D", "PU" }));
//			if (batchHdr == null) {
//				throw new CoreDataNotFoundException("Batch Status not Open/Partially Uploaded");
//			}
//			CSVFormat csvFormat = CSVFormat.DEFAULT;
//			// try (BufferedReader fileReader = new BufferedReader(new
//			// InputStreamReader(file));
//
//			CSVParser csvParser = new CSVParser(new StringReader(fileData), csvFormat);
//			List<TrnInsInstrument> listInst = new ArrayList<TrnInsInstrument>();
//			int i = 0;
//			String header = "";
//			/*
//			 * for (CSVRecord csvRecord : csvParser) { if (i == 0) { header =
//			 * csvRecord.get(1); if (!(header.equals(Integer.toString(batchId)))) { throw
//			 * new
//			 * CoreDataNotFoundException("Input Batch No and File Batch number not matching"
//			 * ); } } if ((i > 1) && (!(csvRecord.get(0).equals(header)))) { if
//			 * (csvRecord.get(7).equals("BOU") && csvRecord.get(8) == null) { throw new
//			 * CoreBadRequestException("Bounce Reason is not provided for master agreement "
//			 * + csvRecord.get(0) + " and instrument" + csvRecord.get(2)); } } i++;
//			 * 
//			 * }
//			 * 
//			 * i = 0;
//			 */
//
//			String instrumentType = fileName.split("_")[0];
//
//			log.info("instrumentType " + instrumentType);
//
//			for (CSVRecord csvRecord : csvParser) {
//				TrnInsInstrument instrument = null;
//				if (i == 0) {
//					header = csvRecord.get(1);
//					if (!(header.equals(Integer.toString(batchId)))) {
//						throw new CoreDataNotFoundException("Input Batch No and File Batch number not matching");
//					}
//				}
//				if ((i > 1) && (!(csvRecord.get(0).equals(header)))) {
//					// String[] details = allData.get(i);
//					String dateFormat = env.getProperty("lms.global.date.format");
//					DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
//					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
//					String[] splitData = csvRecord.get(3).split("_");
//					log.info("Umrn and Instrument No " + csvRecord.get(0) + "  " + splitData[0]);
//					if (instrumentType.equalsIgnoreCase("ENACH")) {
//						instrument = instRepo.findByUmrnAndInstrumentNoAndMasterAgr(csvRecord.get(0), splitData[0],
//								splitData[1]);
//					} else {
//						instrument = instRepo.findByMasterAgrAndInstrumentNo(splitData[1], splitData[0]);
//					}
//
//					if (instrument == null) {
//						batchStatus = "PU";
//					} else {
//						instrument.setInstrumentStatus(csvRecord.get(13));
//						if (csvRecord.get(13).equalsIgnoreCase("BOU")) {
//							if (csvRecord.get(14) == null || csvRecord.get(14).equals("")) {
//								throw new CoreDataNotFoundException(
//										"Bounce Reason not found for master agreement " + splitData[1]);
//							}
//
//							List<GstListDto> gstListTemp = gstService.getGstList(splitData[1], "BOUNCE", 500d);
//						}
//
//						instrument.setBounceReason(csvRecord.get(14));
//						instrument.setDtStatusUpdate(businessDate);
//						log.info(" Date =" + csvRecord.get(15));
//						if (csvRecord.get(15).equalsIgnoreCase("") || csvRecord.get(15) == null) {
//
//						} else {
//							instrument.setDtSettlementDate(sdf.parse(csvRecord.get(15)));
//						}
//
//						instrument.setUtrNo(csvRecord.get(16));
//						listInst.add(instrument);
//
//						customerSet.add(instrument.getCustomerId());
//					}
//
//				}
//				i++;
//
//			}
//
//			for (String customer : customerSet) {
//				String paymentRelease = paymentService.paymentRelease(customer, businessDate);
//
//				log.info(" paymentRelease Result " + paymentRelease);
//			}
//
//			TrnInsBatchStatus batchSts = new TrnInsBatchStatus();
//			batchSts.setBatchStatus(batchStatus);
//			batchSts.setBatchStatusChangeDt(businessDate);
//			batchSts.setFileName(fileName);
//			// batchSts.setFileLocation(file);
//			batchSts.setUserId("SYSTEM");
//
//			batchHdr.setBatchStatus(batchStatus);
//			batchSts.setBatchHdr(batchHdr);
//
//			batchStatusRepo.save(batchSts);
//			instRepo.saveAll(listInst);
//			hdrRepo.save(batchHdr);
//
//		} catch (CoreDataNotFoundException e) {
//			result = "fail";
//			throw e;
//		} catch (Exception e) {
//			result = "fail";
//			throw e;
//		}
//		return result;
//	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class }) 
	// As per deepak G suggestion we have change in this method for batch upload file Above commented method is existing method in lms 
	public String batchUploadCsv(Integer batchId, String fileData, String fileName, Date businessDate)
			throws Exception {
		String result = "success";
		String batchStatus = "C";
		Set<String> customerSet = new LinkedHashSet<String>();
		try {
			TrnInsBatchHdr batchHdr = hdrRepo.findByBatchIdAndBatchStatusIn(batchId,
					Arrays.asList(new String[] { "D", "PU" }));
			log.info("batchHdr========>" + batchHdr);
			System.out.println(batchHdr);
			if (batchHdr == null) {
				throw new CoreDataNotFoundException("Batch Status not Open/Partially Uploaded");
			}
			CSVFormat csvFormat = CSVFormat.DEFAULT;
			// try (BufferedReader fileReader = new BufferedReader(new
			// InputStreamReader(file));
			log.info("csvFormat========>" + csvFormat);

			CSVParser csvParser = new CSVParser(new StringReader(fileData), csvFormat);
			List<TrnInsInstrument> listInst = new ArrayList<TrnInsInstrument>();
			log.info("listInst========>" + listInst);
			int i = 0;
			String header = "";
			/*
			 * for (CSVRecord csvRecord : csvParser) { if (i == 0) { header =
			 * csvRecord.get(1); if (!(header.equals(Integer.toString(batchId)))) { throw
			 * new
			 * CoreDataNotFoundException("Input Batch No and File Batch number not matching"
			 * ); } } if ((i > 1) && (!(csvRecord.get(0).equals(header)))) { if
			 * (csvRecord.get(7).equals("BOU") && csvRecord.get(8) == null) { throw new
			 * CoreBadRequestException("Bounce Reason is not provided for master agreement "
			 * + csvRecord.get(0) + " and instrument" + csvRecord.get(2)); } } i++;
			 * 
			 * }
			 * 
			 * i = 0;
			 */

			String instrumentType = fileName.split("_")[0];

			log.info("instrumentType " + instrumentType);

			for (CSVRecord csvRecord : csvParser) {
				TrnInsInstrument instrument = null;
				if (i == 0) {
					header = csvRecord.get(1);
					if (!(header.equals(Integer.toString(batchId)))) {
						throw new CoreDataNotFoundException("Input Batch No and File Batch number not matching");
					}
				}
				if ((i > 1) && (!(csvRecord.get(0).equals(header)))) {
					// String[] details = allData.get(i);
					
					String dateFormat = env.getProperty("lms.global.date.format");
					DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
					SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
					String[] splitData = csvRecord.get(2).split("_");
					String[] splitData1 = csvRecord.get(10).split("_");
					log.info("Umrn and Instrument No " + csvRecord.get(0) + "  " + splitData[0] + " " + csvRecord.get(10));
			//		log.info("splitData[1]rtgfg " + splitData[1] );
					if (instrumentType.equalsIgnoreCase("ENACH")) {
						instrument = instRepo.findByUmrnAndInstrumentNoAndMasterAgr(csvRecord.get(10), splitData[0], csvRecord.get(0)
								);
					} else {
						instrument = instRepo.findByMasterAgrAndInstrumentNo(splitData[1], splitData[0]);
					}

					if (instrument == null) {
						batchStatus = "PU";
						log.info("batchStatus" + batchStatus);
					} else {
						instrument.setInstrumentStatus(csvRecord.get(7));
						if (csvRecord.get(7).equalsIgnoreCase("BOU")) {
							if (csvRecord.get(8) == null || csvRecord.get(8).equals("")) {
								throw new CoreDataNotFoundException(
										"Bounce Reason not found for master agreement " + splitData[1]);
							}

							List<GstListDto> gstListTemp = gstService.getGstList(csvRecord.get(0), "BOUNCE", 500d);
						}

						instrument.setBounceReason(csvRecord.get(8));
						instrument.setDtStatusUpdate(businessDate);
//						log.info(" Date =" + csvRecord.get(15));
//						if (csvRecord.get(15).equalsIgnoreCase("") || csvRecord.get(15) == null) {
//
//						} else {
//							instrument.setDtSettlementDate(sdf.parse(csvRecord.get(15)));
//						}

						instrument.setUtrNo(csvRecord.get(11));
						listInst.add(instrument);

						customerSet.add(instrument.getCustomerId());
					}

				}
				i++;

			}

			for (String customer : customerSet) {
				String paymentRelease = paymentService.paymentRelease(customer, businessDate);

				log.info(" paymentRelease Result " + paymentRelease);
			}

			TrnInsBatchStatus batchSts = new TrnInsBatchStatus();
			batchSts.setBatchStatus(batchStatus);
			batchSts.setBatchStatusChangeDt(businessDate);
			batchSts.setFileName(fileName);
			// batchSts.setFileLocation(file);
			batchSts.setUserId("SYSTEM");

			batchHdr.setBatchStatus(batchStatus);
			batchSts.setBatchHdr(batchHdr);

			batchStatusRepo.save(batchSts);
			instRepo.saveAll(listInst);
			hdrRepo.save(batchHdr);

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
	public String batchUploadConsolidatedCsvErrorLog(String fileData, String fileName, Date businessDate)
			throws Exception {
		String result = "success";
		String batchStatus = "C";
		List<TrnInsBatchHdr> batchHdrList = new ArrayList<>();
		CSVFormat csvFormat = CSVFormat.DEFAULT;
		CSVParser csvParser = new CSVParser(new StringReader(fileData), csvFormat);
		int i = 0;
		for (CSVRecord csvRecord : csvParser) {
			if (i == 0) {
				batchErrorRepo.deleteAllFileData(fileName);
			}
			if ((i > 0) && (csvRecord.get(0).length() > 0)) {
				BatchErrorLog errorLog = new BatchErrorLog();
				if (((csvRecord.get(21).equalsIgnoreCase("FAILED"))
						|| (csvRecord.get(21).equalsIgnoreCase("SUCCESS")))) {

					String[] splitData = csvRecord.get(10).split("_");
					// AgrMasterAgreement masterObj =
					// mastRepo.findByOriginationApplnNo(splitData[1]);
					AgrMasterAgreement masterObj = mastRepo.findByMastAgrId(csvRecord.get(32));
					String homeState = masterObj.getHomeState();
					String servState = masterObj.getServState();

					String status = (csvRecord.get(21).equalsIgnoreCase("FAILED")) ? "BOU" : "CLR";
					if (status.equalsIgnoreCase("BOU")) {
						if (csvRecord.get(25) == null || csvRecord.get(25).equals("")) {
							// throw new CoreDataNotFoundException("Bounce Reason not found for master
							// agreement " + masterObj.getMastAgrId());
							errorLog.setFileName(fileName);
							errorLog.setErrorMessage("Bounce Reason not found ");
							errorLog.setBusinessDate(businessDate);
							errorLog.setOriginationNo(splitData[1]);
							batchErrorRepo.save(errorLog);
							result = "false";
						}

						// List<GstListDto> gstListTemp =
						// gstService.getGstList(masterObj.getMastAgrId(), "BOUNCE", 500d);
					}

					if (homeState.equalsIgnoreCase("") || servState.equalsIgnoreCase("") || homeState == null
							|| servState == null) {
						// throw new CoreDataNotFoundException("Home State or Servicing State not found
						// for " + masterObj.getMastAgrId());
						errorLog.setFileName(fileName);
						errorLog.setBusinessDate(businessDate);
						errorLog.setErrorMessage((errorLog.getErrorMessage() == null ? "" : errorLog.getErrorMessage())
								+ " # Home State or Servicing State not found");
						errorLog.setOriginationNo(splitData[1]);
						batchErrorRepo.save(errorLog);
						result = "false";
					}
				}
			}
			i++;
		}
		batchHdrList = hdrRepo.findByBatchStatus("D");
		TrnInsBatchStatus batchSts = new TrnInsBatchStatus();
		batchSts.setBatchStatus(batchStatus);
		batchSts.setBatchStatusChangeDt(businessDate);
		batchSts.setFileName(fileName);
		// batchSts.setFileLocation(file);
		batchSts.setUserId("SYSTEM");

		for (TrnInsBatchHdr batchHdr : batchHdrList) {

			List<TrnInsBatchInstruments> batchInstList = batchInstRepo.findByBatchHdrBatchId(batchHdr.getBatchId());
			for (TrnInsBatchInstruments batchInst : batchInstList) {
				log.info(" BatchId " + batchInst.getInstrumentId());
				TrnInsInstrument instrument = instRepo.findByInstrumentId(batchInst.getInstrumentId());
				if (instrument == null) {
					BatchErrorLog errorLog = new BatchErrorLog();
					errorLog.setFileName(fileName);
					errorLog.setErrorMessage("Instrument id" + batchInst.getInstrumentId() + "not found ");
					errorLog.setBusinessDate(businessDate);
					batchErrorRepo.save(errorLog);
					result = "false";
				}
			}

		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String batchUploadConsolidatedCsvFile(String fileData, String fileName, Date businessDate) throws Exception {
		String result = "success";
		Set<String> customerSet = new LinkedHashSet<String>();
		try {
			CSVFormat csvFormat = CSVFormat.DEFAULT;
			// try (BufferedReader fileReader = new BufferedReader(new
			// InputStreamReader(file));

			String validationResult = this.batchUploadConsolidatedCsvErrorLog(fileData, fileName, businessDate);
			if (validationResult.equals("false")) {
				throw new CoreDataNotFoundException("Plese check the error log");
			}

			CSVParser csvParser = new CSVParser(new StringReader(fileData), csvFormat);
			List<TrnInsInstrument> listInst = new ArrayList<TrnInsInstrument>();
			int i = 0;
			String header = "";

			String instrumentType = fileName.split("_")[0];

			instrumentType = "ENACH";

			log.info("instrumentType " + instrumentType);

			for (CSVRecord csvRecord : csvParser) {
				TrnInsInstrument instrument = null;

				if ((i > 0) && (csvRecord.get(0).length() > 0)) {
					if (((csvRecord.get(21).equalsIgnoreCase("FAILED"))
							|| (csvRecord.get(21).equalsIgnoreCase("SUCCESS")))) {

						// String[] details = allData.get(i);
						String dateFormat = env.getProperty("lms.global.date.format");
						DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(dateFormat);
						SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
						String[] splitData = csvRecord.get(10).split("_");

						// AgrMasterAgreement masterObj =
						// mastRepo.findByOriginationApplnNo(splitData[1]);
						log.info("Umrn and Originatio No and Master Agreement from file " + csvRecord.get(3) + "  "
								+ csvRecord.get(10) + csvRecord.get(32));

						log.info("Details  " + csvRecord.get(3));
						log.info(" value 20 " + csvRecord.get(20));
						// log.info("master obj " + masterObj.getMastAgrId());
						if (instrumentType.equalsIgnoreCase("ENACH")) {
							log.info("Inside Enach");
							instrument = instRepo.findByUmrnAndInstrumentNoAndMasterAgrAndInstrumentStatusNotIn(
									csvRecord.get(3), splitData[0], csvRecord.get(32),
									Arrays.asList(new String[] { "BOU", "CLR" }));
						} else {
							log.info("Else Enach");
							instrument = instRepo.findByInstrumentNoAndMasterAgrAndInstrumentStatusNotIn(splitData[0],
									csvRecord.get(32), Arrays.asList(new String[] { "BOU", "CLR" }));
						}

						if (instrument == null) {
							log.info("Instrument Null");
							// batchStatus = "PU";
						} else {
							String status = (csvRecord.get(21).equalsIgnoreCase("FAILED")) ? "BOU" : "CLR";
							log.info("Status " + status);
							instrument.setInstrumentStatus(status);
							if (status.equalsIgnoreCase("BOU")) {
								if (csvRecord.get(25) == null || csvRecord.get(25).equals("")) {
									throw new CoreDataNotFoundException(
											"Bounce Reason not found for master agreement " + csvRecord.get(32));
								}

								List<GstListDto> gstListTemp = gstService.getGstList(csvRecord.get(32), "BOUNCE", 500d);
							}
							log.info("Status " + status);
							instrument.setBounceReason(csvRecord.get(25));
							instrument.setDtStatusUpdate(businessDate);
							log.info(" Date =" + csvRecord.get(19));
							if (csvRecord.get(19).equalsIgnoreCase("") || csvRecord.get(19) == null) {

							} else {
								instrument.setDtSettlementDate(sdf.parse(csvRecord.get(19)));
							}
							if (status.equalsIgnoreCase("CLR")) {
								instrument.setUtrNo(csvRecord.get(29));
							}

							listInst.add(instrument);

							customerSet.add(instrument.getCustomerId());

							String paymentRelease = paymentService.paymentRelease(instrument.getCustomerId(),
									businessDate);
						}
					}
				}
				i++;

			}

			/*
			 * for (String customer : customerSet) { String paymentRelease =
			 * paymentService.paymentRelease(customer, businessDate);
			 * 
			 * log.info(" paymentRelease Result " + paymentRelease); }
			 */

			instRepo.saveAll(listInst);

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
	public static void deleteFiles(File dirPath) {
		File filesList[] = dirPath.listFiles();
		log.info("InstrumentServices :: deleteFiles :: Files list : {}",dirPath.listFiles());
		log.info("InstrumentServices :: deleteFiles :: dirPath : {}",dirPath);
		for (File file : filesList) {
			if (file.isFile()) {
				file.delete();
			} else {
				deleteFiles(file);
			}
		}
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<VLmsHcInstrumentVsPayapplied> getBatchHealthDetails(Integer batchId) {

		List<VLmsHcInstrumentVsPayapplied> resultList = new ArrayList<VLmsHcInstrumentVsPayapplied>();

		resultList = batchHeathViewRepo.findByBatchId(batchId);

		return resultList;

	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Page<FutureInstrumentsListDto> getInstrumentList(Date fromDate, Date toDate, Pageable pageable )
			throws Exception {

		List<FutureInstrumentsListDto> futureInstrumentListDto = new ArrayList<FutureInstrumentsListDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			if (!(fromDate == null)) {
				List<TrnInsInstrument> trnInstrumentList = trnInsInstrumentRepository.findBydtInstrumentDateBetween(fromDate, toDate);

				if (trnInstrumentList.size() == 0) {
					throw new InstrumentDataNotFoundException("Instrument details not found.");
				}
				for (TrnInsInstrument insInstrument : trnInstrumentList) {
					FutureInstrumentsListDto futureInstrumentDto = new FutureInstrumentsListDto();
					BeanUtils.copyProperties(insInstrument, futureInstrumentDto);
					futureInstrumentDto.setDtInstrumentDate(sdf.format(insInstrument.getDtInstrumentDate()));
					futureInstrumentListDto.add(futureInstrumentDto);
				}
			} 
			
	        return pageableUtils.convertToPage(futureInstrumentListDto, pageable);

		} catch (InstrumentDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

}
	
}
