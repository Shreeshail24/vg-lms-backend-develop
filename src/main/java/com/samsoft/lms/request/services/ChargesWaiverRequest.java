package com.samsoft.lms.request.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.approvalSettings.repositories.ApprovalSettingRepository;
import com.samsoft.lms.approvalSettings.services.ApprovalService;
import com.samsoft.lms.core.dto.VAgrInterestAccrualHistoryDto;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.VAgrInterestAccrualHistory;
import com.samsoft.lms.core.entities.VAgrTrnChargesWaiver;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.core.repositories.VAgrTrnWaiverChargesRepository;
import com.samsoft.lms.las.util.PageableUtils;

import com.samsoft.lms.request.dto.ChargesWaiverDto;
import com.samsoft.lms.request.dto.ChargesWaiverGridDto;
import com.samsoft.lms.request.dto.ChargesWaiverParamListDto;
import com.samsoft.lms.request.dto.PartPrepaymentDto;
import com.samsoft.lms.request.entities.AgrTrnReqChargesWaiverDtl;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesBookingDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqChargesWaiverDtlRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.dto.ChargesWaiverApplicationDto;
import com.samsoft.lms.transaction.services.ChargesWaiverApplicationService;

@Service
public class ChargesWaiverRequest {

	@Autowired
	private AgrTrnRequestHdrRepository reqHdrRepo;

	@Autowired
	private AgrMasterAgreementRepository masterRepo;

	@Autowired
	private AgrTrnReqChargesBookingDtlRepository chargeRepo;

	@Autowired
	private AgrTrnReqChargesWaiverDtlRepository waiverRepo;

	@Autowired
	private AgrTrnRequestStatusRepository statusRepo;

	@Autowired
	private ChargesWaiverApplicationService waiverService;

	@Autowired
	private Environment env;
	
	@Autowired
	private PageableUtils pageableUtils;
	
	@Autowired
	private VAgrTrnWaiverChargesRepository waiverChargesRepo;
	
	@Autowired
	private ApprovalService approvalService;
	

