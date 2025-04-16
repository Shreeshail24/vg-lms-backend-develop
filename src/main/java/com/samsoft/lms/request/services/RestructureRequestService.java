package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.entities.AgrRepayVariation;
import com.samsoft.lms.agreement.repositories.AgrRepayVariationRepository;
import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.dto.EventFeeOutputDto;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrRepaySchedule;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.request.dto.EstScheduleList;
import com.samsoft.lms.request.dto.RestructureDtlList;
import com.samsoft.lms.request.dto.RestructureJpaDto;
import com.samsoft.lms.request.dto.RestructureReceivableListDto;
import com.samsoft.lms.request.dto.RestructureReqDtoMain;
import com.samsoft.lms.request.dto.RestructureVariationList;
import com.samsoft.lms.request.entities.AgrTrnReqEstRepaySchedule;
import com.samsoft.lms.request.entities.AgrTrnReqRescheduleDtl;
import com.samsoft.lms.request.entities.AgrTrnReqRestructureDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqEstRepayScheduleRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRescheduleDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqRestructureDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.ForclosureDueDetails;
import com.samsoft.lms.transaction.dto.ForclosureReceivableListDto;
import com.samsoft.lms.transaction.services.RestructureApplicationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestructureRequestService {

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrTrnReqRestructureDtlRepository restructureRepo;

	@Autowired
	private AgrTrnReqRescheduleDtlRepository rescheduleRepo;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private Environment env;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private AgrTrnReqEstRepayScheduleRepository estScheduleRepo;

	@Autowired
	private AgrRepayVariationRepository variationRepo;

	@Autowired
	private RestructureApplicationService restructureApplication;
	@Autowired
	private AgrTrnRequestHdrRepository hdrRepo;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Transactional
	public String restructureRequest(RestructureReqDtoMain restructureDto) throws Exception {

		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String result = "sucess";
		try {

			AgrMasterAgreement masterObj = masterRepo.findByMastAgrId(restructureDto.getMastAgrId());
			AgrLoans loan = loanRepo.getLoanIdByMasterAgreement(restructureDto.getMastAgrId()).get(0);
			log.info("Inside Restructure Req");

			AgrTrnRequestHdr hdr = new AgrTrnRequestHdr();
			hdr.setActivityCode(restructureDto.getTranType());
			hdr.setDtRequest(sdf.parse(restructureDto.getDtRequest()));
			hdr.setFlowType("NF");
			hdr.setMasterAgreement(masterObj);
			hdr.setReason(restructureDto.getReason());
			hdr.setRemark(restructureDto.getRemark());
			hdr.setReqStatus(restructureDto.getRequestStatus());
			hdr.setUserId(restructureDto.getUserId());

			log.info("getRestructureList size {} ", restructureDto.getRestructureList().size());

			for (RestructureDtlList restructureDtl : restructureDto.getRestructureList()) {
				AgrTrnReqRestructureDtl restructure = new AgrTrnReqRestructureDtl();

				restructure.setBalanceAmount(restructureDtl.getBalanceAmount());
				restructure.setCapitalizeAmount(restructureDtl.getCapitalizaAmount());
				restructure.setDueAmount(restructureDtl.getDueAmount());
				restructure.setLoanId(loan.getLoanId());
				restructure.setMasterAgrId(restructureDto.getMastAgrId());
				restructure.setRequestHdr(hdr);
				restructure.setTaxAmount(restructureDtl.getTaxAmount());
				restructure.setTotalOS(restructureDtl.getTotalOsAmount());
				restructure.setTranHead(restructureDtl.getTranHead());
				restructure.setTranType(restructureDtl.getTranType());
				restructure.setUserId(restructureDto.getUserId());
				restructure.setWaiveAmount(restructureDtl.getWaiverAmount());
				log.info(" Restrucute before save");

				restructureRepo.save(restructure);

				log.info("Restructure after save");
			}

			List<AgrRepaySchedule> scheduleDtl = repayRepo
					.findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(restructureDto.getMastAgrId(),
							sdf.parse(restructureDto.getDtRequest()));

			log.info("scheduleDtl installmentNo {} ", scheduleDtl.get(0).getInstallmentNo());

			AgrTrnReqRescheduleDtl reschedule = new AgrTrnReqRescheduleDtl();
			reschedule.setBpiAmount(commonService.getInterestAccruedTillDateByTranType(restructureDto.getMastAgrId(),
					"INTEREST_ACCRUAL"));
			reschedule.setChangeFactor(restructureDto.getChangeFactor());
			reschedule.setDreAmount(restructureDto.getDreAmount());
			reschedule.setDtNewInstStartDate(sdf.parse(restructureDto.getNewInstallmentStartDate()));
			reschedule.setDtTranDate(sdf.parse(restructureDto.getDtRequest()));
			reschedule.setLoanId(loan.getLoanId());
			reschedule.setMasterAgrId(restructureDto.getMastAgrId());
			reschedule.setNetReceivable(restructureDto.getNetReceivable());
			reschedule.setNewAssetClass(restructureDto.getNewAssetClass());
			reschedule.setNewCycleDay(restructureDto.getNewCycleDay());
			reschedule.setNewFinanceAmt(restructureDto.getNewFinanceAmount());
			reschedule.setNewIndexRate(restructureDto.getNewIndexRate());
			reschedule.setNewInstallmentAmount(restructureDto.getNewEmi());
			reschedule.setNewOffsetRate(restructureDto.getNewOffsetRate());
			reschedule.setNewRepayFrequency(restructureDto.getNewRepayFrequency());
			reschedule.setNewSpreadRate(restructureDto.getNewSpreadRate());
			reschedule.setNewTenor(restructureDto.getNewTenor());
			reschedule.setNewInterestRate(restructureDto.getNewInterestRate());
			reschedule.setOldAssetClass(loan.getAssetClass());
			reschedule.setOldCycleDay(loan.getCycleDay());
			reschedule.setOldIndexRate(loan.getIndexRate() == null ? 0 : loan.getIndexRate());
			reschedule.setOldInstallmentAmount(scheduleDtl.get(0).getInstallmentAmount());
			reschedule.setOldInterestRate(loan.getInterestRate());
			reschedule.setOldOffsetRate(loan.getOffsetRate() == null ? 0 : loan.getOffsetRate());
			reschedule.setOldRepayFrequency(loan.getRepayFreq());
			reschedule.setOldSpreadRate(loan.getSpreadRate() == null ? 0 : loan.getSpreadRate());
			reschedule.setOldTenor(loan.getTenor());
			reschedule.setOldUnbilledPrincipal(loan.getUnbilledPrincipal());
			reschedule.setReason(restructureDto.getReason());
			reschedule.setRequestHdr(hdr);
			reschedule.setRestructureReason(restructureDto.getRestructureReason());
			reschedule.setRestructureRemark(restructureDto.getRestructureRemark());
			reschedule.setTranType(restructureDto.getTranType());
			reschedule.setUserId(restructureDto.getUserId());

			log.info(" Reschedule before save");

			rescheduleRepo.save(reschedule);

			log.info(" Reschedule after save");

			log.info(" Estimated schedule size {}", restructureDto.getEstScheduleList().size());

			for (EstScheduleList estSchedule : restructureDto.getEstScheduleList()) {

				AgrTrnReqEstRepaySchedule estSch = new AgrTrnReqEstRepaySchedule();

				estSch.setBpiAmount(estSchedule.getBpiAmount());
				estSch.setClosingPrincipal(estSchedule.getClosingBalance());
				estSch.setInstallmentAmount(estSchedule.getInstallmentAmount());
				estSch.setDtInstallmentDate(sdf.parse(estSchedule.getInstallmentDate()));
				estSch.setInstallmentNo(estSchedule.getInstallmentNo());
				estSch.setIntAccrualFreq(estSchedule.getInterestAccrualFrequency());
				estSch.setInterestAmount(estSchedule.getInterestAmount());
				estSch.setInterestBasis(estSchedule.getInterestBasis());
				estSch.setInterestRate(estSchedule.getInterestRate());
				estSch.setOpeningPrincipal(estSchedule.getOpeningAmount());
				estSch.setPrincipalAmount(estSchedule.getPrincipalAmount());

				log.info(" Est Schedule before save");

				estScheduleRepo.save(estSch);

				log.info(" Est Schedule after save");

			}

			log.info(" Variation size {}", restructureDto.getVariationList().size());

			for (RestructureVariationList restVariation : restructureDto.getVariationList()) {
				int count = 0;
				AgrRepayVariation repayVariation = new AgrRepayVariation();
				repayVariation.setAdjustmentFactor(restVariation.getAdjustmentFactor());
				repayVariation.setLoans(loan);
				restVariation.setFromInstallmentNo(restVariation.getFromInstallmentNo());
				repayVariation.setNoOfInstallments(restVariation.getNoOfInstallment());
				repayVariation.setUserId(restructureDto.getUserId());
				repayVariation.setVariationOption(restVariation.getVariation());
				repayVariation.setVariationType(restVariation.getOption());
				repayVariation.setVariationValue(restVariation.getValue());
				repayVariation.setSrNo(++count);

				log.info(" variaton before save");

				variationRepo.save(repayVariation);

				log.info(" variaton after save");
			}

			log.info(" header before save");

			AgrTrnRequestHdr hdrSaved = hdrRepo.save(hdr);

			AgrTrnRequestStatus status = new AgrTrnRequestStatus();
			status.setRequestHdr(hdr);
			status.setDtReqChangeDate(sdf.parse(restructureDto.getDtRequest()));
			status.setReqStatus("PENDING");
			status.setReason(restructureDto.getReason());
			status.setRemark(restructureDto.getRemark());

			statusRepo.save(status);

			log.info(" header after save and Request Id is {} ", hdrSaved.getReqId());

			/*
			 * log.info(" Restructure Application before call");
			 * restructureApplication.restructureApplication(restructureDto.getMastAgrId(),
			 * hdrSaved.getReqId(), restructureDto.getDtRequest());
			 * 
			 * log.info(" Restructure Application after call");
			 */

		} catch (Exception e) {
			result = "fail";
			throw e;
		}

		return result;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<RestructureReceivableListDto> getRestructureReceivableList(String mastAgrId) throws Exception {
		List<RestructureReceivableListDto> result = new ArrayList<>();

		try {
			List<RestructureJpaDto> dueList = dueRepo.restructureReceivables(mastAgrId);
			AgrLoans agrLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId).get(0);
			for (RestructureJpaDto due : dueList) {
				RestructureReceivableListDto dueDetails = new RestructureReceivableListDto();
				dueDetails.setTranType(due.getDueCategory());
				dueDetails.setTranHead(due.getDueHead());
				dueDetails.setDueAmount(commonService.numberFormatter(due.getDueAmount()));
				dueDetails.setTaxAmount(commonService.numberFormatter(due.getDueTaxAmount()));
				dueDetails.setTotalOsAmount(commonService.numberFormatter(due.getDueAmount()+(due.getDueTaxAmount()==null ?0: due.getDueTaxAmount())));
				dueDetails.setBalanceAmount(dueDetails.getTotalOsAmount());
				

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
					RestructureReceivableListDto penalAccrualDue = new RestructureReceivableListDto();
					penalAccrualDue.setTranType("FEE");
					penalAccrualDue.setTranHead("PENAL");
					penalAccrualDue.setDueAmount(penalAccrual);
					penalAccrualDue.setBalanceAmount(penalAccrual);
					result.add(penalAccrualDue);

				}
			}

			double interestAccrual = commonService
					.numberFormatter(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));

			if (interestAccrual > 0) {
				RestructureReceivableListDto interestAccrualDue = new RestructureReceivableListDto();
				interestAccrualDue.setTranType("BPI");
				interestAccrualDue.setTranHead("INTEREST");
				interestAccrualDue.setDueAmount(interestAccrual);
				interestAccrualDue.setBalanceAmount(interestAccrual);
				result.add(interestAccrualDue);

			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

}
