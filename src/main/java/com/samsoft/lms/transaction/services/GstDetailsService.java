package com.samsoft.lms.transaction.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrFeeParam;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.TabMstTax;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrFeeParamRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTaxDueDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranHeaderRepository;
import com.samsoft.lms.core.repositories.TabMstOrgBranchRepository;
import com.samsoft.lms.core.repositories.TabMstTaxRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.transaction.dto.GstListDto;
import com.samsoft.lms.transaction.repositories.VAgrTrnChargesHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GstDetailsService {

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

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<GstListDto> getGstList(String mastAgrId, String feeCode, Double feeAmount) {
		List<GstListDto> gstList = new ArrayList<GstListDto>();
		log.info("mastAgrId is {} and feeCode is {}", mastAgrId, feeCode);
		try {
			AgrMasterAgreement mastAgreement = agrRepo.findByMastAgrId(mastAgrId);
			if (mastAgreement == null) {
				throw new CoreDataNotFoundException("Master Agreement not found.");
			}
			if (mastAgreement.getGstExempted().equalsIgnoreCase("Y")) {
				return gstList = new ArrayList<GstListDto>();
			}
			AgrFeeParam fee = feeRepo.findByMasterAgreementMastAgrIdAndFeeCode(mastAgrId, feeCode);
			if (fee == null) {
				throw new CoreDataNotFoundException("Fee not found.");
			}
			
			log.info(" getTaxApplicatbleYN =  "+fee.getTaxApplicatbleYN());
			
			if (fee.getTaxApplicatbleYN().equalsIgnoreCase("N")) {
				return gstList = new ArrayList<GstListDto>();
			}
			/*
			 * TabMstOrgBranch homeBranch =
			 * orgBranchRepo.findByOrgBrId(mastAgreement.getHomeState()); TabMstOrgBranch
			 * servBranch = orgBranchRepo.findByOrgBrId(mastAgreement.getServState());
			 */
			String homeState = mastAgreement.getHomeState();
			String servState = mastAgreement.getServState();
			log.info(" homeState is {} and servState is {} ",homeState, servState);
			if (homeState.equalsIgnoreCase("") || servState.equalsIgnoreCase("") || homeState==null || servState== null) {
				throw new CoreDataNotFoundException("Home State or Servicing State not found for "+ mastAgrId);
			}

			if (homeState.equalsIgnoreCase(servState)) {
				log.info(" homeState 1 ");
				List<TabMstTax> taxList = taxRepo.findByStateAndTaxCodeIn(servState,
						Arrays.asList(new String[] { "CGST", "SGST" }));
				Double taxAmount = 0d;
				for (TabMstTax tax : taxList) {
					taxAmount = feeAmount * (tax.getTaxPer() / 100);
					GstListDto gstDto = new GstListDto();
					gstDto.setTaxCode(tax.getTaxCode());
					gstDto.setTaxDescription(tax.getTaxDescription());
					gstDto.setTaxAmount(commService.numberFormatter(taxAmount));
					gstDto.setTaxPercentage(tax.getTaxPer());
					gstList.add(gstDto);
				}

			} else {
				
				log.info(" homeState 2 ");
				List<TabMstTax> taxList = taxRepo.findByStateAndTaxCodeIn(servState,
						Arrays.asList(new String[] { "IGST" }));
				Double taxAmount = 0d;
				for (TabMstTax tax : taxList) {
					taxAmount = feeAmount * (tax.getTaxPer() / 100);
					GstListDto gstDto = new GstListDto();
					gstDto.setTaxCode(tax.getTaxCode());
					gstDto.setTaxDescription(tax.getTaxDescription());
					gstDto.setTaxAmount(commService.numberFormatter(taxAmount));
					gstDto.setTaxPercentage(tax.getTaxPer());
					gstList.add(gstDto);
				}
			}

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return gstList;
	}
}
