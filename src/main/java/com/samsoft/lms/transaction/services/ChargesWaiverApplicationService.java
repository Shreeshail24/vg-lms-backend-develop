package com.samsoft.lms.transaction.services;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.transaction.dto.ChargesWaiverApplicationDto;
import com.samsoft.lms.transaction.entities.VAgrTrnChargesHistory;
import com.samsoft.lms.transaction.repositories.VAgrTrnChargesHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChargesWaiverApplicationService {

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private VAgrTrnChargesHistoryRepository chargesHistRepo;

	@Autowired
	private AgrTrnTranDetailsRepository tranRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private PaymentApplicationServices paymentService;

	@Autowired
	private Environment env;

	@Autowired
	private AgrCustLimitSetupRepository limitRepo;

	@Autowired
	private CommonServices commonService;
	
	@Autowired
	private SupplierFinanceUtility suppUtility;

	@Transactional(rollbackFor = { RuntimeException.class, Exception.class, Error.class })
	public String chargesWaiverApplication(ChargesWaiverApplicationDto chargesWaiver) throws Exception {
		String result = "success";

		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			AgrMasterAgreement master = masterRepo.findByMastAgrId(chargesWaiver.getMastAgrId());
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(chargesWaiver.getMastAgrId()).get(0);

			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(chargesWaiver.getMastAgrId());

			AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			hdr.setMasterAgr(master);
			hdr.setTranDate(sdf.parse(chargesWaiver.getTranDate()));
			hdr.setTranType("CHARGES_WAIVER");
			hdr.setReason(chargesWaiver.getChargeWaiveReason());
			hdr.setRemark(chargesWaiver.getChargeWavaieRemark());
			hdr.setSource(chargesWaiver.getSource());
			hdr.setReqId(chargesWaiver.getSourceId());
			hdr.setIntrumentId(chargesWaiver.getInstallmentNo());
			hdr.setRefTranId(chargesWaiver.getChargeBookTranId());
			hdr.setUserID(chargesWaiver.getUserId());

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranEvent("CHARGES_WAIVER");
			event.setTranAmount(chargesWaiver.getChargeWaiveAmount());
			event.setUserId(chargesWaiver.getUserId());
			event.setTranHeader(hdr);

			Double remainingAmount = chargesWaiver.getChargeWaiveAmount();

			log.info("remainingAmount " + remainingAmount);

			Optional<VAgrTrnChargesHistory> chargeHist = chargesHistRepo.findById(chargesWaiver.getChargeBookTranId());
			if (chargeHist.isPresent()) {
				VAgrTrnChargesHistory history = chargeHist.get();

				log.info("history.getWaivableAmount() " + history.getWaivableAmount());

				if (history.getWaivableAmount() >= chargesWaiver.getChargeWaiveAmount()) {
					AgrTrnDueDetails dueDtls = dueRepo.findByTranDtlId(history.getTranDtlId());
					// log.info("dueDtls "+dueDtls);
					if (dueDtls.getDueAmount() > 0) {
						Double waiveAmount = Math.min(chargesWaiver.getChargeWaiveAmount(), dueDtls.getDueAmount());
						remainingAmount = remainingAmount - waiveAmount;

						AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
						tranDtl.setEventDtl(event);
						tranDtl.setMasterAgr(master);
						tranDtl.setLoan(loan);
						tranDtl.setTranCategory("FEE");
						tranDtl.setDtDueDate(sdf.parse(chargesWaiver.getTranDate()));
						tranDtl.setTranHead(chargesWaiver.getTranHead());
						tranDtl.setTranAmount(waiveAmount * -1);
						tranDtl.setTranSide("CR");
						tranDtl.setInstallmentNo(chargesWaiver.getInstallmentNo());
						tranDtl.setDtDueDate(sdf.parse(chargesWaiver.getTranDate()));

						if (limitSetup != null) {
							tranDtl.setAvailableLimit(
									commonService.numberFormatter(limitSetup.getAvailableLimit() + waiveAmount));
							tranDtl.setUtilizedLimit(
									commonService.numberFormatter(limitSetup.getUtilizedLimit() - waiveAmount));
							log.info("Update Limit before ");
							paymentService.updateLimit(chargesWaiver.getMastAgrId(), waiveAmount, "ADD", "WAIVER",
									hdr.getTranId(), "WAIVER");

							log.info("Update Limit After ");
						}

						tranRepo.save(tranDtl);

						// Supplier Finance Changes Start

						if (loan.getLoanType().equals("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), master.getProductCode(), "ADD", waiveAmount,
									hdr.getTranId(), "WAIVER", sdf.parse(chargesWaiver.getTranDate()));

						}

						// Supplier Finance Changes End

						List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(history.getDueDtlId());
						for (AgrTrnTaxDueDetails tax : taxList) {
							Double gstProRata = (waiveAmount * tax.getDueTaxAmount() / history.getWaivableAmount());
							tax.setDueTaxAmount(tax.getDueTaxAmount() - gstProRata);
							taxRepo.save(tax);

							taxRepo.deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(dueDtls.getDueDtlId(), 0d);

							AgrTrnTranDetail tranDtlTax = new AgrTrnTranDetail();
							tranDtlTax.setEventDtl(event);
							tranDtlTax.setMasterAgr(master);
							tranDtlTax.setLoan(loan);
							tranDtlTax.setTranCategory(tax.getTaxCategory());
							tranDtlTax.setDtDueDate(sdf.parse(chargesWaiver.getTranDate()));
							tranDtlTax.setTranHead(tax.getTaxHead());
							tranDtlTax.setTranAmount(gstProRata);
							tranDtlTax.setTranSide("CR");
							tranDtlTax.setInstallmentNo(chargesWaiver.getInstallmentNo());
							tranDtlTax.setDtDueDate(sdf.parse(chargesWaiver.getTranDate()));

							if (limitSetup != null) {
								tranDtlTax.setAvailableLimit(
										commonService.numberFormatter(limitSetup.getAvailableLimit() + gstProRata));
								tranDtlTax.setUtilizedLimit(
										commonService.numberFormatter(limitSetup.getUtilizedLimit() - gstProRata));
								log.info("Update Limit before ");
								paymentService.updateLimit(chargesWaiver.getMastAgrId(), gstProRata, "ADD", "WAIVER",
										hdr.getTranId(), "WAIVER");

								log.info("Update Limit After ");
							}
							// Supplier Finance Changes Start

							if (loan.getLoanType().equals("OD")) {
								suppUtility.updateCustomerLimit(master.getMastAgrId(),
										master.getOriginationApplnNo(), master.getCustomerId(), master.getProductCode(),
										"ADD", gstProRata, hdr.getTranId(), "WAIVER",
										sdf.parse(chargesWaiver.getTranDate()));

							}

							// Supplier Finance Changes End
							tranRepo.save(tranDtlTax);

						}

						AgrTrnDueDetails dueSave = dueRepo.findByDueDtlId(history.getDueDtlId());
						dueSave.setDueAmount(dueSave.getDueAmount() - waiveAmount);

						dueRepo.save(dueSave);

						dueRepo.deleteByMastAgrIdAndDueAmountLessThanEqual(chargesWaiver.getMastAgrId(), 0d);

						if (chargesWaiver.getTranHead().equalsIgnoreCase("PENAL")) {
							master.setTotalDues(master.getTotalDues() - waiveAmount);
							master.setPenalDues(master.getPenalDues() - waiveAmount);

							if (chargesWaiver.getLoanId() != null) {
								loan.setTotalDues(loan.getTotalDues() - waiveAmount);
								loan.setPenalDues(loan.getPenalDues() - waiveAmount);
							}

						} else {
							master.setTotalDues(master.getTotalDues() - waiveAmount);
							master.setOtherChargesDues(master.getOtherChargesDues() - waiveAmount);
							if (chargesWaiver.getLoanId() != null) {
								loan.setTotalDues(loan.getTotalDues() - waiveAmount);
								loan.setOtherChargesDues(loan.getOtherChargesDues() - waiveAmount);
							}
						}

						AgrTrnTranDetail tranUpdate = tranRepo.findByTranDtlId(history.getTranDtlId());
						if (tranUpdate.getRevAmount() == null) {
							tranUpdate.setRevAmount(0d);
						}
						tranUpdate.setRevAmount(tranUpdate.getRevAmount() + chargesWaiver.getChargeWaiveAmount());

						tranRepo.save(tranUpdate);

					}

					if (remainingAmount > 0) {
						AgrTrnTranDetail tran2 = new AgrTrnTranDetail();
						tran2.setEventDtl(event);
						tran2.setMasterAgr(master);
						tran2.setLoan(loan);
						tran2.setTranCategory("EXCESS");
						tran2.setTranHead("EXCESS_AMOUNT");
						tran2.setTranAmount(remainingAmount);
						tran2.setTranSide("CR");
						tran2.setInstallmentNo(chargesWaiver.getInstallmentNo());
						tran2.setDtDueDate(sdf.parse(chargesWaiver.getTranDate()));

						if (limitSetup != null) {
							tran2.setAvailableLimit(
									commonService.numberFormatter(limitSetup.getAvailableLimit() + remainingAmount));
							tran2.setUtilizedLimit(
									commonService.numberFormatter(limitSetup.getUtilizedLimit() - remainingAmount));
							log.info("Update Limit before ");
							paymentService.updateLimit(chargesWaiver.getMastAgrId(), remainingAmount, "ADD", "WAIVER",
									hdr.getTranId(), "WAIVER");

							log.info("Update Limit After ");
						}
						// Supplier Finance Changes Start

						if (loan.getLoanType().equals("OD")) {
							suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
									master.getCustomerId(), master.getProductCode(), "ADD", remainingAmount,
									hdr.getTranId(), "WAIVER_EX", sdf.parse(chargesWaiver.getTranDate()));

						}

						// Supplier Finance Changes End
						tranRepo.save(tran2);

						AgrMasterAgreement updateExcess = paymentService.updateExcess(chargesWaiver.getMastAgrId(),
								remainingAmount, "ADD");

						log.info(" After Excess Update " + updateExcess);
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return result;

	}
}
