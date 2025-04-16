package com.samsoft.lms.newux.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samsoft.lms.agreement.dto.CustomerAgreementDetailsResDto;
import com.samsoft.lms.agreement.entities.AgrDisbursement;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.repositories.AgrDisbursementRepository;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.dto.AgreementLoanInfoDto;
import com.samsoft.lms.core.dto.AgreementRepaymentListDto;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.repositories.AgrLoansRepository;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.AgrTrnDueDetailsRepository;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.newux.dto.response.GetLoanDetailsResDto;
import com.samsoft.lms.newux.dto.response.GetLoanOverviewResDto;
import com.samsoft.lms.newux.dto.response.getloans.*;
import com.samsoft.lms.newux.exceptions.LoansNotFoundException;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoanDetailsService {

    @Autowired
    private Environment env;

    @Autowired
    private AgrMasterAgreementRepository agrRepo;

    @Autowired
    private AgrCustomerRepository custRepo;

    @Autowired
    private AgrLoansRepository loanRepo;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private AgrDisbursementRepository agrDisbursementRepository;
    

    public GetAllLoansResDto getAllLoans(String type) throws Exception {

        GetAllLoansResDto getAllLoansResDto = new GetAllLoansResDto();
        List<AllLoanDetailsResDto> loans = new ArrayList<>();
        List<AgrMasterAgreement> allAgreementList = null;
        AllLoanDetailsResDto allLoanDetailsResDto = null;

        try {

            if (type == null) {
                throw new LoansNotFoundException("Required Type.");
            }

            switch (type) {
                case "All":

                    //Get All Loans Count
                    getAllLoansResDto.setTotalLoans(agrRepo.getTotalLoans());

                    //Get Live Loans Count
                    getAllLoansResDto.setTotalLiveLoans(agrRepo.getActiveLoansByAgreementStatus("L"));

                    //Get Closed Loans Count
                    getAllLoansResDto.setTotalClosedLoans(agrRepo.getActiveLoansByAgreementStatus("C"));

                    allAgreementList = agrRepo.getAllOrderByDisbDate();
                    log.info("LoanDetailsService :: getAllLoans :: allAgreementList: {}", allAgreementList.size());

                    if (!allAgreementList.isEmpty()) {

                        for (AgrMasterAgreement agreement : allAgreementList) {
                            allLoanDetailsResDto = this.getLoans(agreement);
                            loans.add(allLoanDetailsResDto);
                        }
                        log.info("getAllLoans===> " + loans);
                        getAllLoansResDto.setLoans(loans);
                    }
                    break;
                case "TotalLiveLoans":

                    //Get All Loans Count
                    getAllLoansResDto.setTotalLoans(agrRepo.getTotalLoans());

                    //Get Live Loans Count
                    getAllLoansResDto.setTotalLiveLoans(agrRepo.getActiveLoansByAgreementStatus("L"));

                    //Get Closed Loans Count
                    getAllLoansResDto.setTotalClosedLoans(agrRepo.getActiveLoansByAgreementStatus("C"));

                    //Get All Data
                    allAgreementList = agrRepo.findByAgreementStatus("L");
                    if (!allAgreementList.isEmpty()) {

                        for (AgrMasterAgreement agreement : allAgreementList) {
                            allLoanDetailsResDto = this.getLoans(agreement);
                            loans.add(allLoanDetailsResDto);
                        }
                        log.info("getAllLoans===> " + loans);
                        getAllLoansResDto.setLoans(loans);
                    }
                    break;
                case "TotalClosedLoans":

                    //Get All Loans Count
                    getAllLoansResDto.setTotalLoans(agrRepo.getTotalLoans());

                    //Get Live Loans Count
                    getAllLoansResDto.setTotalLiveLoans(agrRepo.getActiveLoansByAgreementStatus("L"));

                    //Get Closed Loans Count
                    getAllLoansResDto.setTotalClosedLoans(agrRepo.getActiveLoansByAgreementStatus("C"));

                    //Get All Data
                    allAgreementList = agrRepo.findByAgreementStatus("C");
                    if (!allAgreementList.isEmpty()) {

                        for (AgrMasterAgreement agreement : allAgreementList) {
                            allLoanDetailsResDto = this.getLoans(agreement);
                            loans.add(allLoanDetailsResDto);
                        }
                        log.info("getAllLoans===> " + loans);
                        getAllLoansResDto.setLoans(loans);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return getAllLoansResDto;
    }

    protected AllLoanDetailsResDto getLoans(AgrMasterAgreement agreement) throws Exception {

        AllLoanDetailsResDto allLoanDetailsResDto = null;
        LoanAccountDetailsResDto loanAccountDetails = new LoanAccountDetailsResDto();
        CustomerDetailsResDto customerDetails = new CustomerDetailsResDto();
        ContactDetailsResDto contactDetails = new ContactDetailsResDto();
        LoanDetailsResDto loanDetails = new LoanDetailsResDto();

        try {

            if (agreement != null) {
                allLoanDetailsResDto = new AllLoanDetailsResDto();

                //Set Loan Type
                allLoanDetailsResDto.setLoanType(agreement.getPortfolioCode());

                //Set Loan Account Details
                loanAccountDetails.setMastAgrId(agreement.getMastAgrId());
                loanAccountDetails.setOriginationApplnNo(agreement.getOriginationApplnNo());
                allLoanDetailsResDto.setLoanAccountDetails(loanAccountDetails);

                //Set Loan Details
                loanDetails.setLoanAmount(agreement.getLoanAmount());
                loanDetails.setNextInstallmentAmount(agreement.getNextInstallmentAmount());
                allLoanDetailsResDto.setLoanDetails(loanDetails);

                //Set Due Date
                allLoanDetailsResDto.setDueDate(this.convertToDateFormat(agreement.getDtNextInstallment()));

                List<AgrCustomer> customerList = custRepo
                        .findByMasterAgrMastAgrIdOrderByDtUserDateDesc(agreement.getMastAgrId());

                if (!customerList.isEmpty()) {
                    for (AgrCustomer customer : customerList) {
                        if (customer.getCustomerType().equalsIgnoreCase("B")) {

                            //Set Customer Details
                            customerDetails.setCustomerId(customer.getCustomerId());
                            customerDetails.setCustomerName(this.agreementService.getCustomerName(customer));
                            customerDetails.setCustInternalId(customer.getCustInternalId());

                            allLoanDetailsResDto.setCustomerDetails(customerDetails);

                            //Set Contact Details
                            contactDetails.setMobile(customer.getMobile());
                            contactDetails.setEmail(customer.getEmailId());
                            allLoanDetailsResDto.setContactDetails(contactDetails);
                        }
                    }
                }

                //Set Bucket
                allLoanDetailsResDto.setBucket(this.getBucket(agreement.getDpd()));

                //Set Loan Status
                allLoanDetailsResDto.setAgreementStatus(this.getAgreementStatus(agreement.getAgreementStatus()));

                //Set Disb Date
                AgrDisbursement agrDisbursement = agrDisbursementRepository.findFirstByMastAgrMastAgrIdOrderByDtDisbDateDesc(agreement.getMastAgrId());
                if(agrDisbursement != null) {
                    allLoanDetailsResDto.setDisbDate(new SimpleDateFormat("dd-MM-yyyy").format(agrDisbursement.getDtDisbDate()));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        return allLoanDetailsResDto;
    }

    public GetLoanDetailsResDto getLoanDetails(String mastAgrId) throws Exception {

        GetLoanDetailsResDto getLoanDetailsResDto = new GetLoanDetailsResDto();

        try {

            //Get Customer Agreement Details
            CustomerAgreementDetailsResDto customerAgreementDetails = agreementService.getCustomerAgreementDetails(mastAgrId);
            if (customerAgreementDetails == null) {
                throw new AgreementDataNotFoundException("Master Agreement Details not found");
            }
            getLoanDetailsResDto.setCustomerAgreementDetails(customerAgreementDetails);

            //Set Master Agreement Ids List
            List<String> mastAgrIdList = agreementService.getMastAgrIdListByCustomerId(customerAgreementDetails.getCustomerId());
            if (mastAgrIdList.isEmpty()) {
                throw new AgreementDataNotFoundException("Master Agreement data not found");
            }
            getLoanDetailsResDto.setMastAgrIds(mastAgrIdList);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return getLoanDetailsResDto;
    }

    public GetLoanOverviewResDto getLoanOverview(String mastAgrId) throws Exception {

        GetLoanOverviewResDto getLoanOverviewResDto = new GetLoanOverviewResDto();

        try {

            //Get Agreement Info
            AgreementInfoDto agreementInfo = agreementService.getAgreementInfo(mastAgrId);
            if (agreementInfo != null) {
                getLoanOverviewResDto.setAgreementInfo(agreementInfo);
            }

            //Get Agreement Loan Info
            List<AgreementLoanInfoDto> agreementLoanInfoList = agreementService.getAgreementLoanInfoByMastAgrId(mastAgrId);
            if (!agreementLoanInfoList.isEmpty()) {
                getLoanOverviewResDto.setAgreementLoanInfoList(agreementLoanInfoList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return getLoanOverviewResDto;

    }

    public AllLoanDetailsResDto getLoansByMastAgrId(String mastAgrId) throws Exception {
        AllLoanDetailsResDto allLoanDetailsResDto = new AllLoanDetailsResDto();
        try {

            AgrMasterAgreement agreement = agrRepo.findByMastAgrId(mastAgrId);
            if(agreement != null) {
                allLoanDetailsResDto = this.getLoans(agreement);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return allLoanDetailsResDto;

    }

    public String convertToDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        String dateFormat = env.getProperty("lms.global.date.format");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }

    //Get Bucket
    private String getBucket(Integer dpd) {

        if(dpd == 0) {
            return "STD";
        } else if(dpd >= 1 && dpd <= 30) {
            return "1-30";
        } else if(dpd > 31 && dpd <= 60) {
            return "31-60";
        } else if(dpd > 61 && dpd <= 90) {
            return "61-90";
        } else {
            return "90+";
        }
    }

    //Get Loan Status
    private String getAgreementStatus(String agreementStatus) {

        switch (agreementStatus) {
            case "L":
                return "Live";
            case "C":
                return "Closed";
        }

        return null;
    }
    
    public AgreementRepaymentListDto getAgreementRepaymentList(String mastAgrId) throws Exception {
    	AgreementRepaymentListDto repayDto = new AgreementRepaymentListDto();
        try {
        	repayDto = agreementService.getAgreementRepaymentList(mastAgrId);         
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return repayDto;

    }

	public AgreementRepaymentListDto getAllAgreementRepaymentList(String originationApplnNo) throws Exception {
    	AgreementRepaymentListDto repayDto = new AgreementRepaymentListDto();
        try {        	
        	repayDto = agreementService.getAllAgreementRepaymentList(originationApplnNo);         
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return repayDto;
	}
}
