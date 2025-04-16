package com.samsoft.lms.customer.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.agreement.dto.CustomerBoardingDto;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.dto.VAgrInterestAccrualHistoryMainDto;
import com.samsoft.lms.customer.dto.CustomerAddressDto;
import com.samsoft.lms.customer.dto.CustomerAddressTypeDto;
import com.samsoft.lms.customer.dto.CustomerBasicInfoDto;
import com.samsoft.lms.customer.dto.CustomerContactDto;
import com.samsoft.lms.customer.dto.CustomerSearchDto;
import com.samsoft.lms.customer.dto.CustomerSearchDtoMain;
import com.samsoft.lms.customer.exceptions.CustomerDataNotFoundException;
import com.samsoft.lms.customer.exceptions.CustomerInternalServerError;
import com.samsoft.lms.customer.services.CustomerAddressService;
import com.samsoft.lms.customer.services.CustomerServices;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.las.util.PaginationUtil;

@RestController
@RequestMapping(value = "/customer")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:4200", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in", "https://localhost:8010","http://dev.techvgi.com"} ,allowedHeaders = "*")
public class CustomerController {

	@Autowired
	private CustomerServices custService;
	
	@Autowired
	private PageableUtils pageableUtils;

	@Autowired
	private CustomerAddressService custAddrService;

	@Transactional
	@GetMapping(value = "/getCustomerList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSearchDto>> getCustomerList(@RequestParam String type,
			@RequestParam String value, @RequestParam Integer pageNo, @RequestParam Integer pageSize,
			@RequestParam(required = false, defaultValue = "mastAgrId") String sortBy,
	        @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
            Page <CustomerSearchDto> customerList = custService.getCustomerList(type, value, pageable);
           
            // Create response headers
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), customerList);
            return ResponseEntity.ok().headers(headers).body(customerList.getContent());
                    
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }
	

	@GetMapping(value = "/searchCustomerListByCustomerId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CustomerSearchDto>> getCustomerListByCustomerId(@RequestParam String customerId,
			@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize);
			List<CustomerSearchDto> customerList = custService.getCustomerListByCustomerId(customerId, paging);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@GetMapping(value = "/searchCustomerListByMasterAgreement", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CustomerSearchDto>> getCustomerListByMasterAgreement(
			@RequestParam String masterAgreement, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize);
			List<CustomerSearchDto> customerList = custService.getCustomerListByMasterAgreement(masterAgreement,
					paging);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@GetMapping(value = "/searchCustomerListByMobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CustomerSearchDto>> getCustomerListByMobile(@RequestParam String mobile,
			@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize);
			List<CustomerSearchDto> customerList = custService.getCustomerListByMobile(mobile, paging);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/searchCustomerListByPan", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CustomerSearchDto>> getCustomerListByPan(@RequestParam String pan,
			@RequestParam Integer pageNo, @RequestParam Integer pageSize) throws Exception {
		try {
			Pageable paging = PageRequest.of(pageNo, pageSize);
			List<CustomerSearchDto> customerList = custService.getCustomerListByPan(pan, paging);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@GetMapping(value = "/getCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerBasicInfoDto> getCustomer(@RequestParam String customerId) throws Exception {
		try {
			CustomerBasicInfoDto customerList = custService.getCustomerByCustomerId(customerId);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@GetMapping(value = "/getCustomerContactInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerContactDto> getCustomerContactInfo(@RequestParam String customerId) throws Exception {
		try {
			CustomerContactDto customerList = custService.getCustomerContactInfo(customerId);
			return ResponseEntity.ok(customerList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}
	}

	@GetMapping(value = "/getCustomerAddressTypeList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<CustomerAddressTypeDto>> getCustomerAddressTypeList(@RequestParam String customerId)
			throws Exception {
		try {
			Set<CustomerAddressTypeDto> customerTypeList = custAddrService.getCustomersAddressTypeList(customerId);
			return ResponseEntity.ok(customerTypeList);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@GetMapping(value = "/getCustomerAddress", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerAddressDto> getCustomerAddress(@RequestParam String customerId,
			@RequestParam String addrType) throws Exception {
		try {
			CustomerAddressDto customer = custAddrService.getCustomerAddress(customerId, addrType);
			return ResponseEntity.ok(customer);
		} catch (CustomerDataNotFoundException e) {
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new CustomerInternalServerError(e.getMessage());
		}

	}

	@PostMapping("/createAndUpdateCustomer")
	public FourFinResponse<Boolean> createAndUpdateCustomer(@RequestBody List<CustomerBoardingDto> customers, @RequestParam String mastAgrId) throws Exception {

		FourFinResponse<Boolean> response = new FourFinResponse<>();

		try {
			Boolean res = custService.createAndUpdateCustomer(customers, mastAgrId);

			if(res) {
				response.setHttpStatus(HttpStatus.OK);
				response.setResponseCode(HttpStatus.OK.value());
				response.setData(res);
			} else {
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setResponseCode(HttpStatus.NOT_FOUND.value());
				response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
				response.setData(null);
			}
		} catch (CustomerDataNotFoundException e) {

			response.setHttpStatus(HttpStatus.NOT_FOUND);
			response.setResponseCode(HttpStatus.NOT_FOUND.value());
			response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
			response.setResponseMessage(e.getMessage());
			throw new CustomerDataNotFoundException(e.getMessage());
		} catch (Exception e) {

			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			response.setResponseMessage(e.getMessage());
			throw new CustomerInternalServerError(e.getMessage());
		}

		return response;
	}
	 @GetMapping("/lms/customer/download/{customerId}")
	    public ResponseEntity<byte[]> displayImage(@PathVariable String customerId) {
	        return custService.getImageByCustomerIdForDownload(customerId);
	    }
	}