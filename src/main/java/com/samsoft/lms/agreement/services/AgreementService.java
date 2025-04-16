package com.samsoft.lms.agreement.services;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.dto.*;
import com.samsoft.lms.agreement.entities.*;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.repositories.*;
import com.samsoft.lms.core.dto.*;
import com.samsoft.lms.core.entities.*;
import com.samsoft.lms.core.repositories.*;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.las.entities.AgrCollateralShares;
import com.samsoft.lms.las.services.AgrCollateralSharesService;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.services.DrawDownRequestService;
import com.samsoft.lms.transaction.dto.GetLimitDtlsDto;
import com.samsoft.lms.transaction.entities.AgrTrnLimitDtls;
import com.samsoft.lms.transaction.repositories.AgrTrnLimitDtlsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class AgreementService {

	@Autowired
	private AgrMasterAgreementRepository agrRepo;

	@Autowired
	private AgrLoansRepository loanRepo;
	
	@Autowired
	private PageableUtils pageableUtils;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrFeeParamRepository feeRepo;

	@Autowired
	private AgrTrnDueDetailsRepository dueRepo;

	@Autowired
	private AgrCustLimitSetupRepository limitRepo;

	@Autowired
	private AgrRepayScheduleRepository repayRepo;

	@Autowired
	private VArgTranHistoryRepository tranHistRepo;

	@Autowired
	private VAgrTranHistoryOdRepository tranHistOdRepo;

	@Autowired
	private VAgrInterestAccrualHistoryRepository intAccRepo;

	@Autowired
	private CommonServices commonService;

	@Autowired
	private Environment env;

	@Autowired
	private AgrCustomerRepository custRepo;

	@Autowired
	private AgrColenderDtlRepository colenderRepo;

	@Autowired
	private AgrEpaySetupRepository epayRepo;

	@Autowired
	private AgrCollateralRepository collateralRepo;

	@Autowired
	private TabOrganizationRepository orgRepo;

	@Autowired
	private TabMstColenderRepository colenderMstRepo;

	@Autowired
	private AgrTrnLimitDtlsRepository trnLimitRepo;

	@Autowired
	private AgrCollateralSharesService agrCollateralSharesService;
	
	@Autowired
	private AgrInvoiceDetailsRepository agrInvoiceDetailsRepository;
	
	@Autowired
	private AgrMasterAgreementRepository agrMasterAgreementRepository;
	
	@Autowired
	private CustApplLimitSetupRepository custLimitRepo;

	@Autowired
	private CustomerServices customerServices;

	@Autowired
	private CustApplLimitSetupRepository custApplLimitSetupRepository;

	@Autowired
	private DrawDownRequestService drawDownRequestService;
	
	@Autowired
	private AgrRepayVariationRepository agrRepayRepo;

	public Page<AgrMasterAgreementDto> getCustomerAgreementList(String customerId, Pageable pageable) throws Exception {
		List<AgrMasterAgreementDto> listAgreementDto = new ArrayList<AgrMasterAgreementDto>();
		try {
			List<AgrMasterAgreement> listAgreement = agrRepo.findAllByCustomerId(customerId);
		log.debug("listAgreement-------->" + listAgreement);
			AgrCustomer customer = custRepo.findFirstByCustomerId(customerId);
			Date dtBusiness = orgRepo.findAll().get(0).getDtBusiness();
			if (listAgreement.size() <= 0) {
				throw new AgreementDataNotFoundException("No agreement found for customer " + customerId);
			}
			for (AgrMasterAgreement agreement : listAgreement) {
				AgrMasterAgreementDto agreementDto = new AgrMasterAgreementDto();
				BeanUtils.copyProperties(agreement, agreementDto);
				AgrLoans loans = loanRepo.findByMasterAgreementMastAgrId(agreementDto.getMastAgrId()).get(0);
				agreementDto
						.setOutstandingAmount(commonService.getmasterAgrOutstandingAmount(agreementDto.getMastAgrId()));
				agreementDto.setAgreementStatus(
						commonService.getDescription("AGREEMENT_STATUS", agreement.getAgreementStatus()));
				agreementDto
						.setCustomerType(commonService.getDescription("CUSTOMER_CATEGORY", customer.getCustCategory()));
				agreementDto.setTotalTenor(loans.getTenor());
				agreementDto.setBalanceTenor(commonService.getBalanceTenor(loans.getLoanId(), dtBusiness));

				// Added Two New Fields
				agreementDto.setDtNextInstallment(this.convertToDateFormat(agreement.getDtNextInstallment()));

				listAgreementDto.add(agreementDto);
			}

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		 return pageableUtils.convertToPage(listAgreementDto, pageable);
	}

	public CustomerAgreementDetailsResDto getCustomerAgreementDetails(String mastAgrId) throws Exception {
		CustomerAgreementDetailsResDto agreementDto = new CustomerAgreementDetailsResDto();
		try {
			AgrMasterAgreement agreement = agrRepo.findByMastAgrId(mastAgrId);
			AgrCustomer customer = custRepo.findFirstByCustomerId(agreement.getCustomerId());
			Date dtBusiness = orgRepo.findAll().get(0).getDtBusiness();
			if (agreement != null) {

				BeanUtils.copyProperties(agreement, agreementDto);
				List<AgrLoans> loansList = loanRepo.findByMasterAgreementMastAgrId(agreement.getMastAgrId());
				AgrLoans loans = null;
				if (!loansList.isEmpty()) {
					loans = loansList.get(0);
				}
				agreementDto
						.setOutstandingAmount(commonService.getmasterAgrOutstandingAmount(agreement.getMastAgrId()));
				agreementDto.setAgreementStatus(
						commonService.getDescription("AGREEMENT_STATUS", agreement.getAgreementStatus()));
				agreementDto.setCustomerType(customer.getCustCategory() != null
						? commonService.getDescription("CUSTOMER_CATEGORY", customer.getCustCategory())
						: null);
				agreementDto.setStatus(commonService.getDescription("CUSTOMER_STATUS", customer.getStatus()));
				agreementDto.setTotalTenor(loans != null ? (loans.getTenor() != null ? loans.getTenor() : 0) : 0);
				agreementDto.setBalanceTenor(
						loans != null ? commonService.getBalanceTenor(loans.getLoanId(), dtBusiness) : 0);

				agreementDto.setCustomerName(this.getCustomerName(customer));

				// Added Two New Fields
				agreementDto.setDtNextInstallment(this.convertToDateFormat(agreement.getDtNextInstallment()));

			}

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return agreementDto;
	}

	public AgreementInfoDto getAgreementInfo(String mastAgrId) throws Exception {
		AgreementInfoDto agreementInfoDto = new AgreementInfoDto();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			AgrMasterAgreement agreementDetails = agrRepo.findByMastAgrId(mastAgrId);
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			List<AgrLoans> loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrRepaySchedule repay = null;
			AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);

			List<AgrColenderDtl> colenderList = colenderRepo.findByMasterAgrMastAgrId(mastAgrId);
			if (agreementDetails == null) {
				throw new AgreementDataNotFoundException("No agreement information is available");
			}
			BeanUtils.copyProperties(agreementDetails, agreementInfoDto);
			AgrLoans loan = null;
			if (loans.size() > 0) {
				loan = loans.get(0);
				List<AgrRepaySchedule> agrRepayScheduleList = repayRepo.findByMasterAgrIdAndLoanId(mastAgrId,
						loan.getLoanId());
				if (!agrRepayScheduleList.isEmpty()) {
					repay = agrRepayScheduleList.get(0);
				}

				if (repay != null) {
					agreementInfoDto.setAdvanceEmiAmount(repay.getInstallmentAmount() * loan.getNoOfAdvEmi());
				}
			}
			agreementInfoDto.setOdYn("false");
			
			if(loans != null)
			{
				agreementInfoDto.setFinalDisbursement(loan.getFinalDisbursement());
				
			}

			CustApplLimitSetup custApplLimitSetup = custApplLimitSetupRepository.findByOriginationApplnNo(agreementDetails.getOriginationApplnNo());

			if (limit != null || custApplLimitSetup != null) {

				AgrProduct prod = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
				agreementInfoDto.setUtilizedLimit(agreementDetails.getPortfolioCode().equalsIgnoreCase("SF") ? Objects.requireNonNull(custApplLimitSetup).getUtilizedLimit() : Objects.requireNonNull(limit).getUtilizedLimit());
				agreementInfoDto.setAvailableLimit(agreementDetails.getPortfolioCode().equalsIgnoreCase("SF") ? Objects.requireNonNull(custApplLimitSetup).getAvailableLimit() : Objects.requireNonNull(limit).getAvailableLimit());
				agreementInfoDto.setDtOdClosure(sdf.format(agreementDetails.getPortfolioCode().equalsIgnoreCase("SF") ? Objects.requireNonNull(custApplLimitSetup).getDtLimitExpired() : Objects.requireNonNull(limit).getDtLimitExpired()));
				agreementInfoDto.setDtSanctionedDate(sdf.format(agreementDetails.getPortfolioCode().equalsIgnoreCase("SF") ? Objects.requireNonNull(custApplLimitSetup).getDtLimitSanctioned() : Objects.requireNonNull(limit).getDtLimitSanctioned()));
				agreementInfoDto.setSanctionedLimit(agreementDetails.getPortfolioCode().equalsIgnoreCase("SF") ? Objects.requireNonNull(custApplLimitSetup).getLimitSanctioned() : Objects.requireNonNull(limit).getLimitSanctionAmount());

				agreementInfoDto.setDropLineFreq(
						commonService.getDescriptionForTranactions("REPAY_FREQUENCY", product.getDropLIneFreq()));
				if(prod.getProdType() != null) {
					if ((prod.getProdType().equals("DL")) || (prod.getProdType().equals("ND"))) {
						agreementInfoDto.setOdYn("true");
						agreementInfoDto.setNextInstallmentAmount(0d);
						agreementInfoDto.setPrevInstallmentAmount(0d);
					}
				}
			}

			agreementInfoDto.setOutstandingAmount(commonService.getmasterAgrOutstandingAmount(mastAgrId));
			agreementInfoDto.setOverdueAmount(commonService.getMasterTotalDues(mastAgrId));
			agreementInfoDto
					.setOutstandingAmount(commonService.getmasterAgrOutstandingAmount(agreementInfoDto.getMastAgrId()));
			agreementInfoDto.setDtNextInstallment(convertToDateFormat(agreementDetails.getDtNextInstallment()));
			agreementInfoDto.setDtPrevInstallment(convertToDateFormat(agreementDetails.getDtPrevInstallment()));
			agreementInfoDto.setAgreementStatus(
					commonService.getDescription("AGREEMENT_STATUS", agreementDetails.getAgreementStatus()));

			agreementInfoDto
					.setAccruedPenal(commonService.getInterestAccruedTillDateByTranType(mastAgrId, "PENAL_ACCRUAL"));
			agreementInfoDto.setAccruedInterest(
					commonService.getInterestAccruedTillDateByTranType(mastAgrId, "INTEREST_ACCRUAL"));
			agreementInfoDto
					.setTotalReceivable(agreementInfoDto.getOverdueAmount() + agreementInfoDto.getAccruedPenal());
			agreementInfoDto.setLoanAmount(agreementDetails.getLoanAmount());
			List<String> colenderTempList = new ArrayList<String>();
			for (AgrColenderDtl colender : colenderList) {
				
				String numericPart = colender.getColenderCode().replaceAll("\\D", ""); // Removes all non-digit characters
				int number = Integer.parseInt(numericPart);
				TabMstColender tabMstColender = colenderMstRepo
						.findByColenderId(number);
				if (tabMstColender != null) {
					colenderTempList.add(tabMstColender.getColenderName());
				}
			}
			agreementInfoDto.setColender(colenderTempList);

		} catch (AgreementDataNotFoundException e) {
			e.printStackTrace();
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return agreementInfoDto;
	}

	public List<AgreementLoanListDto> getAgreementLoanList(String mastAgrId) throws Exception {
		List<AgreementLoanListDto> agrLoanList = new ArrayList<AgreementLoanListDto>();
		try {
			List<AgrLoans> listLoans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			if (listLoans.size() <= 0) {
				throw new AgreementDataNotFoundException("No loans available for " + mastAgrId);
			}
			for (AgrLoans loan : listLoans) {
				AgreementLoanListDto loanDto = new AgreementLoanListDto();
				loanDto.setLoanId(loan.getLoanId());
				loanDto.setMasterAgreement(loan.getMasterAgreement().getMastAgrId());

				agrLoanList.add(loanDto);
			}

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return agrLoanList;
	}

	public AgreementLoanInfoDto getAgreementLoanInfo(String mastAgrId, String loanId) throws Exception {
		AgreementLoanInfoDto loanDto = new AgreementLoanInfoDto();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			AgrLoans loanDetails = loanRepo.findByMasterAgreementMastAgrIdAndLoanId(mastAgrId, loanId);
			List<AgrRepaySchedule> repayList = repayRepo.findByMasterAgrIdAndLoanId(mastAgrId, loanId);
			if (loanDetails == null) {
				throw new AgreementDataNotFoundException(
						"No loans information available for master agreement " + mastAgrId + " and loan " + loanId);
			}
			BeanUtils.copyProperties(loanDetails, loanDto);
			loanDto.setTotalTenor(repayList.size());
			loanDto.setDtDisbursement(sdf.format(loanDetails.getDtLastDisbDate()));
			loanDto.setDtTenorEndDate(sdf.format(loanDetails.getDtTenorEndDate()));
			loanDto.setDtTenorStartDate(sdf.format(loanDetails.getDtTenorStartDate()));
			loanDto.setBalanceTenor(commonService.getBalanceTenor(loanId, commonService.getBusinessDateInDate()));
			loanDto.setInterestType(loanDetails.getInterestType());
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return loanDto;
	}

	public List<AgreementLoanInfoDto> getAgreementLoanInfoByMastAgrId(String mastAgrId) throws Exception {
		List<AgreementLoanInfoDto> agreementLoanInfoDtoList = new ArrayList<>();
		AgreementLoanInfoDto loanDto = null;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {

			List<AgrLoans> loanDetails = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);

			for (AgrLoans loans : loanDetails) {
				loanDto = new AgreementLoanInfoDto();
				List<AgrRepaySchedule> repayList = repayRepo.findByMasterAgrIdAndLoanId(mastAgrId, loans.getLoanId());
				if (loanDetails == null) {
					throw new AgreementDataNotFoundException("No loans information available for master agreement "
							+ mastAgrId + " and loan " + loans.getLoanId());
				}
				BeanUtils.copyProperties(loans, loanDto);
				loanDto.setTotalTenor(repayList.size());
				loanDto.setDtDisbursement(sdf.format(loans.getDtLastDisbDate()));
				loanDto.setDtTenorEndDate(sdf.format(loans.getDtTenorEndDate()));
				loanDto.setDtTenorStartDate(sdf.format(loans.getDtTenorStartDate()));
				loanDto.setBalanceTenor(
						commonService.getBalanceTenor(loans.getLoanId(), commonService.getBusinessDateInDate()));
				loanDto.setInterestType(loans.getInterestType());
				agreementLoanInfoDtoList.add(loanDto);
			}

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return agreementLoanInfoDtoList;
	}

	public List<AgreementProductInfoDto> getAgreementProductInfo(String mastAgrId) throws Exception {
		List<AgreementProductInfoDto> prodDtoList = new ArrayList<AgreementProductInfoDto>();

		try {
			AgrProduct productInfo = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			if (productInfo == null) {
				throw new AgreementDataNotFoundException(
						"No product information available for master agreement " + mastAgrId);
			}
			// for (AgrProduct agrProduct : productInfo) {
			AgreementProductInfoDto prodDto = new AgreementProductInfoDto();
			BeanUtils.copyProperties(productInfo, prodDto);
			prodDto.setMastAgrId(mastAgrId);
			prodDto.setAmortizationType(
					commonService.getDescription("AMORTIZATION_TYPE", productInfo.getAmortizationType()));
			prodDto.setPenalAccountingBasis(
					(commonService.getDescription("PENAL_ACCNT_BASIS", productInfo.getPenalAccountingBasis())));
			prodDtoList.add(prodDto);
			// }

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return prodDtoList;
	}

	
	public Page<AgreementFeeListDto> getAgreementFeeList(String mastAgrId, Pageable pageable) throws Exception {
	    try {
	        // Fetch all data as a list
	        List<AgrFeeParam> feeList = feeRepo.findByMasterAgreementMastAgrId(mastAgrId);
	        int totalCount = feeList.size();
	        System.out.println("totalCount------->" + totalCount);
	        
	        // If no data is found, throw an exception
	        if (feeList.isEmpty()) {
	            throw new AgreementDataNotFoundException(
	                    "No fee information available for master agreement " + mastAgrId);
	        }

	        // Convert entities to DTOs
	        List<AgreementFeeListDto> feeDtoList = feeList.stream().map(fee -> {
	            AgreementFeeListDto feeDto = new AgreementFeeListDto();
	            BeanUtils.copyProperties(fee, feeDto);
	            feeDto.setMastAgrId(mastAgrId);
	            feeDto.setTaxApplicatbleYN(fee.getTaxApplicatbleYN());
	            return feeDto;
	        }).collect(Collectors.toList());

	        // Return a Page object for DTOs using convertToPage
	        return pageableUtils.convertToPage(feeDtoList, pageable);

	    } catch (AgreementDataNotFoundException e) {
	        throw new AgreementDataNotFoundException(e.getMessage());
	    } catch (Exception e) {
	        throw e;
	    }
	}
	
	public Page<AgreementDueListDto> getAgreementDueList(String mastAgrId, Pageable pageable) throws Exception {
	    try {
	        // Get paginated data from the repository
	        Page<AgrTrnDueDetails> duePage = dueRepo.findByMastAgrId(mastAgrId, pageable);

	        // If no data found, throw an exception
	        if (duePage.isEmpty()) {
	            throw new AgreementDataNotFoundException(
	                    "No dues information available for master agreement " + mastAgrId);
	        }

	        // Convert entities to DTOs
	        List<AgreementDueListDto> dueDtoList = duePage.getContent().stream().map(due -> {
	            AgreementDueListDto dueDto = new AgreementDueListDto();
	            BeanUtils.copyProperties(due, dueDto);
	            dueDto.setMastAgrId(mastAgrId);
	            dueDto.setTranDtlId(due.getTranDtlId());
	            dueDto.setDtDueDate(convertToDateFormat(due.getDtDueDate()));
	            return dueDto;
	        }).collect(Collectors.toList());

	        // Return a Page object with DTOs
	        return new PageImpl<>(dueDtoList, pageable, duePage.getTotalElements());
	    } catch (AgreementDataNotFoundException e) {
	        throw new AgreementDataNotFoundException(e.getMessage());
	    } catch (Exception e) {
	        throw e;
	    }
	}	

	public List<AgreementLimitSetupListDto> getAgreementLimitList(String mastAgrId) throws Exception {
		List<AgreementLimitSetupListDto> limitDtoList = new ArrayList<AgreementLimitSetupListDto>();

		try {
			AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			if ( limit == null) {
				throw new AgreementDataNotFoundException("No limits information available for master agreement " + mastAgrId);
			}
//			for( AgrCustLimitSetup limit: limitList) {
				AgreementLimitSetupListDto limitDto = new AgreementLimitSetupListDto();
				BeanUtils.copyProperties(limit, limitDto);
				limitDto.setMasterAgreement(mastAgrId);
				limitDto.setDtLimitExpired(convertToDateFormat(limit.getDtLimitExpired()));
				limitDto.setDtLimitSanctioned(convertToDateFormat(limit.getDtLimitSanctioned()));
				limitDto.setDrawingPower(limit.getAvailableLimit());

				limitDto.setAvailableLimitAmount(limit.getAvailableLimit());
				Double allPendingRequestsAmount = drawDownRequestService.getAllPendingRequestsAmount(mastAgrId);
				limitDto.setAllPendingRequestsAmount(allPendingRequestsAmount);
				limitDtoList.add(limitDto);
//			}
			
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return limitDtoList;
	}

	
	public Page<AgreementAmortListDto> getAgreementAmortList(String mastAgrId, String loanId, Pageable pageable) throws Exception {
	    try {
	        Page<AgrRepaySchedule> repayPage;

	        // Fetch paginated data based on loanId
	        if (loanId == null) {
	            repayPage = repayRepo.findByMasterAgrId(mastAgrId, pageable);

	            if (repayPage.isEmpty()) {
	                throw new AgreementDataNotFoundException(
	                        "No schedule information available for master agreement " + mastAgrId);
	            }
	        } else {
	            repayPage = repayRepo.findByMasterAgrIdAndLoanId(mastAgrId, loanId, pageable);

	            if (repayPage.isEmpty()) {
	                throw new AgreementDataNotFoundException(
	                        "No schedule information available for master agreement " + mastAgrId + " and loan " + loanId);
	            }
	        }

	        // Convert entities to DTOs
	        List<AgreementAmortListDto> repayDtoList = repayPage.getContent().stream().map(repay -> {
	            AgreementAmortListDto repayDto = new AgreementAmortListDto();
	            BeanUtils.copyProperties(repay, repayDto);
	            repayDto.setLoanId(repay.getLoanId());
	            repayDto.setRepaySchId(repay.getRepaySchId());
	            repayDto.setDtInstallment(convertToDateFormat(repay.getDtInstallment()));
	            repayDto.setDtPaymentDate(convertToDateFormat(repay.getDtPaymentDate()));
	            return repayDto;
	        }).collect(Collectors.toList());

	        // Return a Page object with DTOs
	        return new PageImpl<>(repayDtoList, pageable, repayPage.getTotalElements());
	    } catch (AgreementDataNotFoundException e) {
	        throw new AgreementDataNotFoundException(e.getMessage());
	    } catch (Exception e) {
	        throw e;
	    }
	}

	
	
	
	public List<AgreementAmortListDto> getAgreementAmortList(String mastAgrId, String loanId) throws Exception {
		List<AgreementAmortListDto> repayDtoList = new ArrayList<AgreementAmortListDto>();
		List<AgrRepaySchedule> repayList = new ArrayList<AgrRepaySchedule>();
		try {
			if (loanId == null) {
				repayList = repayRepo.findByMasterAgrIdOrderByRepaySchId(mastAgrId);
				if (repayList.size() <= 0) {
					throw new AgreementDataNotFoundException(
							"No schedule information available for master agreement " + mastAgrId);
				}
			} else {
				repayList = repayRepo.findByMasterAgrIdAndLoanId(mastAgrId, loanId);
				if (repayList.size() <= 0) {
					throw new AgreementDataNotFoundException("No schedule information available for master agreement "
							+ mastAgrId + " and loan " + loanId);
				}
			}

			for (AgrRepaySchedule repay : repayList) {
				AgreementAmortListDto repayDto = new AgreementAmortListDto();
				BeanUtils.copyProperties(repay, repayDto);
				repayDto.setLoanId(loanId);
				repayDto.setRepaySchId(repay.getRepaySchId());
				repayDto.setLoanId(repay.getLoanId());
				repayDto.setDtInstallment(convertToDateFormat(repay.getDtInstallment()));
				repayDto.setDtPaymentDate(convertToDateFormat(repay.getDtPaymentDate()));
				repayDtoList.add(repayDto);
			}
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return repayDtoList;
	}

	
	
	

	public AgreementRepaymentListDto getAgreementRepaymentList(String mastAgrId) throws Exception {
		AgreementRepaymentListDto agreementRepaymentListDto = new AgreementRepaymentListDto();
		List<AgreementRepaymentRecordsDto> agreementRepaymentRecordsList = new ArrayList<AgreementRepaymentRecordsDto>();
		List<AgrRepaySchedule> repayList = new ArrayList<AgrRepaySchedule>();
		AgrInvoiceDetails invoice = agrInvoiceDetailsRepository.findByMasterMastAgrId(mastAgrId);
		try {
				repayList = repayRepo.findByMasterAgrIdOrderByRepaySchId(mastAgrId);
				if (repayList.size() <= 0) {
					throw new AgreementDataNotFoundException(
							"No schedule information available for master agreement " + mastAgrId);
				}
			
			for (AgrRepaySchedule repay : repayList) {
				AgreementRepaymentRecordsDto repayDto = new AgreementRepaymentRecordsDto();
				BeanUtils.copyProperties(repay, repayDto);
				repayDto.setRepaySchId(repay.getRepaySchId());
				repayDto.setLoanId(repay.getLoanId());
				repayDto.setDtInstallment(convertToDateFormat(repay.getDtInstallment()));
				repayDto.setDtPaymentDate(convertToDateFormat(repay.getDtPaymentDate()));
				repayDto.setInvoiceId(invoice.getInvId());
				repayDto.setInvoiceNo(invoice.getInvoiceNo());
				agreementRepaymentRecordsList.add(repayDto);
			}
			agreementRepaymentListDto.setAgreementRepaymentRecords(agreementRepaymentRecordsList);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return agreementRepaymentListDto;
	}
	
	public AgreementRepaymentListDto getAllAgreementRepaymentList(String originationApplnNo) throws Exception {
		AgreementRepaymentListDto agreementRepaymentListDto = new AgreementRepaymentListDto();
		List<AgreementRepaymentRecordsDto> agreementRepaymentRecordsList = new ArrayList<AgreementRepaymentRecordsDto>();
		List<AgrRepaySchedule> repayList = new ArrayList<AgrRepaySchedule>();
		try {
        	List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository.findAllByOriginationApplnNo(originationApplnNo);
        	log.info("agrMasterAgreementList " + agrMasterAgreementList);
			for (AgrMasterAgreement masterAgreement : agrMasterAgreementList) {
				AgrInvoiceDetails invoice = agrInvoiceDetailsRepository.findByMasterMastAgrId(masterAgreement.getMastAgrId());

				repayList = repayRepo.findByMasterAgrIdOrderByRepaySchId(masterAgreement.getMastAgrId());
				if (repayList.size() <= 0) {
					throw new AgreementDataNotFoundException("No schedule information available for master agreement " + masterAgreement.getMastAgrId());
				}

				int i = 0;
				double unusedLimit = 0.0;
				CustApplLimitSetup limit = custLimitRepo.findByOriginationApplnNo(originationApplnNo);
				for (AgrRepaySchedule repay : repayList) {
					AgreementRepaymentRecordsDto repayDto = new AgreementRepaymentRecordsDto();
					BeanUtils.copyProperties(repay, repayDto);
					repayDto.setRepaySchId(repay.getRepaySchId());
					repayDto.setLoanId(repay.getLoanId());
					if(repay.getDtInstallment() != null) {
						repayDto.setDtInstallment(convertToDateFormat(repay.getDtInstallment()));
					}
					if(repay.getDtPaymentDate() != null) {
						repayDto.setDtPaymentDate(convertToDateFormat(repay.getDtPaymentDate()));
					}						
					repayDto.setInvoiceId(invoice.getInvId());
					repayDto.setInvoiceNo(invoice.getInvoiceNo());
					repayDto.setUtilisedLimit(repay.getClosingPrincipal());
					if(i == 0) {
						unusedLimit =((double) Math.round(limit.getAvailableLimit())) + ((double) Math.round(repay.getPrincipalAmount()));	
					}else {
						unusedLimit = unusedLimit + ((double) Math.round(repay.getPrincipalAmount()));
					}
					repayDto.setUnusedLimit(unusedLimit);
					agreementRepaymentRecordsList.add(repayDto);
					i++;
				}
			}
			agreementRepaymentRecordsList = getSortedList(agreementRepaymentRecordsList);
			agreementRepaymentListDto.setAgreementRepaymentRecords(agreementRepaymentRecordsList);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return agreementRepaymentListDto;
	}
		
	public Page<VAgrTranHistoryDto> getAgreementTranHistoryList(String mastAgrId, Pageable pageable) throws Exception {
	    try {
	        // Fetch paginated transaction history
	        List<VAgrTranHistory> tranHistPage = tranHistRepo.findAllByMastAgrId(mastAgrId);

	        if (tranHistPage.isEmpty()) {
	            throw new AgreementDataNotFoundException(
	                    "No transaction history available for master agreement " + mastAgrId);
	        }

	        // Map entities to DTOs
	        List<VAgrTranHistoryDto> tranHistDtoList = tranHistPage.stream().map(tranHist -> {
	            VAgrTranHistoryDto tranHistDto = new VAgrTranHistoryDto();
	            BeanUtils.copyProperties(tranHist, tranHistDto);
	           // tranHistDto.setSrNo(tranHistPage.getContent().indexOf(tranHist) + 1); // Sequential numbering
	            tranHistDto.setDtTranDate(convertToDateFormat(tranHist.getDtTranDate()));
	            return tranHistDto;
	        }).collect(Collectors.toList());

	        // Convert List to Page
	        return pageableUtils.convertToPage(tranHistDtoList, pageable);

	    } catch (AgreementDataNotFoundException e) {
	        throw new AgreementDataNotFoundException(e.getMessage());
	    } catch (Exception e) {
	        throw e;
	    }
	}


	public VAgrTranHistoryHeaderDto getAgreementTranHistoryHeader(String mastAgrId) throws Exception {
		VAgrTranHistoryHeaderDto tranHistDtoList = new VAgrTranHistoryHeaderDto();

		try {

			tranHistDtoList.setPrincipalOutstanding(commonService.getMasterAgrUnbilledPrincipal(mastAgrId));
			tranHistDtoList.setChargesDue(commonService.getAllChargeDuesOfMasterAgr(mastAgrId));
			tranHistDtoList.setDueBalance(commonService.getMasterTotalDues(mastAgrId));
			tranHistDtoList.setInstallmentDue(commonService.getHeadwiseDuesForMasterAgr(mastAgrId, "BPI")
					+ commonService.getHeadwiseDuesForMasterAgr(mastAgrId, "PRINCIPAL")
					+ commonService.getHeadwiseDuesForMasterAgr(mastAgrId, "INTEREST"));
			tranHistDtoList.setExcessAmount(commonService.getMasterAgrExcessAmount(mastAgrId));

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		return tranHistDtoList;
	}
		
	public Page <VAgrInterestAccrualHistoryDto> getAgreementIntAccList(String mastAgrId, String loanId, String frDate,
			String tillDate, Pageable paging) throws Exception {
		//VAgrInterestAccrualHistoryMainDto main = new VAgrInterestAccrualHistoryMainDto();
		List<VAgrInterestAccrualHistoryDto> intAccHistDtoList = new ArrayList<VAgrInterestAccrualHistoryDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date fromDate = sdf.parse(frDate);
		Date toDate = sdf.parse(tillDate);
		try {
			List<VAgrInterestAccrualHistory> intAccList = intAccRepo
					.findAllByMastAgrIdAndLoanIdAndDtTranDateBetween(mastAgrId, loanId, fromDate, toDate);
			if (intAccList.size() <= 0) {
				throw new AgreementDataNotFoundException("No interest accrual history available for master agreement "
						+ mastAgrId + " and loan " + loanId);
			}
		    
			    intAccHistDtoList = intAccList.stream().map(intAcc -> {
			    VAgrInterestAccrualHistoryDto intAccDto = new VAgrInterestAccrualHistoryDto();
			    BeanUtils.copyProperties(intAcc, intAccDto);
			    intAccDto.setDtTranDate(convertToDateFormat(intAcc.getDtTranDate()));
			    return intAccDto;
			}).collect(Collectors.toList());

		        return pageableUtils.convertToPage(intAccHistDtoList, paging);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		
		
	}
	
	

	public AllOdAgreementMainDto getAllOdAgreementDetails(String mastAgrId, Integer pageNo, Integer pageSize) {
		AllOdAgreementMainDto mainDto = new AllOdAgreementMainDto();
		List<AllOdAgreementDetailsDto> allAgreementDtoList = new ArrayList<AllOdAgreementDetailsDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
		Pageable paging = PageRequest.of(pageNo, pageSize);
		try {
			List<AgrMasterAgreement> allAgreementList = new ArrayList<>();
			if (mastAgrId == null) {
				allAgreementList = agrRepo
						.findByPortfolioCodeInOrderByDtUserDateDesc(Arrays.asList(new String[] { "DL", "ND" }), paging);
			} else {
				allAgreementList = agrRepo.findByMastAgrIdAndPortfolioCodeInOrderByDtUserDateDesc(mastAgrId,
						Arrays.asList(new String[] { "DL", "ND" }), paging);

			}
			if (allAgreementList == null) {
				throw new AgreementDataNotFoundException("Agreement not available.");
			}
			for (AgrMasterAgreement agreement : allAgreementList) {
				List<AgrCustomer> customerList = custRepo
						.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(agreement.getMastAgrId());
				AllOdAgreementDetailsDto allAgreementDto = new AllOdAgreementDetailsDto();
				AgrCustLimitSetup limitDetails = limitRepo.findByMasterAgreementMastAgrId(agreement.getMastAgrId());
				BeanUtils.copyProperties(limitDetails, allAgreementDto);

				for (AgrCustomer customer : customerList) {
					if (customer.getCustomerType().equalsIgnoreCase("B")) {

						BeanUtils.copyProperties(customer, allAgreementDto);
						allAgreementDto.setMastAgrId(agreement.getMastAgrId());
						String name = "";
						if (customer.getFirstName() != null) {
							name = customer.getFirstName();
						}
						if (customer.getMiddleName() != null) {
							name = name + " " + customer.getMiddleName();
						}
						if (customer.getLastName() != null) {
							name = name + " " + customer.getLastName();
						}
						allAgreementDto.setCustomerName(name);
						allAgreementDto.setOriginationApplnNo(agreement.getOriginationApplnNo());
						allAgreementDto.setCustInternalId(customer.getCustInternalId());
						allAgreementDto.setCustCategory(
								commonService.getDescription("CUSTOMER_CATEGORY", customer.getCustCategory()));
						allAgreementDto.setBoardingDate(dateTimeFormatter.format(customer.getDtUserDate()));
						allAgreementDtoList.add(allAgreementDto);
					}
				}
			}

			if (mastAgrId == null) {
				mainDto.setTotalRows(agrRepo
						.findByPortfolioCodeInOrderByDtUserDateDesc(Arrays.asList(new String[] { "DL", "ND" })).size());
			} else {
				mainDto.setTotalRows(1);
			}
			mainDto.setAllAgreementDto(allAgreementDtoList);
		} catch (Exception e) {
			throw e;
		}
		return mainDto;
	}

	public AllAgreementMainDto getAllAgreementDetails(Integer pageNo, Integer pageSize) {
		AllAgreementMainDto mainDto = new AllAgreementMainDto();
		List<AllAgreementDetailsDto> allAgreementDtoList = new ArrayList<AllAgreementDetailsDto>();
		String dateFormat = env.getProperty("lms.global.date.format");
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
		Pageable paging = PageRequest.of(pageNo, pageSize);
		try {
			List<AgrMasterAgreement> allAgreementList = agrRepo.findAllByOrderByDtUserDateDesc(paging);
			if (allAgreementList == null) {
				throw new AgreementDataNotFoundException("Agreement not available.");
			}
			for (AgrMasterAgreement agreement : allAgreementList) {
				List<AgrCustomer> customerList = custRepo
						.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(agreement.getMastAgrId());
//				List<AgrCustomerResponseDto> customerList = custRepo.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(agreement.getMastAgrId());

				for (AgrCustomer customer : customerList) {
					if (customer.getCustomerType().equalsIgnoreCase("B")) {
						AllAgreementDetailsDto allAgreementDto = new AllAgreementDetailsDto();
						BeanUtils.copyProperties(customer, allAgreementDto);
						allAgreementDto.setMastAgrId(agreement.getMastAgrId());
						String name = "";
						if (customer.getFirstName() != null) {
							name = customer.getFirstName();
						}
						if (customer.getMiddleName() != null) {
							name = name + " " + customer.getMiddleName();
						}
						if (customer.getLastName() != null) {
							name = name + " " + customer.getLastName();
						}
						allAgreementDto.setCustomerName(name);
						allAgreementDto.setOriginationApplnNo(agreement.getOriginationApplnNo());
						allAgreementDto.setCustInternalId(customer.getCustInternalId());
						allAgreementDto.setCustCategory(
								commonService.getDescription("CUSTOMER_CATEGORY", customer.getCustCategory()));
						allAgreementDto.setBoardingDate(dateTimeFormatter.format(customer.getDtUserDate()));
						allAgreementDtoList.add(allAgreementDto);
					}
				}
			}

			mainDto.setTotalRows(agrRepo.findAll().size());
			mainDto.setAllAgreementDto(allAgreementDtoList);
		} catch (Exception e) {
			throw e;
		}
		return mainDto;
	}

	public List<GetEpayAgreementDto> getEpayAgreementDetails(String mastAgrId) {
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			List<GetEpayAgreementDto> epayDtoList = new ArrayList<GetEpayAgreementDto>();

			List<AgrEpaySetup> epayList = epayRepo.findAllByMastAgreementMastAgrId(mastAgrId);
			if (epayList.size() == 0) {
				throw new AgreementDataNotFoundException("Epay details not available.");
			}
			for (AgrEpaySetup epay : epayList) {

				GetEpayAgreementDto epayDto = new GetEpayAgreementDto();
				BeanUtils.copyProperties(epay, epayDto);
				epayDto.setDtFromDate(sdf.format(epay.getDtFromDate()));
				epayDto.setDtToDate(sdf.format(epay.getDtToDate()));
				epayDto.setAccountType(commonService.getDescription("ACCOUNT_TYPE", epay.getAccountType()));
				epayDto.setMandateStatus(commonService.getDescription("MANDATE_STATUS", epay.getMandateStatus()));
				epayDto.setMandateType(commonService.getDescription("MANDATE_TYPE", epay.getMandateType()));
				epayDto.setVirtualId(epay.getMastAgreement().getVirtualId());
				epayDtoList.add(epayDto);
			}
			return epayDtoList;
		} catch (Exception e) {
			throw e;
		}

	}

	public List<CollateralDetailsDto> getCollateralDetails(String mastAgrId) {
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			List<CollateralDetailsDto> collateralDtoList = new ArrayList<CollateralDetailsDto>();

			List<AgrCollateral> collateralList = collateralRepo.findByMastAgrMastAgrId(mastAgrId);
			if (collateralList.size() == 0) {
				throw new AgreementDataNotFoundException("Collateral details not available.");
			}
			for (AgrCollateral collateral : collateralList) {

				CollateralDetailsDto collateralDto = new CollateralDetailsDto();
				BeanUtils.copyProperties(collateral, collateralDto);
				collateralDto.setDtCreation(sdf.format(collateral.getDtCreation()));
				collateralDto.setDtValuation(sdf.format(collateral.getDtValuation()));
				collateralDtoList.add(collateralDto);
			}
			return collateralDtoList;
		} catch (Exception e) {
			throw e;
		}

	}

	public VTranHistoryOdMainDto getAgreementTranHistoryOdList(String mastAgrId, Date fromDate, Date toDate,
			Integer pageNo, Integer pageSize) throws Exception {
		VTranHistoryOdMainDto tranHistList = new VTranHistoryOdMainDto();
		List<VAgrTranHistoryOd> odList = new ArrayList<VAgrTranHistoryOd>();
		Pageable paging = PageRequest.of(pageNo, pageSize);
		try {
			odList = tranHistOdRepo.findAllByMastAgrIdAndDtTranDateBetweenOrderByTranDtlIdAsc(mastAgrId, fromDate,
					toDate, paging);
			if (odList.size() <= 0) {
				throw new AgreementDataNotFoundException("No transaction history available");
			}

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
		tranHistList.setTotalRows(odList.size());
		tranHistList.setTranList(odList);
		return tranHistList;
	}

	public VAgrTranHistoryOdHeaderDto getAgreementTranHistoryOdHeader(String mastAgrId) throws Exception {
		VAgrTranHistoryOdHeaderDto tranHistOdHdr = new VAgrTranHistoryOdHeaderDto();
		float interestRate = 0;

		try {
			AgrMasterAgreement master = agrRepo.findByMastAgrId(mastAgrId);
			if (master == null) {
				throw new AgreementDataNotFoundException("Master Agreement data not found");
			}
			AgrProduct product = prodRepo.findByMasterAgreementMastAgrId(mastAgrId);
			if (product == null) {
				throw new AgreementDataNotFoundException("Product data not found");
			}
			List<AgrLoans> loans = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
			AgrLoans loan = null;
			if (loans.size() > 0) {
				loan = loans.get(0);
				interestRate = loan.getInterestRate() != null ? loan.getInterestRate() : 0;

				log.info("interestRate: " + interestRate);
			}
			AgrCustLimitSetup limit = limitRepo.findByMasterAgreementMastAgrId(mastAgrId);
			if (limit == null) {
				throw new AgreementDataNotFoundException("Limit data not found");
			}
			tranHistOdHdr.setAvailableLimit(limit.getAvailableLimit());
			tranHistOdHdr.setCustomer(master.getCustomerId());
			tranHistOdHdr.setDropLineFreq(product.getDropLIneFreq());
			tranHistOdHdr.setInterestAccrual(product.getInterestAccrualFrequ());
			tranHistOdHdr.setInterestRate(interestRate);
			tranHistOdHdr.setMastAgrId(mastAgrId);
			tranHistOdHdr.setSanctionedLimit(limit.getLimitSanctionAmount());
			tranHistOdHdr.setTenor(master.getTenor());
			tranHistOdHdr.setUtilizedLimit(limit.getUtilizedLimit());

			// Get Collateral Shares Data
			AgrCollateralShares agrCollateralShares = agrCollateralSharesService
					.getAgrCollateralSharesByMastAgrId(mastAgrId);
			if (agrCollateralShares != null) {
				tranHistOdHdr.setDrawingPower(agrCollateralShares.getDrawingPower());
			}

		} catch (Exception e) {
			throw e;
		}
		return tranHistOdHdr;
	}

	public List<String> getMastAgrIdListByCustomerId(String customerId) throws Exception {

		try {

			List<String> mastAgrIdList = agrRepo.findMastAgrIdListByCustomerId(customerId);

			if (!mastAgrIdList.isEmpty()) {
				return mastAgrIdList;
			}

		} catch (Exception e) {
			e.printStackTrace();

			throw e;
		}
		return null;
	}

	public String getCustomerName(AgrCustomer customer) {

		if (customer != null) {
			String name = "";
			if (customer.getFirstName() != null) {
				name = customer.getFirstName();
			}
			if (customer.getMiddleName() != null) {
				name = name + " " + customer.getMiddleName();
			}
			if (customer.getLastName() != null) {
				name = name + " " + customer.getLastName();
			}
			return name;
		}

		return null;
	}

	public List<GetLimitDtlsDto> getLimitDetails(String mastAgrId) {
		List<GetLimitDtlsDto> result = new ArrayList();
		try {

			List<AgrTrnLimitDtls> limitList = trnLimitRepo.findByMasterAgreementMastAgrIdOrderByLimitTranId(mastAgrId);
			for (AgrTrnLimitDtls limit : limitList) {
				GetLimitDtlsDto dtlDto = new GetLimitDtlsDto();
				BeanUtils.copyProperties(limit, dtlDto);
				dtlDto.setMastAgrId(limit.getMasterAgreement().getMastAgrId());
				result.add(dtlDto);
			}
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

	public String convertToDateFormat(Date date) {
		if (date == null) {
			return null;
		}
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);

	}
	
	private List<AgreementRepaymentRecordsDto> getSortedList(List<AgreementRepaymentRecordsDto> agreementRepaymentRecordsList) throws Exception{
		try {
			Collections.sort(agreementRepaymentRecordsList, new Comparator<AgreementRepaymentRecordsDto>() {  
				//DateFormat f = new SimpleDateFormat("MM/dd/yyyy '@'hh:mm a"); 
				String dateFormat = env.getProperty("lms.global.date.format");
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				@Override  
				public int compare(AgreementRepaymentRecordsDto a, AgreementRepaymentRecordsDto b) {  
				try {  
				  return sdf.parse(a.getDtInstallment()).compareTo(sdf.parse(b.getDtInstallment()));  
				    } catch (ParseException e) {  
				      throw new IllegalArgumentException(e);  
				    }  
				}  
				});  
		}catch(Exception e) {
			throw e;
		}
		return agreementRepaymentRecordsList;
	}

    
	// Create Get API for Amortization Variation List
	public Page<RepayVariationDto> getAgreementAmortVariationList(String mastAgrId, Pageable pageable) throws Exception {
	    List<RepayVariationDto> repayDtoList = new ArrayList<>();

	    try {
	        // Validate input parameters
	        if (mastAgrId == null || mastAgrId.trim().isEmpty()) {
	            throw new AgreementDataNotFoundException("Master Agreement ID cannot be null or empty");
	        }

	        // Fetch all loans associated with the Master Agreement
	        List<AgrLoans> agrLoansList = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
	        if (agrLoansList.isEmpty()) {
	            throw new AgreementDataNotFoundException("No loans found for Master Agreement ID: " + mastAgrId);
	        }

	        // Iterate over each loan to fetch repayments variations
	        for (AgrLoans agrLoans : agrLoansList) {
	            List<AgrRepayVariation> repayVariationList = agrRepayRepo.findByLoansLoanId(agrLoans.getLoanId());

	            if (repayVariationList.isEmpty()) {
	                // Log or handle case where no repayments variations are found for a specific Loan ID
	                System.out.println("No repayment variations found for Loan ID: " + agrLoans.getLoanId());
	                continue;
	            }

	            // Map AgrRepayVariation entities to RepayVariationDto
	            for (AgrRepayVariation repayVariation : repayVariationList) {
	                RepayVariationDto repayDto = new RepayVariationDto();
	                BeanUtils.copyProperties(repayVariation, repayDto);
	                repayDto.setFromInstallmentNo(repayVariation.getFromInstallmentNo());
	                repayDto.setNoOfInstallment(repayVariation.getNoOfInstallments());
	                repayDto.setVariationType(repayVariation.getVariationType());
	                repayDto.setVariationOption(repayVariation.getVariationOption());
	                repayDto.setVariationValue(repayVariation.getVariationValue());
	                repayDto.setAdjustmentFactor(repayVariation.getAdjustmentFactor());
	                repayDtoList.add(repayDto);
	            }
	        }

	        // Convert the accumulated list to a paginated response
	       

	    }  catch (Exception e) {
	        throw e;
	    } 
	 return pageableUtils.convertToPage(repayDtoList, pageable);
	}



}
