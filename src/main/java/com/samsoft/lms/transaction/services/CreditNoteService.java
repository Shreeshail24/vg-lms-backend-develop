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
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentAllocRepository;
import com.samsoft.lms.instrument.repositories.TrnInsInstrumentRepository;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.CreditNoteApplicationDto;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;

@Service
public class CreditNoteService {

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
	private AgrCustLimitSetupRepository limitRepo;
	@Autowired
	private PaymentApplicationServices paymentServ;
	@Autowired
	private TrnInsInstrumentRepository insRepo;
	@Autowired
	private CommonServices commService;
	@Autowired
	private ReqStatusUpdateService reqStatusUpdateService;
	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;
	@Autowired
	private SupplierFinanceUtility suppUtility;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String creditNoteApplication(CreditNoteApplicationDto creditDto) throws Exception {
		String result = "Success";
		try {
			String mastAgrId = null;
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo.findById(Integer.parseInt(creditDto.getSourceId()));

			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
				mastAgrId = reqHdr.getMasterAgreement().getMastAgrId();
			} else {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			AgrMasterAgreement mastAgr = agrRepo.findByMastAgrId(mastAgrId);
			AgrLoans loanId = loanRepo.findByLoanId(creditDto.getLoanId());
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);

			List<TabOrganization> org = orgRepo.findAll();
			if (org.size() == 0) {
				throw new TransactionDataNotFoundException("Organization details not found.");
			}

			Date businessDate = org.get(0).getDtBusiness();

			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			hdr.setMasterAgr(mastAgr);
			hdr.setTranDate(sdf.parse(creditDto.getTranDate()));
			hdr.setTranType("CREDIT_NOTE");
			hdr.setRemark(creditDto.getCrNoteRemark());
			hdr.setSource(creditDto.getSource());
			hdr.setReqId(creditDto.getSourceId());
			hdr.setUserID(creditDto.getUserId());
			if (limitSetup != null) {
				hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(hdr);
			event.setTranEvent("CREDIT_NOTE");
			event.setTranAmount(commService.numberFormatter(creditDto.getCrNoteAmount()));
			event.setUserId(creditDto.getUserId());

			AgrTrnTranDetail details = new AgrTrnTranDetail();
			details.setEventDtl(event);
			details.setMasterAgr(mastAgr);
			details.setLoan(loanId);
			details.setTranCategory("CREDIT_NOTE");
			details.setTranHead(creditDto.getTranHead());
			details.setDtDueDate(sdf.parse(creditDto.getTranDate()));
			details.setTranAmount(commService.numberFormatter(creditDto.getCrNoteAmount()));
			details.setTranSide("CR");
			details.setInstallmentNo(creditDto.getInstallmentNo());
			details.setDtlRemark("Credit Note Booked for " + creditDto.getTranHead());
			if (limitSetup != null) {
				details.setAvailableLimit(limitSetup.getAvailableLimit() + creditDto.getCrNoteAmount());
				details.setUtilizedLimit(limitSetup.getUtilizedLimit() - creditDto.getCrNoteAmount());
				paymentServ.updateLimit(mastAgr.getMastAgrId(), (creditDto.getCrNoteAmount()), "ADD", "CR_NOTE",
						hdr.getTranId(), "INTERFACE");
			}

			// Supplier Finance Changes Start

			if (loanId.getLoanType().equals("OD")) {
				suppUtility.updateCustomerLimit(mastAgr.getMastAgrId(), mastAgr.getOriginationApplnNo(),
						mastAgr.getCustomerId(), mastAgr.getProductCode(), "ADD", creditDto.getCrNoteAmount(),
						hdr.getTranId(), "CREDIT", sdf.parse(creditDto.getTranDate()));

			}

			// Supplier Finance Changes End

			TrnInsInstrument inst = new TrnInsInstrument();
			inst.setMasterAgr(mastAgr.getMastAgrId());
			inst.setCustomerId(loanId.getCustomerId());
			inst.setPayType("INSTALLMENT");
			inst.setDtReceipt(businessDate);
			inst.setDtInstrumentDate(businessDate);
			inst.setInstrumentStatus("CLR");
			inst.setInstrumentType("CRN");
			inst.setInstrumentAmount(creditDto.getCrNoteAmount());
			inst.setNclStatus("N");
			inst.setSource(creditDto.getSource());
			inst.setSourceId(creditDto.getSourceId());
			inst.setInstrumentAmount(creditDto.getCrNoteAmount());
			inst.setUserId(creditDto.getUserId());

			insRepo.save(inst);
			inst.setInstrumentNo(Integer.toString(inst.getInstrumentId()));

			insRepo.save(inst);

			TrnInsInstrumentAlloc instAlloc = new TrnInsInstrumentAlloc();
			instAlloc.setInstrument(inst);
			instAlloc.setLoanId(loanId.getLoanId());
			instAlloc.setTranCategory("INSTALLMENT");
			instAlloc.setTranHead(creditDto.getTranHead());
			instAlloc.setApportionAmount(creditDto.getCrNoteAmount());
			instAlloc.setUserId(creditDto.getUserId());

			tranDtlRepo.save(details);
			allocRepo.save(instAlloc);

			// This is for testing only remove in production

			paymentService.manualLoanApportionmentPaymentApplication(mastAgrId, inst.getInstrumentId(),
					sdf.parse(creditDto.getTranDate()));

			reqStatusUpdateService.updateRequestStatus(Integer.parseInt(creditDto.getSourceId()), "APPROVED");

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

}
