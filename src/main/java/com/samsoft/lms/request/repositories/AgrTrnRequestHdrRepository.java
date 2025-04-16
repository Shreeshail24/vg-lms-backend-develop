package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeAndStatusResponseDto;
import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeResponseDto;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

import java.util.Date;
import java.util.List;

@Repository
public interface AgrTrnRequestHdrRepository extends JpaRepository<AgrTrnRequestHdr, Integer>{

    public AgrTrnRequestHdr findByReqId(Integer requestId);
    
    public AgrTrnRequestHdr findByReqIdAndActivityCode(Integer requestId, String activityCode);
    
	List<AgrTrnRequestHdr> findAllBydtRequestAndActivityCode (Date RequestedDate, String activityCode);

	//@Query(value = "select a.* from AgrTrnRequestHdr a, where a.activityCode = :activityCode ")
	//@Query(value = "select a.*,concat(c.sFirstName, ' ', c.sMiddleName, ' ', c.sLastName) as customerName, c.sMobile from agr_trn_request_hdr a, agr_master_agreement ma left join agr_customers c on ma.sCustomerId = c.sCustomerId where a.sMastAgrId=ma.sMastAgrId and sActivityCode = :activityCode",nativeQuery=true)
	@Query(nativeQuery=true)
	List<SrWorklistByActivityCodeResponseDto> getAllSRByActivityCode(@Param("activityCode") String activityCode);
	
	//@Query(value = "select a.*,concat(c.sFirstName, ' ', c.sMiddleName, ' ', c.sLastName) as customerName, c.sMobile from agr_trn_request_hdr a, agr_master_agreement ma left join agr_customers c on ma.sCustomerId = c.sCustomerId where a.sMastAgrId=ma.sMastAgrId and sActivityCode = :activityCode and sReqStatus = :status and sReqStatus = :otherStatus",nativeQuery=true)
	@Query(nativeQuery=true)
	List<SrWorklistByActivityCodeAndStatusResponseDto> getAllSRByActivityCodeAndStatus(@Param("activityCode") String activityCode, @Param("status") String status, @Param("otherStatus") String otherStatus);

	@Query(value = "select count(*) from agr_trn_request_hdr where sActivityCode = :activityCode", nativeQuery = true)
	Integer getSrCountByActivityCode(@Param("activityCode") String activityCode);
	
	@Query(value = "select count(*) from agr_trn_request_hdr where sActivityCode = :activityCode and (sReqStatus = :status or sReqStatus = :otherStatus)", nativeQuery = true)
	Integer getSrCountByActivityCodeANdStatus(@Param("activityCode") String activityCode, @Param("status") String status, @Param("otherStatus") String otherStatus);

	@Query(value = "select count(*) from agr_trn_request_hdr where sActivityCode = :activityCode and (sReqStatus = :status)", nativeQuery = true)
	Integer getTransactionsCountByActivityCodeANdStatus(@Param("activityCode") String activityCode, @Param("status") String status);

	@Query(value = "SELECT * FROM agr_trn_request_hdr WHERE sReqStatus IN (:statuses) ", nativeQuery = true)
	List<AgrTrnRequestHdr> getRequestsByStatuses(@Param("statuses") List<String> statuses);
	

	@Query(value = "SELECT * FROM agr_trn_request_hdr WHERE sReqStatus IN (:statuses) AND sActivityCode = :activityCode", nativeQuery = true)
	List<AgrTrnRequestHdr> getRequestsByStatusesAndActivityCode(@Param("statuses") List<String> statuses,@Param("activityCode") String activityCode);
	

	public boolean existsByMasterAgreement_MastAgrIdAndActivityCodeAndReqStatus(String masterAgreement,String activityCode, String requestStatus);


	 @Query("SELECT COUNT(a) FROM AgrTrnRequestHdr a WHERE a.userId = :userId AND a.reqStatus = 'PENDING'")
	    long countPendingRequestsByUserId(@Param("userId") String userId);
	 
	@Query("SELECT h FROM AgrTrnRequestHdr h WHERE h.reqStatus = :status AND h.reqId IN :requestIds")
	List<AgrTrnRequestHdr> getRequestsByStatusAndIds(@Param("status") String status, @Param("requestIds") List<Integer> requestIds);


	@Query("SELECT h FROM AgrTrnRequestHdr h WHERE h.dtRequest = :date AND h.reqId IN :requestIds")
	List<AgrTrnRequestHdr> findAllBydtRequestAndIds(@Param("date") Date dtRequest, @Param("requestIds") List<Integer> requestIds);
	
	List<AgrTrnRequestHdr> findAllByReqId(Integer reqId);

}

//select a.*,concat(c.sFirstName, ' ', c.sMiddleName, ' ', c.sLastName) as customerName, c.sMobile from agr_trn_request_hdr a, agr_master_agreement ma left join agr_customers c on ma.sCustomerId = c.sCustomerId where a.sMastAgrId=ma.sMastAgrId and sActivityCode = 'RECEIPT'