package com.samsoft.lms.transaction.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.TabOrganization;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;
import com.samsoft.lms.instrument.entities.TrnPayoutInstrument;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnPayoutInstrumentRepository;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.RefundApplicationDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;

@Service
public class RefundService {

	@Autowired
	private AgrMasterAgreementRepository agrRepo;
	@Autowired
	private Environment env;
	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;
	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private TabOrganizationRepository orgRepo;
	@Autowired
	private TrnInsInstrumentAllocRepository allocRepo;
	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;
	@Autowired
	private PaymentApplicationServices paymentService;
	@Autowired
	private TrnPayoutInstrumentRepository payoutRepo;
	@Autowired
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private PaymentApplicationServices paymentServ;
	@Autowired
	private CommonServices commService;
	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;
	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private SupplierFinanceUtility suppUtility;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String refundApplication(RefundApplicationDto refundDto) throws Exception {
		String result = "";
		try {

			String mastAgrId = null;
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo.findById(Integer.parseInt(refundDto.getSourceId()));

			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
				mastAgrId = reqHdr.getMasterAgreement().getMastAgrId();
			} else {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			AgrMasterAgreement mastAgr = agrRepo.findByMastAgrId(mastAgrId);
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			hdr.setMasterAgr(mastAgr);
			hdr.setTranDate(sdf.parse(refundDto.getDtTranDate()));
			hdr.setTranType("REFUND");
			hdr.setRemark(refundDto.getRefundRemark());
			hdr.setSource(refundDto.getSource());
			hdr.setReqId(refundDto.getSourceId());
			hdr.setUserID(refundDto.getUserId());
			if (limitSetup != null) {
				hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(hdr);
			event.setTranEvent("REFUND");
			event.setTranAmount(commService.numberFormatter(refundDto.getRefundAmount()));
			event.setUserId(refundDto.getUserId());

			AgrTrnTranDetail details = new AgrTrnTranDetail();
			details.setEventDtl(event);
			details.setMasterAgr(mastAgr);
			details.setTranCategory("EXCESS");
			details.setTranHead("EXCESS_AMT");
			details.setTranAmount(commService.numberFormatter(refundDto.getRefundAmount()));
			details.setTranSide("DR");
			details.setDtlRemark("Refund for Excess");
			details.setDtDueDate(sdf.parse(refundDto.getDtTranDate()));
			if (limitSetup != null) {
				details.setAvailableLimit(limitSetup.getAvailableLimit() - refundDto.getRefundAmount());
				details.setUtilizedLimit(limitSetup.getUtilizedLimit() + refundDto.getRefundAmount());
				paymentServ.updateLimit(mastAgr.getMastAgrId(), (refundDto.getRefundAmount()), "DED", "REFUND",
						hdr.getTranId(), "INTERFACE");
			}

			// Supplier Finance Changes Start
			AgrLoans loan = loanRepo.findByMasterAgreementMastAgrId(mastAgr.getMastAgrId()).get(0);
			if (loan.getLoanType().equals("OD")) {
				suppUtility.updateCustomerLimit(mastAgr.getMastAgrId(), mastAgr.getOriginationApplnNo(),
						mastAgr.getCustomerId(), mastAgr.getProductCode(), "DED", refundDto.getRefundAmount(),
						hdr.getTranId(), "REFUND", sdf.parse(refundDto.getDtTranDate()));

			}

			// Supplier Finance Changes End

			TrnPayoutInstrument payout = new TrnPayoutInstrument();
			payout.setMasterAgr(mastAgr);
			payout.setCustomerId(mastAgr.getCustomerId());
			payout.setSource(refundDto.getSource());
			payout.setSourceId(refundDto.getSourceId());
			payout.setDtInstrumentDate(sdf.parse(refundDto.getDtInstrument()));
			payout.setPayType("REFUND");
			payout.setInstrumentType(refundDto.getInstrumentType());
			payout.setAccountNo(refundDto.getAccountNo());
			payout.setAccountType(refundDto.getAccountType());
			payout.setInstrumentNo(refundDto.getInstrumentNo());
			payout.setIfscCode(refundDto.getIfscCode());
			payout.setBankBranchCode(refundDto.getBranchCode());
			payout.setBankCode(refundDto.getBankCode());
			payout.setInstrumentAmount(refundDto.getRefundAmount());
			payout.setInstrumentStatus("NEW");
			payout.setUtrNo(refundDto.getUtrNo());
			payout.setDtPayment(sdf.parse(refundDto.getDtTranDate()));
			payout.setUserId(refundDto.getUserId());

			paymentService.updateExcess(mastAgrId, refundDto.getRefundAmount(), "CLEAR");

			tranDtlRepo.save(details);
			payoutRepo.save(payout);

			reqStatusUpdateService.updateRequestStatus(Integer.parseInt(refundDto.getSourceId()), "APPROVED");

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

}
