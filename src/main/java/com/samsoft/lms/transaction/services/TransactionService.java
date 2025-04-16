package com.samsoft.lms.transaction.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrFeeParam;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.TabMstTax;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrFeeParamRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.repositories.TabMstOrgBranchRepository;
import com.samsoft.lms.core.repositories.TabMstTaxRepository;
import com.samsoft.lms.core.services.ColenderDueService;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.ChargeBookedHistDto;
import com.samsoft.lms.transaction.dto.ChargesBookingDto;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.entities.VAgrTrnChargesHistory;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.repositories.VAgrTrnChargesHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {

	@Autowired
	private AgrMasterAgreementRepository agrRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository dueTaxRepo;

	@Autowired
	private TabMstOrgBranchRepository orgBranchRepo;

	@Autowired
	private TabMstTaxRepository taxRepo;

	@Autowired
	private AgrFeeParamRepository feeRepo;

	@Autowired
	private AgrTrnTranHeaderRepository hdrRepo;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrCustLimitSetupRepository limitRepo;

	@Autowired
	private PaymentApplicationServices paymentServ;

	@Autowired
	private VAgrTrnChargesHistoryRepository chargeBookedHistRepo;

	@Autowired
	private CommonServices commService;

	@Autowired
	private GstDetailsService gstService;

	@Autowired
	private ColenderDueService conlenderDueService;

	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;

	@Autowired
	private SupplierFinanceUtility suppUtility;

	@Autowired
	private AgrProductRepository prodRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String chargeBookingApply(ChargesBookingDto chargesBookingDto) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String mastAgrId = null;
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo
					.findById(Integer.parseInt(chargesBookingDto.getSourceId()));

			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
				mastAgrId = reqHdr.getMasterAgreement().getMastAgrId();
			} else {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);// Moved

			AgrMasterAgreement master = agrRepo.findByMastAgrId(mastAgrId);
			AgrTrnTranHeader tranHdr = new AgrTrnTranHeader();
			tranHdr.setMasterAgr(master);
			tranHdr.setTranDate(sdf.parse(chargesBookingDto.getTrandDate()));
			tranHdr.setTranType("CHARGES_BOOKING");
			tranHdr.setRemark("Charges Booking");
			tranHdr.setSource(chargesBookingDto.getSource());
			tranHdr.setReqId(chargesBookingDto.getSourceId());
			tranHdr.setUserID(chargesBookingDto.getUserId());
			if (limitSetup != null) {
				tranHdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(tranHdr);
			event.setTranEvent("CHARGES_BOOKING");
			event.setTranAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			event.setUserId(chargesBookingDto.getUserId());

			AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
			tranDtl.setMasterAgr(master);
			tranDtl.setEventDtl(event);
			tranDtl.setLoan(loanRepo.findByLoanId(chargesBookingDto.getLoanId()));
			tranDtl.setTranCategory("FEE");
			tranDtl.setTranHead(chargesBookingDto.getTranHead());
			tranDtl.setTranAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			tranDtl.setTranSide("DR");
			tranDtl.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
			tranDtl.setInstallmentNo(chargesBookingDto.getInstallmentNo());
			tranDtl.setDtlRemark(chargesBookingDto.getTranHead() + " Charges Booked");
			if (limitSetup != null) {
				tranDtl.setAvailableLimit(limitSetup.getAvailableLimit() - chargesBookingDto.getChargeAmount());
				tranDtl.setUtilizedLimit(limitSetup.getUtilizedLimit() + chargesBookingDto.getChargeAmount());
				paymentServ.updateLimit(master.getMastAgrId(), (chargesBookingDto.getChargeAmount()), "DED", "CHG_BK",
						tranHdr.getTranId(), "INTERFACE");
			}
			tranDtlRepo.save(tranDtl);

			// Supplier Finance Changes Start

			if (loanRepo.findByLoanId(chargesBookingDto.getLoanId()).getLoanType().equals("OD")) {
				suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
						master.getCustomerId(), master.getProductCode(), "DED", chargesBookingDto.getChargeAmount(),
						tranHdr.getTranId(), chargesBookingDto.getUserId(),
						sdf.parse(chargesBookingDto.getTrandDate()));

			}

			// Supplier Finance Changes End
			AgrTrnDueDetails due = new AgrTrnDueDetails();
			due.setTranDtlId(tranDtl.getTranDtlId());
			due.setMastAgrId(mastAgrId);
			due.setLoanId(chargesBookingDto.getLoanId());
			due.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
			due.setDueCategory("FEE");
			due.setDueHead(chargesBookingDto.getTranHead());
			due.setDueAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			due.setInstallmentNo(chargesBookingDto.getInstallmentNo());

			List<GstListDto> gstList = gstService.getGstList(mastAgrId, chargesBookingDto.getTranHead(),
					chargesBookingDto.getChargeAmount());

			dueRepo.save(due);
			AgrTrnTranDetail save = tranDtlRepo.save(tranDtl);

			for (GstListDto gst : gstList) {
				AgrTrnTranDetail dtl = new AgrTrnTranDetail();
				dtl.setEventDtl(event);
				dtl.setMasterAgr(master);
				dtl.setLoan(loanRepo.findByLoanId(chargesBookingDto.getLoanId()));
				dtl.setTranCategory("TAX");
				dtl.setTranHead(gst.getTaxCode());
				dtl.setTranAmount(commService.numberFormatter(gst.getTaxAmount()));
				dtl.setTranSide("DR");
				dtl.setDtlRemark(gst.getTaxCode() + " tax amount");
				dtl.setInstallmentNo(chargesBookingDto.getInstallmentNo());
				dtl.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
				dtl.setRefTranDtlId(save.getTranDtlId());
				tranDtlRepo.save(dtl);
				AgrTrnTaxDueDetails dueTax = new AgrTrnTaxDueDetails();
				dueTax.setDueDetail(due);
				dueTax.setTaxCategory("TAX");
				dueTax.setTaxHead(gst.getTaxCode());
				dueTax.setDueTaxAmount(commService.numberFormatter(gst.getTaxAmount()));

				dueTaxRepo.save(dueTax);

				ColenderDueDto colenderDto = new ColenderDueDto();
				colenderDto.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
				colenderDto.setDueAmount(chargesBookingDto.getChargeAmount());
				colenderDto.setDueCategory("FEE");
				colenderDto.setDueHead(chargesBookingDto.getTranHead());
				colenderDto.setInstallmentNo(chargesBookingDto.getInstallmentNo());
				colenderDto.setLoanId(chargesBookingDto.getLoanId());
				colenderDto.setMastAgrId(mastAgrId);
				colenderDto.setTranDtlId(tranDtl.getTranDtlId());
				colenderDto.setUserId("SYSTEM");
				conlenderDueService.generateColenderDues(colenderDto);

			}

			reqStatusUpdateService.updateRequestStatus(Integer.parseInt(chargesBookingDto.getSourceId()), "APPROVED");

			return result;

		} catch (CoreDataNotFoundException e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (TransactionDataNotFoundException e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<ChargeBookedHistDto> getAgreementChargedBookedList(String mastAgrId) {

		List<ChargeBookedHistDto> result = new ArrayList<ChargeBookedHistDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			List<VAgrTrnChargesHistory> chargedBookedList = chargeBookedHistRepo.findByMastAgrId(mastAgrId);
			if (chargedBookedList.size() == 0) {
				throw new TransactionDataNotFoundException("No Charges Booked for master agreement " + mastAgrId);
			}

			for (VAgrTrnChargesHistory chargeBooked : chargedBookedList) {
				ChargeBookedHistDto chargedBookedDto = new ChargeBookedHistDto();
				BeanUtils.copyProperties(chargeBooked, chargedBookedDto);
				chargedBookedDto.setDtTranDate(sdf.format(chargeBooked.getDtTranDate()));
				result.add(chargedBookedDto);
			}
			return result;

		} catch (TransactionDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String chargeBookingBatchApply(ChargesBookingDto chargesBookingDto) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			String mastAgrId = null;
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo
					.findById(Integer.parseInt(chargesBookingDto.getSourceId()));

			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
				mastAgrId = reqHdr.getMasterAgreement().getMastAgrId();
			} else {
				mastAgrId = agrRepo.findByMastAgrId(chargesBookingDto.getMastAgrId()).getMastAgrId();
			}

			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);// Moved

			AgrMasterAgreement master = agrRepo.findByMastAgrId(mastAgrId);
			AgrLoans loan = loanRepo.findByLoanId(chargesBookingDto.getLoanId());
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrTrnTranHeader tranHdr = new AgrTrnTranHeader();
			tranHdr.setMasterAgr(master);
			tranHdr.setTranDate(sdf.parse(chargesBookingDto.getTrandDate()));
			tranHdr.setTranType("CHARGES_BOOKING");
			tranHdr.setRemark("Charges Booking");
			tranHdr.setSource(chargesBookingDto.getSource());
			tranHdr.setReqId(chargesBookingDto.getSourceId());
			tranHdr.setUserID(chargesBookingDto.getUserId());
			if (limitSetup != null) {
				tranHdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(tranHdr);
			event.setTranEvent("CHARGES_BOOKING");
			event.setTranAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			event.setUserId(chargesBookingDto.getUserId());

			AgrTrnTranDetail tranDtl = new AgrTrnTranDetail();
			tranDtl.setMasterAgr(master);
			tranDtl.setEventDtl(event);
			tranDtl.setLoan(loanRepo.findByLoanId(chargesBookingDto.getLoanId()));
			tranDtl.setTranCategory("FEE");
			tranDtl.setTranHead(chargesBookingDto.getTranHead());
			tranDtl.setTranAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			tranDtl.setTranSide("DR");
			tranDtl.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
			tranDtl.setInstallmentNo(chargesBookingDto.getInstallmentNo());
			tranDtl.setDtlRemark(chargesBookingDto.getTranHead() + " Charges Booked");
			if (limitSetup != null) {
				tranDtl.setAvailableLimit(limitSetup.getAvailableLimit() - chargesBookingDto.getChargeAmount());
				tranDtl.setUtilizedLimit(limitSetup.getUtilizedLimit() + chargesBookingDto.getChargeAmount());
				paymentServ.updateLimit(master.getMastAgrId(), (chargesBookingDto.getChargeAmount()), "DED", "CHG_BK",
						tranHdr.getTranId(), "INTERFACE");
			}

			tranDtlRepo.save(tranDtl);
			AgrTrnDueDetails due = new AgrTrnDueDetails();
			due.setTranDtlId(tranDtl.getTranDtlId());
			due.setMastAgrId(mastAgrId);
			due.setLoanId(chargesBookingDto.getLoanId());
			due.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
			due.setDueCategory("FEE");
			due.setDueHead(chargesBookingDto.getTranHead());
			due.setDueAmount(commService.numberFormatter(chargesBookingDto.getChargeAmount()));
			due.setInstallmentNo(chargesBookingDto.getInstallmentNo());

			List<GstListDto> gstList = gstService.getGstList(mastAgrId, chargesBookingDto.getTranHead(),
					chargesBookingDto.getChargeAmount());

			AgrTrnDueDetails saveDue = dueRepo.save(due);
			AgrTrnTranDetail save = tranDtlRepo.save(tranDtl);
			double totalTax = 0;
			for (GstListDto gst : gstList) {
				AgrTrnTranDetail dtl = new AgrTrnTranDetail();
				dtl.setEventDtl(event);
				dtl.setMasterAgr(master);
				dtl.setLoan(loanRepo.findByLoanId(chargesBookingDto.getLoanId()));
				dtl.setTranCategory("TAX");
				dtl.setTranHead(gst.getTaxCode());
				dtl.setTranAmount(commService.numberFormatter(gst.getTaxAmount()));
				dtl.setTranSide("DR");
				dtl.setDtlRemark(gst.getTaxCode() + " tax amount");
				dtl.setInstallmentNo(chargesBookingDto.getInstallmentNo());
				dtl.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
				dtl.setRefTranDtlId(save.getTranDtlId());
				tranDtlRepo.save(dtl);
				AgrTrnTaxDueDetails dueTax = new AgrTrnTaxDueDetails();
				dueTax.setDueDetail(saveDue);
				dueTax.setTaxCategory("TAX");
				dueTax.setTaxHead(gst.getTaxCode());
				dueTax.setDueTaxAmount(commService.numberFormatter(gst.getTaxAmount()));

				totalTax += gst.getTaxAmount();

				dueTaxRepo.save(dueTax);

				ColenderDueDto colenderDto = new ColenderDueDto();
				colenderDto.setDtDueDate(sdf.parse(chargesBookingDto.getTrandDate()));
				colenderDto.setDueAmount(chargesBookingDto.getChargeAmount());
				colenderDto.setDueCategory("FEE");
				colenderDto.setDueHead(chargesBookingDto.getTranHead());
				colenderDto.setInstallmentNo(chargesBookingDto.getInstallmentNo());
				colenderDto.setLoanId(chargesBookingDto.getLoanId());
				colenderDto.setMastAgrId(mastAgrId);
				colenderDto.setTranDtlId(tranDtl.getTranDtlId());
				colenderDto.setUserId("SYSTEM");
				conlenderDueService.generateColenderDues(colenderDto);

			}

			// Supplier Finance Changes Start

			if (loan.getLoanType().equalsIgnoreCase("OD")) {
				suppUtility.updateCustomerLimit(master.getMastAgrId(), master.getOriginationApplnNo(),
						master.getCustomerId(), product.getProdCode(), "DED",
						commService.numberFormatter(chargesBookingDto.getChargeAmount() + totalTax),
						tranHdr.getTranId(), chargesBookingDto.getUserId(),
						sdf.parse(chargesBookingDto.getTrandDate()));

			}
			// Supplier Finance Changes End

			// reqStatusUpdateService.updateRequestStatus(Integer.parseInt(chargesBookingDto.getSourceId()),
			// "APR");

			return result;

		} catch (CoreDataNotFoundException e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (TransactionDataNotFoundException e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw new TransactionDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}
	}

}
