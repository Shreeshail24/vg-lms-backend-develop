package com.samsoft.lms.agreement.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.agreement.dto.AllAgreementMainDto;
import com.samsoft.lms.agreement.dto.AllOdAgreementMainDto;
import com.samsoft.lms.agreement.dto.CollateralDetailsDto;
import com.samsoft.lms.agreement.dto.GetEpayAgreementDto;
import com.samsoft.lms.agreement.dto.RepayVariationDto;
import com.samsoft.lms.agreement.dto.VAgrTranHistoryDtoMain;
import com.samsoft.lms.agreement.dto.VTranHistoryOdMainDto;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.services.AgreementService;
import com.samsoft.lms.core.dto.AgrMasterAgreementDto;
import com.samsoft.lms.core.dto.AgreementAmortListDto;
import com.samsoft.lms.core.dto.AgreementDueListDto;
import com.samsoft.lms.core.dto.AgreementFeeListDto;
import com.samsoft.lms.core.dto.AgreementInfoDto;
import com.samsoft.lms.core.dto.AgreementLimitSetupListDto;
import com.samsoft.lms.core.dto.AgreementLoanInfoDto;
import com.samsoft.lms.core.dto.AgreementLoanListDto;
import com.samsoft.lms.core.dto.AgreementProductInfoDto;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.dto.VAgrInterestAccrualHistoryDto;
import com.samsoft.lms.core.dto.VAgrInterestAccrualHistoryMainDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryHeaderDto;
import com.samsoft.lms.core.dto.VAgrTranHistoryOdHeaderDto;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.las.util.PaginationUtil;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsNotFoundException;
import com.samsoft.lms.request.dto.ChargesWaiverDto;
import com.samsoft.lms.transaction.dto.GetLimitDtlsDto;

@RestController
@RequestMapping(value = "/agreement")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in" , "https://qa-losone.4fin.in"}, allowedHeaders = "*")
public class AgreementController {

	@Autowired
	private AgreementService agrService;

	@Autowired
	private Environment env;
	
	
	@GetMapping(value = "/getCustomerAgreementList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgrMasterAgreementDto>> getCustomerAgreementList(
    		@RequestParam String customerId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "customerId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir)  {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
           Page<AgrMasterAgreementDto> pageResult = agrService.getCustomerAgreementList(customerId, pageable);
             
           HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
           
        
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }	