	@Transactional(rollbackFor = {RuntimeException.class, Exception.class, Error.class})
	public String chargesWaiverRequest(ChargesWaiverDto chargesWaiverdto) throws Exception {
	    String result = "";
	    try {
	        String dateFormat = env.getProperty("lms.global.date.format");
	        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	        
	        // Check if a request already exists for the given master agreement ID
	        if(chargesWaiverdto.getReqId() == null)
	        {
	           String mastAgrId  = chargesWaiverdto.getMastAgrId();
	       
	        boolean exists = reqHdrRepo.existsByMasterAgreement_MastAgrIdAndActivityCodeAndReqStatus(mastAgrId,"CHARGES_WAIVER","PENDING");
	        if (exists) {
	            throw new IllegalArgumentException("A request already exists for the given Master Agreement ID.");
	        }
	        
	       }
	        List<AgrTrnReqChargesWaiverDtl> waiverList = new ArrayList<>();
	        AgrMasterAgreement master = masterRepo.findByMastAgrId(chargesWaiverdto.getMastAgrId());
	        AgrTrnRequestHdr reqHdr = new AgrTrnRequestHdr();
	        reqHdr.setMasterAgreement(master);
	        reqHdr.setDtRequest(sdf.parse(chargesWaiverdto.getRequestDate()));
	        reqHdr.setActivityCode("CHARGES_WAIVER");
	        reqHdr.setReqStatus(chargesWaiverdto.getRequestStatus());
	        reqHdr.setFlowType(chargesWaiverdto.getFlowType());
	        if ("NoFlow".equals(chargesWaiverdto.getFlowType())) {
	        	reqHdr.setReqStatus("APPROVED");
			} else {
				reqHdr.setReqStatus(chargesWaiverdto.getRequestStatus());
			}
	        reqHdr.setReason(chargesWaiverdto.getReason());
	        reqHdr.setRemark(chargesWaiverdto.getRemark());
	        reqHdr.setUserId(chargesWaiverdto.getUserId());
	        if (chargesWaiverdto.getReqId() != null) {
	            reqHdr.setReqId(chargesWaiverdto.getReqId());
	        }
	        
            reqHdrRepo.save(reqHdr);
               
            if (chargesWaiverdto.getReqId() != null) {
	            // If reqId is not null, fetch the existing records
	            List<AgrTrnReqChargesWaiverDtl> existingWaivers = waiverRepo.findByRequestHdrReqId(chargesWaiverdto.getReqId());
	            Map<Integer, AgrTrnReqChargesWaiverDtl> existingWaiversMap = existingWaivers.stream()
	                .collect(Collectors.toMap(AgrTrnReqChargesWaiverDtl::getChargeBookTranId, waiver -> waiver));
	            
	            for (ChargesWaiverParamListDto param : chargesWaiverdto.getWaiverParamList()) {
	                AgrTrnReqChargesWaiverDtl waiver;
	                if (existingWaiversMap.containsKey(param.getChargeTranId())) {
	                    // Update the existing waiver record
	                    waiver = existingWaiversMap.get(param.getChargeTranId());
	                } else {
	                    // Create a new waiver record
	             
	                    waiver = new AgrTrnReqChargesWaiverDtl();
	                }
	                waiver.setMastAgrId(chargesWaiverdto.getMastAgrId());
	                waiver.setLoanId(chargesWaiverdto.getLoanId());
	                waiver.setChargeBookTranId(param.getChargeTranId());
	                waiver.setDtChargeWaiver(sdf.parse(chargesWaiverdto.getRequestDate()));
	                waiver.setTranCategory("FEE");
	                waiver.setTranHead(param.getTranHead());
	                waiver.setChargeWaiveAmount(param.getWaiverAmount());
	                waiver.setInstallmentNo(param.getInstallmentNo());
	                waiver.setReasonCode(param.getWaiverReason());
	                waiver.setRemark(param.getWaiverRemark());
	                waiver.setUserId(chargesWaiverdto.getUserId());
	                waiver.setRequestHdr(reqHdr);

	                waiverList.add(waiver);
	            }
	        } else {
	            for (ChargesWaiverParamListDto param : chargesWaiverdto.getWaiverParamList()) {
	                AgrTrnReqChargesWaiverDtl waiver = new AgrTrnReqChargesWaiverDtl();
	                waiver.setMastAgrId(chargesWaiverdto.getMastAgrId());
	                waiver.setLoanId(chargesWaiverdto.getLoanId());
	                waiver.setChargeBookTranId(param.getChargeTranId());
	                waiver.setDtChargeWaiver(sdf.parse(chargesWaiverdto.getRequestDate()));
	                waiver.setTranCategory("FEE");
	                waiver.setTranHead(param.getTranHead());
	                waiver.setChargeWaiveAmount(param.getWaiverAmount());
	                waiver.setInstallmentNo(param.getInstallmentNo());
	                waiver.setReasonCode(param.getWaiverReason());
	                waiver.setRemark(param.getWaiverRemark());
	                waiver.setUserId(chargesWaiverdto.getUserId());
	                waiver.setRequestHdr(reqHdr);

	                waiverList.add(waiver);
	            }
	        }

	        waiverRepo.saveAll(waiverList);

//	        AgrTrnRequestStatus status;
//	        if (chargesWaiverdto.getReqId() != null) {
//	            AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(chargesWaiverdto.getReqId());
//	            if (existingRequestStatus != null) {
//	                status = existingRequestStatus;
//	            } else {
//	                status = new AgrTrnRequestStatus();
//	            }
//	        } else {
//	            status = new AgrTrnRequestStatus();
//	        }
//
//	        status.setReqStatus(chargesWaiverdto.getRequestStatus() != null ? chargesWaiverdto.getRequestStatus() : "PENDING");
//	        status.setReason(chargesWaiverdto.getReason());
//	        status.setRemark(chargesWaiverdto.getRemark());
//	        status.setDtReqChangeDate(sdf.parse(chargesWaiverdto.getRequestDate()));
//	        status.setUserId(chargesWaiverdto.getUesrId());
//	        status.setRequestHdr(reqHdr);
//
//	        statusRepo.save(status);
	        
			if (chargesWaiverdto.getReqId() != null) {

				AgrTrnRequestStatus existingRequestStatus = statusRepo.findByRequestHdrReqId(chargesWaiverdto.getReqId());
						
				if (existingRequestStatus != null) {
					existingRequestStatus.setRequestHdr(reqHdr);
					existingRequestStatus.setDtReqChangeDate(sdf.parse(chargesWaiverdto.getRequestDate()));
					existingRequestStatus.setReqStatus(chargesWaiverdto.getRequestStatus());
					existingRequestStatus.setReason(chargesWaiverdto.getReason());
					existingRequestStatus.setRemark(chargesWaiverdto.getRemark());
					
					statusRepo.save(existingRequestStatus);
				}
					
			} 
			
			 String userId = chargesWaiverdto.getAllocatedUserId();	
			if (("NoFlow".equals(chargesWaiverdto.getFlowType()))) {
				AgrTrnRequestStatus status = new AgrTrnRequestStatus();
				status.setRequestHdr(reqHdr);
		    	status.setUserId(chargesWaiverdto.getUserId());
	    	    status.setReqStatus("APPROVED");
	    	    status.setDtReqChangeDate(sdf.parse(chargesWaiverdto.getRequestDate()));
	    	    status.setReason(chargesWaiverdto.getReason());
	    	    status.setRemark(chargesWaiverdto.getRemark());
		     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
		       statusRepo.save(status);
				
			}
			
			else {
		        String allocationResult = approvalService.allocateRequest(reqHdr.getReqId(),userId,
		        		chargesWaiverdto.getReason(), chargesWaiverdto.getRemark(),chargesWaiverdto.getRequestStatus(),reqHdr.getActivityCode());
	
		        if (!"Success".equals(allocationResult)) {
		            throw new Exception("Request Allocation Failed");
		        }
		      }
	        
			if ("NoFlow".equalsIgnoreCase(chargesWaiverdto.getFlowType()) || 
				    ("MakerChecker".equalsIgnoreCase(chargesWaiverdto.getFlowType()) && 
				     "VERIFIED".equalsIgnoreCase(chargesWaiverdto.getRequestStatus()))) {
				
	        for (ChargesWaiverParamListDto param : chargesWaiverdto.getWaiverParamList()) {
			ChargesWaiverApplicationDto waiverApplicationDto = new ChargesWaiverApplicationDto();
			waiverApplicationDto.setMastAgrId(chargesWaiverdto.getMastAgrId());
			waiverApplicationDto.setChargeBookTranId(param.getChargeTranId());
			waiverApplicationDto.setChargeWaiveAmount(param.getWaiverAmount());
			waiverApplicationDto.setChargeWavaieRemark(param.getWaiverRemark());
			waiverApplicationDto.setChargeWaiveReason(param.getWaiverReason());
			waiverApplicationDto.setInstallmentNo(param.getInstallmentNo());
			waiverApplicationDto.setLoanId(param.getLoanId());
			waiverApplicationDto.setSource("REQ");
			waiverApplicationDto.setSourceId(Integer.toString(reqHdr.getReqId()));
			waiverApplicationDto.setTranDate(chargesWaiverdto.getRequestDate());
			waiverApplicationDto.setTranHead(param.getTranHead());
			waiverApplicationDto.setUserId(chargesWaiverdto.getUserId());

			waiverService.chargesWaiverApplication(waiverApplicationDto);
		}
			}
	    
	    } catch (Exception e) {
	        throw e;
	    }

	    return result;
	}
	
	
	// ----- Method to get Credit note requests by status, date and reqId
			public Page<ChargesWaiverDto> getWaiverChargesReqList(List<String> statusList, String activityCode,
					Integer reqNo, String date, Pageable pageable) throws Exception {

				List<ChargesWaiverDto> chargesWaiverReqDtoList = new ArrayList<>();
				try {

					List<AgrTrnRequestHdr> agrTrnRequestHdrList = new ArrayList<>();

					if (!statusList.isEmpty() || reqNo == 0 && date == null) {
						
						agrTrnRequestHdrList = reqHdrRepo.getRequestsByStatusesAndActivityCode(statusList, activityCode);
						 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
				                throw new CoreDataNotFoundException("No records found for the provided status : " + statusList);
				            }

					} else if (reqNo != 0 || statusList == null || date == null) {
						
						AgrTrnRequestHdr agrTrnRequestHdr = reqHdrRepo.findByReqIdAndActivityCode(reqNo, activityCode);
						 if (agrTrnRequestHdr == null) {
				                throw new CoreDataNotFoundException("No record found for the given request ID: " + reqNo);
				            }
						agrTrnRequestHdrList.add(agrTrnRequestHdr);

					} else if (date != null || statusList == null || reqNo == null) {
						// change string date into Date format
						String dateFormat = env.getProperty("lms.global.date.format");
						SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
						Date requestedDate = sdf.parse(date);
						agrTrnRequestHdrList = reqHdrRepo.findAllBydtRequestAndActivityCode(requestedDate, activityCode);
						
						 if (agrTrnRequestHdrList == null || agrTrnRequestHdrList.isEmpty()) {
				                throw new CoreDataNotFoundException("No records found for the requested date: " + date);
				            }	
					
					}

					if (!agrTrnRequestHdrList.isEmpty() && agrTrnRequestHdrList != null) {

						for (AgrTrnRequestHdr agrTrnRequestHdr : agrTrnRequestHdrList) {
							ChargesWaiverDto ChargesWaiverReqDto = new ChargesWaiverDto();
							
							ChargesWaiverReqDto.setReqId(agrTrnRequestHdr.getReqId());
							ChargesWaiverReqDto.setRequestDate(agrTrnRequestHdr.getDtRequest().toString());
							ChargesWaiverReqDto.setFlowType(agrTrnRequestHdr.getFlowType());
							ChargesWaiverReqDto.setMastAgrId(agrTrnRequestHdr.getMasterAgreement().getMastAgrId());
							ChargesWaiverReqDto.setReason(agrTrnRequestHdr.getReason());
							ChargesWaiverReqDto.setRemark(agrTrnRequestHdr.getRemark());
							ChargesWaiverReqDto.setRequestStatus(agrTrnRequestHdr.getReqStatus());
							ChargesWaiverReqDto.setUserId(agrTrnRequestHdr.getUserId());

							AgrTrnRequestStatus agrTrnReqStatus = statusRepo
									.findByRequestHdrReqId(agrTrnRequestHdr.getReqId());
							
						if (agrTrnReqStatus != null) {
							ChargesWaiverReqDto.setAllocatedUserId(agrTrnReqStatus.getUserId());
							
								}	
						
							chargesWaiverReqDtoList.add(ChargesWaiverReqDto);
						}
						
						
					}

					// Return a Page object for DTOs using convertToPage
					return pageableUtils.convertToPage(chargesWaiverReqDtoList, pageable);

				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			
			public String convertToDateFormat(Date date) {
				if (date == null) {
					return null;
				}
				String dateFormat = env.getProperty("lms.global.date.format");
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				return sdf.format(date);

			}
			public Page <ChargesWaiverGridDto> getWaiverChargesReqGridList(String mastAgrId, Pageable paging) throws Exception {
				//VAgrInterestAccrualHistoryMainDto main = new VAgrInterestAccrualHistoryMainDto();
				List<ChargesWaiverGridDto> waiverChargesDtoList = new ArrayList<ChargesWaiverGridDto>();
				try {
					List<VAgrTrnChargesWaiver> waiverChargesList = waiverChargesRepo.findBymastAgrId(mastAgrId);
					if (waiverChargesList.size() <= 0) {
						throw new AgreementDataNotFoundException("No Data available for master agreement "
								+ mastAgrId);
					}
				    
				     	waiverChargesDtoList = waiverChargesList.stream().map(waiveCharges -> {
					    	ChargesWaiverGridDto waiverChargeDto = new ChargesWaiverGridDto();
					    BeanUtils.copyProperties(waiveCharges, waiverChargeDto);
					//    intAccDto.setDtTranDate(convertToDateFormat(intAcc.getDtTranDate()));
					    waiverChargeDto.setDueDtlId(waiveCharges.getDueDetailId());
					    waiverChargeDto.setDueHead(waiveCharges.getDueHead());
					    waiverChargeDto.setMastAgrId(waiveCharges.getMastAgrId());
					    waiverChargeDto.setTaxAmount(waiveCharges.getTaxAmount());
					    waiverChargeDto.setTranAmount(waiveCharges.getTransactionAmount());
					    waiverChargeDto.setTranDtlId(waiveCharges.getTransactionDetailId());
					    waiverChargeDto.setTranId(waiveCharges.getTransactionId());
					    waiverChargeDto.setTranType(waiveCharges.getTransactionType());
					    waiverChargeDto.setWaivableAmount(waiveCharges.getWaivableAmount());
					    waiverChargeDto.setTranDate(convertToDateFormat(waiveCharges.getTransactionDate()));
					    return waiverChargeDto;
					}).collect(Collectors.toList());

				        return pageableUtils.convertToPage(waiverChargesDtoList, paging);

				} catch (AgreementDataNotFoundException e) {
					throw new AgreementDataNotFoundException(e.getMessage());
				} catch (Exception e) {
					throw e;
				}
				
				
			}

}
