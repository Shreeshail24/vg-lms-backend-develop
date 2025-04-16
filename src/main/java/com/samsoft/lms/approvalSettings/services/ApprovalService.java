package com.samsoft.lms.approvalSettings.services;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.approvalSettings.entities.JwtUtil;
import com.samsoft.lms.core.entities.TabMstSystemParameters;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.repositories.TabMstSystemParametersRepository;
import com.samsoft.lms.core.services.CommonServices;
import com.samsoft.lms.las.util.PageableUtils;
import com.samsoft.lms.request.dto.CreditNoteReqDto;
import com.samsoft.lms.request.dto.PendingForApprovalDto;
import com.samsoft.lms.request.entities.AgrTrnReqCreditNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnReqInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;
import com.samsoft.lms.request.repositories.AgrTrnReqInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnReqPayoutInstrumentRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestHdrRepository;
import com.samsoft.lms.request.repositories.AgrTrnRequestStatusRepository;
import com.samsoft.lms.transaction.exceptions.TransactionDataNotFoundException;
import com.samsoft.lms.transaction.exceptions.TransactionInternalServerError;
import com.samsoft.lms.userRest.dto.SecurityRoleDTO;
import com.samsoft.lms.userRest.dto.SecurityUserDTO;
import com.samsoft.lms.userRest.service.UserRestTamplateService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
	public class ApprovalService {
		
		@Autowired
		private TabMstSystemParametersRepository systemParameterRepo;
		
		@Autowired
		private AgrTrnRequestHdrRepository hdrRepo;
		
		@Autowired
		private UserRestTamplateService userRestTempalteService;
		
		@Autowired
		private AgrTrnRequestStatusRepository statusRepo;
		
		@Autowired
		private AgrTrnRequestHdrRepository requestHeaderRepo;
		
		@Autowired
		private AgrTrnRequestStatusRepository agrTrnRequestStatus;
		
		@Autowired
		private JwtUtil jwtUtil;
		
		@Autowired
		private Environment env;
		
		@Autowired
		private AgrTrnReqInstrumentRepository agrTrnReqInstrumentRepository;

		@Autowired
		private AgrTrnReqPayoutInstrumentRepository agrTrnReqPayoutInstrumrntRepository;
		
		@Autowired
		private PageableUtils pageableUtils;
		

		@Transactional
	    public String allocateRequest(Integer prm_RequestID, String prm_AllocateToUserID, String prm_Reason, String prm_Remark, String status, String activityCode) {
	        boolean allocatedYN = false;
	        String returnMessage;
	        	if (prm_AllocateToUserID != null  && !prm_AllocateToUserID.trim().isEmpty()){
	            insertRequestStatus(prm_RequestID, prm_AllocateToUserID, prm_Reason, prm_Remark, status);
	            allocatedYN = true;
	            
	         } else if (prm_AllocateToUserID == null)  {

	            String mReqUserID = getUserIDByRequestID(prm_RequestID);
	            long userId = Long.parseLong(mReqUserID); 
				    Optional<String> jwtToken = jwtUtil.getCurrentUserJWT();
				    String authHeader = jwtToken.map(token -> "Bearer " + token).orElse(null);
	             
	             long securityRoleId = 0;
	             String securityRoleName ="";
	            ResponseEntity<SecurityUserDTO> userObject = userRestTempalteService.getSecurityUser(authHeader,userId);
	            SecurityUserDTO user = userObject.getBody();
	            boolean isAdmin = user.getSecurityRoles().stream()
	                    .anyMatch(role -> role.getRoleName().equalsIgnoreCase("LMS_ADMIN"));
	            
	            if (isAdmin) {
	                insertRequestStatus(prm_RequestID, mReqUserID, "ALLOCATED", "Auto Allocation", status);
	                return "Success"; // Immediately return success if allocated
	            }

	            Set<SecurityRoleDTO> roles = user.getSecurityRoles();
	            for (SecurityRoleDTO securityRole : roles)
	            {
	            	if(activityCode.equals("RECEIPT") || activityCode.equals("REFUND") || 
	            			activityCode.equals("FORECLOSURE") || activityCode.equals("PART_PREPAYMENT") || 
	            			activityCode.equals("CREDIT_NOTE") || activityCode.equals("DEBIT_NOTE") || activityCode.equals("CHARGES_WAIVER")
	            			|| activityCode.equals("CHARGES_BOOKING"))
	            	{
	            		if(securityRole.getRoleName().equals("LMS_OPS")) {
	            			
	            			securityRoleId = securityRole.getId();
	            			break;
	            		}
	            		
	            		else if (securityRole.getRoleName().equals("LMS_OFFICER")) {
	            			
	            			securityRoleId = securityRole.getId();
	            		}
	            		
	            	}

	            }

	            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
	            List<SecurityUserDTO> mUserListByRole = userRestTempalteService.getAllSecurityUsers(authHeader, securityRoleId, pageable);
	           // long mPendingReqPool = getValueBySysParaCode("PENDING_REQ_POOL_SIZE");       
	            
//	            for (SecurityUserDTO securityUser : mUserListByRole)
//	            {
//	           
//	            	String userID = securityUser.getId().toString();
//	            	long userIDs =securityUser.getId();	            	
//	                if (userID.equals(mReqUserID)) {
//	                    continue;  // Skip this user and check the next one
//	                }
//
//	                long mPendingReq = getPendingRequestByUserID(userIDs);
//	                if (mPendingReqPool > mPendingReq) {
//	                    insertRequestStatus(prm_RequestID, userID, "ALLOCATED", "Auto Allocation", status);
//	                    allocatedYN = true;
//	                    break;  // Exit loop after allocating
//	                }
//	            	
//	            }
	            
	            long mPendingReqPool = getValueBySysParaCode("PENDING_REQ_POOL_SIZE");
	            SecurityUserDTO selectedUser = null;
	            long minPendingReq = Long.MAX_VALUE;

	            
	            for (SecurityUserDTO securityUser : mUserListByRole) {
	                long userIDs = securityUser.getId();
	                
	                // Skip if this is the same user who made the request
	                if (userIDs == Long.parseLong(mReqUserID)) {
	                    continue;
	                }

	                long mPendingReq = getPendingRequestByUserID(userIDs);

	                // Find the user with the least pending requests
	                if (mPendingReq < minPendingReq) {
	                    minPendingReq = mPendingReq;
	                    selectedUser = securityUser;
	                }
	            }
	            
	            if (selectedUser != null && minPendingReq < mPendingReqPool) {
	                insertRequestStatus(prm_RequestID, selectedUser.getId().toString(), "ALLOCATED", "Auto Allocation", status);
	                allocatedYN = true;
	                System.out.println("Request allocated to user: " + selectedUser.getId());
	            } else {
	                System.out.println("No suitable user found for allocation.");
	            }



	        }
	         

	        if (allocatedYN) {
	            returnMessage = "Success";
	        } else {
	            returnMessage = "Failed";
	        }

	        return returnMessage;
	    }

	    private void insertRequestStatus(Integer requestId, String userId, String reason, String remark, String status) {
	        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
	        AgrTrnRequestStatus reqS; 

	        AgrTrnRequestHdr hdr = requestHeaderRepo.findByReqId(requestId);

	 	if (hdr.getReqId() != null) {

			AgrTrnRequestStatus existingRequestStatus = agrTrnRequestStatus.findByRequestHdrReqId(hdr.getReqId());
			
					
			if (existingRequestStatus != null) {
				reqS = existingRequestStatus;
			} else {
				reqS = new AgrTrnRequestStatus();
			}
		} else {
			reqS = new AgrTrnRequestStatus();
		}
	        
	       
	       reqS.setUserId(userId);
	       reqS.setReqStatus(status);
	       reqS.setReason(reason);
	       reqS.setRemark(remark);
	       reqS.setDtReqChangeDate(hdr.getDtRequest());
	    //   reqS.setDtReqChangeDate(currentTimestamp);
	      reqS.setRequestHdr(hdr);
	     //  reqS.setRequestHdr(reqS.getRequestHdr().getReqId());
	       statusRepo.save(reqS);
	    }
	    	    
	    private String getUserIDByRequestID(Integer requestNo) {
	        if (requestNo == null || requestNo == 0) {
	            throw new IllegalArgumentException("Invalid request ID: " + requestNo);
	        }

	        AgrTrnRequestHdr agrTrnRequestHdr = hdrRepo.findByReqId(requestNo);
	        if (agrTrnRequestHdr == null) {
	            throw new CoreDataNotFoundException("No record found for the given request ID: " + requestNo);
	        }

	        // Get user ID and convert to Integer
	        String userIdStr = agrTrnRequestHdr.getUserId();
	        
	        try {
	            return userIdStr;
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid user ID format: " + userIdStr);
	        }
	    }
	     
	    private int getValueBySysParaCode(String paramCode) {
	        TabMstSystemParameters systemParameter = systemParameterRepo.findBysysParaCode(paramCode);
	        int reqCount = 0; // Default value to handle cases where parsing fails or parameter is null

	        if (systemParameter != null && systemParameter.getSysParaValue() != null) {
	            try {
	                reqCount = Integer.parseInt(systemParameter.getSysParaValue());
	            } catch (NumberFormatException e) {
	                // Log the exception and return default value
	                System.err.println("Invalid number format for system parameter: " + paramCode);
	            }
	        }
	        return reqCount;
	    }

	    private long getPendingRequestByUserID(long userId) {
	        // Simulate fetching pending request count for a user
	    	String userIdno = String.valueOf(userId) ;
	    	// long noOfPendingReq = hdrRepo.countPendingRequestsByUserId(userIdno);
	    	 long noOfPendingReq = statusRepo.countPendingRequestsByUserId(userIdno);
	    	 
	        return noOfPendingReq; // Example pending requests count
	    }
	

	    public Page<PendingForApprovalDto> getPendingForApprovalReqList(
	            String status, Integer reqNo, String date, String userid, Boolean isAdminAllocated, Pageable pageable) throws Exception {

	        List<PendingForApprovalDto> pendingForApprovalReqDtoList = new ArrayList<>();

	        try {
	            List<AgrTrnRequestHdr> agrTrnRequestHeaderList = new ArrayList<>();
	            List<Integer> requestIds = new ArrayList<>();

	            // Step 1: Fetch request IDs based on user role
	            if (userid != null) {
	                if (Boolean.TRUE.equals(isAdminAllocated)) {
	                    // If user is admin, fetch only their request IDs
	                    requestIds = agrTrnRequestStatus.findRequestIdsByUserId(userid);
	                } 
	                else 
	                {
	                    // If user is not admin, fetch request IDs of all users
	                    requestIds = agrTrnRequestStatus.findRequestIdsByStatus(status);
	                }
	            } 

	            if (requestIds.isEmpty()) {
	                throw new CoreDataNotFoundException("No requests found for the provided filters.");
	            }

	            // Step 2: Convert date string to Date object if provided
	            Date requestedDate = null;
	            if (date != null && !date.trim().isEmpty() && !"null".equalsIgnoreCase(date)) {
	                SimpleDateFormat sdf = new SimpleDateFormat(env.getProperty("lms.global.date.format"));
	                requestedDate = sdf.parse(date);
	            }

	            // Step 3: Apply filtering conditions
	            if (requestedDate != null) {
	                agrTrnRequestHeaderList = requestHeaderRepo.findAllBydtRequestAndIds(requestedDate, requestIds);
	            } else if (reqNo != 0) {
	                agrTrnRequestHeaderList = requestHeaderRepo.findAllByReqId(reqNo);
	            } else if ("PENDING".equals(status)) {
	                agrTrnRequestHeaderList = requestHeaderRepo.getRequestsByStatusAndIds(status, requestIds);
	            }

	            if (agrTrnRequestHeaderList.isEmpty()) {
	                throw new CoreDataNotFoundException("No records found for the requested filters.");
	            }

	            // Step 4: Map to DTOs
	            for (AgrTrnRequestHdr agrTrnRequestHeader : agrTrnRequestHeaderList) {
	                PendingForApprovalDto dto = new PendingForApprovalDto();
	                dto.setDtRequestDate(agrTrnRequestHeader.getDtRequest().toString());
	                dto.setRemark(agrTrnRequestHeader.getRemark());
	                dto.setRequestStatus(agrTrnRequestHeader.getReqStatus());
	                dto.setReqId(agrTrnRequestHeader.getReqId());
	                dto.setActivityCode(agrTrnRequestHeader.getActivityCode());
	                dto.setMastAgrId(agrTrnRequestHeader.getMasterAgreement().getMastAgrId());

	                AgrTrnRequestStatus agrTrnReqStatus = agrTrnRequestStatus.findByRequestHdrReqId(agrTrnRequestHeader.getReqId());
	                if (agrTrnReqStatus != null) {
	                    dto.setUserId(agrTrnReqStatus.getUserId());
	                }

	                if ("REFUND".equals(agrTrnRequestHeader.getActivityCode())) {
	                    AgrTrnReqPayoutInstrument payoutInstrument = agrTrnReqPayoutInstrumrntRepository.findByRequestHdrReqId(agrTrnRequestHeader.getReqId());
	                    if (payoutInstrument != null) {
	                        dto.setAmount(payoutInstrument.getInstrumentAmount());
	                        dto.setPaymentMode(payoutInstrument.getPayMode());
	                    }
	                } else {
	                    AgrTrnReqInstrument instrument = agrTrnReqInstrumentRepository.findByRequestHdrReqId(agrTrnRequestHeader.getReqId());
	                    if (instrument != null) {
	                        dto.setAmount(instrument.getInstrumentAmount());
	                        dto.setPaymentMode(instrument.getPayMode());
	                    }
	                }

	                pendingForApprovalReqDtoList.add(dto);
	            }

	            return pageableUtils.convertToPage(pendingForApprovalReqDtoList, pageable);

	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }

	    
	    
	    public int getPendingForApprovalRequest(String reqStatus,String userId) {
			try {

				List<AgrTrnRequestStatus> pendingRequests = statusRepo.findByReqStatusAndUserId(reqStatus,userId);

				if (pendingRequests == null || pendingRequests.isEmpty()) {
					throw new TransactionDataNotFoundException("No pending requests found for status: " + reqStatus);
				}
			

				return pendingRequests.size();
			}
			catch (Exception e) {

				throw new TransactionInternalServerError(
						"Error occurred while fetching pending requests: " + e.getMessage());
			}
	}


	}


	




	

