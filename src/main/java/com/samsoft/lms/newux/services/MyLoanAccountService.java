package com.samsoft.lms.newux.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.dto.CustomerAgreementDetailsResDto;
import com.samsoft.lms.agreement.entities.AgrEpaySetup;
import com.samsoft.lms.agreement.entities.AgrInvoiceDetails;
import com.samsoft.lms.agreement.repositories.AgrEpaySetupRepository;
import com.samsoft.lms.agreement.services.AgrInvoiceDetailsService;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.entities.AgrCustLimitSetup;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.core.exceptions.ApiException;
import com.samsoft.lms.core.repositories.AgrCustLimitSetupRepository;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.CustApplLimitSetupRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.newux.dto.response.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@Slf4j
public class MyLoanAccountService {

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    @Autowired
    private CustApplLimitSetupRepository custApplLimitSetupRepository;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private AgrLoansRepository loanRepo;

    @Autowired
    private AgrCustLimitSetupRepository agrCustLimitSetupRepository;

    @Autowired
    private CommonServices commonService;

    @Autowired
    private LoanDetailsService loanDetailsService;

    @Autowired
    private AgrInvoiceDetailsService agrInvoiceDetailsService;

    @Autowired
    private AgrEpaySetupRepository agrEpaySetupRepository;

    @Cacheable
    public List<GetNonODAccountDetailsResDto> getNonODAccountDetails(String customerId) throws Exception {

        List<GetNonODAccountDetailsResDto> getNonODAccountDetailsResDtoList = new ArrayList<>();
        GetNonODAccountDetailsResDto getNonODAccountDetailsResDto = null;
        try {

            if (customerId == null) {
                throw new ApiException("Required Customer Id.", HttpStatus.NOT_FOUND);
            }

            //Get Agr Master Agreement Details
            List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository.findByCustomerIdAndPortfolioCodeIn(customerId, Arrays.asList("BL", "AU", "SB"));
            log.info("MyLoanAccountService :: getNonODAccountDetails :: agrMasterAgreement: {}", agrMasterAgreementList);

            for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
                getNonODAccountDetailsResDto = new GetNonODAccountDetailsResDto();
                if (agrMasterAgreement != null) {

                    getNonODAccountDetailsResDto.setLoanApplicationNo(agrMasterAgreement.getOriginationApplnNo());
                    getNonODAccountDetailsResDto.setSanctionAmount(agrMasterAgreement.getSanctionedAmount());
                    getNonODAccountDetailsResDto.setEmiAmount(agrMasterAgreement.getNextInstallmentAmount());
                    getNonODAccountDetailsResDto.setLoanStatus(agrMasterAgreement.getDpd() > 0 ? "Overdue" : "Regular");
                    getNonODAccountDetailsResDto.setPortfolioCode(agrMasterAgreement.getPortfolioCode());
                    getNonODAccountDetailsResDto.setCustomerId(agrMasterAgreement.getCustomerId());
                    getNonODAccountDetailsResDto.setMastAgrId(agrMasterAgreement.getMastAgrId());
                    getNonODAccountDetailsResDto.setVirtualId(agrMasterAgreement.getVirtualId());
                    getNonODAccountDetailsResDto.setVpaId(agrMasterAgreement.getVpaId());

                    AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(agrMasterAgreement.getMastAgrId());

                    if (agrCustLimitSetup != null) {
                        getNonODAccountDetailsResDto.setLimitId(agrCustLimitSetup.getLimitId());
                    }

                    GetCustomerDetailsResDto getCustomerDetailsResDto = customerDetailsService.getCustomerDetails(agrMasterAgreement.getMastAgrId(), agrMasterAgreement.getCustomerId());
                    log.info("MyLoanAccountService :: getNonODAccountDetails :: getCustomerDetailsResDto: {}", getCustomerDetailsResDto);

                    if(getCustomerDetailsResDto != null) {
                        getNonODAccountDetailsResDto.setCustomerName(getCustomerDetailsResDto.getCustomerName());
                        getNonODAccountDetailsResDto.setMobile(getCustomerDetailsResDto.getMobile());
                        getNonODAccountDetailsResDto.setEmail(getCustomerDetailsResDto.getEmail());

                    }

                    getNonODAccountDetailsResDtoList.add(getNonODAccountDetailsResDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("MyLoanAccountService :: getNonODAccountDetails :: Method: getMyAccountDetails");
            log.error("MyLoanAccountService :: getNonODAccountDetails :: Request: {}", customerId);
            log.error("MyLoanAccountService :: getNonODAccountDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return getNonODAccountDetailsResDtoList;
    }

    @Cacheable
    public List<GetODAccountDetailsResDto> getODAccountDetails(String customerId) throws Exception {

        List<GetODAccountDetailsResDto> getODAccountDetailsResDtoList = new ArrayList<>();
        GetODAccountDetailsResDto getODAccountDetailsResDto = null;
        try {

            if (customerId == null) {
                throw new ApiException("Required Customer Id.", HttpStatus.NOT_FOUND);
            }

            log.info("Customerid : {}", customerId);
            //Get Agr Master Agreement Details
            List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository.findByCustomerIdAndPortfolioCodeIn(customerId, Arrays.asList("OD"));
            log.info("MyLoanAccountService :: getODAccountDetails :: agrMasterAgreement: {}", agrMasterAgreementList);

            if (!agrMasterAgreementList.isEmpty()) {
                for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
                    getODAccountDetailsResDto = new GetODAccountDetailsResDto();

                    getODAccountDetailsResDto.setLoanApplicationNo(agrMasterAgreement.getOriginationApplnNo());
                    //Get Agr Cust Limit Setup
                    AgrCustLimitSetup agrCustLimitSetup = agrCustLimitSetupRepository.findByMasterAgreementMastAgrId(agrMasterAgreement.getMastAgrId());

                    if (agrCustLimitSetup != null) {

                        getODAccountDetailsResDto.setSanctionedLimit(agrCustLimitSetup.getLimitSanctionAmount());
                        getODAccountDetailsResDto.setAvailableLimit(agrCustLimitSetup.getAvailableLimit());
                        getODAccountDetailsResDto.setUtilizedLimit(agrCustLimitSetup.getUtilizedLimit());
                        getODAccountDetailsResDto.setLimitId(agrCustLimitSetup.getLimitId());
                    }

                    getODAccountDetailsResDto.setOverdueAmount(commonService.getMasterTotalDues(agrMasterAgreement.getMastAgrId()));
                    getODAccountDetailsResDto.setLoanStatus(agrMasterAgreement.getDpd() > 0 ? "Overdue" : "Regular");
                    getODAccountDetailsResDto.setPortfolioCode(agrMasterAgreement.getPortfolioCode());
                    getODAccountDetailsResDto.setCustomerId(agrMasterAgreement.getCustomerId());
                    getODAccountDetailsResDto.setMastAgrId(agrMasterAgreement.getMastAgrId());
                    getODAccountDetailsResDto.setVirtualId(agrMasterAgreement.getVirtualId());
                    getODAccountDetailsResDto.setVpaId(agrMasterAgreement.getVpaId());

                    GetCustomerDetailsResDto getCustomerDetailsResDto = customerDetailsService.getCustomerDetails(agrMasterAgreement.getMastAgrId(), agrMasterAgreement.getCustomerId());
                    log.info("MyLoanAccountService :: getNonODAccountDetails :: getCustomerDetailsResDto: {}", getCustomerDetailsResDto);

                    if(getCustomerDetailsResDto != null) {
                        getODAccountDetailsResDto.setCustomerName(getCustomerDetailsResDto.getCustomerName());
                        getODAccountDetailsResDto.setMobile(getCustomerDetailsResDto.getMobile());
                        getODAccountDetailsResDto.setEmail(getCustomerDetailsResDto.getEmail());

                    }

                    getODAccountDetailsResDtoList.add(getODAccountDetailsResDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("MyLoanAccountService :: getODAccountDetails :: Method: getODAccountDetails");
            log.error("MyLoanAccountService :: getODAccountDetails :: Request: {}", customerId);
            log.error("MyLoanAccountService :: getODAccountDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return getODAccountDetailsResDtoList;
    }

    @Cacheable
    public List<GetSFAccountDetailsResDto> getSFAccountDetails(String customerId) throws Exception {

        List<GetSFAccountDetailsResDto> getSFAccountDetailsResDtoList = new ArrayList<>();
        GetSFAccountDetailsResDto getSFAccountDetailsResDto = null;
        try {
            if (customerId == null) {
                throw new ApiException("Required Customer Id.", HttpStatus.NOT_FOUND);
            }

            //Get Cust Appl Limit Setup Repository
            List<CustApplLimitSetup> custApplLimitSetupList = custApplLimitSetupRepository.findByCustomerId(customerId);
            log.info("MyLoanAccountService :: getSFAccountDetails :: custApplLimitSetupList: {}", custApplLimitSetupList);

            if (!custApplLimitSetupList.isEmpty()) {
                for (CustApplLimitSetup custApplLimitSetup : custApplLimitSetupList) {

                            getSFAccountDetailsResDto = new GetSFAccountDetailsResDto();

                            getSFAccountDetailsResDto.setSanctionedLimit(custApplLimitSetup.getLimitSanctioned());
                            getSFAccountDetailsResDto.setAvailableLimit(custApplLimitSetup.getAvailableLimit());
                            getSFAccountDetailsResDto.setUtilizedLimit(custApplLimitSetup.getUtilizedLimit());
                            getSFAccountDetailsResDto.setLoanApplicationNo(custApplLimitSetup.getOriginationApplnNo());
                            // TODO: 04/04/23 Overdueamout will set to limitlevel, For now it will be set as O
                            getSFAccountDetailsResDto.setOverdueAmount(0.0);
                            getSFAccountDetailsResDto.setLoanStatus("Regular");
                            getSFAccountDetailsResDto.setPortfolioCode("SF");
                            getSFAccountDetailsResDto.setCustomerId(customerId);
                            getSFAccountDetailsResDto.setCustomerName(custApplLimitSetup.getFullName());
                            getSFAccountDetailsResDto.setLimitId(custApplLimitSetup.getCustApplLimitId());
                            getSFAccountDetailsResDtoList.add(getSFAccountDetailsResDto);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("MyLoanAccountService :: getODAccountDetails :: Method: getODAccountDetails");
            log.error("MyLoanAccountService :: getODAccountDetails :: Request: {}", customerId);
            log.error("MyLoanAccountService :: getODAccountDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return getSFAccountDetailsResDtoList;
    }

    public GetNonODDetails getNonODDetails(String mastAgrId) throws Exception {

        GetNonODDetails getNonODDetails = new GetNonODDetails();
        try {

            if (mastAgrId == null) {
                throw new ApiException("Required Master Agreement Number.", HttpStatus.NOT_FOUND);
            }

            //Get Agr Master Agreement Details
            AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByMastAgrId(mastAgrId);
            log.info("MyLoanAccountService :: getNonODDetails :: agrMasterAgreement: {}", agrMasterAgreement);

            if (agrMasterAgreement != null) {

                getNonODDetails.setLoanApplicationNo(agrMasterAgreement.getOriginationApplnNo());
                getNonODDetails.setSanctionedAmount(agrMasterAgreement.getSanctionedAmount());

                CustomerAgreementDetailsResDto customerAgreementDetailsResDto = agreementService.getCustomerAgreementDetails(mastAgrId);
                log.info("MyLoanAccountService :: getNonODDetails :: customerAgreementDetailsResDto: {}", customerAgreementDetailsResDto);

                if (customerAgreementDetailsResDto != null) {

                    getNonODDetails.setCustomerName(customerAgreementDetailsResDto.getCustomerName());
                    getNonODDetails.setTotalTenure(customerAgreementDetailsResDto.getTotalTenor());
                    getNonODDetails.setBalanceTenure(customerAgreementDetailsResDto.getBalanceTenor());
                    getNonODDetails.setEmiDate(customerAgreementDetailsResDto.getDtNextInstallment());
                }

                GetLoanOverviewResDto getLoanOverviewResDto = loanDetailsService.getLoanOverview(mastAgrId);
                log.info("MyLoanAccountService :: getNonODDetails :: getLoanOverviewResDto: {}", getLoanOverviewResDto);

                if (getLoanOverviewResDto != null) {
                    getNonODDetails.setEmiAmount(getLoanOverviewResDto.getAgreementInfo().getOverdueAmount());
                }


                List<AgrLoans> loanDetails = loanRepo.findByMasterAgreementMastAgrId(mastAgrId);
                log.info("MyLoanAccountService :: getNonODDetails :: loanDetails: {}", loanDetails);

                if (!loanDetails.isEmpty()) {
                    getNonODDetails.setRoi(loanDetails.get(0).getInterestRate());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("MyLoanAccountService :: getNonODDetails :: Method: getNonODDetails");
            log.error("MyLoanAccountService :: getNonODDetails :: Request: {}", mastAgrId);
            log.error("MyLoanAccountService :: getNonODDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return getNonODDetails;
    }

    public GetODAndSFDetails getODAndSFDetails(String mastAgrId) throws Exception {

        GetODAndSFDetails getODAndSFDetails = new GetODAndSFDetails();
        try {

            if (mastAgrId == null) {
                throw new ApiException("Required Master Agreement Number.", HttpStatus.NOT_FOUND);
            }

            //Get Agr Master Agreement Details
            AgrMasterAgreement agrMasterAgreement = agrMasterAgreementRepository.findByMastAgrId(mastAgrId);
            log.info("MyLoanAccountService :: getODAndSFDetails :: agrMasterAgreement: {}", agrMasterAgreement);

            if (agrMasterAgreement != null) {

                getODAndSFDetails.setLoanApplicationNo(agrMasterAgreement.getOriginationApplnNo());

                CustomerAgreementDetailsResDto customerAgreementDetailsResDto = agreementService.getCustomerAgreementDetails(mastAgrId);
                log.info("MyLoanAccountService :: getODAndSFDetails :: customerAgreementDetailsResDto: {}", customerAgreementDetailsResDto);

                if (customerAgreementDetailsResDto != null) {

                    getODAndSFDetails.setCustomerName(customerAgreementDetailsResDto.getCustomerName());
                }

                GetLoanOverviewResDto getLoanOverviewResDto = loanDetailsService.getLoanOverview(mastAgrId);
                log.info("MyLoanAccountService :: getODAndSFDetails :: getLoanOverviewResDto: {}", getLoanOverviewResDto);

                if (getLoanOverviewResDto != null) {
                    getODAndSFDetails.setOverdueAmount(getLoanOverviewResDto.getAgreementInfo().getOverdueAmount());
                    getODAndSFDetails.setSanctionedLimit(getLoanOverviewResDto.getAgreementInfo().getSanctionedLimit());
                    getODAndSFDetails.setUtilizedLimit(getLoanOverviewResDto.getAgreementInfo().getUtilizedLimit());
                    getODAndSFDetails.setAvailableLimit(getLoanOverviewResDto.getAgreementInfo().getAvailableLimit());
                }

                getODAndSFDetails.setRoi(agrMasterAgreement.getInterestRate());

                List<AgrEpaySetup> agrEpaySetupList = agrEpaySetupRepository.findAllByMastAgreementMastAgrIdAndActive(mastAgrId, "Y");
                if(!agrEpaySetupList.isEmpty()){
                    AgrEpaySetup agrEpaySetup = agrEpaySetupList.get(0);
                    getODAndSFDetails.setBankCode(agrEpaySetup.getBankCode());
                    getODAndSFDetails.setAccountNo(agrEpaySetup.getAccountNo());
                    getODAndSFDetails.setAccountHolderName(agrEpaySetup.getAccountHolderName());
                    getODAndSFDetails.setAccountType(agrEpaySetup.getAccountType().equalsIgnoreCase("SA") ? "Saving" : "Current");
                    getODAndSFDetails.setIfscCode(agrEpaySetup.getIfscCode());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            log.error("MyLoanAccountService :: getODAndSFDetails :: Method: getNonODDetails");
            log.error("MyLoanAccountService :: getODAndSFDetails :: Request: {}", mastAgrId);
            log.error("MyLoanAccountService :: getODAndSFDetails :: Error: {}", e.getMessage());

            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return getODAndSFDetails;
    }

    public List<SFMasterAgreementsResDto> getSFMasterAgreements(String loanApplicationNo)throws Exception {
        List<SFMasterAgreementsResDto> sfMasterAgreementsResDtoList = new ArrayList<>();
        SFMasterAgreementsResDto sFMasterAgreementsResDto = null;

        try {

            if (loanApplicationNo == null) {
                throw new ApiException("Required loanApplicationNo Id.", HttpStatus.NOT_FOUND);
            }

            log.info("loanApplicationNo : {}", loanApplicationNo);
            CustApplLimitSetup custApplLimitSetup = custApplLimitSetupRepository.findByOriginationApplnNo(loanApplicationNo);

            List<AgrMasterAgreement> agrMasterAgreementList = agrMasterAgreementRepository.findByOriginationApplnNoAndPortfolioCodeIn(loanApplicationNo, Arrays.asList("SF"));
            log.info("MyLoanAccountService :: getSFMasterAgreements :: agrMasterAgreementList: {}", agrMasterAgreementList);

            if (!agrMasterAgreementList.isEmpty()) {
                for (AgrMasterAgreement agrMasterAgreement : agrMasterAgreementList) {
                    sFMasterAgreementsResDto = new SFMasterAgreementsResDto();

                    sFMasterAgreementsResDto.setLoanApplicationNo(agrMasterAgreement.getOriginationApplnNo());
                    sFMasterAgreementsResDto.setOverdueAmount(commonService.getMasterTotalDues(agrMasterAgreement.getMastAgrId()));
                    sFMasterAgreementsResDto.setLoanStatus(agrMasterAgreement.getDpd() > 0 ? "Overdue" : "Regular");
                    sFMasterAgreementsResDto.setPortfolioCode(agrMasterAgreement.getPortfolioCode());
                    sFMasterAgreementsResDto.setCustomerId(agrMasterAgreement.getCustomerId());
                    sFMasterAgreementsResDto.setMastAgrId(agrMasterAgreement.getMastAgrId());
                    sFMasterAgreementsResDto.setVirtualId(agrMasterAgreement.getVirtualId());
                    sFMasterAgreementsResDto.setVpaId(agrMasterAgreement.getVpaId());

                    AgrInvoiceDetails agrInvoiceDetails = agrInvoiceDetailsService.getAgrInvoiceDetailsByMastAgrId(agrMasterAgreement.getMastAgrId());
                    sFMasterAgreementsResDto.setInvoiceDate(agrInvoiceDetails != null ? agrInvoiceDetails.getDtInvoiceDate() : null);

                    GetCustomerDetailsResDto getCustomerDetailsResDto = customerDetailsService.getCustomerDetails(agrMasterAgreement.getMastAgrId(), agrMasterAgreement.getCustomerId());
                    log.info("MyLoanAccountService :: getSFMasterAgreements :: getCustomerDetailsResDto: {}", getCustomerDetailsResDto);

                    if(getCustomerDetailsResDto != null) {
                        sFMasterAgreementsResDto.setCustomerName(getCustomerDetailsResDto.getCustomerName());
                    }

                    sFMasterAgreementsResDto.setAvailableLimit(custApplLimitSetup.getAvailableLimit());
                    sFMasterAgreementsResDto.setSanctionedLimit(custApplLimitSetup.getLimitSanctioned());
                    sFMasterAgreementsResDto.setUtilizedLimit(custApplLimitSetup.getUtilizedLimit());
                    log.info("sfMasterAgreementsResDtoList: {}", sfMasterAgreementsResDtoList);
                    sfMasterAgreementsResDtoList.add(sFMasterAgreementsResDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("MyLoanAccountService :: getSFMasterAgreements :: Method: getSFMasterAgreements");
            log.error("MyLoanAccountService :: getSFMasterAgreements :: Request: {}", loanApplicationNo);
            log.error("MyLoanAccountService :: getSFMasterAgreements :: Error: {}", e.getMessage());
            throw new ApiException(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return sfMasterAgreementsResDtoList;
    }
}
