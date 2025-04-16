package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.request.dto.RestructureJpaDto;
import com.samsoft.lms.request.dto.SettlementDtlDto;
import com.samsoft.lms.request.dto.WriteoffReceivableListDto;
import com.samsoft.lms.request.dto.WriteoffReqDtlDto;
import com.samsoft.lms.request.dto.WriteoffReqDto;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoff;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoffDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqSettlementDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqWriteoffDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;

@Service

public class WriteoffRequestService {
	@Autowired
	private Environment env;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnReqWriteoffDtlRepository writeoffDtlRepo;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;

	@Autowired
	private AgrTrnRequestHdrRepository hdrRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String writeoffRequest(WriteoffReqDto writeoffDto) throws Exception {

		String result = "sucess";
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(writeoffDto.getMastAgrId());
			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setActivityCode("WRITEOFF");
			hdr.setDtRequest(sdf.parse(writeoffDto.getDtRequest()));
			hdr.setFlowType("NF");
			hdr.setMasterAgreement(masterObj);
			hdr.setReason(writeoffDto.getReason());
			hdr.setRemark(writeoffDto.getRemark());
			hdr.setReqStatus(writeoffDto.getReqStatus());
			hdr.setUserId(writeoffDto.getUserId());

			hdrRepo.save(hdr);

			AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			status.setRequestHdr(hdr);
			status.setDtReqChangeDate(sdf.parse(writeoffDto.getDtRequest()));
			status.setReqStatus("PENDING");
			status.setReason(writeoffDto.getReason());
			status.setRemark(writeoffDto.getRemark());

			statusRepo.save(status);

			AgrTrnReqWriteoff writeoff = new AgrTrnReqWriteoff();
			writeoff.setDtTranDate(sdf.parse(writeoffDto.getDtRequest()));
			writeoff.setLoanId(writeoffDto.getLoanId());
			writeoff.setMasterAgrId(masterObj.getMastAgrId());
			writeoff.setReasonCode(writeoffDto.getReason());
			writeoff.setRemark(writeoffDto.getRemark());
			writeoff.setRequestHdr(hdr);
			writeoff.setUserId(writeoffDto.getUserId());
			writeoff.setWriteoffAmount(writeoffDto.getWriteoffAmount());

			for (WriteoffReqDtlDto writeoffReqDtlDto : writeoffDto.getWriteoffDtl()) {

				AgrTrnReqWriteoffDtl writeoffReqDtl = new AgrTrnReqWriteoffDtl();
				writeoffReqDtl.setAmount(writeoffReqDtlDto.getDueAmount());
				writeoffReqDtl.setTotalAmount(writeoffReqDtlDto.getTotalAmount());
				writeoffReqDtl.setTaxAmount(writeoffReqDtlDto.getTaxAmount());
				writeoffReqDtl.setLoanId(writeoffReqDtlDto.getLoanId());
				writeoffReqDtl.setTranCategory(writeoffReqDtlDto.getTranType());
				writeoffReqDtl.setTranHead(writeoffReqDtlDto.getTranHead());
				writeoffReqDtl.setUserId(writeoffDto.getUserId());
				writeoffReqDtl.setWriteoffId(writeoff);

				writeoffDtlRepo.save(writeoffReqDtl);

			}

		} catch (Exception e) {
			throw e;
		}

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<WriteoffReceivableListDto> getWriteoffReceivableList(String mastAgrId) throws Exception {
		List<WriteoffReceivableListDto> result = new ArrayList<>();

		try {
			List<RestructureJpaDto> dueList = dueRepo.restructureReceivables(mastAgrId);
			AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			for (RestructureJpaDto due : dueList) {
				WriteoffReceivableListDto dueDetails = new WriteoffReceivableListDto();
				dueDetails.setTranType(due.getDueCategory());
				dueDetails.setTranHead(due.getDueHead());
				dueDetails.setDueAmount(commonService.numberFormatter(due.getDueAmount()));
				dueDetails.setTaxAmount(commonService.numberFormatter(due.getDueTaxAmount()));
				dueDetails.setTotalAmount(commonService.numberFormatter(
						due.getDueAmount() + (due.getDueTaxAmount() == null ? 0 : due.getDueTaxAmount())));
				dueDetails.setLoanId(agrLoans.getLoanId());

				/*
				 * if (due.getDueCategory().equalsIgnoreCase("FEE")) { List<AgrTrnTaxDueDetails>
				 * taxList = taxRepo.findByDueDetailDueDtlId(due.getDueDtlId()); for
				 * (AgrTrnTaxDueDetails tax : taxList) { totalAmount += tax.getDueTaxAmount();
				 * totalReceivable += tax.getDueTaxAmount(); } }
				 */

				result.add(dueDetails);

			}

			Integer maxDpd = sysRepo.getMaxDpd("PENAL_ACCRUAL", agrLoans.getLoanId());
			Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(mastAgrId).getGraceDays();
			if (maxDpd == null) {
				maxDpd = 0;
			}
			if (graceDays == null) {
				graceDays = 0;
			}
			if (maxDpd > graceDays) {
				double penalAccrual = commonService.numberFormatter(
						commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL"));
				if (penalAccrual > 0) {
					WriteoffReceivableListDto penalAccrualDue = new WriteoffReceivableListDto();
					penalAccrualDue.setTranType("FEE");
					penalAccrualDue.setTranHead("PENAL");
					penalAccrualDue.setDueAmount(penalAccrual);
					penalAccrualDue.setLoanId(agrLoans.getLoanId());
					penalAccrualDue.setTotalAmount(penalAccrual);
					result.add(penalAccrualDue);

				}
			}

			double interestAccrual = commonService
					.numberFormatter(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));

			if (interestAccrual > 0) {
				WriteoffReceivableListDto interestAccrualDue = new WriteoffReceivableListDto();
				interestAccrualDue.setTranType("BPI");
				interestAccrualDue.setTranHead("INTEREST");
				interestAccrualDue.setDueAmount(interestAccrual);
				interestAccrualDue.setLoanId(agrLoans.getLoanId());
				interestAccrualDue.setTotalAmount(interestAccrual);
				result.add(interestAccrualDue);

			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
}
