package com.samsoft.lms.approvalSettings.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.approvalSettings.dto.ApprovalSettingDto;
import com.samsoft.lms.approvalSettings.entities.ApprovalSetting;
import com.samsoft.lms.approvalSettings.repositories.ApprovalSettingRepository;
import com.samsoft.lms.core.dto.AgrMasterAgreementDto;
import com.samsoft.lms.core.entities.AgrLoans;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.TabMstSystemParameters;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.customer.dto.CustomerSearchDto;
import com.samsoft.lms.customer.entities.AgrCustomer;
import com.samsoft.lms.customer.exceptions.CustomerDataNotFoundException;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.dto.CreditNoteReqDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApprovalSettingsService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ApprovalSettingRepository approvalSettingRepository;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public ApprovalSettingDto createApprovalSetting(ApprovalSettingDto approvalSettingDto) throws Exception {
		Boolean result = true;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		log.info("Refund Request Dto " + approvalSettingDto);
		try {			
            ApprovalSetting approvalSetting = new ApprovalSetting();
            approvalSetting.setApprovalSettingId(approvalSettingDto.getApprovalSettingId());
            approvalSetting.setCreatedBy(approvalSettingDto.getCreatedBy());
            approvalSetting.setDtUserDateTime(sdf.parse(approvalSettingDto.getDtUserDateTime()));
            approvalSetting.setFlowType(approvalSettingDto.getFlowType());
            approvalSetting.setLastUpdatedBy(approvalSettingDto.getLastUpdatedBy());
            approvalSetting.setRequestType(approvalSettingDto.getRequestType());
            approvalSetting.setUserId(approvalSettingDto.getUserId());
            approvalSetting.setFlowTypeDesc(approvalSettingDto.getFlowTypeDesc());
            
            approvalSettingRepository.save(approvalSetting);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return approvalSettingDto;

	}



	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public ApprovalSettingDto updateApprovalSetting(Integer Id ,ApprovalSettingDto approvalSettingDto) throws Exception {
		Boolean result = true;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		log.info("Refund Request Dto " + approvalSettingDto);
		ApprovalSetting approvalSetting;
		try {			
			
			if (approvalSettingDto.getApprovalSettingId() != null) {

				ApprovalSetting existingApprovalSetting = approvalSettingRepository.
						findByApprovalSettingId(approvalSettingDto.getApprovalSettingId());
				if (existingApprovalSetting != null) {
					approvalSetting = existingApprovalSetting;
				} else {
					approvalSetting = new ApprovalSetting();
				}
			} else {
				approvalSetting = new ApprovalSetting();
			}
           // ApprovalSetting approvalSetting = new ApprovalSetting();
            approvalSetting.setApprovalSettingId(approvalSettingDto.getApprovalSettingId());
            approvalSetting.setCreatedBy(approvalSettingDto.getCreatedBy());
            approvalSetting.setDtUserDateTime(sdf.parse(approvalSettingDto.getDtUserDateTime()));
            approvalSetting.setFlowType(approvalSettingDto.getFlowType());
            approvalSetting.setLastUpdatedBy(approvalSettingDto.getLastUpdatedBy());
            approvalSetting.setRequestType(approvalSettingDto.getRequestType());
            approvalSetting.setUserId(approvalSettingDto.getUserId());
            approvalSetting.setFlowTypeDesc(approvalSettingDto.getFlowTypeDesc());
            approvalSettingRepository.save(approvalSetting);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return approvalSettingDto;

	}
	
	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	public Page<ApprovalSettingDto> getApprovalSettingById(Integer Id, Pageable page) throws Exception {
	    Boolean result = true;
	    String dateFormat = env.getProperty("lms.global.date.format");
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    List<ApprovalSettingDto> ApprovalSettingDtoList = new ArrayList<>();
	    
	    try {            
	        ApprovalSetting existingApprovalSetting = approvalSettingRepository.findByApprovalSettingId(Id);
	        
	        if (existingApprovalSetting != null) {
	            ApprovalSettingDto approvalSettingDto = new ApprovalSettingDto();
	            approvalSettingDto.setApprovalSettingId(existingApprovalSetting.getApprovalSettingId());
	            approvalSettingDto.setCreatedBy(existingApprovalSetting.getCreatedBy());
	            approvalSettingDto.setFlowType(existingApprovalSetting.getFlowType());
	            approvalSettingDto.setLastUpdatedBy(existingApprovalSetting.getLastUpdatedBy());
	            approvalSettingDto.setRequestType(existingApprovalSetting.getRequestType());
	            approvalSettingDto.setUserId(existingApprovalSetting.getUserId());
	            approvalSettingDto.setFlowTypeDesc(existingApprovalSetting.getFlowTypeDesc());
	            ApprovalSettingDtoList.add(approvalSettingDto);

	            return pageableUtils.convertToPage(ApprovalSettingDtoList, page);
	        } else {
	            return Page.empty();
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
     		throw e;
	    }
	}

	@Transactional(rollbackFor = { RuntimeException.class, Error.class, Exception.class })
	 public void deleteApprovalSetting(Integer id) {
	 log.debug("Request to delete ApprovalSetting : {}", id);
	        approvalSettingRepository.deleteByApprovalSettingId(id);
	    }
	
	
	 @Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
	 public Page<ApprovalSettingDto> getAllApprovalSettings(Pageable pageable) throws Exception {
	      //  List<ApprovalSetting> listSetting = new ArrayList<ApprovalSetting>();
	        List<ApprovalSettingDto> listSettingResponse = new ArrayList<>();
	        try {
	        	
	           // listCustomer = custRepo.findAllByCustomerIdIgnoreCaseOrderByDtUserDateDesc(customerId);
	        	
	        	List<ApprovalSetting> listSetting = approvalSettingRepository.findAll();
	            if (listSetting.size() <= 0) {
	                throw new CoreBadRequestException("No Approval Settings Are Available");
	            }
	            for (ApprovalSetting list : listSetting) {
	            	ApprovalSettingDto ApprovalSettingResponse = new ApprovalSettingDto();
	                BeanUtils.copyProperties(list, ApprovalSettingResponse);
	                
	                ApprovalSettingResponse.setApprovalSettingId(list.getApprovalSettingId());
	                ApprovalSettingResponse.setCreatedBy(list.getCreatedBy());
	                ApprovalSettingResponse.setDtLastUpdated(list.getLastUpdatedBy());
	                ApprovalSettingResponse.setFlowType(list.getFlowType());
	                ApprovalSettingResponse.setLastUpdatedBy(list.getLastUpdatedBy());
	                ApprovalSettingResponse.setRequestType(list.getRequestType());
	                ApprovalSettingResponse.setUserId(list.getUserId());
	                ApprovalSettingResponse.setFlowTypeDesc(list.getFlowTypeDesc());

	               
	                listSettingResponse.add(ApprovalSettingResponse);
	            }
	        } catch (CustomerDataNotFoundException e) {
	            throw new CustomerDataNotFoundException(e.getMessage());
	        } catch (Exception e) {
	            throw e;
	        }
	        return pageableUtils.convertToPage(listSettingResponse, pageable);
	    }
	 
		public List<ApprovalSetting> getApprovalSettingByRequestType(String requestType) {
			try {
				List<ApprovalSetting> approvalSetting = approvalSettingRepository.findByRequestType(requestType);
				if (approvalSetting == null) {
					throw new CoreDataNotFoundException("No Approval Setting is Available " +approvalSetting);
				}
				return approvalSetting;
			} catch (Exception e) {
				throw e;
			}

		}
	 
}
