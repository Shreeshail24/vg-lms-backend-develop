package com.samsoft.lms.core.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.VMstProvisionSetup;
import com.samsoft.lms.batch.exceptions.EodExceptions;
import com.samsoft.lms.batch.repositories.VMstProvisionSetupRepository;
import com.samsoft.lms.core.dto.DistinctDueHeadDto;
import com.samsoft.lms.core.dto.GetAssetClassDto;
import com.samsoft.lms.core.dto.HeadWiseDueDto;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnSysTranDtl;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;
import com.samsoft.lms.core.entities.TabMstLookups;
import com.samsoft.lms.core.entities.TabMstSystemParameters;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrRepayScheduleRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnSysTranDtlRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.TabMstLookupsRepository;
import com.samsoft.lms.core.repositories.TabMstSystemParametersRepository;
import com.samsoft.lms.core.repositories.TabOrganizationRepository;

@Service
@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
public class CommonServices {

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private AgrMasterAgreementRepository agrRepo;

	@Autowired
	private AgrLoansRepository loanRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrTrnTaxDueDetailsRepository taxRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private TabOrganizationRepository orgRepo;

	@Autowired
	private AgrTrnSysTranDtlRepository sysRepo;

	@Autowired
	private TabMstLookupsRepository lookupRepo;

	@Autowired
	private VMstProvisionSetupRepository provisionRepo;

	@Autowired
	private Environment env;
	
	@Autowired
	private TabMstSystemParametersRepository systemParameterRepo;