	@GetMapping(value = "/getAgreementLoanInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementLoanInfoDto getCustomerAgreementList(@RequestParam String mastAgrId, @RequestParam String loanId)
			throws Exception {
		AgreementLoanInfoDto loanInfo = null;
		try {
			loanInfo = agrService.getAgreementLoanInfo(mastAgrId, loanId);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return loanInfo;
	}
	
	
	

	@GetMapping(value = "/getAgreementInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementInfoDto getAgreementInfo(@RequestParam String masterAgreement) throws Exception {
		AgreementInfoDto agrInfoDto = null;
		try {
			agrInfoDto = agrService.getAgreementInfo(masterAgreement);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return agrInfoDto;
	}

	@GetMapping(value = "/getAgreementLoanList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AgreementLoanListDto> getAgreementLoanList(@RequestParam String masterAgreement) throws Exception {
		List<AgreementLoanListDto> listLoan = null;
		try {
			listLoan = agrService.getAgreementLoanList(masterAgreement);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return listLoan;
	}

	@GetMapping(value = "/getAgreementProductInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AgreementProductInfoDto> getAgreementProductInfo(@RequestParam String masterAgreement)
			throws Exception {
		List<AgreementProductInfoDto> product = null;
		try {
			product = agrService.getAgreementProductInfo(masterAgreement);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return product;
	}


	@GetMapping(value = "/getAgreementFeeList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgreementFeeListDto>> getAgreementFeeList(
            @RequestParam String masterAgreement,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "mastAgrId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir)  {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
           Page<AgreementFeeListDto> pageResult = agrService.getAgreementFeeList(masterAgreement, pageable);
             
           HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
           
        
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }

	
	@GetMapping(value = "/getAgreementDueList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgreementDueListDto>> getAgreementDueList(
            @RequestParam String masterAgreement,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "dtDueDate") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir)  {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
           Page<AgreementDueListDto> pageResult = agrService.getAgreementDueList(masterAgreement, pageable);
            
            // Create response headers
            HttpHeaders headers = new HttpHeaders();
           // headers.add("X-Total-Count", String.valueOf(pageResult.getTotalElements()));
            headers.add("X-Total-Count", Long.toString(pageResult.getTotalElements()));
            headers.add("X-Total-Pages", String.valueOf(pageResult.getTotalPages()));
            headers.add("X-Current-Page", String.valueOf(pageResult.getNumber()));
            headers.add("X-Page-Size", String.valueOf(pageResult.getSize()));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pageResult.getContent());
                    
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }

	
	
	@GetMapping(value = "/getAgreementLimitList", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AgreementLimitSetupListDto> getAgreementLimitList(@RequestParam String masterAgreement) throws Exception {
		List<AgreementLimitSetupListDto> limitDto = new ArrayList<AgreementLimitSetupListDto>();
		try {
			limitDto = agrService.getAgreementLimitList(masterAgreement);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		
		return limitDto;
	}

	
	@GetMapping(value = "/getAgreementAmortList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgreementAmortListDto>> getAgreementAmortList(
    		@RequestParam String masterAgreement,
            @RequestParam(required = false) String loanId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "masterAgrId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir)  {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
           Page<AgreementAmortListDto> pageResult = agrService.getAgreementAmortList(masterAgreement, loanId, pageable);
            
            // Create response headers
            HttpHeaders headers = new HttpHeaders();
           // headers.add("X-Total-Count", String.valueOf(pageResult.getTotalElements()));
            headers.add("X-Total-Count", Long.toString(pageResult.getTotalElements()));
            headers.add("X-Total-Pages", String.valueOf(pageResult.getTotalPages()));
            headers.add("X-Current-Page", String.valueOf(pageResult.getNumber()));
            headers.add("X-Page-Size", String.valueOf(pageResult.getSize()));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pageResult.getContent());
                    
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }
	
	@GetMapping(value = "/getAgreementTranHistoryList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<VAgrTranHistoryDto>> getAgreementTranHistoryList(
	        @RequestParam String masterAgreement,
	        @RequestParam(required = false, defaultValue = "0") Integer pageNo,
	        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	        @RequestParam(required = false, defaultValue = "userId") String sortBy,
	        @RequestParam(required = false, defaultValue = "asc") String sortDir) {
	    try {
	        // Create pageable object
	        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
	        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));

	        // Get paginated result
	        Page<VAgrTranHistoryDto> pageResult = agrService.getAgreementTranHistoryList(masterAgreement, pageable);

	        // Generate pagination headers
	        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
	                ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	        
	        return ResponseEntity.ok().headers(headers).body(pageResult.getContent());

	    } catch (AgreementDataNotFoundException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
	    }
	}

	
	

	@GetMapping(value = "/getAgreementTranHistoryHeader", produces = MediaType.APPLICATION_JSON_VALUE)
	public VAgrTranHistoryHeaderDto getAgreementTranHistoryHeader(@RequestParam String masterAgreement)
			throws Exception {
		VAgrTranHistoryHeaderDto tranHistoryDtoHdr = null;
		try {
			tranHistoryDtoHdr = agrService.getAgreementTranHistoryHeader(masterAgreement);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return tranHistoryDtoHdr;
	}

	@GetMapping(value = "/getAgreementIntAccrualList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VAgrInterestAccrualHistoryDto>> getAgreementIntAccrualList(
    		@RequestParam String masterAgreement, @RequestParam String loanId, @RequestParam String fromDate, @RequestParam String toDate,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "mastAgrId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir)  {
        try {
            // Create pageable object
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
            
            // Get paginated result
           Page<VAgrInterestAccrualHistoryDto> pageResult = agrService.getAgreementIntAccList(masterAgreement,loanId,fromDate,toDate, pageable);
            
           HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
           return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
                    
        } catch (AgreementDataNotFoundException e) {
           // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
        }
    }
	

	@GetMapping(value = "/getAllAgreementDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public AllAgreementMainDto getAllAgreementDetails(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			return agrService.getAllAgreementDetails(pageNo, pageSize);
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/getAllOdAgreementDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public AllOdAgreementMainDto getAllOdAgreementDetails(@RequestParam(required = false) String mastAgrId,
			@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			return agrService.getAllOdAgreementDetails(mastAgrId, pageNo, pageSize);
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/getEpayAgreementDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GetEpayAgreementDto> getEpayAgreementDetails(@RequestParam String mastAgrId) {
		try {
			return agrService.getEpayAgreementDetails(mastAgrId);
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/getCollateralDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CollateralDetailsDto> getCollateralDetails(@RequestParam String mastAgrId) {
		try {
			return agrService.getCollateralDetails(mastAgrId);
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping(value = "/getAgreementTranHistoryOdList", produces = MediaType.APPLICATION_JSON_VALUE)
	public VTranHistoryOdMainDto getAgreementTranHistoryOdList(@RequestParam String masterAgreement,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam Integer pageNo,
			@RequestParam Integer pageSize) throws Exception {
		String dateFormat = env.getProperty("lms.global.date.format");

		VTranHistoryOdMainDto tranHistoryDtoList = new VTranHistoryOdMainDto();
		try {
			Date fDate = new SimpleDateFormat(dateFormat).parse(fromDate);
			Date tDate = new SimpleDateFormat(dateFormat).parse(toDate);
			tranHistoryDtoList = agrService.getAgreementTranHistoryOdList(masterAgreement, fDate, tDate, pageNo,
					pageSize);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return tranHistoryDtoList;
	}

	@GetMapping(value = "/getAgreementTranHistoryOdHeader", produces = MediaType.APPLICATION_JSON_VALUE)
	public VAgrTranHistoryOdHeaderDto getAgreementTranHistoryOdHeader(@RequestParam String masterAgreement)
			throws Exception {
		VAgrTranHistoryOdHeaderDto tranHistoryDtoHdr = null;
		try {
			tranHistoryDtoHdr = agrService.getAgreementTranHistoryOdHeader(masterAgreement);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return tranHistoryDtoHdr;
	}

	@GetMapping(value = "/getLimitDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GetLimitDtlsDto> getLimitDetails(@RequestParam String mastAgrId) throws Exception {
		List<GetLimitDtlsDto> result = new ArrayList<>();
		try {
			result = agrService.getLimitDetails(mastAgrId);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	
	// Create Rest Controller method for getAgreementAmortVariationList
	 @GetMapping(value = "/getAgreementAmortVariationList", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<RepayVariationDto>> getAgreementAmortVariationList( 
	    		@RequestParam String masterAgreement,
	            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
	            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	            @RequestParam(required = false, defaultValue = "variationType") String sortBy,
	            @RequestParam(required = false, defaultValue = "asc") String sortDir) throws Exception {
	        try {
	        	                                                                                                                                                                                                                                                                                       
	        	// Create pageable object
	            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
	                Sort.Direction.DESC : Sort.Direction.ASC;
	            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
	        	
	            Page<RepayVariationDto> pageResult = agrService.getAgreementAmortVariationList(masterAgreement, pageable);
	        	
	            
	        	 HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	             return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
	        	
	           
	        } catch (AgreementDataNotFoundException e) {
	            // log.error("Agreement data not found for masterAgreement: {}", masterAgreement, e);
	             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
	         } catch (Exception e) {
	             //log.error("Error retrieving agreement due list for masterAgreement: {}", masterAgreement, e);
	             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request", e);
	         }
	    }

	
}
