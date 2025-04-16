package com.samsoft.lms.request.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;
import com.samsoft.lms.request.entities.AgrTrnRequestStatus;

@Repository
public interface AgrTrnRequestStatusRepository extends JpaRepository<AgrTrnRequestStatus, Integer>{

	AgrTrnRequestStatus findByRequestHdrReqId(Integer reqId);
	
	@Query("SELECT COUNT(a) FROM AgrTrnRequestStatus a WHERE a.userId = :userId AND a.reqStatus = 'PENDING'")
    long countPendingRequestsByUserId(@Param("userId") String userId);
	
	@Query(value = "SELECT * FROM AgrTrnRequestStatus WHERE sReqStatus = :status", nativeQuery = true)
	List<AgrTrnRequestStatus> getRequestsByStatus(@Param("status") String status);
	
	@Query("SELECT r.requestHdr.reqId FROM AgrTrnRequestStatus r WHERE r.userId = :userId")
	List<Integer> findRequestIdsByUserId(@Param("userId") String userId);

	List<AgrTrnRequestStatus> findByReqStatusAndUserId(String reqStatus, String userId);

	@Query("SELECT r.requestHdr.reqId FROM AgrTrnRequestStatus r WHERE r.reqStatus = :status")
	List<Integer> findRequestIdsByStatus(@Param("status") String status);
}