	public Integer getBalanceTenor(String loanId, Date businessDate) throws Exception {
		Integer result = 0;
		try {
			result = repayRepo.getBalanceTenor(loanId, businessDate);
			if (result == null) {
				result = 0;
			}
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public Double getMasterAgrUnbilledPrincipal(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			if (masterAgrObj != null) {
				result = masterAgrObj.getUnbilledPrincipal();
			}

		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getMasterAgrExcessAmount(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getExcessAmount();
			if (result == null) {
				result = 0.0;
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getLoanUnbilledPrincipal(String loanId) throws Exception {
		Double result = 0.0d;
		try {
			result = loanRepo.findByLoanId(loanId).getUnbilledPrincipal();
			if (result == null) {
				result = 0.0;
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getMasterTotalDues(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailMastAgrId(masterAgreement);
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdOrderByDtDueDate(masterAgreement);
			for (AgrTrnTaxDueDetails tax : taxList) {
				result += tax.getDueTaxAmount();
			}

			for (AgrTrnDueDetails due : dueList) {
				result += due.getDueAmount();
			}
			if (result == null) {
				result = 0.0;
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getLoanTotalDues(String loanId) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByLoanId(loanId);
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailLoanId(loanId);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getAllChargeDuesOfMasterAgr(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdAndDueCategory(masterAgreement, "FEE");
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailMastAgrId(masterAgreement);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getAllPenalChargesOfMasterAgr(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdAndDueCategoryAndDueHead(masterAgreement, "FEE",
					"PENAL");
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailMastAgrId(masterAgreement);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getAllChargesExceptPenalOfMasterAgr(String masterAgreement) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdAndDueCategoryAndDueHeadNotIn(masterAgreement,
					"FEE", Arrays.asList(new String[] { "PENAL" }));
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailMastAgrId(masterAgreement);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getHeadwiseDuesForMasterAgr(String masterAgreement, String dueHead) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdAndDueHead(masterAgreement, dueHead);
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailMastAgrId(masterAgreement);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getHeadwiseDuesForLoan(String loanId, String dueHead) throws Exception {
		Double result = 0.0d;
		try {
			List<AgrTrnDueDetails> dueList = dueRepo.findByLoanIdAndDueHead(loanId, dueHead);
			if (dueList.size() > 0) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailLoanId(loanId);
				for (AgrTrnDueDetails due : dueList) {
					result += due.getDueAmount();
				}
				if (taxList.size() > 0) {
					for (AgrTrnTaxDueDetails tax : taxList) {
						result += tax.getDueTaxAmount();
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public String getMasterAgrStatus(String masterAgreement) throws Exception {
		String result;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getAgreementStatus();
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String getLoanStatus(String loanId) throws Exception {
		String result;
		try {
			result = loanRepo.findByLoanId(loanId).getLoanAdditionalStatus();
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public Integer getMasterAgrDpd(String masterAgreement) throws Exception {
		Integer result;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getDpd();
			if (result == null) {
				result = 0;
			}
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String getMasterAgrAssetClass(String masterAgreement) throws Exception {
		String result;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getAssetClass();
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String getMasterAgrNpaStatus(String masterAgreement) throws Exception {
		String result;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getNpaStatus();
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String getLoanAssetClass(String loanId) throws Exception {
		String result;
		try {
			result = loanRepo.findByLoanId(loanId).getAssetClass();
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public Integer getLoanDpd(String loanId) throws Exception {
		Integer result;
		try {
			result = loanRepo.findByLoanId(loanId).getDpd();
			if (result == null) {
				result = 0;
			}
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public Double getLoanOutstandingAmount(String loanId) throws Exception {
		Double result = 0.0;
		try {
			result = loanRepo.findByLoanId(loanId).getUnbilledPrincipal() + this.getLoanTotalDues(loanId);
			if (result == null) {
				result = 0.0d;
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getmasterAgrOutstandingAmount(String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			AgrMasterAgreement masterAgrObj = agrRepo.findByMastAgrId(masterAgreement);
			result = masterAgrObj.getUnbilledPrincipal()/* + this.getAllChargeDuesOfMasterAgr(masterAgreement) */;
			if (result == null) {
				result = 0.0;
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getInterestAccruedTillDate(String masterAgreement) throws Exception {
		Double result = 0.0;
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYn(masterAgreement, "N");
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				result = result + agrTrnSysTranDtl.getTranAmount();
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getInterestAccruedTillDateByTranType(String masterAgreement, String tranType) throws Exception {
		Double result = 0.0;
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(masterAgreement, "N",
					tranType);
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				result = result + agrTrnSysTranDtl.getTranAmount();
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getAddInterestAccrued(String masterAgreement, String tranType) throws Exception {
		Double result = 0.0;
		try {
			Integer maxDpd = loanRepo.findByMasterAgreementMastAgrId(masterAgreement).get(0).getDpd();
			Integer graceDays = prodRepo.findByMasterAgreementMastAgrId(masterAgreement).getGraceDays();
			if (maxDpd == null) {
				maxDpd = 0;
			}
			if (graceDays == null) {
				graceDays = 0;
			}
			if (maxDpd > graceDays) {
				List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(masterAgreement, "N",
						tranType);
				for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
					result = result + agrTrnSysTranDtl.getTranAmount();
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public Double getInterestAccruedTillDateByTranTypeAndByLoanId(String masterAgreement, String tranType,
			String loanId) throws Exception {
		Double result = 0.0;
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranTypeAndLoanId(masterAgreement,
					"N", tranType, loanId);
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				result = result + agrTrnSysTranDtl.getTranAmount();
			}
		} catch (Exception e) {
			throw e;
		}
		return numberFormatter(result);
	}

	public String updateInterestAccrualForOd(String masterAgreement) throws Exception {
		String result = "success";
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(masterAgreement, "N",
					"INTEREST_ACCRUAL");
			List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				agrTrnSysTranDtl.setAdjustedYn("X");
				saveSysList.add(agrTrnSysTranDtl);
			}
			sysRepo.saveAll(saveSysList);
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String updateInterestAccrual(String masterAgreement) throws Exception {
		String result = "success";
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(masterAgreement, "N",
					"INTEREST_ACCRUAL");
			List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				agrTrnSysTranDtl.setAdjustedYn("Y");
				saveSysList.add(agrTrnSysTranDtl);
			}
			sysRepo.saveAll(saveSysList);
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String updatePenalAccrual(String masterAgreement) throws Exception {
		String result = "success";
		try {
			List<AgrTrnSysTranDtl> sysList = sysRepo.findByMastAgrIdAndAdjustedYnAndTranType(masterAgreement, "N",
					"PENAL_ACCRUAL");
			List<AgrTrnSysTranDtl> saveSysList = new ArrayList<AgrTrnSysTranDtl>();
			for (AgrTrnSysTranDtl agrTrnSysTranDtl : sysList) {
				agrTrnSysTranDtl.setAdjustedYn("Y");
				saveSysList.add(agrTrnSysTranDtl);
			}
			sysRepo.saveAll(saveSysList);
		} catch (Exception e) {
			throw e;
		}
		return (result);
	}

	public String getBusinessDateInString() throws Exception {
		String result = "";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(orgRepo.findAll().get(0).getDtBusiness());
		} catch (Exception e) {
			throw e;
		}
	}

	public Date getBusinessDateInDate() throws Exception {
		String result = "";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return orgRepo.findAll().get(0).getDtBusiness();
		} catch (Exception e) {
			throw e;
		}
	}

	public String getDescription(String lookupType, String code) {
		try {
			TabMstLookups lookup = lookupRepo.findByLookupTypeAndCode(lookupType, code);
			if (lookup == null) {
				return code;
			}
			return lookup.getDescription();
		} catch (Exception e) {
			throw e;
		}

	}

	public String getDescriptionForTranactions(String lookupType, String code) {
		try {
			TabMstLookups lookup = lookupRepo.findByLookupTypeAndCode(lookupType, code);
			if (lookup == null) {
				return code;
			}
			return lookup.getDescription();
		} catch (Exception e) {
			throw e;
		}

	}

	public List<TabMstLookups> getLookupValues(String lookupType) {
		try {
			List<TabMstLookups> lookup = lookupRepo.findAllByLookupType(lookupType);
			if (lookup == null) {
				throw new CoreDataNotFoundException("Data not found for Lookup Type " + lookupType);
			}
			return lookup;
		} catch (Exception e) {
			throw e;
		}

	}

	public List<HeadWiseDueDto> getMasterHeadwiseDueList(String mastAgrId) throws Exception {
		List<HeadWiseDueDto> headwiseDueList = new ArrayList<HeadWiseDueDto>();

		List<DistinctDueHeadDto> distinctHeadList = dueRepo.findDinstinctDueHeads(mastAgrId);
		Double dueAmount = 0.0;
		for (DistinctDueHeadDto distinctHead : distinctHeadList) {
			dueAmount = 0.0;
			List<AgrTrnDueDetails> dueList = dueRepo.findByMastAgrIdAndDueHead(mastAgrId, distinctHead.getDueHead());
			for (AgrTrnDueDetails due : dueList) {
				List<AgrTrnTaxDueDetails> taxList = taxRepo.findByDueDetailDueDtlId(due.getDueDtlId());
				Double taxAmount = 0.0;
				for (AgrTrnTaxDueDetails tax : taxList) {
					taxAmount = taxAmount + tax.getDueTaxAmount();
				}
				dueAmount = dueAmount + due.getDueAmount();
			}
			HeadWiseDueDto dto = new HeadWiseDueDto();
			dto.setMastAgrId(mastAgrId);
			dto.setDueHead(distinctHead.getDueHead());
			dto.setDueCategory(distinctHead.getDueCategory());
			dto.setDueAmount(dueAmount);

			headwiseDueList.add(dto);
		}
		dueAmount = getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL");

		HeadWiseDueDto dto = new HeadWiseDueDto();
		dto.setMastAgrId(mastAgrId);
		dto.setDueHead("ACCRUED");
		dto.setDueCategory("PENAL");
		dto.setDueAmount(dueAmount);

		headwiseDueList.add(dto);

		return headwiseDueList;
	}

	public GetAssetClassDto getNewAssetClass(String portfolio, Integer dpd) {
		GetAssetClassDto dto = new GetAssetClassDto();

		VMstProvisionSetup provisioning = provisionRepo
				.findByPortfolioCdAndDpdFromLessThanEqualAndDpdToGreaterThanEqual(portfolio, dpd, dpd);

		dto.setAssetClassCd(provisioning.getAssetClassCd());
		dto.setNpaFlag(provisioning.getNpaFlag());

		return dto;

	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
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
	
	public List<TabMstSystemParameters> getSystemParameterValues(String SysParaCode) {
		try {
			List<TabMstSystemParameters> systemParameter = systemParameterRepo.findAllBysysParaCode (SysParaCode);
			if (systemParameter == null) {
				throw new CoreDataNotFoundException("Data not found for SysParaCode Type " +SysParaCode);
			}
			return systemParameter;
		} catch (Exception e) {
			throw e;
		}

	}
	
}
