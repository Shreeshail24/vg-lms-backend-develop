package com.samsoft.lms.batch.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import com.samsoft.lms.agreement.entities.AgrCollateral;
import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.repositories.AgrCollateralRepository;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.services.DisbursementService;
import com.samsoft.lms.batch.dto.DueDetailsDto;
import com.samsoft.lms.batch.dto.EodStatusDto;
import com.samsoft.lms.batch.dto.EodStatusMainDto;
import com.samsoft.lms.batch.entities.AgrTrnBkpDueDetails;
import com.samsoft.lms.batch.entities.AgrTrnBkpLoanDetails;
import com.samsoft.lms.batch.entities.AgrTrnBkpSummary;
import com.samsoft.lms.batch.entities.AgrTrnBkpTaxDueDetails;
import com.samsoft.lms.batch.entities.AgrTrnSysProvisionDtls;
import com.samsoft.lms.batch.entities.ArcAgrTrnBkpDueDetails;
import com.samsoft.lms.batch.entities.ArcAgrTrnBkpLoanDetails;
import com.samsoft.lms.batch.entities.ArcAgrTrnBkpSummary;
import com.samsoft.lms.batch.entities.ArcAgrTrnBkpTaxDueDetails;
import com.samsoft.lms.batch.entities.ArcCustApplLimitBkpSetup;
import com.samsoft.lms.batch.entities.CustApplLimitBkpSetup;
import com.samsoft.lms.batch.entities.EodLog;
import com.samsoft.lms.batch.entities.EodStatus;
import com.samsoft.lms.batch.entities.LoanProductView;
import com.samsoft.lms.batch.entities.VMstProvisionSetup;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.batch.repositories.AgrTrnBkpDueDetailsRepository;
import com.samsoft.lms.batch.repositories.AgrTrnBkpLoanDetailsRepository;
import com.samsoft.lms.batch.repositories.AgrTrnBkpSummaryRepository;
import com.samsoft.lms.batch.repositories.AgrTrnBkpTaxDueDetailsRepository;
import com.samsoft.lms.batch.repositories.AgrTrnSysProvisionDtlsRepository;
import com.samsoft.lms.batch.repositories.ArcAgrTrnBkpDueDetailsRepository;
import com.samsoft.lms.batch.repositories.ArcAgrTrnBkpLoanDetailsRepository;
import com.samsoft.lms.batch.repositories.ArcAgrTrnBkpSummaryRepository;
import com.samsoft.lms.batch.repositories.ArcAgrTrnBkpTaxDueDetailsRepository;
import com.samsoft.lms.batch.repositories.ArcCustApplLimitBkpSetupRepository;
import com.samsoft.lms.batch.repositories.CustApplLimitBkpSetupRepository;
import com.samsoft.lms.batch.repositories.EodLogRepository;
import com.samsoft.lms.batch.repositories.EodStatusRepository;
import com.samsoft.lms.batch.repositories.LoanProductViewRepository;
import com.samsoft.lms.batch.repositories.VMstProvisionSetupRepository;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.dto.GetAssetClassDto;
import com.samsoft.lms.core.dto.SysTranDtlDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProdSlabwiseInterest;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnSysTranDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.entities.TabOrganization;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProdSlabwiseInterestRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.core.services.ColenderDueService;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.InterestService;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.instrument.dto.BatchCreateResDto;
import com.samsoft.lms.instrument.entities.TrnInsBatchHdr;
import com.samsoft.lms.instrument.entities.TrnInsBatchInstruments;
import com.samsoft.lms.instrument.entities.TrnInsBatchStatus;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.repositories.TrnInsBatchHdrRepository;
import com.samsoft.lms.instrument.repositories.TrnInsBatchInstrumentsRepository;
import com.samsoft.lms.instrument.repositories.TrnInsBatchStatusRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.instrument.services.InstrumentServices;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementPaySchRepository;
import com.samsoft.lms.util.PlatformUtils;

@Service
@Slf4j
public class EODServices {

	@Autowired
	private AgrTrnTranHeaderRepository headerRepo;
	@Autowired
	private AgrMasterAgreementRepository masterRepo;
	@Autowired
	private AgrTrnBkpLoanDetailsRepository bkpDetailRepo;
	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;
	@Autowired
	private AgrRepayScheduleRepository repayRepo;
	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private TabOrganizationRepository orgRepo;
	@Autowired
	private AgrTrnTaxDueDetailsRepository taxDueRepo;
	@Autowired
	private AgrTrnBkpTaxDueDetailsRepository bkpTaxDueRepo;
	@Autowired
	private AgrTrnBkpSummaryRepository bkpSummaryRepo;
	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;
	@Autowired
	private EodLogRepository eodLogRepo;
	@Autowired
	private AgrTrnBkpDueDetailsRepository bkpDueRepo;
	@Autowired
	private InterestService interestService;
	@Autowired
	private AgrProductRepository prodRepo;
	@Autowired
	private LoanProductViewRepository loanProductViewRepo;
	@Autowired
	private EodStatusRepository eodStatusRepo;
	@Autowired
	private Environment env;
	@Autowired
	private CommonServices commService;
	@Autowired
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private PaymentApplicationServices paymentServ;
	@Autowired
	private InstrumentServices instrumentService;
	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	@Autowired
	private VMstProvisionSetupRepository provisionViewRepo;
	@Autowired
	private AgrTrnSysProvisionDtlsRepository provisionSysRepo;
	@Autowired
	private AgrCollateralRepository collRepo;
	@Autowired
	private ColenderDueService conlenderDueService;
	@Autowired
	private ArcAgrTrnBkpSummaryRepository arcSummaryRepo;
	@Autowired
	private ArcAgrTrnBkpLoanDetailsRepository arcLoanRepo;
	@Autowired
	private ArcAgrTrnBkpDueDetailsRepository arcDueRepo;
	@Autowired
	private ArcAgrTrnBkpTaxDueDetailsRepository arcTaxRepo;
	@Autowired
	private AgrTrnBkpLoanDetailsRepository bkpLoanRepo;
	@Autowired
	private EodErrorService eodErrorService;
	@Autowired
	private AgrTrnReqSettlementPaySchRepository settlmentRepo;
	@Autowired
	private DisbursementService disbService;
	@Autowired
	private CustApplLimitSetupRepository custLimitRepo;
	@Autowired
	private CustApplLimitBkpSetupRepository custLimitBkpRepo;
	@Autowired
	private ArcCustApplLimitBkpSetupRepository arcCustLimitRepo;
	@Autowired
	private SupplierFinanceUtility suppUtility;
	@Autowired
	private TrnInsBatchInstrumentsRepository batchInstRepo;
	@Autowired
	private TrnInsBatchStatusRepository batchStatusRepo;
	@Autowired
	private AgrProdSlabwiseInterestRepository slabRepo;
	@Autowired
	private AgrDisbursementRepository disbRepo;
	@Autowired
	private TrnInsBatchHdrRepository hdrRepo;

	private String globalCustomerId;

	private String globalLoanId;

	@Autowired
	private PlatformUtils platformUtils;

	@Value("${lms.batch.create.mail}")
	private String batchCreateMail;

