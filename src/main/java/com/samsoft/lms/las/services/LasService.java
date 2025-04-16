package com.samsoft.lms.las.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.samsoft.lms.aws.dto.DocumentViewResDto;
import com.samsoft.lms.aws.service.AWSService;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.las.dto.request.*;
import com.samsoft.lms.las.dto.response.*;
import com.samsoft.lms.las.entities.AgrCollateralShares;
import com.samsoft.lms.las.entities.AuditAgrCollateralShares;
import com.samsoft.lms.las.entities.TabUploadExcel;
import com.samsoft.lms.las.entities.TabUploadLasTrail;
import com.samsoft.lms.las.repositories.AgrCollateralSharesRepository;
import com.samsoft.lms.las.util.FileUploadUtil;
import com.samsoft.lms.las.util.PoiExcelHelper;
import com.samsoft.lms.las.validator.FileUploadValidator;
import com.samsoft.lms.request.dto.DreAllocationDto;
import com.samsoft.lms.request.dto.DreRequestOnlineDto;
import com.samsoft.lms.request.services.DreRequestService;
import com.samsoft.lms.util.PlatformUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class LasService {

	@Autowired
	private FileUploadValidator fileUploadValidator;

	@Autowired
	private FileUploadUtil fileUploadUtil;

	@Autowired
	private AWSService awsService;

	@Autowired
	private PoiExcelHelper poiExcelHelper;

	@Autowired
	private TabUploadExcelService tabUploadExcelService;

	@Autowired
	private TabUploadLasTrailService tabUploadLasTrailService;

	@Autowired
	private AgrCollateralSharesService agrCollateralSharesService;

	@Autowired
	private AgrCustLimitSetupRepository agrCustLimitSetupRepository;

	@Autowired
	private AgrCollateralSharesRepository agrCollateralSharesRepository;

	@Autowired
	private PlatformUtils platFormUtils;

	@Autowired
	private DreRequestService dreRequestService;

	@Autowired
	private CommonServices commonServices;

	@Autowired
	private CustomerServices customerServices;

	@Autowired
	private AgrMasterAgreementRepository agrMasterAgreementRepository;

	@Autowired
	private AuditAgrCollateralSharesService auditAgrCollateralSharesService;

	@Transactional
	public Boolean uploadLasExcel(MultipartFile file, String userId) throws Exception {

		try {

			fileUploadValidator.validateFileUpload(file, userId);

			// Get Local File Path
			GetFilePathResDto getFilePathResDto = fileUploadUtil.getFilePath(file);

			// Upload File in S3 Bucket
			String s3Path = awsService.uploadToS3Bucket(file.getContentType(), getFilePathResDto.getLocalFilePath());

			// Read Excel
			List<Map<String, Object>> prepareExcelData = poiExcelHelper
					.readJExcel(getFilePathResDto.getLocalFilePath());
			awsService.deleteLocalFileAndMkdir(getFilePathResDto.getDestFolder());

			TabUploadExcelReqDto tabUploadExcelReqDto = new TabUploadExcelReqDto();
			tabUploadExcelReqDto.setFileName(getFilePathResDto.getLocalFilePath());
			tabUploadExcelReqDto.setFileUrl(s3Path);
			tabUploadExcelReqDto.setUserId(userId);
			TabUploadExcel tabUploadExcel = tabUploadExcelService.saveTabUploadExcel(tabUploadExcelReqDto);

			Map<String, Double> groupOfMasterAgrId = new TreeMap<>();

			// Validate Excel Data
			for (Map<String, Object> map : prepareExcelData) {
				TabUploadLasTrailReqDto tabUploadLasTrailReqDto = this.validateExcel(map);
				tabUploadLasTrailReqDto.setUploadLasId(tabUploadExcel.getUploadLasId());
				TabUploadLasTrail tabUploadLasTrail = tabUploadLasTrailService
						.saveTabUploadLasTrail(tabUploadLasTrailReqDto);
				log.info("uploadLasExcel===> tabUploadLasTrail: " + tabUploadLasTrail);

				if (!groupOfMasterAgrId.containsKey(tabUploadLasTrailReqDto.getMastAgrId())) {
					Double firstRowVal = tabUploadLasTrailReqDto.getPriceOfShare()
							* tabUploadLasTrailReqDto.getQuantityOfShare();
					groupOfMasterAgrId.put(tabUploadLasTrailReqDto.getMastAgrId(), firstRowVal);
				} else {
					Double price = groupOfMasterAgrId.get(tabUploadLasTrailReqDto.getMastAgrId());
					price += tabUploadLasTrailReqDto.getPriceOfShare() * tabUploadLasTrailReqDto.getQuantityOfShare();
					groupOfMasterAgrId.replace(tabUploadLasTrailReqDto.getMastAgrId(), price);
				}
			}

			log.info("groupOfMasterAgrIdmap groupOfMasterAgrIdmap groupOfMasterAgrIdmap"
					+ groupOfMasterAgrId.toString());
			// Get Calculate FMV & Drawing Power
			Boolean res = this.calculateFmvAndDrawingPower(groupOfMasterAgrId, tabUploadExcel.getUploadLasId());

			if (res) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: uploadLasExcel");
			log.error("Request: " + userId);
			log.error("Error: " + e);

			throw e;
		}

		return Boolean.FALSE;
	}

	private TabUploadLasTrailReqDto validateExcel(Map<String, Object> prepareExcelData) throws Exception {

		StringBuffer errorDesc = new StringBuffer();
		TabUploadLasTrailReqDto tabUploadLasTrailReqDto = new TabUploadLasTrailReqDto();

		try {

			for (Map.Entry<String, Object> entry : prepareExcelData.entrySet()) {

				Object tab = entry.getValue();

				switch (entry.getKey()) {
				case "Agreement No":
					if (tab == null || tab.toString().isEmpty()) {
						log.info("Loan Agreement Number is Mandatory");
						errorDesc.append("Loan Agreement Number is mandatory, ");
					} else {
						tabUploadLasTrailReqDto.setMastAgrId(entry.getValue().toString());
					}
					break;
				case "ISIN":
					if (tab == null || tab.toString().isEmpty()) {
						log.info("ISIN is Mandatory");
						errorDesc.append("ISIN is mandatory, ");
					} else {
						tabUploadLasTrailReqDto.setIsin(entry.getValue().toString());
					}
					break;
				case "Name Of Share":
					if (tab == null || tab.toString().isEmpty()) {
						log.info("Name Of Share is Mandatory");
						errorDesc.append("Name Of Share is mandatory, ");
					} else {
						tabUploadLasTrailReqDto.setNameOfShare(entry.getValue().toString());
					}
					break;
				case "Quantity Of Share":
					if (tab == null || tab.toString().isEmpty()) {
						log.info("Quantity Of Share is Mandatory");
						errorDesc.append("Quantity Of Share is mandatory, ");
					} else {
						tabUploadLasTrailReqDto.setQuantityOfShare(Integer.parseInt(entry.getValue().toString()));
					}
					break;
				case "Price Of Share":
					if (tab == null || tab.toString().isEmpty()) {
						log.info("Price Of Share is Mandatory");
						errorDesc.append("Price Of Share is mandatory, ");
					} else {
						tabUploadLasTrailReqDto.setPriceOfShare(Double.parseDouble(entry.getValue().toString()));
					}
					break;
				}
			}

			return tabUploadLasTrailReqDto;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: validateExcel");
			log.error("Request : " + prepareExcelData);
			log.error("Error: " + e);

			throw e;
		}
	}

	// Calculate FMV & Drawing Power
	@Transactional
	private Boolean calculateFmvAndDrawingPower(Map<String, Double> groupOfMasterAgrId, Integer uploadLasId)
			throws Exception {

		Double ltv = 0.0;
		Double dp = 0.0;
		Double drawingPower = 0.0;
		Double avlLimit = 0.0;
		boolean flag = true;
		int dpDiff = 0;

		try {

			for (Map.Entry<String, Double> entry : groupOfMasterAgrId.entrySet()) {

				// Get agrCustLimitSetup
				AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository
						.findByMasterAgreementMastAgrId(entry.getKey());
				log.info("calculateFmvAndDrawingPower===> agrCustLimitSetup: " + agrCustLimitSetup);
				if (agrCustLimitSetup != null) {
					ltv = agrCustLimitSetup.getLtv();
					avlLimit = agrCustLimitSetup.getAvailableLimit();
					log.info("Before avlLimit" + avlLimit);
				}

				// Calculate drawingPower
				Double fmv = Double.parseDouble(entry.getValue().toString());
				drawingPower = (fmv * ltv) / 100;
				log.info("Before drawingPower===> " + drawingPower);

				AgrCollateralShares agrCollateralShares = agrCollateralSharesRepository.findByMastAgrId(entry.getKey());
				if (agrCollateralShares != null && agrCollateralShares.getDrawingPower() != null) {
					log.info("Inside agrCollateralShares===> " + agrCollateralShares);
					flag = false;
					dp = agrCollateralShares.getDrawingPower();
					if (drawingPower > agrCustLimitSetup.getLimitSanctionAmount()) {
						// Trigger - can not exceed dp more then sanction limit

						avlLimit += agrCustLimitSetup.getLimitSanctionAmount() - dp;
						agrCustLimitSetup.setAvailableLimit(avlLimit);
						agrCustLimitSetupRepository.save(agrCustLimitSetup);
						drawingPower = agrCustLimitSetup.getLimitSanctionAmount();
					} else {
						log.info("dp===> " + dp);
						dpDiff = (int) (drawingPower - dp);
						log.info("dpDiff===> " + dpDiff);
						if (dpDiff > 0) {
							avlLimit += (drawingPower - dp);
							log.info("After avlLimit" + avlLimit);
							// Update Available Limit
							agrCustLimitSetup.setAvailableLimit(avlLimit);
							agrCustLimitSetupRepository.save(agrCustLimitSetup);
						} else {
							// Trigger Limit Exhaust
//                            platFormUtils.sendLasMail("aniket.jadhav@4fin.in", "Your Over Draft account Limit has been Overutilised, For adding balance kindly click on the Proceed Button", "Limit Over Utilised.", entry.getKey());

							log.info("Trigger Limit Exhaust");
							avlLimit += (double) dpDiff;
							agrCustLimitSetup.setAvailableLimit(avlLimit);
							agrCustLimitSetupRepository.save(agrCustLimitSetup);
						}
					}
				}

				if (flag) {
					if (drawingPower < agrCustLimitSetup.getLimitSanctionAmount()) {
						log.info("Inside flag After drawingPower===> " + drawingPower);
						agrCustLimitSetup.setAvailableLimit(drawingPower);
						agrCustLimitSetupRepository.save(agrCustLimitSetup);
					}
				}

				// Update AgrCollateralShares
				AgrCollateralSharesReqDto agrCollateralSharesReqDto = new AgrCollateralSharesReqDto();
				agrCollateralSharesReqDto.setMastAgrId(entry.getKey());
				agrCollateralSharesReqDto.setTrailDate(new Date());
				agrCollateralSharesReqDto.setFmv(fmv);
				agrCollateralSharesReqDto.setDrawingPower(drawingPower);
				if (agrCustLimitSetup != null) {
					agrCollateralSharesReqDto.setActualLtv((agrCustLimitSetup.getUtilizedLimit() / fmv) * 100);
				}
				agrCollateralSharesReqDto.setUploadLasId(uploadLasId);
				agrCollateralShares = agrCollateralSharesService.saveAgrCollateralShares(agrCollateralSharesReqDto);
				log.info("calculateFmvAndDrawingPower===> agrCollateralShares: " + agrCollateralShares);

				// Save AuditAgrCollateralShares
				AuditAgrCollateralSharesReqDto auditAgrCollateralSharesReqDto = new AuditAgrCollateralSharesReqDto();
				auditAgrCollateralSharesReqDto.setMastAgrId(entry.getKey());
				auditAgrCollateralSharesReqDto.setFmv(fmv);
				auditAgrCollateralSharesReqDto.setDrawingPower(drawingPower);
				if (agrCustLimitSetup != null && agrCollateralShares.getFmv() != null) {
					auditAgrCollateralSharesReqDto
							.setActualLtv((agrCustLimitSetup.getUtilizedLimit() / agrCollateralShares.getFmv()) * 100);
				}
				auditAgrCollateralSharesReqDto.setTrailDate(new Date());
				auditAgrCollateralSharesReqDto.setUploadLasId(uploadLasId);
				AuditAgrCollateralShares auditAgrCollateralShares = auditAgrCollateralSharesService
						.saveAuditAgrCollateralShares(auditAgrCollateralSharesReqDto);
				log.info("calculateFmvAndDrawingPower===> auditAgrCollateralShares: " + auditAgrCollateralShares);

				if (agrCollateralShares.getActualLtv() > 50) {
					platFormUtils.sendLasMail("aniket.jadhav@4fin.in",
							"Your Over Draft account Limit has been Overutilised, For adding balance kindly click on the Proceed Button",
							"Limit Over Utilised.", entry.getKey());
					log.info("Trigger Actual LTV > 50");
				}
			}

			return Boolean.TRUE;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: calculateFmvAndDrawingPower");
			log.error("Request : " + groupOfMasterAgrId + " uploadLasId: " + uploadLasId);
			log.error("Error: " + e);

			throw e;
		}

	}

	public Boolean updateLas(String mastAgrId, Double amount, String type) throws Exception {

//        Double avlLimit = 0.0;
//        Double utilizedAmt = 0.0;
		try {

//            //Get agrCollateralShares
//            AgrCollateralShares agrCollateralShares = agrCollateralSharesRepository.findByMastAgrId(mastAgrId);
//            log.info("updateLas===> agrCollateralShares: " + agrCollateralShares);
//            //Get agrCustLimitSetup
//            AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(mastAgrId);
//            log.info("updateLas===> agrCustLimitSetup: " + agrCustLimitSetup);
//            if(agrCustLimitSetup != null) {
//
//                //Utilized amount - deposit amount
//                utilizedAmt = agrCustLimitSetup.getUtilizedLimit() - amount;
//                log.info("Before avlLimit" + utilizedAmt);
//                //dp - Utilized amount
//                avlLimit = agrCollateralShares.getDrawingPower() - utilizedAmt;
//                log.info("Before avlLimit" + avlLimit);
//
//                agrCustLimitSetup.setAvailableLimit(avlLimit);
//                agrCustLimitSetup.setUtilizedLimit(utilizedAmt);
//                agrCustLimitSetupRepository.save(agrCustLimitSetup);
//
//                return Boolean.TRUE;
//            }

			if (type.equals("LiquidationOfShares")) {
				amount = amount * -1;
			}

			DreRequestOnlineDto dreOnlineDto = new DreRequestOnlineDto();
			dreOnlineDto.setMasterAgreementId(mastAgrId);
			dreOnlineDto.setInstrumentDate(commonServices.getBusinessDateInString());
			dreOnlineDto.setRequestDate(commonServices.getBusinessDateInString());
			dreOnlineDto.setPaymentType("INSTALLMENT");
			dreOnlineDto.setPaymentMode("DRE");
			dreOnlineDto.setInstrumentType("CA");
			dreOnlineDto.setReason("CP");
			dreOnlineDto.setRemark("CP");
			dreOnlineDto.setFlowType("NF");
			dreOnlineDto.setDepositRefNo("12345");
			dreOnlineDto.setInstrumentAmount(amount);
			dreOnlineDto.setProvisionalReceiptFlag("1234");
			dreOnlineDto.setRequestStatus("APR");
			dreOnlineDto.setUserId("UserId");
			dreOnlineDto.setUtrNo("UTR0123123");

			List<DreAllocationDto> dreAllocation = new ArrayList<>();
			DreAllocationDto dreAllocationDto = new DreAllocationDto();
			dreAllocationDto.setAllocatedAmount(0.0);
			dreAllocationDto.setChargeBookTranId(0);
			dreAllocationDto.setInstallmentNo(0);
			dreAllocationDto.setLoanId("");
			dreAllocationDto.setTranCategory("");
			dreAllocationDto.setTranHead("");
			dreAllocation.add(dreAllocationDto);
			dreOnlineDto.setDreAllocation(dreAllocation);

			String res = dreRequestService.dreRequestOnline(dreOnlineDto);
			log.info("Response===> " + res);

			return Boolean.TRUE;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: updateLas");
			log.error("Request : " + mastAgrId);
			log.error("Error: " + e);

			throw e;
		}

	}

//    private DocumentViewResDto downloadLasExcel(String mastAgrId) throws Exception {
//        DocumentViewResDto documentViewResDto = null;
//        try {
//
//            AgrCollateralShares agrCollateralShares = agrCollateralSharesService.getAgrCollateralSharesByMastAgrId(mastAgrId);
//            log.info("agrCollateralShares===> " + agrCollateralShares);
//            if(agrCollateralShares != null) {
//                TabUploadExcel tabUploadExcel = tabUploadExcelService.getTabUploadExcelByUploadLasId(agrCollateralShares.getUploadLasId());
//                log.info("tabUploadExcel===> " + tabUploadExcel);
//                if(tabUploadExcel != null) {
//                    documentViewResDto = awsService.documentView(tabUploadExcel.getFileUrl());
//                    log.info("documentViewResDto===>" + documentViewResDto);
//                }
//            }
//
//            return documentViewResDto;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("Method: downloadLasExcel");
//            log.error("Request : " + mastAgrId);
//            log.error("Error: " + e);
//
//            throw e;
//        }
//    }

	public LatestValuationSharesResDto latestValuationShares(String mastAgrId) throws Exception {
		LatestValuationSharesResDto latestValuationSharesResDto = new LatestValuationSharesResDto();

		try {

			AgrCollateralShares agrCollateralShares = agrCollateralSharesService
					.getAgrCollateralSharesByMastAgrId(mastAgrId);
			log.info("agrCollateralShares===> " + agrCollateralShares);
			if (agrCollateralShares != null) {
				TabUploadExcel tabUploadExcel = tabUploadExcelService
						.getTabUploadExcelByUploadLasId(agrCollateralShares.getUploadLasId());
				log.info("tabUploadExcel===> " + tabUploadExcel);
				if (tabUploadExcel != null) {
					List<TabUploadLasTrail> tabUploadLasTrails = tabUploadLasTrailService
							.getTabUploadLasTrail(tabUploadExcel.getUploadLasId(), agrCollateralShares.getMastAgrId());
					if (!tabUploadLasTrails.isEmpty()) {
						latestValuationSharesResDto.setUploadLasTrails(tabUploadLasTrails);
					}
				}

				latestValuationSharesResDto.setMastAgrId(mastAgrId);
				latestValuationSharesResDto.setFmv(agrCollateralShares.getFmv());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: latestValuationShares");
			log.error("Request : " + mastAgrId);
			log.error("Error: " + e);

			throw e;
		}

		return latestValuationSharesResDto;
	}

	public GetLasDetailsResDto getLasDetails(String mastAgrId) throws Exception {

		GetLasDetailsResDto getLasDetailsResDto = new GetLasDetailsResDto();

		try {
			AgrCollateralShares agrCollateralShares = agrCollateralSharesService
					.getAgrCollateralSharesByMastAgrId(mastAgrId);
			log.info("getLasDetails===> agrCollateralShares: " + agrCollateralShares);

			if (agrCollateralShares != null) {

				getLasDetailsResDto.setMastAgrId(mastAgrId);
				getLasDetailsResDto.setFmv(agrCollateralShares.getFmv());
				getLasDetailsResDto.setDrawingPower(agrCollateralShares.getDrawingPower());
				getLasDetailsResDto.setActualLtv(agrCollateralShares.getActualLtv());

				AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository
						.findByMasterAgreementMastAgrId(mastAgrId);
				log.info("getLasDetails===> agrCustLimitSetup: " + agrCustLimitSetup);

				if (agrCustLimitSetup != null) {

					getLasDetailsResDto.setUtilizedLimit(agrCustLimitSetup.getUtilizedLimit());
					getLasDetailsResDto.setAvailableLimit(agrCustLimitSetup.getAvailableLimit());
					getLasDetailsResDto.setLimitSanctionAmount(agrCustLimitSetup.getLimitSanctionAmount());
					getLasDetailsResDto.setLtv(agrCustLimitSetup.getLtv());

					AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByMastAgrId(mastAgrId);
					log.info("getLasDetails===> agrMasterAgreement: " + agrMasterAgreement);
					if (agrMasterAgreement != null) {
						CustomerBasicInfoDto customerBasicInfoDto = customerServices
								.getCustomerByCustomerId(agrMasterAgreement.getCustomerId());
						log.info("getLasDetails===> customerBasicInfoDto: " + customerBasicInfoDto);
						if (customerBasicInfoDto != null) {
							getLasDetailsResDto.setCustomerName(customerBasicInfoDto.getFirstName() + " "
									+ customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());
						}
					}

				}

				// Get Business Date
				getLasDetailsResDto.setBusinessDate(commonServices.getBusinessDateInString());
			}

			return getLasDetailsResDto;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: getLasDetails");
			log.error("Request : " + mastAgrId);
			log.error("Error: " + e);

			throw e;
		}

	}

	public GetLasPortfolioSummaryResDto getLasPortfolioSummary() throws Exception {

		GetLasPortfolioSummaryResDto getLasPortfolioSummaryResDto = new GetLasPortfolioSummaryResDto();
		List<LasPortfolioDetailsResDto> lasPortfolioDetailsList = new ArrayList<>();
		LasPortfolioDetailsResDto lasPortfolioDetailsResDto = null;
		Double totalSanctionLimit = 0.0;
		Double totalUtilizedLimit = 0.0;
		Double totalValuation = 0.0;

		try {

			// Get Master Agreement List by OD
			List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository
					.findByPortfolioCodeInOrderByDtUserDateDesc(Arrays.asList(new String[] { "OD" }));

			// Add total no of Loans
			getLasPortfolioSummaryResDto.setTotalNoOfLoans(agrMasterAgreementList.size());

			if (!agrMasterAgreementList.isEmpty()) {
				for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
					lasPortfolioDetailsResDto = new LasPortfolioDetailsResDto();

					if (agrMasterAgreement.getCustomerId() != null) {
						CustomerBasicInfoDto customerBasicInfoDto = customerServices
								.getCustomerByCustomerId(agrMasterAgreement.getCustomerId());
						log.info("getLasPortfolioSummary===> customerBasicInfoDto: " + customerBasicInfoDto);
						if (customerBasicInfoDto != null) {

							// Add Customer Name
							lasPortfolioDetailsResDto.setCustomerName(customerBasicInfoDto.getFirstName() + " "
									+ customerBasicInfoDto.getMiddleName() + " " + customerBasicInfoDto.getLastName());

							// Add Home Branch
							lasPortfolioDetailsResDto.setHomeBranch(customerBasicInfoDto.getHomeBranch());
						}
					}

					// Add Master Agreement Id
					lasPortfolioDetailsResDto.setMasterAgrId(agrMasterAgreement.getMastAgrId());

					// Add Customer Id
					lasPortfolioDetailsResDto.setCustomerId(agrMasterAgreement.getCustomerId());

					// Get AgrCustLimitSetup Data
					AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository
							.findByMasterAgreementMastAgrId(agrMasterAgreement.getMastAgrId());
					log.info("getLasPortfolioSummary===> agrCustLimitSetup: " + agrCustLimitSetup);
					if (agrCustLimitSetup != null) {

						// Calculate Sanction Limit
						totalSanctionLimit += agrCustLimitSetup.getLimitSanctionAmount() != null
								? agrCustLimitSetup.getLimitSanctionAmount()
								: 0;

						// Add Sanction Limit
						lasPortfolioDetailsResDto.setSanctionLimit(agrCustLimitSetup.getLimitSanctionAmount() != null
								? agrCustLimitSetup.getLimitSanctionAmount()
								: 0);

						// calculate Utilized Limit
						totalUtilizedLimit += agrCustLimitSetup.getUtilizedLimit() != null
								? agrCustLimitSetup.getUtilizedLimit()
								: 0;

						// Add Utilized Limit
						lasPortfolioDetailsResDto.setUtilizedLimit(
								agrCustLimitSetup.getUtilizedLimit() != null ? agrCustLimitSetup.getUtilizedLimit()
										: 0);
					}

					// Get AgrCollateralShares Data
					AgrCollateralShares agrCollateralShares = agrCollateralSharesService
							.getAgrCollateralSharesByMastAgrId(agrMasterAgreement.getMastAgrId());
					log.info("getLasDetails===> agrCollateralShares: " + agrCollateralShares);
					if (agrCollateralShares != null) {

						// calculate total valuation
						totalValuation += agrCollateralShares.getFmv() != null ? agrCollateralShares.getFmv() : 0;

						// Add FMV
						lasPortfolioDetailsResDto
								.setFmv(agrCollateralShares.getFmv() != null ? agrCollateralShares.getFmv() : 0);

						// Add Actual Ltv
						lasPortfolioDetailsResDto.setActualLtv(
								agrCollateralShares.getActualLtv() != null ? agrCollateralShares.getActualLtv() : 0);

					}

					// Add lasPortfolioDetailsResDto Data in list
					lasPortfolioDetailsList.add(lasPortfolioDetailsResDto);
				}

				// Add total Sanction limit
				getLasPortfolioSummaryResDto.setTotalSanctionLimit(totalSanctionLimit);

				// Add total Utilized Limit
				getLasPortfolioSummaryResDto.setTotalUtilizedLimit(totalUtilizedLimit);

				// Add total Valuation
				getLasPortfolioSummaryResDto.setTotalValuation(totalValuation);

				// Add Portfolio Ltv
				getLasPortfolioSummaryResDto.setPortfolioLtv((totalUtilizedLimit / totalValuation) * 100);

				// Add List in getLasPortfolioSummaryResDto
				getLasPortfolioSummaryResDto.setLasPortfolioDetails(lasPortfolioDetailsList);

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: getLasPortfolioSummary");
			log.error("Error: " + e);

			throw e;
		}

		return getLasPortfolioSummaryResDto;
	}

	public List<UploadLasTrailResDto> getLatestLasUploadExcelTrail() throws Exception {

		List<UploadLasTrailResDto> uploadLasTrailResDtoList = new ArrayList<>();
		UploadLasTrailResDto uploadLasTrailResDto = null;
		try {

			// Get Master Agreement List by OD
			List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository
					.findByPortfolioCodeInOrderByDtUserDateDesc(Arrays.asList(new String[] { "OD" }));

			if (!agrMasterAgreementList.isEmpty()) {
				for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {

					// Get AgrCollateralShares Data
					AgrCollateralShares agrCollateralShares = agrCollateralSharesService
							.getAgrCollateralSharesByMastAgrId(agrMasterAgreement.getMastAgrId());
					log.info("getLatestLasUploadExcelTrail===> agrCollateralShares: " + agrCollateralShares);
					if (agrCollateralShares != null) {

						// getTabUploadLasTrail Data
						List<TabUploadLasTrail> tabUploadLasTrailList = tabUploadLasTrailService.getTabUploadLasTrail(
								agrCollateralShares.getUploadLasId(), agrMasterAgreement.getMastAgrId());
						log.info("getLatestLasUploadExcelTrail===> tabUploadLasTrailList: " + tabUploadLasTrailList);
						if (!tabUploadLasTrailList.isEmpty()) {
							for (TabUploadLasTrail tabUploadLasTrail : tabUploadLasTrailList) {
								uploadLasTrailResDto = new UploadLasTrailResDto();
								uploadLasTrailResDto.setMastAgrId(tabUploadLasTrail.getMastAgrId());
								uploadLasTrailResDto.setIsin(tabUploadLasTrail.getIsin());
								uploadLasTrailResDto.setNameOfShare(tabUploadLasTrail.getNameOfShare());
								uploadLasTrailResDto.setPriceOfShare(tabUploadLasTrail.getPriceOfShare());
								uploadLasTrailResDto.setQuantityOfShare(tabUploadLasTrail.getQuantityOfShare());
								uploadLasTrailResDtoList.add(uploadLasTrailResDto);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method: getLatestLasUploadExcelTrail");
			log.error("Error: " + e);

			throw e;
		}

		return uploadLasTrailResDtoList;
	}

}
