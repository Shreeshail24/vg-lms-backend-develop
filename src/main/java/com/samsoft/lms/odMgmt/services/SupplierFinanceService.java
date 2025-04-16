package com.samsoft.lms.odMgmt.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.dto.CustApplicationProdLimitGetDto;
import com.samsoft.lms.core.dto.CustApplicationProdLimitSetDto;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.master.entity.Agency;
import com.samsoft.lms.master.service.AgencyService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierFinanceService {

    @Autowired
    private Environment env;

    @Autowired
    private CustApplLimitSetupRepository custLimitRepo;

    @Autowired
    private AgrMasterAgreementRepository masterAgreementRepository;

    @Autowired
    private AgrCustLimitSetupRepository agrCustLimitSetupRepository;

    @Autowired
    private AgencyService agencyService;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public Integer setCustApplicationProdLimit(CustApplicationProdLimitSetDto limitDto) throws Exception {
        String result = "";
        CustApplLimitSetup custLimitFinal = null;
        try {
            String dateFormat = env.getProperty("lms.global.date.format");
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            CustApplLimitSetup custApplLimit = custLimitRepo.findByOriginationApplnNoAndCustomerIdAndProductCode(
                    limitDto.getOriginationApplnNo(), limitDto.getCustomerId(), limitDto.getProductCode());

            if (custApplLimit != null) {
                throw new CoreDataNotFoundException(
                        "Customer Limit is already available for " + limitDto.getOriginationApplnNo() + ", "
                                + limitDto.getCustomerId() + ", " + limitDto.getProductCode());
            }

            Agency agency = new Agency();
            if(limitDto.getAgencyId() != null){
                agency = saveAgencyDetails(limitDto);
            }


            CustApplLimitSetup custLimit = new CustApplLimitSetup();
            BeanUtils.copyProperties(limitDto, custLimit);
            custLimit.setDtLimitSanctioned(sdf.parse(limitDto.getDtLimitSanctioned()));
            custLimit.setDtLimitExpired(sdf.parse(limitDto.getDtLimitExpired()));
            custLimit.setAvailableLimit(limitDto.getLimitSanctioned());
            if(limitDto.getAgencyId() != null && agency != null){
                log.info("agency not null ++++++++++++++++++:{}", agency);
                custLimit.setAgencyId(agency.getAgencyId());
            }
            custLimitFinal = custLimitRepo.save(custLimit);

        } catch (Exception e) {
            throw e;
        }

        return custLimitFinal == null ? 0 : custLimitFinal.getCustApplLimitId();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustApplicationProdLimitGetDto getCustApplicationProdLimit(Integer custApplLimitId) throws Exception {
        CustApplicationProdLimitGetDto result = new CustApplicationProdLimitGetDto();

        try {
            String dateFormat = env.getProperty("lms.global.date.format");
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            CustApplLimitSetup custLimit = custLimitRepo.findByCustApplLimitId(custApplLimitId);

            BeanUtils.copyProperties(custLimit, result);
            result.setDtLimitExpired(sdf.format(custLimit.getDtLimitExpired()));
            result.setDtLimitSanctioned(sdf.format(custLimit.getDtLimitSanctioned()));
            //result.setDtLastUpdated(sdf.format(custLimit.getDtLastUpdated()));
            //result.setDtUserDate(sdf.format(custLimit.getDtUserDate()));
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public String validateCustApplicationProdLimit(Integer custApplLimitId, Double amount) throws Exception {
        try {
            String dateFormat = env.getProperty("lms.global.date.format");
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            CustApplLimitSetup custLimit = custLimitRepo.findByCustApplLimitId(custApplLimitId);

            log.info("getUtilizedLimit " + custLimit.getUtilizedLimit());
            log.info("amount " + amount);
            log.info("getLimitSanctioned " + custLimit.getLimitSanctioned());
            if ((custLimit.getUtilizedLimit() + amount) <= custLimit.getLimitSanctioned()) {
                return "successs";
            } else {
                return "Sum of Utilized amount and Disbursed Amount is greater than Sanctioned Amount";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustApplicationProdLimitGetDto> getCustApplicationProdLimitByCustomerId(String customerId, String portfolioCode, String mastAgrId) throws Exception {
        List<CustApplicationProdLimitGetDto> custApplicationProdLimitGetDtoList = new ArrayList<CustApplicationProdLimitGetDto>();
        CustApplicationProdLimitGetDto result = null;
        try {
            String dateFormat = env.getProperty("lms.global.date.format");
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            if (portfolioCode.equalsIgnoreCase("SF")) {

                List<CustApplLimitSetup> custLimit = custLimitRepo.findByCustomerId(customerId);

                if (!custLimit.isEmpty()) {
                    for (CustApplLimitSetup obj : custLimit) {
                        result = new CustApplicationProdLimitGetDto();
                        BeanUtils.copyProperties(obj, result);
                        result.setDtLimitExpired(sdf.format(obj.getDtLimitExpired()));
                        result.setDtLimitSanctioned(sdf.format(obj.getDtLimitSanctioned()));
                        custApplicationProdLimitGetDtoList.add(result);
                    }
                }
            } else {
                AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(mastAgrId);

                if (agrCustLimitSetup != null) {
                    result = new CustApplicationProdLimitGetDto();
                    result.setCustApplLimitId(agrCustLimitSetup.getLimitId());
                    result.setLimitSanctioned(agrCustLimitSetup.getLimitSanctionAmount());
                    result.setUtilizedLimit(agrCustLimitSetup.getUtilizedLimit());
                    result.setAvailableLimit(agrCustLimitSetup.getAvailableLimit());
                    result.setDtLimitExpired(sdf.format(agrCustLimitSetup.getDtLimitExpired()));
                    result.setDtLimitSanctioned(sdf.format(agrCustLimitSetup.getDtLimitSanctioned()));
                    result.setPurpose(agrCustLimitSetup.getPurpose());
                    result.setUserId(agrCustLimitSetup.getUserId());

					AgrMasterAgreement agrMasterAgreement = masterAgreementRepository.findByMastAgrId(mastAgrId);

					if (agrMasterAgreement != null) {
						result.setOriginationApplnNo(agrMasterAgreement.getOriginationApplnNo());
						result.setCustomerId(agrMasterAgreement.getCustomerId());
					}

                    custApplicationProdLimitGetDtoList.add(result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return custApplicationProdLimitGetDtoList;
    }

    public Agency saveAgencyDetails(CustApplicationProdLimitSetDto limitDto) throws Exception{
        try{
            log.info(" saveAgencyDetails: {}", limitDto);
            Agency agency = new Agency(limitDto.getAgencyId(), limitDto.getAgencyName());
            try {
                return agencyService.saveAgency(agency);
            }catch(Exception e){
                e.printStackTrace();
                log.error(" agency: " + agency);
            }
        }catch (Exception e) {
            log.error("In method saveAgencyDetails: " + limitDto);
        }
        return null;
    }

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public List<CustApplicationProdLimitGetDto> getCustApplicationProdLimitByCustomerId(String customerId) throws Exception {
		List<CustApplicationProdLimitGetDto> custApplicationProdLimitGetDtoList = new ArrayList<CustApplicationProdLimitGetDto>();
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

			List<AgrMasterAgreement> agrMasterAgreementList = masterAgreementRepository.findAllByCustomerId(customerId);

			if(!agrMasterAgreementList.isEmpty()) {

				agrMasterAgreementList.forEach(agrMasterAgreement -> {

					if(agrMasterAgreement.getPortfolioCode().equalsIgnoreCase("SF")) {

						List<CustApplLimitSetup> custLimit = custLimitRepo.findByCustomerId(customerId);

						if(!custLimit.isEmpty()) {
							for (CustApplLimitSetup obj : custLimit) {
								CustApplicationProdLimitGetDto result = new CustApplicationProdLimitGetDto();
								BeanUtils.copyProperties(obj, result);
								result.setDtLimitExpired(sdf.format(obj.getDtLimitExpired()));
								result.setDtLimitSanctioned(sdf.format(obj.getDtLimitSanctioned()));
								custApplicationProdLimitGetDtoList.add(result);
							}
						}
					} else {
						AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(agrMasterAgreement.getMastAgrId());

						if(agrCustLimitSetup != null) {
							CustApplicationProdLimitGetDto result = new CustApplicationProdLimitGetDto();
							result.setCustApplLimitId(agrCustLimitSetup.getLimitId());
							result.setOriginationApplnNo(agrMasterAgreement.getOriginationApplnNo());
							result.setCustomerId(agrMasterAgreement.getCustomerId());
							result.setLimitSanctioned(agrCustLimitSetup.getLimitSanctionAmount());
							result.setUtilizedLimit(agrCustLimitSetup.getUtilizedLimit());
							result.setAvailableLimit(agrCustLimitSetup.getAvailableLimit());
							result.setDtLimitExpired(sdf.format(agrCustLimitSetup.getDtLimitExpired()));
							result.setDtLimitSanctioned(sdf.format(agrCustLimitSetup.getDtLimitSanctioned()));
							result.setPurpose(agrCustLimitSetup.getPurpose());
							result.setUserId(agrCustLimitSetup.getUserId());
							custApplicationProdLimitGetDtoList.add(result);
						}
					}

				});
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return custApplicationProdLimitGetDtoList;
	}


}