	@Autowired
	private InstrumentServices instrumentServices;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean installmentBilling(Integer backupId, Date dtBackup) throws ParseException {

		Boolean result = true;
		List<AgrTrnTranDetail> listDetails = new ArrayList<AgrTrnTranDetail>();
		List<AgrTrnDueDetails> listDue = new ArrayList<AgrTrnDueDetails>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			List<AgrTrnBkpLoanDetails> listLoans = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			if (listLoans.isEmpty()) {
				throw new EodExceptions(
						"No Loan is available for the backup Id :" + backupId + " in Loan Backup Table");
			}

			log.info("EODServices :: installmentBilling :: AgrTrnBkpLoanDetails :: Size: {}", listLoans.size());

			Calendar c = Calendar.getInstance();
			c.setTime(dtBackup);
			c.add(Calendar.DATE, 1);
			dtBackup = c.getTime();
			for (AgrTrnBkpLoanDetails bkpLoan : listLoans) {
				AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(bkpLoan.getMastAgrId());
				log.info("Date " + dtBackup.toString());
				globalCustomerId = bkpLoan.getCustomerId();
				globalLoanId = bkpLoan.getLoanId();
				listDetails = new ArrayList<AgrTrnTranDetail>();
				AgrMasterAgreement master = masterRepo.findByMastAgrId(bkpLoan.getMastAgrId());
				// consideration is that only single record will be fetched from below method
				List<AgrRepaySchedule> repayObjList = repayRepo.findByLoanIdAndDtInstallment(bkpLoan.getLoanId(),
						dtBackup);

				log.info("EODServices :: installmentBilling :: AgrTrnBkpLoanDetails :: AgrRepaySchedule Size: {}",
						repayObjList.size());
				for (AgrRepaySchedule repayObj : repayObjList) {
					/*
					 * throw new EodExceptions("No Loan is available for the Loan Id :" +
					 * bkpLoan.getLoanId() + " in Loan Repay Schedule Table");
					 */
					log.info(
							"EODServices :: installmentBilling :: AgrTrnBkpLoanDetails :: AgrRepaySchedule :: Inside If");

					AgrTrnTranHeader header = new AgrTrnTranHeader();
					header.setMasterAgr(master);
					header.setTranDate(repayObj.getDtInstallment());
					header.setTranType("INSTALLMENT_BILLING");
					header.setRemark("Installment Billing for the month");
					header.setSource("SYSTEM");
					header.setReqId("");
					header.setUserID("SYSTEM");
					if (limitSetup != null) {
						header.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
					}

					AgrTrnEventDtl event = new AgrTrnEventDtl();
					event.setTranHeader(header);
					event.setTranEvent("INSTALLMENT_BILLING");
					event.setTranAmount(repayObj.getInstallmentAmount());
					event.setUserId("SYSTEM");

					if (repayObj.getBpiAmount() > 0) {
						AgrTrnTranDetail detail = new AgrTrnTranDetail();
						detail.setEventDtl(event);
						detail.setMasterAgr(master);
						detail.setLoan(loanRepo.findByLoanId(bkpLoan.getLoanId()));
						detail.setTranCategory("INSTALLMENT");
						detail.setTranHead("BPI");
						detail.setTranAmount(commService.numberFormatter(repayObj.getBpiAmount()));
						detail.setTranSide("DR");
						detail.setDtlRemark("BPI Billing for the month");
						detail.setDtDueDate(repayObj.getDtInstallment());
						detail.setInstallmentNo(repayObj.getInstallmentNo());
						if (limitSetup != null) {
							detail.setAvailableLimit(commService
									.numberFormatter(limitSetup.getAvailableLimit() - repayObj.getBpiAmount()));
							detail.setUtilizedLimit(commService
									.numberFormatter(limitSetup.getUtilizedLimit() + repayObj.getBpiAmount()));
							paymentServ.updateLimit(bkpLoan.getMastAgrId(), (repayObj.getBpiAmount()), "DED", "BILLING",
									header.getTranId(), "INTERFACE");
						}

						// header.setTranDetails(listDetails);
						tranDtlRepo.save(detail);
						AgrTrnDueDetails due = new AgrTrnDueDetails();
						due.setTranDtlId(detail.getTranDtlId());
						due.setMastAgrId(master.getMastAgrId());
						due.setLoanId(bkpLoan.getLoanId());
						due.setDtDueDate(repayObj.getDtInstallment());
						due.setDueCategory("INSTALLMENT");
						due.setDueHead("BPI");
						due.setDueAmount(repayObj.getBpiAmount());
						due.setInstallmentNo(repayObj.getInstallmentNo());
						due.setTranDtlId(detail.getTranDtlId());
						// listDue = new ArrayList<AgrTrnDueDetails>();

						listDue.add(due);
						// detail.setTranDueDetails(listDue);
						listDetails.add(detail);

						// Supplier Finance Changes Start

						if (bkpLoan.getLoanType().equals("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), master.getProductCode(), "DED", repayObj.getBpiAmount(),
									header.getTranId(), "EOD", dtBackup);

						}

						// Supplier Finance Changes End

						ColenderDueDto colenderDto = new ColenderDueDto();
						colenderDto.setDtDueDate(dtBackup);
						colenderDto.setDueAmount(repayObj.getBpiAmount());
						colenderDto.setDueCategory("INSTALLMENT");
						colenderDto.setDueHead("BPI");
						colenderDto.setInstallmentNo(repayObj.getInstallmentNo());
						colenderDto.setLoanId(bkpLoan.getLoanId());
						colenderDto.setMastAgrId(master.getMastAgrId());
						colenderDto.setTranDtlId(detail.getTranDtlId());
						colenderDto.setUserId("SYSTEM");
						conlenderDueService.generateColenderDues(colenderDto);
					}

					if (repayObj.getInterestAmount() > 0) {
						AgrTrnTranDetail detail = new AgrTrnTranDetail();
						detail.setEventDtl(event);
						detail.setMasterAgr(master);
						detail.setLoan(loanRepo.findByLoanId(bkpLoan.getLoanId()));
						detail.setTranCategory("INSTALLMENT");
						detail.setTranHead("INTEREST");
						detail.setTranAmount(repayObj.getInterestAmount());
						detail.setTranSide("DR");
						detail.setInstallmentNo(repayObj.getInstallmentNo());
						detail.setDtlRemark("Interest Billing for the month");
						detail.setDtDueDate(repayObj.getDtInstallment());
						if (limitSetup != null) {
							detail.setAvailableLimit(commService
									.numberFormatter(limitSetup.getAvailableLimit() - repayObj.getInterestAmount()));
							detail.setUtilizedLimit(commService
									.numberFormatter(limitSetup.getUtilizedLimit() + repayObj.getInterestAmount()));
							paymentServ.updateLimit(bkpLoan.getMastAgrId(), (repayObj.getInterestAmount()), "DED",
									"BILLING", header.getTranId(), "EOD");
						}

						// Supplier Finance Changes Start

						if (bkpLoan.getLoanType().equals("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), master.getProductCode(), "DED",
									repayObj.getInterestAmount(), header.getTranId(), "EOD", dtBackup);

						}

						// Supplier Finance Changes End

						tranDtlRepo.save(detail);
						AgrTrnDueDetails due = new AgrTrnDueDetails();
						due.setTranDtlId(detail.getTranDtlId());
						due.setMastAgrId(master.getMastAgrId());
						due.setLoanId(bkpLoan.getLoanId());
						due.setDtDueDate(repayObj.getDtInstallment());
						due.setDueCategory("INSTALLMENT");
						due.setDueHead("INTEREST");
						due.setDueAmount(repayObj.getInterestAmount());
						due.setInstallmentNo(repayObj.getInstallmentNo());
						due.setTranDtlId(detail.getTranDtlId());
						// listDue = new ArrayList<AgrTrnDueDetails>();

						listDue.add(due);
						// detail.setTranDueDetails(listDue);
						listDetails.add(detail);

						ColenderDueDto colenderDto = new ColenderDueDto();
						colenderDto.setDtDueDate(dtBackup);
						colenderDto.setDueAmount(repayObj.getInterestAmount());
						colenderDto.setDueCategory("INSTALLMENT");
						colenderDto.setDueHead("INTEREST");
						colenderDto.setInstallmentNo(repayObj.getInstallmentNo());
						colenderDto.setLoanId(bkpLoan.getLoanId());
						colenderDto.setMastAgrId(master.getMastAgrId());
						colenderDto.setTranDtlId(detail.getTranDtlId());
						colenderDto.setUserId("SYSTEM");
						conlenderDueService.generateColenderDues(colenderDto);
					}
					if (repayObj.getPrincipalAmount() > 0) {
						AgrTrnTranDetail detail = new AgrTrnTranDetail();
						detail.setEventDtl(event);
						detail.setMasterAgr(master);
						detail.setLoan(loanRepo.findByLoanId(bkpLoan.getLoanId()));
						detail.setTranCategory("INSTALLMENT");
						detail.setTranHead("PRINCIPAL");
						detail.setTranAmount(repayObj.getPrincipalAmount());
						detail.setTranSide("DR");
						detail.setInstallmentNo(repayObj.getInstallmentNo());
						detail.setDtlRemark("Principal Billing for the month");
						detail.setDtDueDate(repayObj.getDtInstallment());
						if (limitSetup != null) {
							detail.setAvailableLimit(commService.numberFormatter(limitSetup.getAvailableLimit()));
							detail.setUtilizedLimit(commService.numberFormatter(limitSetup.getUtilizedLimit()));
						}
						tranDtlRepo.save(detail);
						AgrTrnDueDetails due = new AgrTrnDueDetails();
						due.setTranDtlId(detail.getTranDtlId());
						due.setMastAgrId(master.getMastAgrId());
						due.setLoanId(bkpLoan.getLoanId());
						due.setDtDueDate(repayObj.getDtInstallment());
						due.setDueCategory("INSTALLMENT");
						due.setDueHead("PRINCIPAL");
						due.setDueAmount(repayObj.getPrincipalAmount());
						due.setInstallmentNo(repayObj.getInstallmentNo());
						// listDue = new ArrayList<AgrTrnDueDetails>();

						listDue.add(due);
						// detail.setTranDueDetails(listDue);
						listDetails.add(detail);

						ColenderDueDto colenderDto = new ColenderDueDto();
						colenderDto.setDtDueDate(dtBackup);
						colenderDto.setDueAmount(repayObj.getPrincipalAmount());
						colenderDto.setDueCategory("INSTALLMENT");
						colenderDto.setDueHead("PRINCIPAL");
						colenderDto.setInstallmentNo(repayObj.getInstallmentNo());
						colenderDto.setLoanId(bkpLoan.getLoanId());
						colenderDto.setMastAgrId(master.getMastAgrId());
						colenderDto.setTranDtlId(detail.getTranDtlId());
						colenderDto.setUserId("SYSTEM");
						conlenderDueService.generateColenderDues(colenderDto);
					}
					// header.setTranDetails(listDetails);
					dueRepo.saveAll(listDue);
					headerRepo.save(header);

					// In below logic master agreement is not considered because loan account is the
					// primary key of ArgLoan table
					AgrLoans loan = loanRepo.findByMasterAgreementMastAgrIdAndLoanId(bkpLoan.getMastAgrId(),
							bkpLoan.getLoanId());
					loan.setUnbilledPrincipal(this.commService
							.numberFormatter(loan.getUnbilledPrincipal() - repayObj.getPrincipalAmount()));
					loan.setCurrentInstallmentNo(repayObj.getInstallmentNo());
					loan.setBalTenor(loan.getTenor() - loan.getCurrentInstallmentNo());

					log.info("After Loan");
					AgrRepaySchedule nextRepay = repayRepo
							.findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndInterestAmountGreaterThanOrderByInstallmentNoAsc(
									master.getMastAgrId(), repayObj.getDtInstallment(), 0d);
					log.info(" nextRepay " + nextRepay);
					if (nextRepay == null) {
						master.setDtNextInstallment(null);
						master.setNextInstallmentAmount(0d);
					} else {
						master.setDtNextInstallment(nextRepay.getDtInstallment());
					}
					master.setUnbilledPrincipal(this.commService
							.numberFormatter(master.getUnbilledPrincipal() - repayObj.getPrincipalAmount()));
					master.setPrevInstallmentAmount(repayObj.getInstallmentAmount());
					master.setDtPrevInstallment(repayObj.getDtInstallment());
					if (nextRepay != null) {
						loan.setCycleDay(Integer.parseInt(sdf.format(nextRepay.getDtInstallment()).split("-")[0]));
					}

					log.info("EODServices :: installmentBilling :: loan: {}, master: {}", loan, master);
					loanRepo.save(loan);
					masterRepo.save(master);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("INSTALLMENT BILLING");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean dropLineBilling(Integer backupId, Date dtBackup) throws ParseException {
		Boolean result = true;

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrTrnTranDetail> detailsList = new ArrayList<AgrTrnTranDetail>();

		List<AgrTrnBkpLoanDetails> listLoans = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);

		for (AgrTrnBkpLoanDetails loan : listLoans) {
			if ((loan.getLoanType().equalsIgnoreCase("DL"))) {
				AgrMasterAgreement masterObj = masterRepo.findByMastAgrIdAndDtNextDrop(loan.getMastAgrId(), dtBackup);
				AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(loan.getMastAgrId());
				AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(loan.getMastAgrId());
				if (masterObj != null) {

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

					Date prinDate = sdf.parse(
							product.getDropLineCycleDay() + "-" + sdf.format(masterObj.getDtNextDrop()).split("-")[1]
									+ "-" + sdf.format(masterObj.getDtNextDrop()).split("-")[2]);

					Calendar c1 = Calendar.getInstance();
					c1.setTime(prinDate);
					c1.add(Calendar.MONTH, addFactorPrin);
					prinDate = c1.getTime();

					masterObj.setDtNextDrop(prinDate);
					masterObj
							.setCurrentDroppedLimit(masterObj.getCurrentDroppedLimit() - masterObj.getNextDropAmount());

					masterRepo.save(masterObj);

					// if (limit.getUtilizedLimit() > masterObj.getCurrentDropAmount()) {
					paymentServ.updateLimit(masterObj.getMastAgrId(), masterObj.getNextDropAmount(), "DED", "DROP", 0,
							"SYSTEM");

					// Added by DeepakG
					AgrTrnTranHeader hdr = new AgrTrnTranHeader();
					hdr.setMasterAgr(masterObj);
					hdr.setTranDate(dtBackup);
					hdr.setTranType("LIMIT_DROPPED");
					hdr.setRemark("Limit Dropped Rs. " + masterObj.getNextDropAmount());
					hdr.setSource("INTERFACE");
					hdr.setUserID("INTERFACE");
					hdr.setSanctionedLimit(limit.getLimitSanctionAmount());

					AgrTrnEventDtl event = new AgrTrnEventDtl();
					event.setTranHeader(hdr);
					event.setTranEvent("LIMT_DROPPED");
					event.setTranAmount(0.0d);
					event.setUserId("INTERFACE");

					AgrTrnTranDetail detail = new AgrTrnTranDetail();
					detail.setEventDtl(event);
					detail.setMasterAgr(masterObj);
					detail.setDtDueDate(dtBackup);
					detail.setTranCategory("LIMIT_SETUP");
					detail.setTranHead("LIMIT_DROPPED");
					detail.setTranAmount(0.0d);
					detail.setTranSide("DR");
					detail.setDtlRemark("Limit Dropped Rs. " + masterObj.getNextDropAmount());
					detail.setAvailableLimit(limit.getAvailableLimit());
					detail.setUtilizedLimit(limit.getUtilizedLimit());
					detailsList.add(detail);

					tranDtlRepo.saveAll(detailsList);
//				
					// }

				}
			}
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public void updateFlag(TabOrganization tabObj) {
		TabOrganization orgObj = new TabOrganization();
		BeanUtils.copyProperties(tabObj, orgObj);
		orgObj.setMaintenanceFlag("Y");
		orgRepo.save(orgObj);

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String beforeMainEod(String orgId, String bussDate) throws ParseException {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		// LocalDate pBussinessDate = LocalDate.parse(bussDate, formatter);
		Date pBussinessDate = new SimpleDateFormat(dateFormat).parse(bussDate);
		TabOrganization orgObj = orgRepo.findAll().get(0);
		Date businessDate = orgRepo.findAll().get(0).getDtBusiness();

		if (pBussinessDate.compareTo(businessDate) < 0) {
			return result = "Invalid Input: Business Date";
		}

		// uncomment below code in production env
		/*
		 * if (pBussinessDate.isAfter(LocalDate.now())) { return result =
		 * "Invalid Input: Business Date"; }
		 */

		updateFlag(orgObj);

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String beforeMainEodScheduler(String orgId, String bussDate, TabOrganization orgObj) throws ParseException {
		String result = "success";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		// LocalDate pBussinessDate = LocalDate.parse(bussDate, formatter);
		Date pBussinessDate = new SimpleDateFormat(dateFormat).parse(bussDate);
		// TabOrganization orgObj = orgRepo.getById(orgId);
		Date businessDate = orgRepo.findAll().get(0).getDtBusiness();

		if (pBussinessDate.compareTo(businessDate) < 0) {
			return result = "Invalid Input: Business Date";
		}

		// uncomment below code in production env
		/*
		 * if (pBussinessDate.isAfter(LocalDate.now())) { return result =
		 * "Invalid Input: Business Date"; }
		 */

		updateFlag(orgObj);

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String mainEod(String orgId, String bussDate) {
		Date pBussinessDate = null;
		String result = "success";
		// String globalCustomerId = "";
		// String globalLoanId = "";
		AgrLoans loanAccount = null;
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
			// LocalDate pBussinessDate = LocalDate.parse(bussDate, formatter);
			pBussinessDate = new SimpleDateFormat(dateFormat).parse(bussDate);

			List<String> listCustomers = loanRepo.findAllCustomers();
			/*
			 * if (listCustomers.isEmpty()) { throw new
			 * EodExceptions("No Live loans are available in the system"); }
			 */
			for (String customer : listCustomers) {
				globalCustomerId = customer;
				eodErrorService.eodStaus(globalCustomerId, "MAIN_EOD", "STARTED", pBussinessDate);
				AgrTrnBkpSummary bkpSummary = new AgrTrnBkpSummary();
				// ArcAgrTrnBkpSummary arcSumamry = new ArcAgrTrnBkpSummary();
				bkpSummary.setDtBackup(pBussinessDate);
				bkpSummary.setCustomerId(customer);

				// BeanUtils.copyProperties(bkpSummary, arcSumamry);
				// List<ArcAgrTrnBkpLoanDetails> listArcBkpLoanDetails = new
				// ArrayList<ArcAgrTrnBkpLoanDetails>();

				// Supplier Finance Changes Start

				List<CustApplLimitSetup> custLimitList = custLimitRepo.findByCustomerId(customer);
				List<CustApplLimitBkpSetup> custLimitBkpList = new ArrayList<>();

				for (CustApplLimitSetup custLimit : custLimitList) {
					CustApplLimitBkpSetup custLimitBkp = new CustApplLimitBkpSetup();
					BeanUtils.copyProperties(custLimit, custLimitBkp);
					custLimitBkp.setBkpSummary(bkpSummary);
					custLimitBkpList.add(custLimitBkp);
				}

				// Supplier Finance Changes End

				List<AgrTrnBkpLoanDetails> listBkpLoanDetails = new ArrayList<AgrTrnBkpLoanDetails>();

				List<LoanProductView> listLoans = loanProductViewRepo.findAllByCustomerId(customer);
				if (listLoans.isEmpty()) {
					throw new EodExceptions(
							"Loans not available for customer " + customer + " in Loan or Product table");
				}
				for (LoanProductView loan : listLoans) {
					globalLoanId = loan.getLoanId();
					AgrTrnBkpLoanDetails bkpLoanDetail = new AgrTrnBkpLoanDetails();
					// ArcAgrTrnBkpLoanDetails arcBkpLoanDetail = new ArcAgrTrnBkpLoanDetails();

					loanAccount = loanRepo.findByLoanId(globalLoanId);
					Date minInstallmentDate = dueRepo.getMinInstallmentDate(globalLoanId);
					if (minInstallmentDate != null) {
						long dpd = (pBussinessDate.getTime() - minInstallmentDate.getTime()) / (1000 * 60 * 60 * 24)
								+ 1;
						loanAccount.setDpd((int) dpd);
						bkpLoanDetail.setDpd((int) dpd);
						loanAccount.getMasterAgreement().setDpd((int) dpd);
					} else {
						loanAccount.setDpd((int) 0);
						bkpLoanDetail.setDpd((int) 0);
						loanAccount.getMasterAgreement().setDpd((int) 0);
					}
					BeanUtils.copyProperties(loan, bkpLoanDetail);
					bkpLoanDetail.setMastAgrId(loan.getMastAgrId());
					bkpLoanDetail.setBkpSummary(bkpSummary);
					bkpLoanDetail.setTotalDues(
							this.commService.numberFormatter(commService.getLoanTotalDues(loan.getLoanId())));
					List<AgrTrnDueDetails> listDueDetails = dueRepo
							.findByMastAgrIdAndLoanIdOrderByDtDueDate(loan.getMastAgrId(), loan.getLoanId());

					// BeanUtils.copyProperties(bkpLoanDetail, arcBkpLoanDetail);
					// arcBkpLoanDetail.setBkpSummary(arcSumamry);
					/*
					 * if (listDueDetails.isEmpty()) { throw new
					 * EodExceptions("Due details are not available for Loan Id " +
					 * loan.getLoanId()); }
					 */
					List<AgrTrnBkpDueDetails> listBkpDues = new ArrayList<AgrTrnBkpDueDetails>();

					// List<ArcAgrTrnBkpDueDetails> listArcBkpDues = new
					// ArrayList<ArcAgrTrnBkpDueDetails>();

					for (AgrTrnDueDetails dueDetail : listDueDetails) {
						AgrTrnBkpDueDetails bkpDueDetail = new AgrTrnBkpDueDetails();
						// ArcAgrTrnBkpDueDetails arcBkpDueDetail = new ArcAgrTrnBkpDueDetails();

						BeanUtils.copyProperties(dueDetail, bkpDueDetail);
						bkpDueDetail.setBkpLoanDetails(bkpLoanDetail);
						bkpDueDetail.setMastAgrId(loan.getMastAgrId());
						bkpDueDetail.setLoanId(loan.getLoanId());

						// BeanUtils.copyProperties(bkpDueDetail, arcBkpDueDetail);
						// arcBkpDueDetail.setBkpLoanDetails(arcBkpLoanDetail);

						List<AgrTrnTaxDueDetails> listTaxDueDetails = taxDueRepo
								.findByDueDetailOrderByTaxDueIdAsc(dueDetail);

						List<AgrTrnBkpTaxDueDetails> listBkpTaxDues = new ArrayList<AgrTrnBkpTaxDueDetails>();

						// List<ArcAgrTrnBkpTaxDueDetails> listArcBkpTaxDues = new
						// ArrayList<ArcAgrTrnBkpTaxDueDetails>();

						if (listTaxDueDetails.size() > 0) {
							for (AgrTrnTaxDueDetails taxDueDetail : listTaxDueDetails) {
								AgrTrnBkpTaxDueDetails bkpTaxDueDetail = new AgrTrnBkpTaxDueDetails();
								// ArcAgrTrnBkpTaxDueDetails arcBkpTaxDueDetail = new
								// ArcAgrTrnBkpTaxDueDetails();

								BeanUtils.copyProperties(taxDueDetail, bkpTaxDueDetail);
								taxDueDetail.setDueDetail(dueDetail);
								listBkpTaxDues.add(bkpTaxDueDetail);

								// BeanUtils.copyProperties(bkpTaxDueDetail, arcBkpTaxDueDetail);
								// arcBkpTaxDueDetail.setBkpDueDetail(arcBkpDueDetail);
								// listArcBkpTaxDues.add(arcBkpTaxDueDetail);

							}
						}
						bkpDueDetail.setBkpTaxDueDetails(listBkpTaxDues);
						listBkpDues.add(bkpDueDetail);

						// arcBkpDueDetail.setBkpTaxDueDetails(listArcBkpTaxDues);
						// listArcBkpDues.add(arcBkpDueDetail);
					}
					bkpLoanDetail.setBkpDueDetails(listBkpDues);
					// arcBkpLoanDetail.setBkpDueDetails(listArcBkpDues);

					listBkpLoanDetails.add(bkpLoanDetail);
					// listArcBkpLoanDetails.add(arcBkpLoanDetail);

				}
				bkpSummary.setBkpLoanDetails(listBkpLoanDetails);

				// arcSumamry.setBkpLoanDetails(listArcBkpLoanDetails);

				AgrTrnBkpSummary saveSummary = bkpSummaryRepo.save(bkpSummary);
				loanRepo.save(loanAccount);

				this.archiveBackup(saveSummary.getBackupId());

				// arcSummaryRepo.save(arcSumamry);

			}

			TabOrganization orgObj1 = orgRepo.getById(orgId);

			orgObj1.setMaintenanceFlag("N");

			BatchCreateResDto batchCreateResDto = instrumentService.batchCreate(orgObj1.getDtBusiness());
			log.info("Batch Create :: batchCreateResDto: {}", batchCreateResDto);
//			FileOutputStream fos = null;
//			File file = null;
			// Send Mail after creating batch
			if (batchCreateResDto != null) {
				log.info("Batch Create :: inside if - send mail");
//				Integer[] arrBatchId = new Integer[1];
//				arrBatchId[0] = Integer.parseInt(batchId);
//
//				String filePath = env.getProperty("lms.batch.file.download.path") + pBussinessDate + ".zip";
//
//				FileInputStream fis = new FileInputStream(filePath);
//				byte[] content = Base64Utils.encode(IOUtils.toByteArray(fis));
//
//				file = new File(filePath);
//				fos = new FileOutputStream(file);
//				fos.write(content);
//
//				fos.close();

				Boolean res = platformUtils.sendBatchCreateMail(batchCreateMail, batchCreateResDto, null);
				log.info("Batch Create :: sendBatchCreateMail :: status: {}", res);
				if (res) {
					log.info("Batch Created - Send Mail.");
				}
			}

			Date bDate = orgObj1.getDtBusiness();
			Calendar c = Calendar.getInstance();
			c.setTime(bDate);
			c.add(Calendar.DATE, 1);
			bDate = c.getTime();
			orgObj1.setDtBusiness(bDate);
			orgRepo.save(orgObj1);

		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			EodLog error = new EodLog();
			error.setComponent("MAIN_EOD");
			error.setErrorMessage(result);
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);

			eodErrorService.eodStaus(globalCustomerId, "MAIN_EOD", "FAILED", pBussinessDate);
		}

		eodErrorService.eodStaus(globalCustomerId, "MAIN_EOD", "ENDED", pBussinessDate);

		try {

			List<AgrTrnBkpSummary> listBkpSummary = bkpSummaryRepo.findAll();
			for (AgrTrnBkpSummary bkpSummary : listBkpSummary) {
				String customerResult = this.customerEod(bkpSummary.getCustomerId(), pBussinessDate);
				if ((customerResult != "")) {
					EodLog error = new EodLog();
					error.setComponent("MAIN_EOD");
					error.setErrorMessage(customerResult);
					error.setCustomer(globalCustomerId);
					error.setLoanId(globalLoanId);
					eodErrorService.eodErrorLog(error);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
			EodLog error = new EodLog();
			error.setComponent("CUSTOMER_EOD");
			error.setErrorMessage(result);
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);

			eodErrorService.eodStaus(globalCustomerId, "CUSTOMER_EOD", "FAILED", pBussinessDate);
		}
		eodErrorService.eodStaus("", "BATCH_STATUS", "STARTED", pBussinessDate);
		this.batchStatusClose(pBussinessDate);
		this.droplineLimitUpdate(pBussinessDate);

		eodErrorService.eodStaus("", "BATCH_STATUS", "ENDED", pBussinessDate);

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String droplineLimitUpdate(Date businessDate) {
		String result = "";
		List<String> listMastAgr = masterRepo.findAllMasterAgreement();
		for (String nonLoanMstAgr : listMastAgr) {
			AgrMasterAgreement masterAgr = masterRepo.findByMastAgrId(nonLoanMstAgr);
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(nonLoanMstAgr);
			if (product.getProdType().equalsIgnoreCase("DL") && businessDate.equals(masterAgr.getDtNextDrop())) {
				List<AgrLoans> listOfLoans = loanRepo.findByMasterAgreementMastAgrId(nonLoanMstAgr);
				if (listOfLoans.size() == 0) {
					paymentServ.updateLimit(masterAgr.getMastAgrId(), masterAgr.getNextDropAmount(), "DED", "DROP", 0,
							"SYSTEM");
				}
			}

		}

		return "success";
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String customerEod(String customer, Date businessDate) {
		String result = "";
		eodErrorService.eodStaus(customer, "CUSTOMER_EOD", "STARTED", businessDate);
		try {
			List<AgrTrnBkpSummary> listBkpSummary = bkpSummaryRepo.findByCustomerIdOrderByBackupIdAsc(customer);
			for (AgrTrnBkpSummary bkpSummary : listBkpSummary) {
				eodErrorService.eodStaus(customer, "INSTALLMENT_BILLLING", "STARTED", businessDate);

				Boolean installmentBillingResult = this.installmentBilling(bkpSummary.getBackupId(),
						bkpSummary.getDtBackup());
				if (installmentBillingResult == false) {
					eodErrorService.eodStaus(customer, "INSTALLMENT_BILLLING", "FAILED", businessDate);
					throw new EodExceptions("Exception in installment Billing for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "INSTALLMENT_BILLLING", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "PENAL_COMPUTE", "STARTED", businessDate);
				Boolean penalComputeResult = this.penalCompute(bkpSummary.getBackupId(), bkpSummary.getDtBackup());
				if (penalComputeResult == false) {
					eodErrorService.eodStaus(customer, "PENAL_COMPUTE", "FAILED", businessDate);
					throw new EodExceptions("Exception in Penal Compute for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "PENAL_COMPUTE", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "DROPLINE_BILLLING", "STARTED", businessDate);
				Boolean droplineBillingResult = this.dropLineBilling(bkpSummary.getBackupId(),
						bkpSummary.getDtBackup());
				if (droplineBillingResult == false) {
					eodErrorService.eodStaus(customer, "DROPLINE_BILLLING", "FAILED", businessDate);
					throw new EodExceptions("Exception in dropline billing for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "PENAL_COMPUTE", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "PENAL_REVERSAL", "STARTED", businessDate);
				Boolean penalReversal = this.penalReversal(bkpSummary.getBackupId(), bkpSummary.getDtBackup());
				if (penalReversal == false) {
					eodErrorService.eodStaus(customer, "PENAL_REVERSAL", "FAILED", businessDate);

					throw new EodExceptions("Exception in Penal Reversal for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "PENAL_REVERSAL", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "PENAL_BOOKING", "STARTED", businessDate);
				Boolean penalBookingResult = this.penalBooking(bkpSummary.getBackupId(), bkpSummary.getDtBackup());
				if (penalBookingResult == false) {
					eodErrorService.eodStaus(customer, "PENAL_BOOKING", "FAILED", businessDate);

					throw new EodExceptions("Exception in Penal Booking for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "PENAL_BOOKING", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL", "STARTED", businessDate);
				Boolean interestAccrualResult = this.interestAccrual(bkpSummary.getBackupId(),
						bkpSummary.getDtBackup());
				if (interestAccrualResult == false) {
					eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL", "FAILED", businessDate);

					throw new EodExceptions("Exception in Interest Accrual for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL_REVERSAL", "STARTED", businessDate);
				Boolean interestAccrualReversalResult = this.interestAccrualReversal(bkpSummary.getBackupId(),
						bkpSummary.getDtBackup());
				if (interestAccrualResult == false) {
					eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL_REVERSAL", "FAILED", businessDate);

					throw new EodExceptions("Exception in Interest Accrual reversal for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "INTEREST_ACCRUAL_REVERSAL", "ENDED", businessDate);
				
				  eodErrorService.eodStaus(customer, "EXCESS_ADJUSTMENT", "STARTED",
				  businessDate); Boolean excessAdjustmentResult =
				  this.excessAdjustmentApi(bkpSummary.getCustomerId(),
				  bkpSummary.getDtBackup()); if (excessAdjustmentResult == false) {
				  eodErrorService.eodStaus(customer, "EXCESS_ADJUSTMENT", "FAILED",
				  businessDate);
				  
				  throw new EodExceptions("Exception in excess adjustment for customer " +
				  customer); } eodErrorService.eodStaus(customer, "EXCESS_ADJUSTMENT", "ENDED",
				  businessDate);
				 
				eodErrorService.eodStaus(customer, "NPA_PROVISIONING", "STARTED", businessDate);
				Boolean npaProvisioningResult = this.npaProvisioning(bkpSummary.getCustomerId(),
						bkpSummary.getDtBackup());
				if (npaProvisioningResult == false) {
					eodErrorService.eodStaus(customer, "NPA_PROVISIONING", "FAILED", businessDate);

					throw new EodExceptions("Exception in npa provisioning for customer " + customer);
				}
				eodErrorService.eodStaus(customer, "NPA_PROVISIONING", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "ACCOUNT_CLOSURE", "STARTED", businessDate);
				Boolean closureApiResult = this.agreementClosureApi(bkpSummary.getCustomerId());
				if (closureApiResult == false) {
					eodErrorService.eodStaus(customer, "ACCOUNT_CLOSURE", "FAILED", businessDate);

					throw new EodExceptions("Exception for account closure " + customer);
				}
				eodErrorService.eodStaus(customer, "ACCOUNT_CLOSURE", "ENDED", businessDate);

				eodErrorService.eodStaus(customer, "SETTLEMENT_WRITEOFF", "STARTED", businessDate);
				Boolean settlementWriteoffApiResult = this.settlementWriteoffApi(bkpSummary.getCustomerId(),
						businessDate);
				if (settlementWriteoffApiResult == false) {
					eodErrorService.eodStaus(customer, "SETTLEMENT_WRITEOFF", "FAILED", businessDate);

					throw new EodExceptions("Exception for settlement writeoff " + customer);
				}
				eodErrorService.eodStaus(customer, "SETTLEMENT_WRITEOFF", "ENDED", businessDate);

				bkpTaxDueRepo.deleteAllByBkpDueDetailBkpLoanDetailsBkpSummaryBackupId(bkpSummary.getBackupId());
				bkpDueRepo.deleteAllByBkpLoanDetailsBkpSummaryBackupId(bkpSummary.getBackupId());
				bkpDetailRepo.deleteAllByBkpSummaryBackupId(bkpSummary.getBackupId());
				// bkpSummaryRepo.deleteAllByBackupId(bkpSummary.getBackupId());

			}
		} catch (Exception e) {
			e.printStackTrace();
			eodErrorService.eodStaus(customer, "CUSTOMER_EOD", "FAILED", businessDate);

			return e.getMessage();
		}
		eodErrorService.eodStaus(customer, "CUSTOMER_EOD", "ENDED", businessDate);
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean penalBooking(Integer backupId, Date pBackupDate) {
		String dateFormat = env.getProperty("lms.global.date.format");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		LocalDate backupDate = LocalDate.parse(new SimpleDateFormat(dateFormat).format(pBackupDate), formatter);

		try {

			List<AgrTrnBkpLoanDetails> listBkpLoanDetails = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			log.info("1 " + listBkpLoanDetails.size());
			for (AgrTrnBkpLoanDetails bkpLoanDetail : listBkpLoanDetails) {
				AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(bkpLoanDetail.getMastAgrId());// Moved
				globalCustomerId = bkpLoanDetail.getCustomerId();
				globalLoanId = bkpLoanDetail.getLoanId();
				log.info("1 " + listBkpLoanDetails.size());
				log.info("2 " + bkpLoanDetail.getPenalCycleDay());
				log.info("3 " + backupDate.getDayOfMonth());
				if (bkpLoanDetail.getPenalCycleDay() == backupDate.getDayOfMonth()) {

					List<SysTranDtlDto> listSysTranDtls = sysRepo.getSysTranDetails("PENAL_ACCRUAL",
							bkpLoanDetail.getLoanId(), pBackupDate);

					for (SysTranDtlDto sysTranDtl : listSysTranDtls) {
						log.info("4 " + listSysTranDtls.size());
						if (sysTranDtl != null) {
							AgrTrnTranHeader header = new AgrTrnTranHeader();
							header.setMasterAgr(masterRepo.findByMastAgrId(bkpLoanDetail.getMastAgrId()));
							header.setTranDate(pBackupDate);
							header.setTranType("CHARGES_BOOKING");
							header.setRemark("Penal Charged Booked");
							header.setSource("SYSTEM");
							header.setUserID("SYSTEM");
							if (limitSetup != null) {
								header.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
							}
							log.info("5 ");
							AgrTrnEventDtl event = new AgrTrnEventDtl();
							event.setTranHeader(header);
							event.setTranEvent("CHARGES_BOOKING");
							event.setTranAmount(this.commService.numberFormatter(sysTranDtl.getTotalDue()));
							event.setUserId("SYSTEM");
							log.info("6 ");
							AgrTrnTranDetail detail = new AgrTrnTranDetail();
							detail.setMasterAgr(masterRepo.findByMastAgrId(bkpLoanDetail.getMastAgrId()));
							detail.setLoan(loanRepo.findByLoanId(bkpLoanDetail.getLoanId()));
							detail.setTranCategory("FEE");
							detail.setTranHead("PENAL");
							detail.setTranAmount(this.commService.numberFormatter(sysTranDtl.getTotalDue()));
							detail.setTranSide("DR");
							detail.setInstallmentNo(sysTranDtl.getInstallmentNo());
							detail.setDtlRemark("Penal booked for Inst# " + sysTranDtl.getInstallmentNo());
							detail.setDtDueDate(pBackupDate);
							detail.setEventDtl(event);
							if (limitSetup != null) {
								detail.setAvailableLimit(this.commService
										.numberFormatter(limitSetup.getAvailableLimit() - detail.getTranAmount()));
								detail.setUtilizedLimit(this.commService
										.numberFormatter(limitSetup.getUtilizedLimit() + detail.getTranAmount()));
							}
							log.info("7 " + detail);
							tranDtlRepo.save(detail);
							log.info("8 " + detail);
							AgrTrnDueDetails due = new AgrTrnDueDetails();
							due.setMastAgrId(bkpLoanDetail.getMastAgrId());
							due.setLoanId(bkpLoanDetail.getLoanId());
							due.setTranDtlId(detail.getTranDtlId());
							due.setDtDueDate(pBackupDate);
							due.setDueCategory("FEE");
							due.setDueHead("PENAL");
							due.setDueAmount(this.commService.numberFormatter(sysTranDtl.getTotalDue()));
							due.setInstallmentNo(sysTranDtl.getInstallmentNo());
							due.setTranDtlId(detail.getTranDtlId());

							AgrTrnDueDetails[] dueArray = new AgrTrnDueDetails[] { due };
							// detail.setTranDueDetails(Arrays.asList(dueArray));

							// AgrTrnTranDetail[] detailsArray = new AgrTrnTranDetail[] { detail };
							// header.setTranDetails(Arrays.asList(detailsArray));
							// headerRepo.save(header);
							log.info("10 ");

							dueRepo.save(due);
							log.info("11 ");
							if (bkpLoanDetail.getLoanType().equals("ND") || bkpLoanDetail.getLoanType().equals("DL")) {
								paymentServ.updateLimit(bkpLoanDetail.getMastAgrId(),
										this.commService.numberFormatter(sysTranDtl.getTotalDue()), "DED", "CHG_BK",
										header.getTranId(), "EOD");
							}

							// Supplier Finance Changes Start

							if (bkpLoanDetail.getLoanType().equals("OD")) {
								AgrMasterAgreement master = masterRepo.findByMastAgrId(bkpLoanDetail.getMastAgrId());
								suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
										master.getCustomerId(), master.getProductCode(), "DED",
										this.commService.numberFormatter(sysTranDtl.getTotalDue()), header.getTranId(),
										"EOD", pBackupDate);

							}

							// Supplier Finance Changes End

							List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
									.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
											"PENAL_ACCRUAL", bkpLoanDetail.getLoanId(), "N", pBackupDate);
							for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
								sysLoan.setAdjustedYn("Y");
								sysRepo.save(sysLoan);
							}

							ColenderDueDto colenderDto = new ColenderDueDto();
							colenderDto.setDtDueDate(pBackupDate);
							colenderDto.setDueAmount(this.commService.numberFormatter(sysTranDtl.getTotalDue()));
							colenderDto.setDueCategory("FEE");
							colenderDto.setDueHead("PENAL");
							colenderDto.setInstallmentNo(sysTranDtl.getInstallmentNo());
							colenderDto.setLoanId(bkpLoanDetail.getLoanId());
							colenderDto.setMastAgrId(bkpLoanDetail.getMastAgrId());
							colenderDto.setTranDtlId(detail.getTranDtlId());
							colenderDto.setUserId("SYSTEM");
							conlenderDueService.generateColenderDues(colenderDto);
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			EodLog error = new EodLog();
			error.setComponent("PENAL_BOOKING");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
			return false;
		}
		return true;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean penalCompute(Integer backupId, Date backupDate) {
		Boolean result = true;
		try {
			List<AgrTrnBkpLoanDetails> listBkpLoanDetails = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			for (AgrTrnBkpLoanDetails bkpLoanDetail : listBkpLoanDetails) {
				globalCustomerId = bkpLoanDetail.getCustomerId();
				globalLoanId = bkpLoanDetail.getLoanId();
				// Optional<Double> installmentDueAmount =
				// bkpDueRepo.getSumOfDueAmount(backupId, "INSTALLMENT");
				List<DueDetailsDto> listDueDtlDto = bkpDueRepo.getDueDetails(bkpLoanDetail.getLoanBackupId(),
						"INSTALLMENT");
				for (DueDetailsDto bkpDueDtl : listDueDtlDto) {
					// if (installmentDueAmount.isPresent()) {
					if (bkpDueDtl.getInstallmentDue() > 0) {
						Double interestAmount = this.commService.numberFormatter(interestService.getInterestAmount(
								bkpDueDtl.getInstallmentDue(), bkpLoanDetail.getPenalInterestRate(),
								bkpLoanDetail.getPenalInterestBasis(), backupDate, backupDate));
						if (interestAmount > 0) {
							AgrTrnSysTranDtl sysTranDtl = new AgrTrnSysTranDtl();
							sysTranDtl.setCustomerId(bkpLoanDetail.getCustomerId());
							sysTranDtl.setMastAgrId(bkpLoanDetail.getMastAgrId());
							sysTranDtl.setLoanId(bkpLoanDetail.getLoanId());
							sysTranDtl.setDtTranDate(backupDate);
							sysTranDtl.setTranType("PENAL_ACCRUAL");
							sysTranDtl.setTranAmount(interestAmount);
							sysTranDtl.setInstallmentNo(bkpDueDtl.getInstallmentNo());
							sysTranDtl.setDpd(bkpLoanDetail.getDpd() + 1);
							sysTranDtl.setRemark("Penal Accrual");
							sysTranDtl.setUserId("SYSTEM");

							sysRepo.save(sysTranDtl);
						}

					}
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("PENAL_COMPUTING");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean penalReversal(Integer backupId, Date backupDate) {
		Boolean result = true;
		try {
			List<AgrTrnBkpLoanDetails> listBkpLoanDetails = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			for (AgrTrnBkpLoanDetails bkpLoanDetail : listBkpLoanDetails) {
				globalCustomerId = bkpLoanDetail.getCustomerId();
				globalLoanId = bkpLoanDetail.getLoanId();
				Double totalDue;
				if (bkpLoanDetail.getTotalDues() == null) {
					totalDue = 0.0d;
				} else {
					totalDue = bkpLoanDetail.getTotalDues();
				}
				if (totalDue == 0) {
					Integer maxDpd = sysRepo.getMaxDpd("PENAL_ACCRUAL", bkpLoanDetail.getLoanId());
					Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(bkpLoanDetail.getMastAgrId())
							.getGraceDays();
					if (maxDpd == null) {
						maxDpd = 0;
					}
					if (graceDays == null) {
						graceDays = 0;
					}
					if (maxDpd < graceDays) {
						Double sumOfDues = this.commService.numberFormatter(sysRepo
								.getSumOfTranAmountOfPenal("PENAL_ACCRUAL", bkpLoanDetail.getLoanId(), backupDate));
						if (sumOfDues != null) {
							if (sumOfDues > 0) {
								AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
								sysTran.setCustomerId(bkpLoanDetail.getCustomerId());
								sysTran.setMastAgrId(bkpLoanDetail.getMastAgrId());
								sysTran.setLoanId(bkpLoanDetail.getLoanId());
								sysTran.setDtTranDate(backupDate);
								sysTran.setTranType("PENAL_ACCRUAL_REVERSAL");
								sysTran.setTranAmount(sumOfDues * -1);

								sysTran.setDpd(maxDpd);
								sysTran.setRemark("Penal Accrual Reversal");
								sysTran.setUserId("SYSTEM");

								sysRepo.save(sysTran);

								List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
										.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
												"PENAL_ACCRUAL", bkpLoanDetail.getLoanId(), "N", backupDate);
								for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
									sysLoan.setAdjustedYn("Y");
									sysRepo.save(sysLoan);
								}
							}

						}

						if (bkpLoanDetail.getLoanType().equals("OD")) {

							Double sumOfAddDues = this.commService.numberFormatter(sysRepo.getSumOfTranAmountOfPenal(
									"ADD_INTEREST_ACCRUAL", bkpLoanDetail.getLoanId(), backupDate));
							if (sumOfAddDues != null) {
								if (sumOfAddDues > 0) {
									AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
									sysTran.setCustomerId(bkpLoanDetail.getCustomerId());
									sysTran.setMastAgrId(bkpLoanDetail.getMastAgrId());
									sysTran.setLoanId(bkpLoanDetail.getLoanId());
									sysTran.setDtTranDate(backupDate);
									sysTran.setTranType("ADD_INTEREST_ACCRUAL_REVERSAL");
									sysTran.setTranAmount(sumOfAddDues * -1);

									sysTran.setDpd(maxDpd);
									sysTran.setRemark("Additional Interest Accrual Reversal");
									sysTran.setUserId("SYSTEM");

									sysRepo.save(sysTran);

									List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
											.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
													"ADD_INTEREST_ACCRUAL", bkpLoanDetail.getLoanId(), "N", backupDate);
									for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
										sysLoan.setAdjustedYn("Y");
										sysRepo.save(sysLoan);
									}
								}

							}

						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("PENAL_REVERSAL");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean interestAccrualReversal(Integer backupId, Date backupDate) {
		Boolean result = true;
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			Boolean odReversalRun = false;
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
			LocalDate date1 = LocalDate.parse(new SimpleDateFormat(dateFormat).format(backupDate), formatter);
			date1 = date1.plusDays(1);
			List<AgrTrnBkpLoanDetails> listBkpLoanDetails = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			for (AgrTrnBkpLoanDetails bkpLoanDetail : listBkpLoanDetails) {
				globalCustomerId = bkpLoanDetail.getCustomerId();
				globalLoanId = bkpLoanDetail.getLoanId();

				Calendar c = Calendar.getInstance();
				c.setTime(backupDate);
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

				Calendar tmp = Calendar.getInstance();
				tmp.setTime(backupDate);

				Calendar backupDatePlus1Cal = Calendar.getInstance();
				backupDatePlus1Cal.setTime(backupDate);
				backupDatePlus1Cal.add(Calendar.DAY_OF_MONTH, 1);

				Date backupDatePlus1Day = backupDatePlus1Cal.getTime();

				if (bkpLoanDetail.getLoanType().equalsIgnoreCase("DL")
						|| bkpLoanDetail.getLoanType().equalsIgnoreCase("ND")) {
					if (bkpLoanDetail.getCycleDay() == date1.getDayOfMonth()) {
						odReversalRun = true;

					}
				}

				if (odReversalRun) {
					List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
							.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
									"INTEREST_ACCRUAL", bkpLoanDetail.getLoanId(), "N", backupDatePlus1Day);
					for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
						sysLoan.setAdjustedYn("X");
						sysRepo.save(sysLoan);
					}
				} else {
					if ((bkpLoanDetail.getCycleDay() == date1.getDayOfMonth())) {
						List<AgrTrnDueDetails> checkRowInDueTable = dueRepo.findByLoanIdAndDtDueDateAndDueCategory(
								globalLoanId, backupDatePlus1Day, "INSTALLMENT");
						if (checkRowInDueTable.size() > 0) {
							Double sumOfIntAccrual = this.commService.numberFormatter(sysRepo.getSumOfTranAmountOfPenal(
									"INTEREST_ACCRUAL", bkpLoanDetail.getLoanId(), backupDatePlus1Day));
							if (sumOfIntAccrual != null) {
								if (sumOfIntAccrual > 0) {
									AgrTrnSysTranDtl sysTran = new AgrTrnSysTranDtl();
									sysTran.setCustomerId(bkpLoanDetail.getCustomerId());
									sysTran.setMastAgrId(bkpLoanDetail.getMastAgrId());
									sysTran.setLoanId(bkpLoanDetail.getLoanId());
									sysTran.setDtTranDate(backupDatePlus1Day);
									sysTran.setInstallmentNo(bkpLoanDetail.getCurrentInstallmentNo());
									sysTran.setTranType("INTEREST_ACCRUAL_REVERSAL");
									sysTran.setTranAmount(sumOfIntAccrual * -1);

									sysTran.setRemark("Interest Accrual Reversal");
									sysTran.setUserId("SYSTEM");

									sysRepo.save(sysTran);

									List<AgrTrnSysTranDtl> sysLoanIdList = sysRepo
											.findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(
													"INTEREST_ACCRUAL", bkpLoanDetail.getLoanId(), "N",
													backupDatePlus1Day);
									for (AgrTrnSysTranDtl sysLoan : sysLoanIdList) {
										sysLoan.setAdjustedYn("Y");
										sysRepo.save(sysLoan);
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("INTEREST_ACCRUAL_REVERSAL");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean interestAccrual(Integer backupId, Date backupDate) {
		Boolean result = true;
		try {
			Date backupDatePrev = backupDate;
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
			LocalDate localBackupDate = LocalDate.parse(new SimpleDateFormat(dateFormat).format(backupDate), formatter);
			// int day = localBackupDate.getDayOfMonth();
			// int endDayOfDate =
			// localBackupDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

			Calendar cal = Calendar.getInstance();
			cal.setTime(backupDate);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int endDayOfDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			if (day == endDayOfDate && day != 31) {
				// backupDatePrev = new
				// SimpleDateFormat(dateFormat).parse(localBackupDate.minusDays(1).toString());
				cal.add(Calendar.DATE, -1);
				backupDatePrev = cal.getTime();
			}

			List<AgrTrnBkpLoanDetails> listBkpLoanDetails = bkpDetailRepo.findAllByBkpSummaryBackupId(backupId);
			for (AgrTrnBkpLoanDetails bkpLoanDetail : listBkpLoanDetails) {
				if ((bkpLoanDetail.getLoanAdditionalStatus() == null) && (bkpLoanDetail.getUnbilledPrincipal() > 0)
						&& bkpLoanDetail.getInterestRate() > 0) {
					double interestAmount = this.commService.numberFormatter(interestService.getInterestAmount(
							bkpLoanDetail.getUnbilledPrincipal(), bkpLoanDetail.getInterestRate(),
							bkpLoanDetail.getInterestBasis(), backupDate, backupDate));
					AgrTrnSysTranDtl sysTranDetails = new AgrTrnSysTranDtl();
					sysTranDetails.setCustomerId(bkpLoanDetail.getCustomerId());
					sysTranDetails.setMastAgrId(bkpLoanDetail.getMastAgrId());
					sysTranDetails.setLoanId(bkpLoanDetail.getLoanId());
					sysTranDetails.setDtTranDate(backupDate);
					sysTranDetails.setTranType("INTEREST_ACCRUAL");
					sysTranDetails.setTranAmount(interestAmount);
					sysTranDetails.setInstallmentNo(bkpLoanDetail.getCurrentInstallmentNo());
					sysTranDetails.setDpd(bkpLoanDetail.getDpd());
					sysTranDetails.setRemark("Interest Accrual");
					sysTranDetails.setUserId("SYSTEM");
					sysRepo.save(sysTranDetails);
				} else {
					List<AgrTrnBkpDueDetails> bkpDueDetailsList = bkpDueRepo
							.findByBkpLoanDetailsLoanBackupId(bkpLoanDetail.getLoanBackupId());
					if (bkpDueDetailsList.size() > 0) {
						AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(bkpLoanDetail.getMastAgrId());
						List<AgrProdSlabwiseInterest> slabLlist = slabRepo.findByMasterAgrId(masterObj.getMastAgrId());
						AgrDisbursement disbursement = disbRepo
								.findFirstByMastAgrMastAgrIdOrderByDisbIdDesc(masterObj.getMastAgrId());
						Date disbDate = disbursement.getDtDisbDate();
						double unbilledPrincipal = commService.getLoanUnbilledPrincipal(bkpLoanDetail.getLoanId());
						int dpd = bkpLoanDetail.getDpd() + 1;

						long diffInMillies = Math.abs(backupDate.getTime() - disbDate.getTime());
						long daysDiff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

						double addIntDiffAccr = 0;// dmg
						if (bkpLoanDetail.getLoanType().equals("OD") && slabLlist.size() > 0) {
							log.info(
									"EODServices :: interestAccrual :: bkpLoanDetail.getLoanType().equals(\"OD\") && slabLlist.size() > 0 :: Inside If");
							AgrProdSlabwiseInterest slabNew = slabRepo.fetchNewSlab(masterObj.getMastAgrId(), dpd);
							if (slabNew == null) {
								log.info("EODServices :: interestAccrual :: slabNew == null :: Inside If");
								slabNew = slabRepo.findFirstByMasterAgrIdOrderByIntSlabIdDesc(masterObj.getMastAgrId());
							}

							AgrProdSlabwiseInterest slabOld = slabRepo
									.findByMasterAgrIdAndSlabAdjustedAndIntSlabIdLessThan(masterObj.getMastAgrId(), "N",
											slabNew.getIntSlabId());

							if (dpd == 1 && slabNew != null) {
								log.info("EODServices :: interestAccrual :: dpd == 1 && slabNew != null :: Inside If");

								addIntDiffAccr = interestService.getInterestAmount(masterObj.getLoanAmount(),
										slabNew.getInterestRate(), bkpLoanDetail.getInterestBasis(), disbDate,
										backupDate);

								if (addIntDiffAccr > 0) {
									log.info("EODServices :: interestAccrual :: addIntDiffAccr > 0 :: Inside If");
									AgrTrnSysTranDtl sysTranDtl = new AgrTrnSysTranDtl();
									sysTranDtl.setCustomerId(bkpLoanDetail.getCustomerId());
									sysTranDtl.setMastAgrId(bkpLoanDetail.getMastAgrId());
									sysTranDtl.setLoanId(bkpLoanDetail.getLoanId());
									sysTranDtl.setDtTranDate(backupDate);
									sysTranDtl.setTranType("ADD_INTEREST_ACCRUAL");
									sysTranDtl.setTranAmount(addIntDiffAccr);
									sysTranDtl.setInstallmentNo(bkpLoanDetail.getCurrentInstallmentNo());
									sysTranDtl.setDpd((int) daysDiff);
									sysTranDtl.setRemark("Additional Interest Accrual");
									sysTranDtl.setUserId("SYSTEM");

									sysRepo.save(sysTranDtl);

									// slabNew.setSlabAdjusted("Y");
									// slabRepo.save(slabNew);
								}
							}
							if (dpd == slabNew.getTenorFrom() && slabOld != null) {
								log.info(
										"EODServices :: interestAccrual :: dpd == slabNew.getTenorFrom() && slabOld != null :: Inside If");
								// if (addIntDiffAccr > 0) {
								addIntDiffAccr = interestService.getInterestAmount(masterObj.getLoanAmount(),
										slabNew.getInterestRate() - slabOld.getInterestRate(),
										bkpLoanDetail.getInterestBasis(), disbDate, backupDate);
								if (addIntDiffAccr > 0) {
									log.info("EODServices :: interestAccrual :: addIntDiffAccr > 0 :: Inside If");

									AgrTrnSysTranDtl sysTranDtl = new AgrTrnSysTranDtl();
									sysTranDtl.setCustomerId(bkpLoanDetail.getCustomerId());
									sysTranDtl.setMastAgrId(bkpLoanDetail.getMastAgrId());
									sysTranDtl.setLoanId(bkpLoanDetail.getLoanId());
									sysTranDtl.setDtTranDate(backupDate);
									sysTranDtl.setTranType("ADD_INTEREST_ACCRUAL");
									sysTranDtl.setTranAmount(addIntDiffAccr);
									sysTranDtl.setInstallmentNo(bkpLoanDetail.getCurrentInstallmentNo());
									sysTranDtl.setDpd((int) daysDiff);
									sysTranDtl.setRemark("Additional Interest Accrual");
									sysTranDtl.setUserId("SYSTEM");

									sysRepo.save(sysTranDtl);

									slabOld.setSlabAdjusted("Y");
									slabRepo.save(slabOld);
								}

								// }
							}

							if (slabNew != null) {
								log.info("EODServices :: interestAccrual :: slabNew != null :: Inside If");

								double addIntAccr = interestService.getInterestAmount(masterObj.getLoanAmount(),
										(slabNew.getInterestRate() + bkpLoanDetail.getInterestRate()),
										bkpLoanDetail.getInterestBasis(), backupDate, backupDate);
								if (addIntAccr > 0) {
									log.info("EODServices :: interestAccrual :: addIntAccr > 0 :: Inside If");

									AgrTrnSysTranDtl sysTranDtl = new AgrTrnSysTranDtl();
									sysTranDtl.setCustomerId(bkpLoanDetail.getCustomerId());
									sysTranDtl.setMastAgrId(bkpLoanDetail.getMastAgrId());
									sysTranDtl.setLoanId(bkpLoanDetail.getLoanId());
									sysTranDtl.setDtTranDate(backupDate);
									sysTranDtl.setTranType("ADD_INTEREST_ACCRUAL");
									sysTranDtl.setTranAmount(addIntAccr);
									sysTranDtl.setInstallmentNo(bkpLoanDetail.getCurrentInstallmentNo());
									sysTranDtl.setDpd(1);
									sysTranDtl.setRemark("Additional Interest Accrual");
									sysTranDtl.setUserId("SYSTEM");

									sysRepo.save(sysTranDtl);

									// slabNew.setSlabAdjusted("Y");
									// slabRepo.save(slabNew);
								}
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("INTEREST_ACCRUAL");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean excessAdjustmentApi(String customerId, Date businessDate) throws Exception {
		Boolean result = true;

		try {
			List<AgrMasterAgreement> masterList = masterRepo
					.findAllByCustomerIdAndExcessAmountGreaterThanAndAgreementStatus(customerId, 0d, "L");
			log.info("masterList " + masterList.size());
			Double excessAdjAmt = 0.0;

			for (AgrMasterAgreement master : masterList) {
				if (commService.getMasterTotalDues(master.getMastAgrId()) > 0) {
					excessAdjAmt = Math.min(commService.getMasterTotalDues(master.getMastAgrId()),
							master.getExcessAmount());

					TrnInsInstrument inst = new TrnInsInstrument();
					inst.setMasterAgr(master.getMastAgrId());
					inst.setCustomerId(customerId);
					inst.setPayType("EXCESS_ADJ");
					inst.setDtReceipt(businessDate);
					inst.setDtInstrumentDate(businessDate);
					// inst.setInstrumentNo(Integer.toString(inst.getInstrumentId()));
					inst.setInstrumentStatus("CLR");
					inst.setInstrumentType("EXCESS");
					inst.setInstrumentAmount(excessAdjAmt);
					inst.setNclStatus("N");
					inst.setSource("EOD");
					inst.setUserId("SYSTEM");
					inst.setDtStatusUpdate(businessDate);

					TrnInsInstrument updateInstrumentNo = instRepo.save(inst);
					updateInstrumentNo.setInstrumentNo(Integer.toString(updateInstrumentNo.getInstrumentId()));
					instRepo.save(updateInstrumentNo);

					List<AgrLoans> loans = loanRepo
							.findByMasterAgreementMastAgrIdAndLoanAdditionalStatusIsNullOrderByTotalDues(
									master.getMastAgrId());

					log.info("loans " + loans.size());
					Double remainingAmount = excessAdjAmt;
					for (AgrLoans loan : loans) {
						Double apportionmentAmount = Math.min(remainingAmount, loan.getTotalDues());
						remainingAmount = remainingAmount - apportionmentAmount;

						TrnInsInstrumentAlloc instAlloc = new TrnInsInstrumentAlloc();
						instAlloc.setLoanId(loan.getLoanId());
						instAlloc.setApportionAmount(apportionmentAmount);
						instAlloc.setUserId("SYSTEM");
						instAlloc.setInstrument(inst);
					}

					instRepo.save(inst);
					master.setExcessAmount(commService.numberFormatter(master.getExcessAmount() - excessAdjAmt));
					masterRepo.save(master);

					String output = paymentServ.paymentRelease(customerId, businessDate);

					log.info("Payment Release resutl " + output);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("EXCESS_ADJ");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean npaProvisioning(String customerId, Date businessDate) throws Exception {
		Boolean result = true;
		String oldAssetClassCd = "";
		Double oldOSPrincipal = 0.0, oldOverDuePrincipal = 0.0, oldSecuredPrincipal = 0.0, oldNonSecuredPrincipal = 0.0;
		Float oldSecuredProvRate = 0.0F, oldNonSecuredProvRate = 0.0F;
		Double oldSecuredProvAmount = 0.0, oldNonSecuredProvAmount = 0.0, oldProvisionAmount = 0.0;
		Integer oldDpd = 0;
		String oldNpaFlag = "", revFlag = "";

		try {

			log.info("customerId " + customerId);
			List<AgrLoans> loanList = new ArrayList<AgrLoans>();
			loanList = loanRepo.findByCustomerIdAndLoanAdditionalStatusIsNull(customerId);
			if (loanList.size() == 0) {
				loanList = loanRepo.findByCustomerIdAndLoanAdditionalStatus(customerId, "");
			}

			for (AgrLoans loan : loanList) {

				VMstProvisionSetup provision = provisionViewRepo
						.findByPortfolioCdAndDpdFromLessThanEqualAndDpdToGreaterThanEqual(
								loan.getMasterAgreement().getPortfolioCode(), loan.getDpd(), loan.getDpd());
				if (provision == null) {
					throw new CoreBadRequestException("Provisioning setup not available for portfolio :"
							+ loan.getMasterAgreement().getPortfolioCode());
				} else {

					GetAssetClassDto newAssetClass = commService
							.getNewAssetClass(loan.getMasterAgreement().getPortfolioCode(), loan.getDpd());
					if (loan.getLastProvId() != null) {
						Optional<AgrTrnSysProvisionDtls> provDtlOptional = provisionSysRepo
								.findById(loan.getLastProvId());
						if (provDtlOptional.isPresent()) {
							AgrTrnSysProvisionDtls provDtl = provDtlOptional.get();
							oldAssetClassCd = provDtl.getAssetClassCd();
							oldOSPrincipal = provDtl.getOSPrincipal();
							oldOverDuePrincipal = provDtl.getOverDuePrincipal();
							oldSecuredPrincipal = provDtl.getSecuredPrincipal();
							oldNonSecuredPrincipal = provDtl.getNonSecuredPrincipal();
							oldSecuredProvRate = provDtl.getSecuredProvRate();
							oldNonSecuredProvRate = provDtl.getNonSecuredProvRate();
							oldSecuredProvAmount = provDtl.getSecuredProvAmount();
							oldNonSecuredProvAmount = provDtl.getNonSecuredProvAmount();
							oldProvisionAmount = provDtl.getProvisionAmount();
							oldDpd = provDtl.getDpd();
							oldNpaFlag = provDtl.getNpaFlag();
							revFlag = "Y";

						}
					} else {
						oldAssetClassCd = null;
						oldOSPrincipal = null;
						oldOverDuePrincipal = null;
						oldSecuredPrincipal = null;
						oldNonSecuredPrincipal = null;
						oldSecuredProvRate = null;
						oldNonSecuredProvRate = null;
						oldSecuredProvAmount = null;
						oldNonSecuredProvAmount = null;
						oldProvisionAmount = null;
						oldDpd = null;
						oldNpaFlag = null;
					}

					List<AgrCollateral> collateralList = collRepo
							.findByMastAgrMastAgrId(loan.getMasterAgreement().getMastAgrId());
					double assetValue = 0d;

					if (!collateralList.isEmpty()) {
						for (AgrCollateral collateral : collateralList) {
							assetValue += collateral.getColtrlValue();
						}
					}

					String newAssetClassCd = provision.getAssetClassCd();
					Double newOSPrincipal = loan.getUnbilledPrincipal();
					Double newOverDuePrincipal = commService
							.getHeadwiseDuesForMasterAgr(loan.getMasterAgreement().getMastAgrId(), "PRINCIPAL");
					Double newSecuredPrincipal = assetValue;
					Double newNonSecuredPrincipal = Math.min(newOSPrincipal, newSecuredPrincipal);
					Float newSecuredProvRate = provision.getSecuredPer();
					Float newNonSecuredProvRate = provision.getUnSecuredPer();
					Double newSecuredProvAmount = newSecuredPrincipal * newSecuredProvRate / 100;
					Double newNonSecuredProvAmount = newNonSecuredPrincipal * newNonSecuredProvRate / 100;
					Double newProvisionAmount = newSecuredProvAmount + newNonSecuredProvAmount;

					if (!(newAssetClass.getAssetClassCd().equalsIgnoreCase(oldAssetClassCd))) {
						if (revFlag.equals("Y")) {
							AgrTrnSysProvisionDtls sysProv = new AgrTrnSysProvisionDtls();
							sysProv.setMaster(loan.getMasterAgreement());
							sysProv.setLoan(loan);
							sysProv.setDtProvisionDate(businessDate);
							sysProv.setAssetClassCd(oldAssetClassCd);
							sysProv.setOSPrincipal(commService.numberFormatter(oldOSPrincipal));
							sysProv.setOverDuePrincipal(commService.numberFormatter(oldOverDuePrincipal));
							sysProv.setSecuredPrincipal(
									commService.numberFormatter(Math.min(oldSecuredPrincipal, assetValue)));
							sysProv.setNonSecuredPrincipal(commService.numberFormatter(oldNonSecuredPrincipal));
							sysProv.setSecuredProvRate(oldSecuredProvRate);
							sysProv.setNonSecuredProvRate(oldNonSecuredProvRate);
							sysProv.setSecuredProvAmount(commService.numberFormatter(oldSecuredProvAmount));
							sysProv.setNonSecuredProvAmount(commService.numberFormatter(oldNonSecuredProvAmount));
							sysProv.setProvisionAmount(commService.numberFormatter(oldProvisionAmount));
							sysProv.setNpaFlag(oldNpaFlag);
							sysProv.setRevFlag("Y");
							sysProv.setDpd(oldDpd);
							sysProv.setUserId("EOD");

							provisionSysRepo.save(sysProv);
						}

						AgrTrnSysProvisionDtls sysProv = new AgrTrnSysProvisionDtls();
						sysProv.setMaster(loan.getMasterAgreement());
						sysProv.setLoan(loan);
						sysProv.setDtProvisionDate(businessDate);
						sysProv.setAssetClassCd(newAssetClassCd);
						sysProv.setOSPrincipal(commService.numberFormatter(newOSPrincipal));
						sysProv.setOverDuePrincipal(commService.numberFormatter(newOverDuePrincipal));
						sysProv.setSecuredPrincipal(
								commService.numberFormatter(Math.min(newSecuredPrincipal, assetValue)));
						sysProv.setNonSecuredPrincipal(commService.numberFormatter(newNonSecuredPrincipal));
						sysProv.setSecuredProvRate(newSecuredProvRate);
						sysProv.setNonSecuredProvRate(newNonSecuredProvRate);
						sysProv.setSecuredProvAmount(commService.numberFormatter(newSecuredProvAmount));
						sysProv.setNonSecuredProvAmount(commService.numberFormatter(newNonSecuredProvAmount));
						sysProv.setProvisionAmount(commService.numberFormatter(newProvisionAmount));
						sysProv.setNpaFlag(newAssetClass.getNpaFlag());
						sysProv.setRevFlag("N");
						sysProv.setDpd(loan.getDpd());
						sysProv.setUserId("EOD");

						AgrTrnSysProvisionDtls savedProvisionDtl = provisionSysRepo.save(sysProv);

						loan.setLastProvId(savedProvisionDtl.getProvId());
						loan.setAssetClass(newAssetClassCd);

						loanRepo.save(loan);

						AgrMasterAgreement masterObj = masterRepo
								.findByMastAgrId(loan.getMasterAgreement().getMastAgrId());
						masterObj.setAssetClass(newAssetClassCd);
						masterObj.setNpaStatus(newAssetClass.getNpaFlag());

						masterRepo.save(masterObj);

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("NPA_PROVISIONING");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean eodStaus(String customerId, String processName, String processStatus, Date businessDate) {
		Boolean result = true;
		try {
			EodStatus status = new EodStatus();
			status.setCustomerId(customerId);
			status.setProcessName(processName);
			status.setProcessStatus(processStatus);
			status.setBusinessDate(businessDate);
			eodStatusRepo.save(status);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("EOD_STATUS");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(customerId);
			error.setBusinessDate(businessDate);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public EodStatusMainDto getEodStatus(Date bussDate, String processName, Integer pageNo, Integer pageSize) {
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		EodStatusMainDto mainDto = new EodStatusMainDto();
		List<EodStatusDto> statusDtoList = new ArrayList<EodStatusDto>();
		List<EodStatus> statusList;
		Pageable paging = PageRequest.of(pageNo, pageSize);

		try {
			if (processName == null) {
				statusList = eodStatusRepo.findAllByBusinessDate(bussDate, paging);
			} else {
				statusList = eodStatusRepo.findAllByBusinessDateAndProcessName(bussDate, processName, paging);
			}
			for (EodStatus eodStatus : statusList) {
				EodStatusDto statusDto = new EodStatusDto();
				BeanUtils.copyProperties(eodStatus, statusDto);
				statusDto.setBusinessDate(sdf.format(eodStatus.getBusinessDate()));
				statusDto.setExecutedTime(eodStatus.getDtUpdatedTime());
				statusDtoList.add(statusDto);
			}

			mainDto.setTotalRows(eodStatusRepo.findByBusinessDate(bussDate).size());
			mainDto.setEodStatus(statusDtoList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mainDto;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean agreementClosureApi(String customer) {
		Boolean result = true;
		try {
			List<AgrMasterAgreement> agreementList = masterRepo
					.findByCustomerIdAndExcessAmountAndTotalDuesAndAgreementStatus(customer, 0d, 0d, "L");
			for (AgrMasterAgreement master : agreementList) {
				Double masterTotalDues = commService.getMasterTotalDues(master.getMastAgrId());
				if (masterTotalDues <= 0) {
					List<AgrLoans> loanList = loanRepo
							.findByMasterAgreementMastAgrIdAndBalTenorAndLoanAdditionalStatusIsNull(
									master.getMastAgrId(), 0);
					for (AgrLoans loan : loanList) {
						loan.setLoanAdditionalStatus("MATURED");
						loanRepo.save(loan);
					}
					List<AgrLoans> openLoanList = loanRepo
							.findByMasterAgreementMastAgrIdAndLoanAdditionalStatusIsNullOrLoanAdditionalStatusNotIn(
									master.getMastAgrId(), Arrays.asList(new String[] { "FORECLOSED", "MATURED" }));
					if (openLoanList.size() <= 0) {
						master.setAgreementStatus("C");
					}

					masterRepo.save(master);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("AGREEMENT_CLOSURE");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean batchStatusClose(Date businessDate) {
		Boolean result = true;
		String batchStatus = "C";
		List<TrnInsBatchHdr> batchHdrList = new ArrayList<>();
		try {
			TrnInsBatchStatus batchSts = new TrnInsBatchStatus();
			batchSts.setBatchStatus(batchStatus);
			batchSts.setBatchStatusChangeDt(businessDate);
			// batchSts.setFileLocation(file);
			batchSts.setUserId("SYSTEM");
			batchHdrList = hdrRepo.findByBatchStatus("D");
			for (TrnInsBatchHdr batchHdr : batchHdrList) {

				List<TrnInsBatchInstruments> batchInstList = batchInstRepo.findByBatchHdrBatchId(batchHdr.getBatchId());
				int instCount = 0;
				for (TrnInsBatchInstruments batchInst : batchInstList) {
					log.info(" BatchId " + batchInst.getInstrumentId());
					TrnInsInstrument instrument = instRepo.findByInstrumentId(batchInst.getInstrumentId());
					if (!(instrument.getInstrumentStatus().equalsIgnoreCase("CLR")
							|| (instrument.getInstrumentStatus().equalsIgnoreCase("BOU")))) {
						instCount++;
						break;
					}
				}

				if (instCount == 0) {
					batchHdr.setBatchStatus("C");
					batchSts.setBatchHdr(batchHdr);
				}

			}

			batchStatusRepo.save(batchSts);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("BATCH_STATUS_CLOSE");
			error.setErrorMessage(e.getMessage());
			error.setCustomer("");
			error.setLoanId("");
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String archiveBackup(Integer backupId) {
		String result = "success";
		try {
			AgrTrnBkpSummary bkpSummary = new AgrTrnBkpSummary();
			Optional<AgrTrnBkpSummary> bkpSummaryOp = bkpSummaryRepo.findById(backupId);
			if (bkpSummaryOp.isPresent()) {
				bkpSummary = bkpSummaryOp.get();

				ArcAgrTrnBkpSummary arcSummary = new ArcAgrTrnBkpSummary();
				BeanUtils.copyProperties(bkpSummary, arcSummary);

				// Supplier Finance Changes Start

				List<CustApplLimitBkpSetup> custLimitBkpList = custLimitBkpRepo.findByBkpSummaryBackupId(backupId);
				List<ArcCustApplLimitBkpSetup> arcCustLimitBkpList = new ArrayList<>();
				for (CustApplLimitBkpSetup custLimitBkp : custLimitBkpList) {
					ArcCustApplLimitBkpSetup arcCustLimit = new ArcCustApplLimitBkpSetup();
					BeanUtils.copyProperties(custLimitBkp, arcCustLimit);
					arcCustLimit.setBkpSummary(arcSummary);
					arcCustLimitBkpList.add(arcCustLimit);
				}

				// Supplier Finance Changes End

				List<AgrTrnBkpLoanDetails> bkpLoanList = bkpLoanRepo.findAllByBkpSummaryBackupId(backupId);

				List<ArcAgrTrnBkpLoanDetails> arcBkpLoanList = new ArrayList<>();
				List<ArcAgrTrnBkpDueDetails> arcBkpDueList = new ArrayList<>();
				List<ArcAgrTrnBkpTaxDueDetails> arcBkpTaxList = new ArrayList<>();

				for (AgrTrnBkpLoanDetails bkpLoan : bkpLoanList) {
					ArcAgrTrnBkpLoanDetails arcLoan = new ArcAgrTrnBkpLoanDetails();
					BeanUtils.copyProperties(bkpLoan, arcLoan);
					arcLoan.setBkpSummary(arcSummary);
					arcBkpLoanList.add(arcLoan);

					List<AgrTrnBkpDueDetails> bkpDueList = bkpDueRepo
							.findByBkpLoanDetailsLoanBackupId(bkpLoan.getLoanBackupId());
					for (AgrTrnBkpDueDetails bkpDue : bkpDueList) {
						ArcAgrTrnBkpDueDetails arcDue = new ArcAgrTrnBkpDueDetails();
						BeanUtils.copyProperties(bkpDue, arcDue);
						arcDue.setBkpLoanDetails(arcLoan);
						arcBkpDueList.add(arcDue);

						List<AgrTrnBkpTaxDueDetails> bkpTaxDueList = bkpTaxDueRepo
								.findByBkpDueDetailDueBackupId(bkpDue.getDueBackupId());
						for (AgrTrnBkpTaxDueDetails taxDue : bkpTaxDueList) {
							ArcAgrTrnBkpTaxDueDetails arcTax = new ArcAgrTrnBkpTaxDueDetails();
							BeanUtils.copyProperties(taxDue, arcTax);
							arcTax.setBkpDueDetail(arcDue);
							arcBkpTaxList.add(arcTax);

						}

					}

				}

				arcSummaryRepo.save(arcSummary);
				arcLoanRepo.saveAll(arcBkpLoanList);
				arcDueRepo.saveAll(arcBkpDueList);
				arcTaxRepo.saveAll(arcBkpTaxList);

			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Boolean settlementWriteoffApi(String customerId, Date businessDate) throws ParseException {
		Boolean result = true;
		try {
			List<AgrLoans> settledLoanList = loanRepo.findByLoanAdditionalStatus("SETTLED");
			List<String> loanList = new ArrayList<>();
			for (AgrLoans loan : settledLoanList) {
				loanList.add(loan.getLoanId());
			}
			List<AgrLoans> loans = loanRepo
					.findByLoanAdditionalStatusAndMasterAgreementAgreementStatusAndMasterAgreementCustomerId("SETTLED",
							"L", customerId);

			for (AgrLoans loan : loans) {

				AgrMasterAgreement master = loan.getMasterAgreement();
				double promiseAmount = settlmentRepo.getSumAmount(master.getMastAgrId()).getSumAmount();

				double paidAmount = instRepo.getSumAmount(master.getMastAgrId(), "SETTLEMENT", "CLR").getSumAmount();

				if ((promiseAmount >= paidAmount) && paidAmount > 0) {

					AgrTrnTranHeader header = new AgrTrnTranHeader();
					header.setMasterAgr(master);
					header.setTranDate(businessDate);
					header.setTranType("SETTLEMENT_WRITEOFF");
					header.setRemark("Settelement Writeoff");
					header.setSource("SYSTEM");
					header.setReqId("");
					header.setUserID("EOD");

					AgrTrnEventDtl event = new AgrTrnEventDtl();
					event.setTranHeader(header);
					event.setTranEvent("SETTLEMENT_WRITEOFF");
					event.setTranAmount(0d);
					event.setUserId("SYSTEM");

					List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdOrderByDtDueDate(master.getMastAgrId());
					{
						for (AgrTrnDueDetails due : dueList) {
							AgrTrnTranDetail detail = new AgrTrnTranDetail();
							detail.setEventDtl(event);
							detail.setMasterAgr(master);
							detail.setLoan(loanRepo.findByMasterAgreementMastAgrId(master.getMastAgrId()).get(0));
							detail.setTranCategory(due.getDueCategory());
							detail.setTranHead(due.getDueHead());
							detail.setTranAmount(commService.numberFormatter(due.getDueAmount()) * -1);
							detail.setTranSide("CR");
							detail.setDtDueDate(due.getDtDueDate());
							detail.setDtlRemark(due.getDueHead() + " writeoff");

							tranDtlRepo.save(detail);

							due.setDueAmount(0d);
							dueRepo.save(due);
						}
					}

					dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(master.getMastAgrId(), 0d);

					log.info("before postEcsInstruments");
					String ecsInstResult = disbService.postEcsInstruments(master.getMastAgrId());
					log.info("After postEcsInstruments");

					master.setAgreementStatus("C");
					masterRepo.save(master);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			EodLog error = new EodLog();
			error.setComponent("SETTLEMENT_WRITEOFF API");
			error.setErrorMessage(e.getMessage());
			error.setCustomer(globalCustomerId);
			error.setLoanId(globalLoanId);
			eodErrorService.eodErrorLog(error);
		}
		return result;
	}
    
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<TabOrganization> getBusinessDate(String orgId) {
	try {
       List<TabOrganization> Date =orgRepo.findByOrgId(orgId);
		return Date;
	}catch (Exception e) {
		throw e;
	}
	
	}
	
}