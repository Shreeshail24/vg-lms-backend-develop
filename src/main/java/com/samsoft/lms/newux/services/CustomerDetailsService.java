package com.samsoft.lms.newux.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.customer.dto.CustomerAddressDto;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.dto.CustomerContactDto;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.services.CustomerAddressService;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.newux.dto.response.CustomerInfoResDto;
import com.samsoft.lms.newux.dto.response.GetCustomerDetailsResDto;
import com.samsoft.lms.newux.dto.response.InstallmentDates;
import com.samsoft.lms.newux.utils.SortCompareUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CustomerDetailsService {

    @Autowired
    private CustomerServices customerServices;

    @Autowired
    private CustomerAddressService customerAddressService;

    @Autowired
    private AgrMasterAgreementRepository agrMasterAgreementRepo;

    @Autowired
    private Environment env;

    //Get Customer Info
    public CustomerInfoResDto getCustomerInfo(String mastAgrId, String customerId) throws Exception {

        CustomerInfoResDto customerInfoResDto = new CustomerInfoResDto();

        try {

            //Customer Basic Info
            CustomerBasicInfoDto customerBasicInfo = customerServices.getCustomerByMastAgrIdAndCustomerId(mastAgrId, customerId);
            if(customerBasicInfo != null) {
                customerInfoResDto.setCustomerBasicInfo(customerBasicInfo);
            }

            //Customer Contact Details
            CustomerContactDto customerContactDetails = customerServices.getCustomerContactInfoByMastAgrIdAndCustomerId(mastAgrId, customerId);
            if(customerContactDetails != null) {
                customerInfoResDto.setCustomerContactDetails(customerContactDetails);
            }

            //Customer Address Details
            List<CustomerAddressDto> customerAddressDetails = customerAddressService.getCustomerAddressByCustInternalId(customerBasicInfo.getCustInternalId());
            if(!customerAddressDetails.isEmpty()) {
                customerInfoResDto.setCustomerAddressDetails(customerAddressDetails);
            }

            return customerInfoResDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get Customer Details
    public GetCustomerDetailsResDto getCustomerDetails(String mastAgrId, String customerId) throws Exception {

        GetCustomerDetailsResDto getCustomerDetailsResDto = new GetCustomerDetailsResDto();
        List<InstallmentDates> dates = new ArrayList<>();

        try {

            //Customer Basic Info
            //Customer Contact Details
            CustomerContactDto customerContactDetails = customerServices.getCustomerContactInfoByMastAgrIdAndCustomerId(mastAgrId, customerId);
            if(customerContactDetails != null) {
                getCustomerDetailsResDto.setCustomerName(this.getCustomerName(customerContactDetails));
                getCustomerDetailsResDto.setMobile(customerContactDetails.getMobile());
                getCustomerDetailsResDto.setEmail(customerContactDetails.getEmailId());
            }

            //Get Total Loan Amount
            getCustomerDetailsResDto.setTotalLoanAmount(agrMasterAgreementRepo.getTotalLoanAmount(customerId));

            //Get Active Loans
            getCustomerDetailsResDto.setActiveLoans(agrMasterAgreementRepo.getActiveLoans(customerId));

            //Get Installment Dates
            List<Date> installmentDates = agrMasterAgreementRepo.getNextInstallmentDates(customerId);

            for (Date installmentDate : installmentDates) {
                if(installmentDate != null) {
                    dates.add(new InstallmentDates(this.convertToDateFormat(installmentDate)));
                }
            }
            if(!dates.isEmpty()) {
                Collections.sort(dates, new SortCompareUtil());
                getCustomerDetailsResDto.setNextEmi(dates.get(0).getInstallmentDate());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return getCustomerDetailsResDto;

    }

    //Get Customer Details
    public GetCustomerDetailsResDto getCustomerDetails(String customerId) throws Exception {

        GetCustomerDetailsResDto getCustomerDetailsResDto = new GetCustomerDetailsResDto();
        List<InstallmentDates> dates = new ArrayList<>();

        try {

            //Customer Basic Info
            //Customer Contact Details
            CustomerContactDto customerContactDetails = customerServices.getCustomerContactInfo(customerId);
            if(customerContactDetails != null) {
                getCustomerDetailsResDto.setCustomerName(this.getCustomerName(customerContactDetails));
                getCustomerDetailsResDto.setMobile(customerContactDetails.getMobile());
                getCustomerDetailsResDto.setEmail(customerContactDetails.getEmailId());
            }

            //Get Total Loan Amount
            getCustomerDetailsResDto.setTotalLoanAmount(agrMasterAgreementRepo.getTotalLoanAmount(customerContactDetails.getCustomerId()));

            //Get Active Loans
            getCustomerDetailsResDto.setActiveLoans(agrMasterAgreementRepo.getActiveLoans(customerContactDetails.getCustomerId()));

            //Get Installment Dates
            List<Date> installmentDates = agrMasterAgreementRepo.getNextInstallmentDates(customerContactDetails.getCustomerId());

            for (Date installmentDate : installmentDates) {
                if(installmentDate != null) {
                    dates.add(new InstallmentDates(this.convertToDateFormat(installmentDate)));
                }
            }
            if(!dates.isEmpty()) {
                Collections.sort(dates, new SortCompareUtil());
                getCustomerDetailsResDto.setNextEmi(dates.get(0).getInstallmentDate());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return getCustomerDetailsResDto;

    }

    protected String getCustomerName(CustomerContactDto customer) {

        if(customer != null) {
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

    public String convertToDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        String dateFormat = env.getProperty("lms.global.date.format");
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);

    }
}
