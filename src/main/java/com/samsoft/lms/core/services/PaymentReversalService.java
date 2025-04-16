package com.samsoft.lms.core.services;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnEventDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.odMgmt.services.SupplierFinanceService;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.transaction.entities.AgrTrnLimitDtls;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentReversalService {

	@Autowired
	private AgrMasterAgreementRepository agrRepo;
	@Autowired
	private TrnInsInstrumentRepository instRepo;
	@Autowired
	private AgrTrnDueDetailsRepository dueDetailRepo;
	@Autowired
	private AgrRepayScheduleRepository scheduleRepo;
	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;
	@Autowired
	private AgrTrnEventDtlRepository eventRepo;
	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;
	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private AgrTrnLimitDtlsRepository agrLimitRepo;
	@Autowired
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private CommonServices commonService;
	@Autowired
	private Environment env;
	@Autowired
	private CoreServices coreService;
	@Autowired
	private PaymentApplicationServices paymentAppService;
	@Autowired
	private AgrCustomerRepository custRepo;
	@Autowired
	private ColenderDueService conlenderDueService;
	@Autowired
	private SupplierFinanceUtility suppUtility;
	@Autowired
	private AgrProductRepository prodRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String paymentReversal(String mastAgrId, Integer instrumentId, Date tranDate) throws Exception {
		String result = "";
		try {

			log.info("Inside Payment Reversal {} {} ", mastAgrId, instrumentId);
			AgrMasterAgreement mastAgr = agrRepo.findByMastAgrId(mastAgrId);
			TrnInsInstrument instrument = instRepo.findByInstrumentId(instrumentId);
			List<AgrLoans> argLoanList = loanRepo.getLoanIdByMasterAgreement(mastAgrId);
			AgrCustomer customer = custRepo.findByMasterAgrMastAgrIdAndCustomerType(mastAgrId, "B");
			log.info("argLoanList====>" + argLoanList.size());

			String loanType = null;
			if (argLoanList.size() > 0) {
				log.info("argLoanList=======>" + argLoanList.get(0));
				loanType = argLoanList.get(0).getLoanType();
			}
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			Double limitAmount = 0d;
			log.info("Inside Payment Reversal before Exiting header");
			AgrTrnTranHeader existingHdr = hdrRepo.findByMasterAgrMastAgrIdAndIntrumentId(mastAgrId, instrumentId);
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);// Moved by DeepakG

			log.info("Inside Payment Reversal header value " + (existingHdr == null));
			if (existingHdr == null) {
				AgrTrnTranHeader hdr = new AgrTrnTranHeader();
				hdr.setMasterAgr(mastAgr);
				hdr.setTranDate(tranDate);
				hdr.setTranType("RECEIPT");
				hdr.setRemark(
						commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", instrument.getInstrumentType())
								+ " Receipt");
				hdr.setSource(instrument.getSource());
				hdr.setReqId(instrument.getSourceId());
				hdr.setIntrumentId(instrumentId);
				hdr.setUserID("SYSTEM");
				if (limitSetup != null) {
					hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
				}
				AgrTrnEventDtl event = new AgrTrnEventDtl();
				event.setTranHeader(hdr);
				event.setTranEvent("RECEIPT");
				event.setTranAmount(numberFormatter(instrument.getInstrumentAmount()));
				event.setUserId("SYSTEM");

				AgrTrnTranDetail details = new AgrTrnTranDetail();
				details.setEventDtl(event);
				details.setMasterAgr(mastAgr);
				details.setLoan(loanRepo.getLoanIdByMasterAgreement(mastAgrId).get(0));
				details.setTranCategory("RECEIPT");
				details.setTranHead("RECEIPT_AMT");
				details.setTranAmount(numberFormatter(instrument.getInstrumentAmount() * -1));
				details.setDtlRemark("Instrument Received");
				details.setDtDueDate(tranDate);
				details.setTranSide("CR");
				if (limitSetup != null) {
					details.setAvailableLimit(
							numberFormatter(limitSetup.getAvailableLimit() + instrument.getInstrumentAmount()));
					details.setUtilizedLimit(
							numberFormatter(limitSetup.getUtilizedLimit() - instrument.getInstrumentAmount()));
				}

				tranDtlRepo.save(details);
				log.info("Payment Reversal 1");
				AgrTrnTranHeader hdrBounce = new AgrTrnTranHeader();
				hdrBounce.setMasterAgr(mastAgr);
				hdrBounce.setTranDate(tranDate);
				hdrBounce.setTranType("RECEIPT_REVERSE");
				hdrBounce.setRemark(
						commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", instrument.getInstrumentType())
								+ " Receipt Reverse");
				hdrBounce.setSource(instrument.getSource());
				hdrBounce.setReqId(instrument.getSourceId());
				hdrBounce.setIntrumentId(instrumentId);
				hdrBounce.setUserID("SYSTEM");
				if (limitSetup != null) {
					hdrBounce.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
				}

				AgrTrnEventDtl eventBounce = new AgrTrnEventDtl();
				eventBounce.setTranHeader(hdrBounce);
				eventBounce.setTranEvent("RECEIPT_REVERSE");
				eventBounce.setTranAmount(numberFormatter(instrument.getInstrumentAmount()));
				eventBounce.setUserId("SYSTEM");

				AgrTrnTranDetail detailsBounce = new AgrTrnTranDetail();
				detailsBounce.setEventDtl(eventBounce);
				detailsBounce.setMasterAgr(mastAgr);
				detailsBounce.setLoan(loanRepo.getLoanIdByMasterAgreement(mastAgrId).get(0));
				detailsBounce.setTranCategory("RECEIPT");
				detailsBounce.setTranHead("RECEIPT_AMT");
				detailsBounce.setDtlRemark("Instrument Bounced");
				detailsBounce.setTranAmount(numberFormatter(instrument.getInstrumentAmount()));
				detailsBounce.setTranSide("DR");
				detailsBounce.setDtDueDate(tranDate);
				if (limitSetup != null) {
					detailsBounce.setAvailableLimit(
							numberFormatter(limitSetup.getAvailableLimit() - instrument.getInstrumentAmount()));
					detailsBounce.setUtilizedLimit(
							numberFormatter(limitSetup.getUtilizedLimit() + instrument.getInstrumentAmount()));
				}
				log.info("Payment Reversal 2");
				tranDtlRepo.save(detailsBounce);
				log.info("Payment Reversal 3");

				EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
						.getEventBaseChargesCalculation(mastAgrId, instrumentId, "BOUNCE", "Y", 0d, "N");

				instrument.setPayBounceYn("Y");
				instrument.setDtPayBounce(tranDate);
				instRepo.save(instrument);

				AgrTrnLimitDtls agrLimitBounce = new AgrTrnLimitDtls();
				agrLimitBounce.setMasterAgreement(mastAgr);
				agrLimitBounce.setTranAmount(0d);
				agrLimitBounce.setTranType("DED");
				agrLimitBounce.setPurpose("LIMIT_FREEZED");
				agrLimitBounce.setRefTranId(hdrBounce.getTranId());
				agrLimitBounce.setUserId("SYSTEM");

				customer.setLimitFreezYn("Y");

				custRepo.save(customer);

				agrLimitRepo.save(agrLimitBounce);

			} else {
				AgrTrnTranHeader hdr = new AgrTrnTranHeader();
				hdr.setMasterAgr(mastAgr);
				hdr.setTranDate(tranDate);
				hdr.setTranType("RECEIPT_REVERSE");
				hdr.setRemark(
						commonService.getDescriptionForTranactions("INSTRUMENT_TYPES", instrument.getInstrumentType())
								+ " Receipt Reversal");
				hdr.setSource(existingHdr.getSource());
				hdr.setReqId(existingHdr.getReqId());
				hdr.setIntrumentId(instrumentId);
				hdr.setUserID("SYSTEM");
				if (limitSetup != null) {
					hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
				}
				double sumTranAmount = 0d;
				List<AgrTrnEventDtl> existingEventList = eventRepo
						.findByTranHeaderTranIdOrderByEventId(existingHdr.getTranId());
				for (AgrTrnEventDtl existingEvent : existingEventList) {
					AgrTrnEventDtl event = new AgrTrnEventDtl();
					event.setTranHeader(hdr);
					event.setTranEvent(existingEvent.getTranEvent());
					event.setTranAmount(numberFormatter(existingEvent.getTranAmount()));
					event.setUserId("SYSTEM");
					List<AgrTrnTranDetail> dtlList = tranDtlRepo.findByEventDtlEventId(existingEvent.getEventId());
					for (AgrTrnTranDetail existingDtl : dtlList) {
						AgrTrnTranDetail details = new AgrTrnTranDetail();
						details.setEventDtl(event);
						details.setMasterAgr(mastAgr);
						details.setLoan(existingDtl.getLoan());
						details.setTranCategory(existingDtl.getTranCategory());
						details.setTranHead(existingDtl.getTranHead());
						details.setTranAmount(numberFormatter(existingDtl.getTranAmount()));
						details.setDtlRemark(existingDtl.getTranHead() + " reversed");
						details.setTranSide("DR");
						details.setInstallmentNo(existingDtl.getInstallmentNo());
						details.setDtDueDate(tranDate);
						details.setRefTranDtlId(existingDtl.getTranDtlId());
						if (limitSetup != null) {
							details.setAvailableLimit(
									numberFormatter(limitSetup.getAvailableLimit() - existingDtl.getTranAmount()));
							details.setUtilizedLimit(
									numberFormatter(limitSetup.getUtilizedLimit() + existingDtl.getTranAmount()));
						}

						sumTranAmount += numberFormatter(existingDtl.getTranAmount());
						if ((loanType.equalsIgnoreCase("DL") || loanType.equalsIgnoreCase("ND"))
								&& (existingDtl.getTranHead().equalsIgnoreCase("PRINCIPAL"))) {
							limitAmount += existingDtl.getTranAmount();
						}

						if (existingDtl.getTranCategory().equalsIgnoreCase("INSTALLMENT")) {
							AgrRepaySchedule repay = scheduleRepo.findByMasterAgrIdAndInstallmentNo(mastAgrId,
									existingDtl.getInstallmentNo());
							repay.setDtPaymentDate(null);
							scheduleRepo.save(repay);
						}

						if (existingDtl.getTranCategory().equalsIgnoreCase("EXCESS")) {
							paymentAppService.updateExcess(mastAgrId, existingDtl.getTranAmount(), "CLEAR");

							EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
									.getEventBaseChargesCalculation(mastAgrId, instrumentId, "BOU", "Y", 0d, "N");
						} else {

							AgrTrnDueDetails due = new AgrTrnDueDetails();
							due.setTranDtlId(existingDtl.getTranDtlId());
							due.setMastAgrId(mastAgrId);
							due.setLoanId(existingDtl.getLoan().getLoanId());
							due.setDtDueDate(tranDate);
							due.setDueCategory(existingDtl.getTranCategory());
							due.setDueHead(existingDtl.getTranHead());
							due.setDueAmount(numberFormatter(existingDtl.getTranAmount()));
							due.setInstallmentNo(existingDtl.getInstallmentNo());

							dueDetailRepo.save(due);

							EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = coreService
									.getEventBaseChargesCalculation(mastAgrId, instrumentId, "BOU", "Y", 0d, "N");

							ColenderDueDto colenderDto = new ColenderDueDto();
							colenderDto.setDtDueDate(tranDate);
							colenderDto.setDueAmount(numberFormatter(existingDtl.getTranAmount()));
							colenderDto.setDueCategory(existingDtl.getTranCategory());
							colenderDto.setDueHead(existingDtl.getTranHead());
							colenderDto.setInstallmentNo(existingDtl.getInstallmentNo());
							colenderDto.setLoanId(existingDtl.getLoan().getLoanId());
							colenderDto.setMastAgrId(mastAgrId);
							colenderDto.setTranDtlId(existingDtl.getTranDtlId());
							colenderDto.setUserId("SYSTEM");
							conlenderDueService.generateColenderDues(colenderDto);

						}

					}

				}

				if (limitAmount > 0) {
					paymentAppService.updateLimit(mastAgrId, limitAmount, "DED", "BOUNCE", hdr.getTranId(), "PAYREV");
				}

				// Supplier Finance Changes Start
				if (loanType.equalsIgnoreCase("OD")) {
					suppUtility.updateCustomerLimit(mastAgrId, mastAgr.getOriginationApplnNo(), mastAgr.getCustomerId(),
							product.getProdCode(), "DED", sumTranAmount, hdr.getTranId(), instrument.getUserId(),
							tranDate);

				}
				// Supplier Finance Changes End
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

	public Double numberFormatter(Double value) {
		if (value == null) {
			return null;
		}
		int digitAfterDecimal = Integer.parseInt(env.getProperty("lms.digits.after.decimal"));
		if (digitAfterDecimal <= 0)
			throw new EodExceptions("Digit After Decimal should be greater than 0");

		DecimalFormat twoDecimals = new DecimalFormat("#.##");
		long factor = (long) Math.pow(10, digitAfterDecimal);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;

	}

}
