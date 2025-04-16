package com.samsoft.lms.agreement.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.dto.AgrInvoiceDetailsDto;
import com.samsoft.lms.agreement.dto.AgreementBoardingDto;
import com.samsoft.lms.agreement.dto.AgreementBoardingOdDto;
import com.samsoft.lms.agreement.dto.AgreementBoardingSFDto;
import com.samsoft.lms.agreement.dto.AgreementValidationDto;
import com.samsoft.lms.agreement.dto.ColenderBoardingDto;
import com.samsoft.lms.agreement.dto.ColenderIncShareBoardingDto;
import com.samsoft.lms.agreement.dto.CollateralBoardingDto;
import com.samsoft.lms.agreement.dto.CustomerAddressAgrDto;
import com.samsoft.lms.agreement.dto.CustomerBoardingDto;
import com.samsoft.lms.agreement.dto.FeeBoardingDto;
import com.samsoft.lms.agreement.dto.SlabWiseInterestBoardingDto;
import com.samsoft.lms.agreement.entities.AgrColenderDtl;
import com.samsoft.lms.agreement.entities.AgrColenderIncShareDtl;
import com.samsoft.lms.agreement.entities.AgrCollateral;
import com.samsoft.lms.agreement.entities.AgrEpaySetup;
import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;
import com.samsoft.lms.agreement.entities.AgrPdcSetup;
import com.samsoft.lms.agreement.entities.TabMstColender;
import com.samsoft.lms.agreement.repositories.AgrColenderDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrColenderIncShareDtlRepository;
import com.samsoft.lms.agreement.repositories.AgrCollateralRepository;
import com.samsoft.lms.agreement.repositories.AgrEpaySetupRepository;
import com.samsoft.lms.agreement.repositories.AgrInvoiceDetailsRepository;
import com.samsoft.lms.agreement.repositories.AgrPdcSetupRepository;
import com.samsoft.lms.agreement.repositories.TabMstColenderRepository;
import com.samsoft.lms.banking.utils.BankingUtil;
import com.samsoft.lms.core.dto.CreateAgrInsuranceDetailReqDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrFeeParam;
import com.samsoft.lms.core.entities.AgrInsuranceDetail;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrProdSlabwiseInterest;
import com.samsoft.lms.core.entities.AgrProduct;
import com.samsoft.lms.core.entities.AgrTrnEventDtl;
import com.samsoft.lms.core.entities.AgrTrnTranDetail;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.entities.TabMstDepositBank;
import com.samsoft.lms.core.entities.TabMstLookups;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrFeeParamRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrProdSlabwiseInterestRepository;
import com.samsoft.lms.core.repositories.AgrProductRepository;
import com.samsoft.lms.core.repositories.AgrTrnTranDetailsRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.core.repositories.TabMstDepositBankRepository;
import com.samsoft.lms.core.repositories.TabMstLookupsRepository;
import com.samsoft.lms.core.services.AgrInsuranceDetailService;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.entities.AgrCustAddress;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustAddressRepository;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.las.dto.request.AgrCollateralSharesReqDto;
import com.samsoft.lms.las.entities.AgrCollateralShares;
import com.samsoft.lms.las.services.AgrCollateralSharesService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AgreementBoardingService {

	@Autowired
	private AgrCustomerRepository custRepo;

	@Autowired
	private AgrCustAddressRepository addrRepo;

	@Autowired
	private AgrCustLimitSetupRepository limitRepo;

	@Autowired
	private AgrProductRepository prodRepo;

	@Autowired
	private AgrFeeParamRepository feeRepo;

	@Autowired
	private AgrColenderDtlRepository colenderRepo;

	@Autowired
	private AgrColenderIncShareDtlRepository colenderShareRepo;

	@Autowired
	private AgrPdcSetupRepository pdcRepo;

	@Autowired
	private AgrEpaySetupRepository epayRepo;

	@Autowired
	private AgrCollateralRepository collateralRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private Environment env;

	@Autowired
	private TabMstLookupsRepository lookupRepo;

	@Autowired
	private TabMstDepositBankRepository depositBankRepo;

	@Autowired
	private AgrTrnTranDetailsRepository detailRepo;

	@Autowired
	private AgrCollateralSharesService agrCollateralSharesService;

	@Autowired
	private AgrProdSlabwiseInterestRepository slabwiseRepo;

	@Autowired
	private CustApplLimitSetupRepository custApplRepo;

	@Autowired
	private AgrInvoiceDetailsRepository invoiceRepo;

	@Autowired
	private AgrInsuranceDetailService agrInsuranceDetailService;

	@Autowired
	private TabMstColenderRepository tabMstColenderRepository;

	@Autowired
	private BankingUtil bankingUtil;

	@Autowired
	private CommonServices commonService;

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<AgreementValidationDto> agreementValidation(AgreementBoardingDto agreementBordingParam) {
		List<AgreementValidationDto> result = new ArrayList<AgreementValidationDto>();
		List<TabMstLookups> lookupList = lookupRepo.findByActiveOrderByLookupType("Y");
		HashMap<String, List<String>> lookupMap = new HashMap<String, List<String>>();
		List<String> lookupTypelist = new ArrayList<String>();
		String lookupType = lookupList.get(0).getLookupType();
		for (TabMstLookups lookup : lookupList) {
			if (!(lookupType.equals(lookup.getLookupType()))) {
				lookupType = lookup.getLookupType();
				lookupTypelist = new ArrayList<String>();
			}
			lookupTypelist.add(lookup.getCode());
			lookupMap.put(lookupType, lookupTypelist);
		}

		if (agreementBordingParam.getMasterAgreement() == null) {
			result.add(
					new AgreementValidationDto("MasterAgreement", "MasterAgreement section not available in input."));
		}

		if (agreementBordingParam.getCustomerList() == null) {
			result.add(new AgreementValidationDto("CustomerList", "CustomerList section not available in input."));
		}

		if (agreementBordingParam.getProduct() == null) {
			result.add(new AgreementValidationDto("Product", "Product section not available in input."));
		}
		if (agreementBordingParam.getFeeList() == null) {
			result.add(new AgreementValidationDto("FeeList", "FeeList section not available in input."));
		}

		if (agreementBordingParam.getEpay() == null) {
			result.add(new AgreementValidationDto("Epay", "Epay section not available in input."));
		}

		if (lookupMap.containsKey("BATCH_INSTRUMENT")) {
			if (!(lookupMap.get("BATCH_INSTRUMENT")
					.contains(agreementBordingParam.getMasterAgreement().getPrefRepayMode()))) {
				result.add(new AgreementValidationDto("MasterAgreement",
						"PrefRepayMode does not match with expected master values."));
			}

			if (!(lookupMap.get("BATCH_INSTRUMENT").contains(agreementBordingParam.getEpay().getInstrumentType()))) {
				result.add(new AgreementValidationDto("Epay",
						"InstrumentType does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("MasterAgreement", "BATCH_INSTRUMENT lookup is not available"));
		}

		List<CustomerBoardingDto> customerList = agreementBordingParam.getCustomerList();
		for (CustomerBoardingDto customerBoardingDto : customerList) {
			if (lookupMap.containsKey("CUSTOMER_TYPE")) {
				if (!(lookupMap.get("CUSTOMER_TYPE").contains(customerBoardingDto.getCustomerType()))) {
					result.add(new AgreementValidationDto("CustomerList",
							"CustomerType does not match with expected master values for customer "
									+ customerBoardingDto.getCustomerId()));
				}
			} else {
				result.add(new AgreementValidationDto("CustomerList", "CUSTOMER_TYPE lookup is not available"));
			}

			if (lookupMap.containsKey("CUSTOMER_CATEGORY")) {
				if (!(lookupMap.get("CUSTOMER_CATEGORY").contains(customerBoardingDto.getCustCategory()))) {
					result.add(new AgreementValidationDto("CustomerList",
							"CustCategory does not match with expected master values for customer "
									+ customerBoardingDto.getCustomerId()));
				}
			} else {
				result.add(new AgreementValidationDto("CustomerList", "CUSTOMER_CATEGORY lookup is not available"));
			}

			List<CustomerAddressAgrDto> customerAddrList = customerBoardingDto.getCustomerAddrList();
			for (CustomerAddressAgrDto customerAddr : customerAddrList) {
				if (lookupMap.containsKey("ADDRESS_TYPE")) {
					if (!(lookupMap.get("ADDRESS_TYPE").contains(customerAddr.getAddrType()))) {
						result.add(new AgreementValidationDto("CustomerList",
								"AddrType does not match with expected master values for customer "
										+ customerBoardingDto.getCustomerId()));
					}
				} else {
					result.add(new AgreementValidationDto("CustomerList", "ADDRESS_TYPE lookup is not available"));
				}
			}
		}

		if (lookupMap.containsKey("INTEREST_TYPE")) {
			if (!(lookupMap.get("INTEREST_TYPE").contains(agreementBordingParam.getProduct().getInterestType()))) {
				result.add(new AgreementValidationDto("Product",
						"InterestType does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "INTEREST_TYPE lookup is not available"));
		}

		if (lookupMap.containsKey("INTEREST_BASIS")) {
			if (!(lookupMap.get("INTEREST_BASIS").contains(agreementBordingParam.getProduct().getInterestBasis()))) {
				result.add(new AgreementValidationDto("Product",
						"InterestBasis does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "INTEREST_BASIS lookup is not available"));
		}

		if (lookupMap.containsKey("EMI_BASIS")) {
			if (!(lookupMap.get("EMI_BASIS").contains(agreementBordingParam.getProduct().getEmiBasis()))) {
				result.add(
						new AgreementValidationDto("Product", "EmiBasis does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "EMI_BASIS lookup is not available"));
		}

		if (lookupMap.containsKey("INTEREST_ACCRUAL_FREQ")) {
			if (!(lookupMap.get("INTEREST_ACCRUAL_FREQ")
					.contains(agreementBordingParam.getProduct().getInterestAccrualFrequ()))) {
				result.add(new AgreementValidationDto("Product",
						"InterestAccrualFrequ does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "INTEREST_ACCRUAL_FREQ lookup is not available"));
		}

		if (lookupMap.containsKey("PENAL_INTEREST_BASIS")) {
			if (!(lookupMap.get("PENAL_INTEREST_BASIS")
					.contains(agreementBordingParam.getProduct().getPenalInterestBasis()))) {
				result.add(new AgreementValidationDto("Product",
						"PenalInterestBasis does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "PENAL_INTEREST_BASIS lookup is not available"));
		}

		if (lookupMap.containsKey("FEE_AC_BASIS")) {
			if (!(lookupMap.get("FEE_AC_BASIS")
					.contains(agreementBordingParam.getProduct().getPenalAccountingBasis()))) {
				result.add(new AgreementValidationDto("Product",
						"PenalAccountingBasis does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Product", "FEE_AC_BASIS lookup is not available"));
		}

		List<FeeBoardingDto> feeList = agreementBordingParam.getFeeList();
		for (FeeBoardingDto fee : feeList) {
			if (lookupMap.containsKey("FEE_TYPE")) {
				if (!(lookupMap.get("FEE_TYPE").contains(fee.getFeeType()))) {
					result.add(new AgreementValidationDto("FeeList",
							"FeeType does not match with expected master values for fee " + fee.getFeeCode()));
				}
			} else {
				result.add(new AgreementValidationDto("FeeList", "FEE_TYPE lookup is not available"));
			}

			if (lookupMap.containsKey("FEE_EVENT")) {
				if (!(lookupMap.get("FEE_EVENT").contains(fee.getFeeEvent()))) {
					result.add(new AgreementValidationDto("FeeList",
							"FeeEvent does not match with expected master values for fee " + fee.getFeeCode()));
				}
			} else {
				result.add(new AgreementValidationDto("FeeList", "FEE_EVENT lookup is not available"));
			}

			if (lookupMap.containsKey("FEE_AC_BASIS")) {
				if (!(lookupMap.get("FEE_AC_BASIS").contains(fee.getFeeAccountingBasis()))) {
					result.add(new AgreementValidationDto("FeeList",
							"FeeAccountingBasis does not match with expected master values for fee "
									+ fee.getFeeCode()));
				}
			} else {
				result.add(new AgreementValidationDto("FeeList", "FEE_AC_BASIS lookup is not available"));
			}

		}
		if (lookupMap.containsKey("ACCOUNT_TYPE")) {
			if (!(lookupMap.get("ACCOUNT_TYPE").contains(agreementBordingParam.getEpay().getAccountType()))) {
				result.add(
						new AgreementValidationDto("Epay", "AccountType does not match with expected master values."));
			}
		} else {
			result.add(new AgreementValidationDto("Epay", "ACCOUNT_TYPE lookup is not available"));
		}

		if (lookupMap.containsKey("MANDATE_TYPE")) {
			if (!(lookupMap.get("MANDATE_TYPE").contains(agreementBordingParam.getEpay().getMandateType()))) {
				result.add(
						new AgreementValidationDto("Epay", "MandateType does not match with expected master values."));
			}

		} else {
			result.add(new AgreementValidationDto("Epay", "MANDATE_TYPE lookup is not available"));
		}

		TabMstDepositBank epayDepositBank = depositBankRepo
				.findByDepositBankId(agreementBordingParam.getEpay().getDepositBank());
		if (epayDepositBank == null) {
			result.add(new AgreementValidationDto("Epay", "DepositBank does not match with expected master values."));
		}

		/*
		 * if (agreementBordingParam.getPdc() != null) {
		 * 
		 * if (lookupMap.containsKey("ACCOUNT_TYPE")) { if
		 * (!(lookupMap.get("ACCOUNT_TYPE").contains(agreementBordingParam.getPdc().
		 * getAccountType()))) { result.add(new AgreementValidationDto("Pdc",
		 * "AccountType does not match with expected master values.")); } } else {
		 * result.add(new AgreementValidationDto("Pdc",
		 * "ACCOUNT_TYPE lookup is not available")); }
		 * 
		 * TabMstDepositBank pdcDepositBank = depositBankRepo
		 * .findByDepositBankId(agreementBordingParam.getPdc().getDepositBank()); if
		 * (pdcDepositBank == null) { result.add( new AgreementValidationDto("Pdc",
		 * "DepositBank does not match with expected master values.")); }
		 * 
		 * }
		 */

		return result;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String agreementBoarding(AgreementBoardingDto agreementBordingParam) throws Exception {
		String result = "success";
		String mastAgr = null;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrCustomer> customerList = new ArrayList<AgrCustomer>();
		List<AgrCustAddress> custAddList = new ArrayList<AgrCustAddress>();
		List<AgrFeeParam> feeList = new ArrayList<AgrFeeParam>();
		List<AgrColenderIncShareDtl> colenderShareList = new ArrayList<AgrColenderIncShareDtl>();
		AgrCustLimitSetup limit = null;
		List<AgrColenderDtl> colenderList = new ArrayList<AgrColenderDtl>();
		AgrPdcSetup pdc = null;
		AgrEpaySetup epay = null;
		AgrCustomer borrowerCust = null;
		List<AgrCollateral> collateralList = new ArrayList<AgrCollateral>();
		List<AgrTrnTranDetail> detailsList = new ArrayList<AgrTrnTranDetail>();
		try {

			AgrMasterAgreement fourFinAgrNo = masterRepo
					.findByOriginationApplnNo(agreementBordingParam.getMasterAgreement().getOriginationApplnNo());
			if (fourFinAgrNo != null) {
				throw new CoreBadRequestException(
						"Agreement is already available in system checked OriginationApplnNo");
			}
			if (agreementBordingParam.getMasterAgreement() == null) {
				throw new CoreDataNotFoundException("Agreement details are not available.");
			}

			if (agreementBordingParam.getMasterAgreement().getHomeState() == null
					|| agreementBordingParam.getMasterAgreement().getServState() == null
					|| agreementBordingParam.getMasterAgreement().getHomeState().equals("")
					|| agreementBordingParam.getMasterAgreement().getServState().equals("")) {
				throw new CoreDataNotFoundException("Home State or Servicing State is not available	");
			}

			mastAgr = agreementBordingParam.getMasterAgreement().getPortfolioCode().substring(0, 2)
					+ Long.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

			AgrMasterAgreement master = new AgrMasterAgreement();
			BeanUtils.copyProperties(agreementBordingParam.getMasterAgreement(), master);
			master.setMastAgrId(mastAgr);
			master.setUserId("INTERFACE");
			master.setAgreementStatus("L");
			// Create Virtual Account Id
			master.setVirtualId(bankingUtil.createVirtualId(mastAgr));
			// Create VPA Account Id
			master.setVpaId(bankingUtil.createVPAId(mastAgr));

			if (agreementBordingParam.getCustomerList().size() <= 0) {
				throw new CoreDataNotFoundException("Customer details are not available.");
			}
			for (CustomerBoardingDto customerDto : agreementBordingParam.getCustomerList()) {

				AgrCustomer customer = new AgrCustomer();
				BeanUtils.copyProperties(customerDto, customer);
				customer.setMasterAgr(master);
				customer.setPrefferedContactTimeFrom(customerDto.getContactTimeFrom());
				customer.setPrefferedContactTimeTo(customerDto.getContactTimeTo());
				customer.setUserId("INTERFACE");

				if (customerDto.getCustomerType().equalsIgnoreCase("B")) {
					borrowerCust = customer;
				}

				if (customerDto.getCustomerAddrList().size() <= 0) {
					throw new CoreDataNotFoundException(
							"Address details are not available for " + customerDto.getCustomerId());
				}
				int customerPrefferedCount = 0;
				for (CustomerAddressAgrDto customerAddrDto : customerDto.getCustomerAddrList()) {
					AgrCustAddress custAddr = new AgrCustAddress();
					BeanUtils.copyProperties(customerAddrDto, custAddr);
					custAddr.setCustomer(customer);
					custAddr.setUserId("INTERFACE");
					custAddr.setCustomerId(customer.getCustomerId());
					custAddList.add(custAddr);
					if (customerAddrDto.getPrefferedAddress().equals("Y")) {
						customerPrefferedCount++;
					}

				}

				if (customerPrefferedCount != 1) {
					throw new CoreDataNotFoundException("One customer address to be marked as Preferred Address");
				}
				customerList.add(customer);
			}

			for (FeeBoardingDto feeDto : agreementBordingParam.getFeeList()) {
				AgrFeeParam fee = new AgrFeeParam();
				BeanUtils.copyProperties(feeDto, fee);
				fee.setMasterAgreement(master);
				fee.setUserId("INTERFACE");
				feeList.add(fee);

			}
			/*
			 * if (agreementBordingParam.getCustomerLimit() != null) { limit = new
			 * AgrCustLimitSetup();
			 * BeanUtils.copyProperties(agreementBordingParam.getCustomerLimit(), limit);
			 * limit.setMasterAgreement(master); limit.setUserId("INTERFACE");
			 * limit.setDtLimitExpired(sdf.parse(agreementBordingParam.getCustomerLimit().
			 * getDtLimitExpired()));
			 * limit.setDtLimitSanctioned(sdf.parse(agreementBordingParam.getCustomerLimit()
			 * .getDtLimitSanctioned()));
			 * limit.setAvailableLimit(agreementBordingParam.getCustomerLimit().
			 * getLimitSanctionAmount());
			 * 
			 * // Added by DeepakG AgrTrnTranHeader hdr = new AgrTrnTranHeader();
			 * hdr.setMasterAgr(master);
			 * hdr.setTranDate(sdf.parse(agreementBordingParam.getCustomerLimit().
			 * getDtLimitSanctioned())); hdr.setTranType("LIMIT_SETUP"); hdr.setRemark(
			 * "Limit Setup for Rs. " +
			 * agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
			 * hdr.setSource("INTERFACE"); hdr.setUserID("INTERFACE");
			 * hdr.setSanctionedLimit(agreementBordingParam.getCustomerLimit().
			 * getLimitSanctionAmount());
			 * 
			 * AgrTrnEventDtl event = new AgrTrnEventDtl(); event.setTranHeader(hdr);
			 * event.setTranEvent("DISBURSEMENT"); event.setTranAmount(0.0d);
			 * event.setUserId("INTERFACE");
			 * 
			 * AgrTrnTranDetail detail = new AgrTrnTranDetail(); detail.setEventDtl(event);
			 * detail.setMasterAgr(master);
			 * detail.setDtDueDate(sdf.parse(agreementBordingParam.getCustomerLimit().
			 * getDtLimitSanctioned())); detail.setTranCategory("LIMIT_SETUP");
			 * detail.setTranHead("LIMIT_AMT"); detail.setTranAmount(0.0d);
			 * detail.setTranSide("DR"); detail.setDtlRemark( "Limit Setup for Rs. " +
			 * agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
			 * detail.setAvailableLimit(agreementBordingParam.getCustomerLimit().
			 * getLimitSanctionAmount()); detail.setUtilizedLimit(0.0d);
			 * detailsList.add(detail);
			 * 
			 * }
			 */
			AgrProduct prod = new AgrProduct();
			if (agreementBordingParam.getProduct() == null) {
				throw new CoreDataNotFoundException("Product details are not available");
			}
			BeanUtils.copyProperties(agreementBordingParam.getProduct(), prod);
			prod.setMasterAgreement(master);
			prod.setUserId("INTERFACE");
			int colenderPresenterCount = 0;
			if (agreementBordingParam.getColender() != null) {
				for (ColenderBoardingDto colenderDtlDto : agreementBordingParam.getColender()) {

					TabMstColender tabMstColender = tabMstColenderRepository
							.findByColenderId(Integer.parseInt(colenderDtlDto.getColenderCode()));

					if (tabMstColender == null) {
						throw new CoreDataNotFoundException("Colender not available.");
					}

					AgrColenderDtl colender = new AgrColenderDtl();
					BeanUtils.copyProperties(colenderDtlDto, colender);
					colender.setMasterAgr(master);
					colender.setUserId("INTERFACE");
					if (colenderDtlDto.getInstrumentPresenterYn().equals("Y")) {
						colenderPresenterCount++;
					}

					if (colenderPresenterCount != 1) {
						throw new CoreDataNotFoundException("Only one calendar can be marked as Instrument Presenter.");
					}

					if (colenderDtlDto.getColenderShare().size() > 0) {
						for (ColenderIncShareBoardingDto colenderShareDto : colenderDtlDto.getColenderShare()) {
							AgrColenderIncShareDtl colenderShare = new AgrColenderIncShareDtl();
							BeanUtils.copyProperties(colenderShareDto, colenderShare);
							colenderShare.setColender(colender);
							colenderShare.setUserId("INTERFACE");
							colenderShareList.add(colenderShare);

						}
					}

					colenderList.add(colender);
				}

			}
			/*
			 * if (agreementBordingParam.getPdc() != null) { pdc = new AgrPdcSetup();
			 * BeanUtils.copyProperties(agreementBordingParam.getPdc(), pdc);
			 * pdc.setMasterAgr(master); pdc.setInstrumentStatus("REC");
			 * pdc.setUserId("INTERFACE");
			 * pdc.setDtInstrumentDate(sdf.parse(agreementBordingParam.getPdc().
			 * getDtInstrumentDate()));
			 * pdc.setDtReceipt(sdf.parse(agreementBordingParam.getPdc().getDtReceipt())); }
			 */

			if (agreementBordingParam.getEpay() != null) {
				epay = new AgrEpaySetup();
				BeanUtils.copyProperties(agreementBordingParam.getEpay(), epay);
				epay.setMastAgreement(master);
				epay.setUserId("INTERFACE");
				epay.setMandateStatus("RC");
				epay.setBankCode(agreementBordingParam.getEpay().getBankName());
				epay.setBankBranchCode(agreementBordingParam.getEpay().getBankBranchName());
				epay.setDtFromDate(sdf.parse(agreementBordingParam.getEpay().getDtFromDate()));
				epay.setDtToDate(sdf.parse(agreementBordingParam.getEpay().getDtToDate()));
				epay.setMandateRefNo(agreementBordingParam.getEpay().getMandateRefNo());
				epay.setCustomer(borrowerCust);
				epay.setCustomerId(borrowerCust.getCustomerId());

				if (agreementBordingParam.getEpay().getMandateRefNo() == null) {
					throw new CoreDataNotFoundException(
							"Please specify UMRN(Unique Mandate Reference No) in EPay Setup.");
				}

			} else {
				throw new CoreDataNotFoundException("Please provide EPay Setup for case boarding.");
			}
			if (agreementBordingParam.getCollateral() != null) {
				for (CollateralBoardingDto collateralDto : agreementBordingParam.getCollateral()) {
					AgrCollateral collateral = new AgrCollateral();
					BeanUtils.copyProperties(collateralDto, collateral);
					collateral.setMastAgr(master);
					collateral.setUserId("INTERFACE");
					collateral.setDtValuation(sdf.parse(collateralDto.getDtValuation()));
					collateral.setDtCreation(sdf.parse(collateralDto.getDtCreation()));
					collateralList.add(collateral);
				}

			}
			masterRepo.save(master);
			custRepo.saveAll(customerList);
			addrRepo.saveAll(custAddList);
			feeRepo.saveAll(feeList);
			/*
			 * if (limit != null) { limitRepo.save(limit);
			 * detailRepo.saveAll(detailsList);// Added by DeepakG }
			 */

			prodRepo.save(prod);
			if (colenderList.size() > 0) {
				colenderRepo.saveAll(colenderList);
				if (colenderShareList != null) {
					colenderShareRepo.saveAll(colenderShareList);
				}
			}
			if (pdc != null) {
				pdcRepo.save(pdc);
			}
			if (epay != null) {
				epayRepo.save(epay);
			}
			if (collateralList.size() > 0) {
				collateralRepo.saveAll(collateralList);
			}

			// Added By Aniket Jadhav
			// Create Insurance Detail
			if (agreementBordingParam.getInsuranceDetails() != null) {

				CreateAgrInsuranceDetailReqDto createAgrInsuranceDetailReqDto = CreateAgrInsuranceDetailReqDto.builder()
						.mastAgrId(master.getMastAgrId())
						.insuranceCompany(agreementBordingParam.getInsuranceDetails().getInsuranceCompany())
						.insurancePremiumAmount(agreementBordingParam.getInsuranceDetails().getInsurancePremiumAmount())
						.build();

				AgrInsuranceDetail agrInsuranceDetail = agrInsuranceDetailService
						.createAgrInsuranceDetail(createAgrInsuranceDetailReqDto);
				log.info("AgreementBoardingService :: agreementBoarding :: agrInsuranceDetail: {}", agrInsuranceDetail);
			}

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return mastAgr;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String agreementBoardingOd(AgreementBoardingOdDto agreementBordingParam) throws Exception {
		String result = "success";
		String mastAgr = null;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrCustomer> customerList = new ArrayList<AgrCustomer>();
		List<AgrCustAddress> custAddList = new ArrayList<AgrCustAddress>();
		List<AgrFeeParam> feeList = new ArrayList<AgrFeeParam>();
		List<AgrColenderIncShareDtl> colenderShareList = new ArrayList<AgrColenderIncShareDtl>();
		AgrCustLimitSetup limit = null;
		List<AgrColenderDtl> colenderList = new ArrayList<AgrColenderDtl>();
		AgrPdcSetup pdc = null;
		AgrEpaySetup epay = null;
		AgrCustomer borrowerCust = null;
		List<AgrTrnTranDetail> detailsList = new ArrayList<AgrTrnTranDetail>();
		try {

			AgrMasterAgreement fourFinAgrNo = masterRepo
					.findByOriginationApplnNo(agreementBordingParam.getMasterAgreement().getOriginationApplnNo());
			if (fourFinAgrNo != null) {
				throw new CoreBadRequestException(
						"Agreement is already available in system checked OriginationApplnNo");
			}
			if (agreementBordingParam.getMasterAgreement() == null) {
				throw new CoreDataNotFoundException("Agreement details are not available.");
			}

			if (agreementBordingParam.getCustomerLimit() == null) {
				throw new CoreDataNotFoundException("Limit details are not available.");

			}

			if (agreementBordingParam.getMasterAgreement().getRepayFreq().equals("")) {
				throw new CoreDataNotFoundException(
						"Repayment Frequency is not available in master agreement section.");

			}

			if (agreementBordingParam.getMasterAgreement().getHomeState() == null
					|| agreementBordingParam.getMasterAgreement().getServState() == null
					|| agreementBordingParam.getMasterAgreement().getHomeState().equals("")
					|| agreementBordingParam.getMasterAgreement().getServState().equals("")) {
				throw new CoreDataNotFoundException("Home State or Servicing State is not available	");
			}

			mastAgr = agreementBordingParam.getMasterAgreement().getPortfolioCode().substring(0, 2)
					+ Long.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

			AgrMasterAgreement master = new AgrMasterAgreement();
			BeanUtils.copyProperties(agreementBordingParam.getMasterAgreement(), master);
			if (agreementBordingParam.getMasterAgreement().getTdsRate() == null) {
				master.setTdsRate(0f);
			}
			master.setMastAgrId(mastAgr);
			master.setCurrentDroppedLimit(agreementBordingParam.getMasterAgreement().getSanctionedAmount());
			master.setUserId("INTERFACE");
			master.setAgreementStatus("L");
			// Create Virtual Account Id
			master.setVirtualId(bankingUtil.createVirtualId(mastAgr));
			// Create VPA Account Id
			master.setVpaId(bankingUtil.createVPAId(mastAgr));
			master.setCycleDay(agreementBordingParam.getMasterAgreement().getCycleDay());

			if (agreementBordingParam.getProduct().getDropLineODYN().equalsIgnoreCase("Y")) {
				int addFactorPrin = 0;

				switch (agreementBordingParam.getProduct().getDropLIneFreq()) {
				case "MONTHLY":
					addFactorPrin = 1;
					break;
				case "BIMONTHLY":
					addFactorPrin = 2;
					break;
				case "QUARTERLY":
					addFactorPrin = 3;
					break;
				case "HALFYEARLY":
					addFactorPrin = 6;
					break;
				case "YEARLY":
					addFactorPrin = 12;
					break;

				}

				Date nextDropDate = sdf.parse(agreementBordingParam.getProduct().getDropLineCycleDay() + "-"
						+ agreementBordingParam.getCustomerLimit().getDtLimitSanctioned().split("-")[1] + "-"
						+ agreementBordingParam.getCustomerLimit().getDtLimitSanctioned().split("-")[2]);

				Calendar c1 = Calendar.getInstance();
				c1.setTime(nextDropDate);
				c1.add(Calendar.MONTH, addFactorPrin);
				nextDropDate = c1.getTime();

				master.setDtNextDrop(nextDropDate);

				if (agreementBordingParam.getProduct().getDropLinePerc() > 0) {
					master.setNextDropAmount(commonService.numberFormatter(
							master.getSanctionedAmount() * agreementBordingParam.getProduct().getDropLinePerc() / 100));
				} else {
					master.setNextDropAmount(
							commonService.numberFormatter(agreementBordingParam.getProduct().getDropLineAmount()));
				}

			}

			if (agreementBordingParam.getCustomerList().size() <= 0) {
				throw new CoreDataNotFoundException("Customer details are not available.");
			}
			for (CustomerBoardingDto customerDto : agreementBordingParam.getCustomerList()) {

				AgrCustomer customer = new AgrCustomer();
				BeanUtils.copyProperties(customerDto, customer);
				customer.setMasterAgr(master);
				customer.setPrefferedContactTimeFrom(customerDto.getContactTimeFrom());
				customer.setPrefferedContactTimeTo(customerDto.getContactTimeTo());
				customer.setUserId("INTERFACE");

				if (customerDto.getCustomerType().equalsIgnoreCase("B")) {
					borrowerCust = customer;
				}

				if (customerDto.getCustomerAddrList().size() <= 0) {
					throw new CoreDataNotFoundException(
							"Address details are not available for " + customerDto.getCustomerId());
				}
				for (CustomerAddressAgrDto customerAddrDto : customerDto.getCustomerAddrList()) {
					AgrCustAddress custAddr = new AgrCustAddress();
					BeanUtils.copyProperties(customerAddrDto, custAddr);
					custAddr.setCustomer(customer);
					custAddr.setUserId("INTERFACE");
					custAddr.setCustomerId(customer.getCustomerId());
					custAddList.add(custAddr);

				}
				customerList.add(customer);
			}

			for (FeeBoardingDto feeDto : agreementBordingParam.getFeeList()) {
				AgrFeeParam fee = new AgrFeeParam();
				BeanUtils.copyProperties(feeDto, fee);
				fee.setMasterAgreement(master);
				fee.setUserId("INTERFACE");
				feeList.add(fee);

			}
			if (agreementBordingParam.getCustomerLimit() != null) {
				limit = new AgrCustLimitSetup();
				BeanUtils.copyProperties(agreementBordingParam.getCustomerLimit(), limit);
				limit.setMasterAgreement(master);
				limit.setUserId("INTERFACE");
				limit.setDtLimitExpired(sdf.parse(agreementBordingParam.getCustomerLimit().getDtLimitExpired()));
				limit.setDtLimitSanctioned(sdf.parse(agreementBordingParam.getCustomerLimit().getDtLimitSanctioned()));
				limit.setAvailableLimit(agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
				// Added by AniketJ
				limit.setLtv(agreementBordingParam.getCustomerLimit().getLtv());

				// Added by DeepakG
				AgrTrnTranHeader hdr = new AgrTrnTranHeader();
				hdr.setMasterAgr(master);
				hdr.setTranDate(sdf.parse(agreementBordingParam.getCustomerLimit().getDtLimitSanctioned()));
				hdr.setTranType("LIMIT_SETUP");
				hdr.setRemark(
						"Limit Setup for Rs. " + agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
				hdr.setSource("INTERFACE");
				hdr.setUserID("INTERFACE");
				hdr.setSanctionedLimit(agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());

				AgrTrnEventDtl event = new AgrTrnEventDtl();
				event.setTranHeader(hdr);
				event.setTranEvent("DISBURSEMENT");
				event.setTranAmount(0.0d);
				event.setUserId("INTERFACE");

				AgrTrnTranDetail detail = new AgrTrnTranDetail();
				detail.setEventDtl(event);
				detail.setMasterAgr(master);
				detail.setDtDueDate(sdf.parse(agreementBordingParam.getCustomerLimit().getDtLimitSanctioned()));
				detail.setTranCategory("LIMIT_SETUP");
				detail.setTranHead("LIMIT_AMT");
				detail.setTranAmount(0.0d);
				detail.setTranSide("DR");
				detail.setDtlRemark(
						"Limit Setup for Rs. " + agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
				detail.setAvailableLimit(agreementBordingParam.getCustomerLimit().getLimitSanctionAmount());
				detail.setUtilizedLimit(0.0d);
				detailsList.add(detail);

			}
			AgrProduct prod = new AgrProduct();
			if (agreementBordingParam.getProduct() == null) {
				throw new CoreDataNotFoundException("Product details are not available");
			}
			BeanUtils.copyProperties(agreementBordingParam.getProduct(), prod);
			prod.setMasterAgreement(master);
			prod.setUserId("INTERFACE");
			prod.setDropLineCycleDay(agreementBordingParam.getProduct().getDropLineCycleDay());
			if (agreementBordingParam.getColender() != null) {
				for (ColenderBoardingDto colenderDtlDto : agreementBordingParam.getColender()) {
					AgrColenderDtl colender = new AgrColenderDtl();
					BeanUtils.copyProperties(colenderDtlDto, colender);
					colender.setMasterAgr(master);
					colender.setUserId("INTERFACE");

					if (colenderDtlDto.getColenderShare().size() > 0) {
						for (ColenderIncShareBoardingDto colenderShareDto : colenderDtlDto.getColenderShare()) {
							AgrColenderIncShareDtl colenderShare = new AgrColenderIncShareDtl();
							BeanUtils.copyProperties(colenderShareDto, colenderShare);
							colenderShare.setColender(colender);
							colenderShare.setUserId("INTERFACE");
							colenderShareList.add(colenderShare);

						}
					}

					colenderList.add(colender);
				}

			}
			/*
			 * if (agreementBordingParam.getPdc() != null) { pdc = new AgrPdcSetup();
			 * BeanUtils.copyProperties(agreementBordingParam.getPdc(), pdc);
			 * pdc.setMasterAgr(master); pdc.setInstrumentStatus("REC");
			 * pdc.setUserId("INTERFACE");
			 * pdc.setDtInstrumentDate(sdf.parse(agreementBordingParam.getPdc().
			 * getDtInstrumentDate()));
			 * pdc.setDtReceipt(sdf.parse(agreementBordingParam.getPdc().getDtReceipt())); }
			 */

			if (agreementBordingParam.getEpay() != null) {
				epay = new AgrEpaySetup();
				BeanUtils.copyProperties(agreementBordingParam.getEpay(), epay);
				epay.setMastAgreement(master);
				epay.setUserId("INTERFACE");
				epay.setMandateStatus("RC");
				epay.setBankCode(agreementBordingParam.getEpay().getBankName());
				epay.setBankBranchCode(agreementBordingParam.getEpay().getBankBranchName());
				if (agreementBordingParam.getEpay().getDtFromDate() != null) {
					epay.setDtFromDate(sdf.parse(agreementBordingParam.getEpay().getDtFromDate()));
				}

				if (agreementBordingParam.getEpay().getDtToDate() != null) {
					epay.setDtToDate(sdf.parse(agreementBordingParam.getEpay().getDtToDate()));
				}
				epay.setMandateRefNo(agreementBordingParam.getEpay().getMandateRefNo());
				epay.setCustomer(borrowerCust);
				epay.setCustomerId(borrowerCust.getCustomerId());
			}
			masterRepo.save(master);
			custRepo.saveAll(customerList);
			addrRepo.saveAll(custAddList);
			feeRepo.saveAll(feeList);
			if (limit != null) {
				limitRepo.save(limit);
				detailRepo.saveAll(detailsList);// Added by DeepakG
			}

			prodRepo.save(prod);
			if (colenderList.size() > 0) {
				colenderRepo.saveAll(colenderList);
				if (colenderShareList != null) {
					colenderShareRepo.saveAll(colenderShareList);
				}
			}
			if (pdc != null) {
				pdcRepo.save(pdc);
			}
			if (epay != null) {
				epayRepo.save(epay);
			}

			// Added by AniketJ
			// Create AgrCollateralShares
			AgrCollateralSharesReqDto agrCollateralSharesReqDto = new AgrCollateralSharesReqDto();
			agrCollateralSharesReqDto.setMastAgrId(mastAgr);
			AgrCollateralShares agrCollateralShares = agrCollateralSharesService
					.saveAgrCollateralShares(agrCollateralSharesReqDto);
			log.info("agrCollateralShares===> " + agrCollateralShares);

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return mastAgr;
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public String agreementBoardingSF(AgreementBoardingSFDto agreementBordingParam) throws Exception {
		String result = "success";
		String mastAgr = null;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<AgrCustomer> customerList = new ArrayList<AgrCustomer>();
		List<AgrCustAddress> custAddList = new ArrayList<AgrCustAddress>();
		List<AgrFeeParam> feeList = new ArrayList<AgrFeeParam>();
		List<AgrColenderIncShareDtl> colenderShareList = new ArrayList<AgrColenderIncShareDtl>();
		List<AgrColenderDtl> colenderList = new ArrayList<AgrColenderDtl>();
		AgrEpaySetup epay = null;
		AgrCustomer borrowerCust = null;
		List<AgrCollateral> collateralList = new ArrayList<AgrCollateral>();
		List<AgrTrnTranDetail> detailsList = new ArrayList<AgrTrnTranDetail>();
		try {

			/*
			 * AgrMasterAgreement fourFinAgrNo = masterRepo
			 * .findByOriginationApplnNo(agreementBordingParam.getMasterAgreement().
			 * getOriginationApplnNo()); if (fourFinAgrNo != null) { throw new
			 * CoreBadRequestException(
			 * "Agreement is already available in system checked OriginationApplnNo"); }
			 */
			if (agreementBordingParam.getMasterAgreement() == null) {
				throw new CoreDataNotFoundException("Agreement details are not available.");
			}

			if (agreementBordingParam.getInvoiceDetails() == null) {
				throw new CoreDataNotFoundException("Invoice details are not available.");
			}

			if (agreementBordingParam.getMasterAgreement().getHomeState() == null
					|| agreementBordingParam.getMasterAgreement().getServState() == null
					|| agreementBordingParam.getMasterAgreement().getHomeState().equals("")
					|| agreementBordingParam.getMasterAgreement().getServState().equals("")) {
				throw new CoreDataNotFoundException("Home State or Servicing State is not available	");
			}

			if (agreementBordingParam.getProduct() == null) {
				throw new CoreDataNotFoundException("Product details are not available");
			}

			if (agreementBordingParam.getCustomerList().size() <= 0) {
				throw new CoreDataNotFoundException("Customer details are not available.");
			}

			mastAgr = agreementBordingParam.getMasterAgreement().getPortfolioCode().substring(0, 2)
					+ Long.toString((long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);

			AgrMasterAgreement master = new AgrMasterAgreement();
			BeanUtils.copyProperties(agreementBordingParam.getMasterAgreement(), master);
			master.setMastAgrId(mastAgr);
			master.setUserId("INTERFACE");
			master.setAgreementStatus("L");
			// Create Virtual Account Id
			master.setVirtualId(bankingUtil.createVirtualId(mastAgr));
			// Create VPA Account Id
			master.setVpaId(bankingUtil.createVPAId(mastAgr));

			List<AgrInvoiceDetails> invList = new ArrayList<>();
			for (AgrInvoiceDetailsDto invoice : agreementBordingParam.getInvoiceDetails()) {
				AgrInvoiceDetails inv = new AgrInvoiceDetails();
				BeanUtils.copyProperties(invoice, inv);
				inv.setMaster(master);
				inv.setDtInvoiceDate(sdf.parse(invoice.getDtInvoiceDate()));
				invList.add(inv);

			}

			for (CustomerBoardingDto customerDto : agreementBordingParam.getCustomerList()) {

				AgrCustomer customer = new AgrCustomer();
				BeanUtils.copyProperties(customerDto, customer);
				customer.setMasterAgr(master);
				customer.setPrefferedContactTimeFrom(customerDto.getContactTimeFrom());
				customer.setPrefferedContactTimeTo(customerDto.getContactTimeTo());
				customer.setUserId("INTERFACE");

				if (customerDto.getCustomerType().equalsIgnoreCase("B")) {
					borrowerCust = customer;
				}

				if (customerDto.getCustomerAddrList().size() <= 0) {
					throw new CoreDataNotFoundException(
							"Address details are not available for " + customerDto.getCustomerId());
				}
				int customerPrefferedCount = 0;
				for (CustomerAddressAgrDto customerAddrDto : customerDto.getCustomerAddrList()) {
					AgrCustAddress custAddr = new AgrCustAddress();
					BeanUtils.copyProperties(customerAddrDto, custAddr);
					custAddr.setCustomer(customer);
					custAddr.setUserId("INTERFACE");
					custAddr.setCustomerId(customer.getCustomerId());
					custAddList.add(custAddr);
					if (customerAddrDto.getPrefferedAddress().equals("Y")) {
						customerPrefferedCount++;
					}

				}

				if (customerPrefferedCount != 1) {
					throw new CoreDataNotFoundException("One customer address to be marked as Preferred Address");
				}

				customerList.add(customer);
			}

			// Supplier Finance Changes Start

			CustApplLimitSetup existingCustApplSetup = custApplRepo.findByOriginationApplnNoAndCustomerIdAndProductCode(
					master.getOriginationApplnNo(), borrowerCust.getCustomerId(),
					agreementBordingParam.getProduct().getProdCode());

			if (existingCustApplSetup == null) {
				throw new CoreDataNotFoundException(
						"Customer Application Limit setup is not available for available for "
								+ master.getOriginationApplnNo() + ", " + borrowerCust.getCustomerId() + ", "
								+ agreementBordingParam.getProduct().getProdCode());
			}

			// Supplier Finance Changes End

			for (FeeBoardingDto feeDto : agreementBordingParam.getFeeList()) {
				AgrFeeParam fee = new AgrFeeParam();
				BeanUtils.copyProperties(feeDto, fee);
				fee.setMasterAgreement(master);
				fee.setUserId("INTERFACE");
				feeList.add(fee);

			}

			AgrProduct prod = new AgrProduct();

			BeanUtils.copyProperties(agreementBordingParam.getProduct(), prod);
			prod.setMasterAgreement(master);
			prod.setUserId("INTERFACE");

			List<AgrProdSlabwiseInterest> slabwiseInterestList = new ArrayList<>();
			if (agreementBordingParam.getProduct().getSlabwiseInterest() != null) {
				for (SlabWiseInterestBoardingDto slabwiseInterestDto : agreementBordingParam.getProduct()
						.getSlabwiseInterest()) {
					AgrProdSlabwiseInterest slabwiseInterest = new AgrProdSlabwiseInterest();
					BeanUtils.copyProperties(slabwiseInterestDto, slabwiseInterest);
					slabwiseInterest.setProduct(prod);
					slabwiseInterestList.add(slabwiseInterest);
				}

			}
			int colenderPresenterCount = 0;
			if (agreementBordingParam.getColender() != null) {
				for (ColenderBoardingDto colenderDtlDto : agreementBordingParam.getColender()) {

					TabMstColender tabMstColender = tabMstColenderRepository
							.findByColenderId(Integer.parseInt(colenderDtlDto.getColenderCode()));

					if (tabMstColender == null) {
						throw new CoreDataNotFoundException("Colender not available.");
					}

					AgrColenderDtl colender = new AgrColenderDtl();
					BeanUtils.copyProperties(colenderDtlDto, colender);
					colender.setMasterAgr(master);
					colender.setUserId("INTERFACE");
					if (colenderDtlDto.getInstrumentPresenterYn().equals("Y")) {
						colenderPresenterCount++;
					}

					if (colenderPresenterCount != 1) {
						throw new CoreDataNotFoundException("Only one calendar can be marked as Instrument Presenter.");
					}

					if (colenderDtlDto.getColenderShare().size() > 0) {
						for (ColenderIncShareBoardingDto colenderShareDto : colenderDtlDto.getColenderShare()) {
							AgrColenderIncShareDtl colenderShare = new AgrColenderIncShareDtl();
							BeanUtils.copyProperties(colenderShareDto, colenderShare);
							colenderShare.setColender(colender);
							colenderShare.setUserId("INTERFACE");
							colenderShareList.add(colenderShare);

						}
					}

					colenderList.add(colender);
				}

			}
			/*
			 * if (agreementBordingParam.getPdc() != null) { pdc = new AgrPdcSetup();
			 * BeanUtils.copyProperties(agreementBordingParam.getPdc(), pdc);
			 * pdc.setMasterAgr(master); pdc.setInstrumentStatus("REC");
			 * pdc.setUserId("INTERFACE");
			 * pdc.setDtInstrumentDate(sdf.parse(agreementBordingParam.getPdc().
			 * getDtInstrumentDate()));
			 * pdc.setDtReceipt(sdf.parse(agreementBordingParam.getPdc().getDtReceipt())); }
			 */

			if (agreementBordingParam.getEpay() != null) {
				epay = new AgrEpaySetup();
				BeanUtils.copyProperties(agreementBordingParam.getEpay(), epay);
				epay.setMastAgreement(master);
				epay.setUserId("INTERFACE");
				epay.setMandateStatus("RC");
				epay.setBankCode(agreementBordingParam.getEpay().getBankName());
				epay.setBankBranchCode(agreementBordingParam.getEpay().getBankBranchName());
				epay.setDtFromDate(sdf.parse(agreementBordingParam.getEpay().getDtFromDate()));
				epay.setDtToDate(sdf.parse(agreementBordingParam.getEpay().getDtToDate()));
				epay.setMandateRefNo(agreementBordingParam.getEpay().getMandateRefNo());
				epay.setCustomer(borrowerCust);
				epay.setCustomerId(borrowerCust.getCustomerId());

				if (agreementBordingParam.getEpay().getMandateRefNo() == null) {
					throw new CoreDataNotFoundException(
							"Please specify UMRN(Unique Mandate Reference No) in EPay Setup.");
				}
			} else {
				throw new CoreDataNotFoundException("Please provide EPay Setup for case boarding.");
			}
			if (agreementBordingParam.getCollateral() != null) {
				for (CollateralBoardingDto collateralDto : agreementBordingParam.getCollateral()) {
					AgrCollateral collateral = new AgrCollateral();
					BeanUtils.copyProperties(collateralDto, collateral);
					collateral.setMastAgr(master);
					collateral.setUserId("INTERFACE");
					collateral.setDtValuation(sdf.parse(collateralDto.getDtValuation()));
					collateral.setDtCreation(sdf.parse(collateralDto.getDtCreation()));
					collateralList.add(collateral);
				}

			}
			AgrMasterAgreement save = masterRepo.save(master);
			custRepo.saveAll(customerList);
			addrRepo.saveAll(custAddList);
			feeRepo.saveAll(feeList);

			if (invList.size() > 0) {
				invoiceRepo.saveAll(invList);
			}

			prodRepo.save(prod);

			if (slabwiseInterestList.size() > 0) {
				for (AgrProdSlabwiseInterest slab : slabwiseInterestList) {
					slab.setMasterAgrId(save.getMastAgrId());
				}
				slabwiseRepo.saveAll(slabwiseInterestList);
			}

			if (colenderList.size() > 0) {
				colenderRepo.saveAll(colenderList);
				if (colenderShareList != null) {
					colenderShareRepo.saveAll(colenderShareList);
				}
			}

			if (epay != null) {
				epayRepo.save(epay);
			}
			if (collateralList.size() > 0) {
				collateralRepo.saveAll(collateralList);
			}

		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		return mastAgr;
	}

}
