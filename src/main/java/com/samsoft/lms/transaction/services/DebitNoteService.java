package com.samsoft.lms.transaction.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.odMgmt.utility.SupplierFinanceUtility;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.transaction.dto.DebitNoteApplicationDto;
import com.samsoft.lms.transaction.entities.AgrTrnColenDueDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnColenDueDtlRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DebitNoteService {

	@Autowired
	private AgrMasterAgreementRepository agrRepo;

	@Autowired
	private Environment env;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;
	@Autowired
	private AgrTrnTranDetailsRepository tranRepo;

	@Autowired
	private AgrLoansRepository loanRepo;
	@Autowired
	private AgrColenderDtlRepository colenderRepo;
	@Autowired
	private AgrTrnColenDueDtlRepository colenDueRepo;
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
	public String debitNoteApplication(DebitNoteApplicationDto debitDto) throws Exception {
		String result = "Success";
		try {

			String mastAgrId = null;
			AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
			Optional<AgrTrnRequestHdr> reqIdPresent = reqHdrRepo.findById(Integer.parseInt(debitDto.getSourceId()));

			if (reqIdPresent.isPresent()) {
				reqHdr = reqIdPresent.get();
				mastAgrId = reqHdr.getMasterAgreement().getMastAgrId();
			} else {
				throw new CoreDataNotFoundException("Source Id not found");
			}

			AgrMasterAgreement mastAgr = agrRepo.findByMastAgrId(mastAgrId);
			AgrLoans loan = loanRepo.findByLoanId(debitDto.getLoanId());
			AgrCustLimitSetup limitSetup = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			hdr.setMasterAgr(mastAgr);
			hdr.setTranDate(sdf.parse(debitDto.getTranDate()));
			hdr.setTranType("DEBIT_NOTE");
			hdr.setRemark("Debit Note");
			hdr.setSource(debitDto.getSource());
			hdr.setReqId(debitDto.getSourceId());
			hdr.setUserID(debitDto.getUserId());
			if (limitSetup != null) {
				hdr.setSanctionedLimit(limitSetup.getLimitSanctionAmount());
			}

			AgrTrnEventDtl event = new AgrTrnEventDtl();
			event.setTranHeader(hdr);
			event.setTranEvent("DEBIT_NOTE");
			event.setTranAmount(commService.numberFormatter(debitDto.getAmount()));
			event.setUserId(debitDto.getUserId());

			AgrTrnTranDetail details = new AgrTrnTranDetail();
			details.setEventDtl(event);
			details.setMasterAgr(mastAgr);
			details.setLoan(loanRepo.findByLoanId(debitDto.getLoanId()));
			details.setTranCategory("DEBIT_NOTE");
			details.setTranHead(debitDto.getTranHead());
			details.setDtDueDate(sdf.parse(debitDto.getTranDate()));
			details.setTranAmount(commService.numberFormatter(debitDto.getAmount()));
			details.setTranSide("DR");
			details.setInstallmentNo(debitDto.getInstallmentNo());
			details.setDtlRemark("Debit Note Booked for " + debitDto.getTranHead());
			if (limitSetup != null) {
				details.setAvailableLimit(limitSetup.getAvailableLimit() - debitDto.getAmount());
				details.setUtilizedLimit(limitSetup.getUtilizedLimit() + debitDto.getAmount());
				paymentServ.updateLimit(mastAgr.getMastAgrId(), (debitDto.getAmount()), "DED", "DR_NOTE",
						hdr.getTranId(), "INTERFACE");
			}

			tranRepo.save(details);

			// Supplier Finance Changes Start

			if (loan.getLoanType().equals("OD")) {
				suppUtility.updateCustomerLimit(mastAgr.getMastAgrId(), mastAgr.getOriginationApplnNo(),
						mastAgr.getCustomerId(), mastAgr.getProductCode(), "DED", debitDto.getAmount(), hdr.getTranId(),
						debitDto.getUserId(), sdf.parse(debitDto.getTranDate()));

			}

			// Supplier Finance Changes End

			log.info("TranDtl Save application");

			AgrTrnDueDetails due = new AgrTrnDueDetails();
			due.setTranDtlId(details.getTranDtlId());
			due.setMastAgrId(mastAgrId);
			due.setLoanId(debitDto.getLoanId());
			due.setTranDtlId(details.getTranDtlId());
			due.setDtDueDate(sdf.parse(debitDto.getTranDate()));
			due.setDueCategory("DEBIT_NOTE");
			due.setDueHead(debitDto.getTranHead());
			due.setDueAmount(commService.numberFormatter(debitDto.getAmount()));
			due.setInstallmentNo(debitDto.getInstallmentNo());

			List<AgrColenderDtl> colenderList = colenderRepo.findByMasterAgrMastAgrId(mastAgrId);
			log.info("colenderList size " + colenderList.size());
			for (AgrColenderDtl agrColenderDtl : colenderList) {
				Double colenderShare = commService
						.numberFormatter(debitDto.getAmount() * (agrColenderDtl.getInterestShare() / 100));
				AgrColenderDtl colender = colenderRepo.findByCoLendId(agrColenderDtl.getCoLendId());

				colender.setInterestDues(commService.numberFormatter(
						colender.getInterestDues() == null ? 0 : colender.getInterestDues() + colenderShare));
				colenderRepo.save(colender);

				log.info("colenderList sved");

				AgrTrnColenDueDtl colenDue = new AgrTrnColenDueDtl();
				colenDue.setTranDtl(details);
				colenDue.setCoLenderId(agrColenderDtl.getCoLendId());
				colenDue.setMastAgrId(mastAgrId);
				colenDue.setLoanId(debitDto.getLoanId());
				colenDue.setDtDueDate(sdf.parse(debitDto.getTranDate()));
				colenDue.setDueCategory("DEBIT_NOTE");
				colenDue.setDueHead(debitDto.getTranHead());
				colenDue.setDueAmount(colenderShare);
				colenDue.setInstallmentNo(debitDto.getInstallmentNo());

				colenDueRepo.save(colenDue);
				log.info("colenderList due sved");
			}

			mastAgr.setTotalDues(commService.numberFormatter(mastAgr.getTotalDues() + debitDto.getAmount()));
			mastAgr.setInterestDues(commService.numberFormatter(mastAgr.getInterestDues() + debitDto.getAmount()));

			loan.setTotalDues(commService.numberFormatter(loan.getTotalDues() + debitDto.getAmount()));
			loan.setInterestDues(commService.numberFormatter(loan.getInterestDues() + debitDto.getAmount()));

			loanRepo.save(loan);

			log.info("final Loan saved");
			tranRepo.save(details);
			log.info("final tran saved");
			dueRepo.save(due);
			log.info("final due saved");

			reqStatusUpdateService.updateRequestStatus(Integer.parseInt(debitDto.getSourceId()), "APPROVED");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error: " + e);
			throw e;
		}

		return result;
	}

}
