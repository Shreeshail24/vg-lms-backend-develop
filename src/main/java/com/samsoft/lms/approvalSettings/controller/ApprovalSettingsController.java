package com.samsoft.lms.approvalSettings.controller;

import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.approvalSettings.dto.ApprovalSettingDto;
import com.samsoft.lms.approvalSettings.entities.ApprovalSetting;
import com.samsoft.lms.approvalSettings.repositories.ApprovalSettingRepository;
import com.samsoft.lms.approvalSettings.services.ApprovalSettingsService;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.core.entities.TabMstSystemParameters;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.customer.dto.CustomerSearchDto;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.las.util.PaginationUtil;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsInternalServerError;
import com.samsoft.lms.newux.exceptions.RecieptReqDetailsNotFoundException;
import com.samsoft.lms.request.dto.ChargesReqBookingDto;


@RestController
@RequestMapping(value = "/settings")
public class ApprovalSettingsController {
	
	@Autowired
	private ApprovalSettingsService approvalSettingService;
	
	@Autowired
	private ApprovalSettingRepository approvalSettingRepository;
	
	@PostMapping(value = "/approval-settings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApprovalSettingDto> createApprovalSetting(@RequestBody ApprovalSettingDto approvalSettingDto)
	        throws Exception {
	    ApprovalSettingDto result;
	    try {
	        if (approvalSettingDto.getApprovalSettingId() != null) {
	        	throw new CoreBadRequestException("A new approvalSetting cannot already have an ID.");
	           
	        } else {
	        	 result = approvalSettingService.createApprovalSetting(approvalSettingDto);
	        }
	    } catch (CoreBadRequestException e) {
	        throw e; // No need to wrap it again
	    } catch (Exception e) {
	        throw new CoreBadRequestException("An error occurred while processing the request: " + e.getMessage());
	    }
	    return ResponseEntity.ok(result);
	}
	
	
	@PutMapping("/approval-settings/{id}")
	public ResponseEntity<ApprovalSettingDto> updateApprovalSetting(
			@PathVariable(value = "id", required = false) final Integer id,
			@RequestBody ApprovalSettingDto approvalSettingDto) throws Exception {
		//log.debug("REST request to update ApprovalSetting : {}, {}", id, approvalSettingDTO);
		if (approvalSettingDto.getApprovalSettingId() == null)
		{
			throw new CoreBadRequestException("Invalid id");
		}
		if (!Objects.equals(id, approvalSettingDto.getApprovalSettingId())) {
			throw new CoreBadRequestException("Invalid ID");
		}
	
		if (!approvalSettingRepository.existsById(id)) {
			throw new CoreBadRequestException("Entity not found");
		}
				
		ApprovalSettingDto result = approvalSettingService.updateApprovalSetting(id,approvalSettingDto);
		return ResponseEntity.ok(result);
	}
	
	
	
	    @GetMapping("/approval-settings/{id}")
	    public ResponseEntity<List<ApprovalSettingDto>> getApprovalSettingById(@RequestParam Integer approvalSettingId,
	    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
	            @RequestParam(required = false, defaultValue = "approvalSettingId") String sortBy,
	            @RequestParam(required = false, defaultValue = "asc") String sortDir)
	    {
	    	  FourFinResponse<List<ApprovalSettingDto>> response = new FourFinResponse<>();
             try { 	
            	 
            	 Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
     	         Sort.Direction.DESC : Sort.Direction.ASC;
     	         Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
     	          	         
     	         Page<ApprovalSettingDto> pageResult = approvalSettingService.getApprovalSettingById(approvalSettingId, pageable);
       	         
       			 HttpHeaders headers = PaginationUtil
       					.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
       			 return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
       	           
       	        } catch (CoreBadRequestException e) {

       	            response.setHttpStatus(HttpStatus.NOT_FOUND);
       	            response.setResponseCode(HttpStatus.NOT_FOUND.value());
       	            response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
       	            response.setResponseMessage(e.getMessage());
       	            throw new CoreBadRequestException(e.getMessage());
       	        } catch (Exception e) {

       	            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
       	            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
       	            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
       	            response.setResponseMessage(e.getMessage());
       	            throw new CoreBadRequestException(e.getMessage());
       	        }    	
		
	}
	    
	    @DeleteMapping("/approval-settings/{id}")
		public ResponseEntity<Void> deleteApprovalSetting(@PathVariable Integer id) {
		//	log.debug("REST request to delete ApprovalSetting : {}", id);
			approvalSettingService.deleteApprovalSetting(id);
			 return ResponseEntity.noContent().build();
//					
		}
	 
		@GetMapping(value = "/approval-settings", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<List<ApprovalSettingDto>> getAllApprovalSettings(
	    		@RequestParam(required = false, defaultValue = "0") Integer pageNo,
	            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
				@RequestParam(required = false, defaultValue = "approvalSettingId") String sortBy,
		        @RequestParam(required = false, defaultValue = "asc") String sortDir) {
	        try {
	            // Create pageable object
	            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
	                Sort.Direction.DESC : Sort.Direction.ASC;
	            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));
	            
	            // Get paginated result
	            Page<ApprovalSettingDto> pageResult = approvalSettingService.getAllApprovalSettings(pageable);
	            
	            // Create response headers
	            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), pageResult);
	            return ResponseEntity.ok().headers(headers).body(pageResult.getContent());
	                    
	        } catch (CoreBadRequestException e) {
	            throw new CoreBadRequestException(e.getMessage());
	        } catch (Exception e) {
	            throw new CoreBadRequestException(e.getMessage());
	        }
	    }
		
		@GetMapping(value = "/approval-settings/request-type", produces = MediaType.APPLICATION_JSON_VALUE)
		public List<ApprovalSetting> getApprovalSettingByRequestType(@RequestParam String RequestType) throws Exception {
			try {
				return approvalSettingService.getApprovalSettingByRequestType(RequestType);
			} catch (Exception e) {
				throw e;
			}
		}

}
