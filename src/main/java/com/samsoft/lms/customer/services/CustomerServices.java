package com.samsoft.lms.customer.services;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.samsoft.lms.agreement.dto.CustomerAddressAgrDto;
import com.samsoft.lms.agreement.dto.CustomerBoardingDto;
import com.samsoft.lms.agreement.dto.MasterAgreementBoradingDto;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgreementFeeListDto;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.dto.CustomerContactDto;
import com.samsoft.lms.customer.dto.CustomerSearchDto;
import com.samsoft.lms.customer.dto.CustomerSearchDtoMain;
import com.samsoft.lms.customer.entities.AgrCustAddress;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.exceptions.CustomerDataNotFoundException;
import com.samsoft.lms.customer.repositories.AgrCustAddressRepository;
import com.samsoft.lms.customer.repositories.AgrCustomerRepository;
import com.samsoft.lms.las.util.PageableUtils;

@Service
@Slf4j
public class CustomerServices {

    @Autowired
    private AgrCustomerRepository custRepo;

    @Autowired
	private AgreementService agrService;
    
    @Autowired
    private CommonServices commService;

    @Autowired
    private AgrCustAddressRepository addrRepo;
    
    @Autowired
	private PageableUtils pageableUtils;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;
	

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public Page<CustomerSearchDto> getCustomerList(String type, String value, Pageable pageable) throws Exception {
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
           // Pageable paging = PageRequest.of(pageNo, pageSize);
            if (type.equals("customer_id")) {
                listCustResponse = getCustomerListByCustomerId(value, pageable);
            } else if (type.equals("master_agreement_id")) {
                listCustResponse = getCustomerListByMasterAgreement(value, pageable);
            } else if (type.equals("mobile_no")) {
                listCustResponse = getCustomerListByMobile(value, pageable);
            } else if (type.equals("pan_no")) {
                listCustResponse = getCustomerListByPan(value, pageable);
            } else if (type.equals("los_application")) {
                listCustResponse = getCustomerListByLosApplicationNo(value, pageable);
            } else if (type.equals("first_name")) {
                listCustResponse = getCustomerListByFirstName(value, pageable);
            }else if (type.equals("aadhar_no")) {
                listCustResponse = getCustomerListByAadharNo(value, pageable);
            }else if (type.equals("cust_category")) {
                listCustResponse = getCustomerListByCustCategory(value, pageable);
            }else if (type.equals("customer_type")) {
                listCustResponse = getCustomerListByCustomerType(value, pageable);
            }else if (type.equals("status")) {
                listCustResponse = getCustomerListByStatus(value, pageable);
            }else if (type.equals("last_name")) {
                listCustResponse = getCustomerListByLastName(value, pageable);
            }
           
            
            return pageableUtils.convertToPage(listCustResponse, pageable);
            
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        
       
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByCustomerId(String customerId, Pageable pageable) throws Exception {
        List<AgrCustomer> listCustomer = new ArrayList<AgrCustomer>();
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByCustomerIdIgnoreCaseOrderByDtUserDateDesc(customerId);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with customer id " + customerId);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
//                custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", cust.getCustCategory()));
//                custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", cust.getCustomerType()));
//                custResponse.setGender(commService.getDescription("GENDER", cust.getGender()));
//                custResponse.setMaritalStatus(commService.getDescription("MARRITAL_STATUS", cust.getMaritalStatus()));
//                custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", cust.getStatus()));
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
//                custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", cust.getResidenceStatus()));
         
//                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
               
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByMasterAgreement(String masterAgreement, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findByMasterAgrMastAgrIdOrderByDtUserDateDesc(masterAgreement);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with master agreement " + masterAgreement);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
//                custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", cust.getCustCategory()));
//                custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", cust.getCustomerType()));
//                custResponse.setGender(commService.getDescription("GENDER", cust.getGender()));
//                custResponse.setMaritalStatus(commService.getDescription("MARRITAL_STATUS", cust.getMaritalStatus()));
//                custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", cust.getStatus()));
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
//                custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", cust.getResidenceStatus()));
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByMobile(String mobile, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByMobileOrderByDtUserDateDesc(mobile);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with mobile no " + mobile);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
//                custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", cust.getCustCategory()));
//                custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", cust.getCustomerType()));
//                custResponse.setGender(commService.getDescription("GENDER", cust.getGender()));
//                custResponse.setMaritalStatus(commService.getDescription("MARRITAL_STATUS", cust.getMaritalStatus()));
//                custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", cust.getStatus()));
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
//                custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", cust.getResidenceStatus()));
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByPan(String pan, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByPanIgnoreCaseOrderByDtUserDateDesc(pan);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with pan no " + pan);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
//                custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", cust.getCustCategory()));
//                custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", cust.getCustomerType()));
//                custResponse.setGender(commService.getDescription("GENDER", cust.getGender()));
//                custResponse.setMaritalStatus(commService.getDescription("MARRITAL_STATUS", cust.getMaritalStatus()));
//                custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", cust.getStatus()));
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
//                custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", cust.getResidenceStatus()));
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustomerBasicInfoDto getCustomerByCustomerId(String customerId) {
        CustomerBasicInfoDto custResponse = null;
        try {
            AgrCustomer listCustomer = custRepo.findFirstByCustomerId(customerId);
            if (listCustomer == null) {
                throw new CustomerDataNotFoundException(
                        "No basic information is available for customer id " + customerId);
            }
            custResponse = new CustomerBasicInfoDto();
            BeanUtils.copyProperties(listCustomer, custResponse);
//            custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", listCustomer.getCustCategory()));
//            custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", listCustomer.getCustomerType()));
//            custResponse.setGender(commService.getDescription("GENDER", listCustomer.getGender()));
//            custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", listCustomer.getStatus()));
//            custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", listCustomer.getResidenceStatus()));
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return custResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustomerBasicInfoDto getCustomerByMastAgrIdAndCustomerId(String mastAgrId, String customerId) {
        CustomerBasicInfoDto custResponse = null;
        try {
            AgrCustomer listCustomer = custRepo.findByMasterAgrMastAgrIdAndCustomerIdAndCustomerType(mastAgrId, customerId, "B");
            if (listCustomer == null) {
                throw new CustomerDataNotFoundException(
                        "No basic information is available for customer id " + customerId);
            }
            custResponse = new CustomerBasicInfoDto();
            BeanUtils.copyProperties(listCustomer, custResponse);
//            custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", listCustomer.getCustCategory()));
//            custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", listCustomer.getCustomerType()));
//            custResponse.setGender(commService.getDescription("GENDER", listCustomer.getGender()));
//            custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", listCustomer.getStatus()));
//            custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", listCustomer.getResidenceStatus()));
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return custResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustomerContactDto getCustomerContactInfo(String customerId) {
        CustomerContactDto custResponse = null;
        try {
            AgrCustomer listCustomer = custRepo.findFirstByCustomerId(customerId);
            if (listCustomer == null) {
                throw new CustomerDataNotFoundException(
                        "No contact information is available for customer id " + customerId);
            }
            custResponse = new CustomerContactDto();
            BeanUtils.copyProperties(listCustomer, custResponse);
//            custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", listCustomer.getStatus()));

        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return custResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustomerContactDto getCustomerContactInfoByMastAgrIdAndCustomerId(String mastAgrId, String customerId) {
        CustomerContactDto custResponse = null;
        try {
            AgrCustomer listCustomer = custRepo.findByMasterAgrMastAgrIdAndCustomerIdAndCustomerType(mastAgrId, customerId, "B");
            if (listCustomer == null) {
                throw new CustomerDataNotFoundException(
                        "No contact information is available for customer id " + customerId);
            }
            custResponse = new CustomerContactDto();
            BeanUtils.copyProperties(listCustomer, custResponse);
//            custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", listCustomer.getStatus()));

        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return custResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public CustomerContactDto getCustomerContactInfoByCustInternalId(Integer custInternalId) {
        CustomerContactDto custResponse = null;
        try {
            AgrCustomer listCustomer = custRepo.findByCustInternalId(custInternalId);
            if (listCustomer == null) {
                throw new CustomerDataNotFoundException(
                        "No contact information is available for customer id " + custInternalId);
            }
            custResponse = new CustomerContactDto();
            BeanUtils.copyProperties(listCustomer, custResponse);
//            custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", listCustomer.getStatus()));

        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return custResponse;

    }

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByLosApplicationNo(String originationApplnNo, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByMasterAgrOriginationApplnNoOrderByDtUserDateDesc(originationApplnNo);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException(
                        "No customer found with master agreement " + originationApplnNo);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
//                custResponse.setCustCategory(commService.getDescription("CUSTOMER_CATEGORY", cust.getCustCategory()));
//                custResponse.setCustomerType(commService.getDescription("CUSTOMER_TYPE", cust.getCustomerType()));
//                custResponse.setGender(commService.getDescription("GENDER", cust.getGender()));
//                custResponse.setMaritalStatus(commService.getDescription("MARRITAL_STATUS", cust.getMaritalStatus()));
//                custResponse.setStatus(commService.getDescription("CUSTOMER_STATUS", cust.getStatus()));
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
//                custResponse.setResidenceStatus(commService.getDescription("RESIDENCE_STATUS", cust.getResidenceStatus()));
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }

	@Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public Boolean createAndUpdateCustomer(List<CustomerBoardingDto> customerList, String mastAgrId) {

		AgrCustomer customer = null;

		try {

			if (customerList.size() <= 0) {
				throw new CoreDataNotFoundException("Customer details are not available.");
			}

			if(mastAgrId == null) {
				throw new CoreDataNotFoundException("Required Master Agreement Id");
			}

			for (CustomerBoardingDto customerDto : customerList) {

				customer = custRepo.findByMasterAgrMastAgrIdAndPanAndCustomerType(mastAgrId, customerDto.getPan(), customerDto.getCustomerType());

				if (customer != null) {
					BeanUtils.copyProperties(customerDto, customer);

					AgrMasterAgreement master = masterRepo.findByMastAgrId(mastAgrId);
					if (master == null) {
						throw new CoreDataNotFoundException("Agreement details are not available.");
					}
					customer.setMasterAgr(master);

					if (customerDto.getCustomerAddrList().size() <= 0) {
						throw new CoreDataNotFoundException(
								"Address details are not available for " + customerDto.getCustomerId());
					}

					for (CustomerAddressAgrDto customerAddrDto : customerDto.getCustomerAddrList()) {
						List<AgrCustAddress> custAddrList = addrRepo.findByCustomerId(customer.getCustomerId());

						if(customerAddrDto.getAddrSeqNo() != null) {
							for (AgrCustAddress custAddr : custAddrList) {
								if(custAddr.getAddrSeqNo()== customerAddrDto.getAddrSeqNo() ) {
									BeanUtils.copyProperties(customerAddrDto, custAddr);
									addrRepo.save(custAddr);
								}
								
							}
						}else {
							AgrCustAddress custAddr = new AgrCustAddress();
							BeanUtils.copyProperties(customerAddrDto, custAddr);
							custAddr.setCustomer(customer);
							custAddr.setUserId("INTERFACE");
							custAddr.setCustomerId(customer.getCustomerId());
							addrRepo.save(custAddr);
						}
					}

					custRepo.save(customer);

				} else {

					customer = new AgrCustomer();
					BeanUtils.copyProperties(customerDto, customer);

					AgrMasterAgreement master = masterRepo.findByMastAgrId(mastAgrId);
					if (master == null) {
						throw new CoreDataNotFoundException("Agreement details are not available.");
					}

					customer.setMasterAgr(master);
					customer.setPrefferedContactTimeFrom(customerDto.getContactTimeFrom());
					customer.setPrefferedContactTimeTo(customerDto.getContactTimeTo());
					customer.setUserId("INTERFACE");

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
						addrRepo.save(custAddr);
					}

					custRepo.save(customer);
				}
			}

			return Boolean.TRUE;
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}
    }

    public AgrCustomer getCustomerByMastAgrIdAndCustomerType(String mastAgrId, String customerType) {

        try {

            return custRepo.findByMasterAgrMastAgrIdAndCustomerType(mastAgrId, customerType);

        } catch (CustomerDataNotFoundException e) {
            e.printStackTrace();
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: Method: getCustomerByCustomerIdAndCustomerType");
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: Request: mastAgrId: {}, customerType: {}", mastAgrId, customerType);
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: CustomerDataNotFoundException :: Error: {}",e.getMessage());

            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: Method: getCustomerByCustomerIdAndCustomerType");
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: Request: mastAgrId: {}, customerType: {}", mastAgrId, customerType);
            log.error("CustomerServices :: getCustomerByCustomerIdAndCustomerType :: Error: {}",e.getMessage());
            throw e;
        }
    }

    protected String getCustomerName(AgrCustomer customer) {

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
    
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByFirstName(String firstName, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
//            listCustomer = custRepo.findAllByFirstNameIgnoreCaseOrderByFirstNameDesc(firstName);
        	listCustomer = custRepo.findAllByFirstNameContainingIgnoreCaseOrderByFirstNameDesc(firstName);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with Name " + firstName);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByAadharNo(String aadharNo, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByAadharNoOrderByFirstNameDesc(aadharNo);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with aadhar no " + aadharNo);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByCustCategory (String custCategory, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByCustCategoryOrderByFirstNameDesc(custCategory);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with customer category " + custCategory);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByCustomerType (String customerType, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByCustomerTypeOrderByFirstNameDesc(customerType);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with customer Type " + customerType);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByStatus (String status, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByStatusOrderByFirstNameDesc(status);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with status " + status);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
    public List<CustomerSearchDto> getCustomerListByLastName (String lastName, Pageable paging) throws Exception {
        List<AgrCustomer> listCustomer;
        List<CustomerSearchDto> listCustResponse = new ArrayList<>();
        try {
            listCustomer = custRepo.findAllByLastNameOrderByLastNameDesc(lastName);
            if (listCustomer.size() <= 0) {
                throw new CustomerDataNotFoundException("No customer found with lastName " + lastName);
            }
            for (AgrCustomer cust : listCustomer) {
                CustomerSearchDto custResponse = new CustomerSearchDto();
                BeanUtils.copyProperties(cust, custResponse);
                custResponse.setMastAgrId(cust.getMasterAgr().getMastAgrId());
                custResponse.setCustomerName(this.getCustomerName(cust));
                
                // Added loan details in customer list 
//                AgreementInfoDto agrInfoDto = agrService.getAgreementInfo(cust.getMasterAgr().getMastAgrId());
//                if(agrInfoDto != null) {
//                	custResponse.setDtDisbursmentDate(agrInfoDto.getDtSanctionedDate());
//                	custResponse.setDtNextInstallment(agrInfoDto.getDtNextInstallment());
//                	custResponse.setLoanAmount(agrInfoDto.getLoanAmount());
//                	custResponse.setEmiAmount(agrInfoDto.getAdvanceEmiAmount());
//                }
                
                listCustResponse.add(custResponse);
            }
        } catch (CustomerDataNotFoundException e) {
            throw new CustomerDataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        return listCustResponse;
    }
    
    
	public ResponseEntity<byte[]> getImageByCustomerIdForDownload(String customerId) {
		Optional<AgrCustomer> cust = Optional.ofNullable(custRepo.findByCustomerId(customerId));

		if (cust.isPresent()) {
			AgrCustomer customer = cust.get();
			String imageUrl = customer.getProfileUrl();

			if (imageUrl != null && !imageUrl.isEmpty()) {
				try {

					RestTemplate restTemplate = new RestTemplate();
					byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
					System.out.println("imageBytes------------------------->" + imageBytes);

					if (imageBytes == null || imageBytes.length == 0) {
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}

					String contentType = getContentType(imageUrl);
					System.out.println("contentType------------------------->" + contentType);

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.parseMediaType(contentType));

					headers.setContentDisposition(
							ContentDisposition.builder("attachment").filename("profile-image.png").build());

					return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	private String getContentType(String imageUrl) {
		String contentType = "application/octet-stream";

		if (imageUrl.endsWith(".png")) {
			contentType = "image/png";
		} else if (imageUrl.endsWith(".gif")) {
			contentType = "image/gif";
		} else if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".jpeg")) {
			contentType = "image/jpeg";
		} else if (imageUrl.endsWith(".bmp")) {
			contentType = "image/bmp";
		} else if (imageUrl.endsWith(".webp")) {
			contentType = "image/webp";
		} else if (imageUrl.endsWith(".svg")) {
			contentType = "image/svg+xml";
		}

		return contentType;
	}
}