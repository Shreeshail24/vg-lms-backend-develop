package com.samsoft.lms.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.entities.AgrColenderIncShareDtl;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrColenderIncShareDtlRepository;
import com.samsoft.lms.core.dto.ColenderDueDto;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.transaction.entities.AgrTrnColenDueDtl;
import com.samsoft.lms.transaction.repositories.AgrTrnColenDueDtlRepository;

@Service
public class ColenderDueService {

	@Autowired
	private AgrColenderDtlRepository colenDtlRepo;

	@Autowired
	private AgrColenderIncShareDtlRepository colenShareRepo;

	@Autowired
	private AgrTrnColenDueDtlRepository tranColenRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnTranDetailsRepository tranDtlRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String generateColenderDues(ColenderDueDto colenderDto) {
		String result = "success";

		List<AgrColenderDtl> colenderDtlList = colenDtlRepo.findByMasterAgrMastAgrId(colenderDto.getMastAgrId());
		float prinPerc = 0, intPerc = 0;
		double princAmount = 0, interestAmount = 0, chargeAmount = 0;

		for (AgrColenderDtl colenderDtl : colenderDtlList) {
			prinPerc = colenderDtl.getPrincipalShare();
			intPerc = colenderDtl.getInterestShare();
			if (colenderDto.getDueHead().equalsIgnoreCase("PRINCIPAL")
					|| colenderDto.getDueHead().equalsIgnoreCase("FUTURE_PRINCIPAL")) {
				if (prinPerc > 0) {
					princAmount = commonService.numberFormatter(colenderDto.getDueAmount() * (prinPerc / 100));
					AgrTrnColenDueDtl due = new AgrTrnColenDueDtl();
					due.setCoLenderId(Integer.parseInt(colenderDtl.getColenderCode()));
					due.setDtDueDate(colenderDto.getDtDueDate());
					due.setDueAmount(princAmount);
					due.setDueCategory(colenderDto.getDueCategory());
					due.setDueHead(colenderDto.getDueHead());
					due.setInstallmentNo(colenderDto.getInstallmentNo());
					due.setLoanId(colenderDto.getLoanId());
					due.setMastAgrId(colenderDto.getMastAgrId());
					due.setTranDtl(tranDtlRepo.findByTranDtlId(colenderDto.getTranDtlId()));
					due.setUserId(colenderDto.getUserId());

					tranColenRepo.save(due);
				}
			}
			if (colenderDto.getDueHead().equalsIgnoreCase("INTEREST")
					|| colenderDto.getDueHead().equalsIgnoreCase("BPI")) {
				if (intPerc > 0) {
					interestAmount = commonService.numberFormatter(colenderDto.getDueAmount() * (intPerc / 100));
					AgrTrnColenDueDtl due = new AgrTrnColenDueDtl();
					due.setCoLenderId(Integer.parseInt(colenderDtl.getColenderCode()));
					due.setDtDueDate(colenderDto.getDtDueDate());
					due.setDueAmount(interestAmount);
					due.setDueCategory(colenderDto.getDueCategory());
					due.setDueHead(colenderDto.getDueHead());
					due.setInstallmentNo(colenderDto.getInstallmentNo());
					due.setLoanId(colenderDto.getLoanId());
					due.setMastAgrId(colenderDto.getMastAgrId());
					due.setTranDtl(tranDtlRepo.findByTranDtlId(colenderDto.getTranDtlId()));
					due.setUserId(colenderDto.getUserId());

					tranColenRepo.save(due);
				}
			}

			if (colenderDto.getDueCategory().equalsIgnoreCase("FEE")) {
				AgrColenderIncShareDtl colenderShareList = colenShareRepo
						.findByColenderCoLendIdAndFeeCode(colenderDtl.getCoLendId(), colenderDto.getDueHead());
				if (colenderShareList != null) {
					if (colenderShareList.getSharePer() > 0) {
						chargeAmount = commonService
								.numberFormatter(colenderDto.getDueAmount() * (colenderShareList.getSharePer() / 100));
						AgrTrnColenDueDtl due = new AgrTrnColenDueDtl();
						due.setCoLenderId(Integer.parseInt(colenderDtl.getColenderCode()));
						due.setDtDueDate(colenderDto.getDtDueDate());
						due.setDueAmount(chargeAmount);
						due.setDueCategory(colenderDto.getDueCategory());
						due.setDueHead(colenderDto.getDueHead());
						due.setInstallmentNo(colenderDto.getInstallmentNo());
						due.setLoanId(colenderDto.getLoanId());
						due.setMastAgrId(colenderDto.getMastAgrId());
						due.setTranDtl(tranDtlRepo.findByTranDtlId(colenderDto.getTranDtlId()));
						due.setUserId(colenderDto.getUserId());

						tranColenRepo.save(due);

						AgrTrnDueDetails dueDetails = dueRepo.findByTranDtlId(colenderDto.getTranDtlId());

						List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(dueDetails.getDueDtlId());
						for (AgrTrnTaxDueDetails tax : taxList) {
							double taxAmount = commonService
									.numberFormatter(tax.getDueTaxAmount() * (colenderShareList.getSharePer() / 100));
							AgrTrnColenDueDtl taxDue = new AgrTrnColenDueDtl();
							taxDue.setCoLenderId(Integer.parseInt(colenderDtl.getColenderCode()));
							taxDue.setDtDueDate(colenderDto.getDtDueDate());
							taxDue.setDueAmount(taxAmount);
							taxDue.setDueCategory(tax.getTaxCategory());
							taxDue.setDueHead(tax.getTaxHead());
							taxDue.setInstallmentNo(colenderDto.getInstallmentNo());
							taxDue.setLoanId(colenderDto.getLoanId());
							taxDue.setMastAgrId(colenderDto.getMastAgrId());
							taxDue.setTranDtl(tranDtlRepo.findByTranDtlId(colenderDto.getTranDtlId()));
							taxDue.setUserId(colenderDto.getUserId());

							tranColenRepo.save(taxDue);

						}

					}
				}

			}

		}

		return result;
	}
}
