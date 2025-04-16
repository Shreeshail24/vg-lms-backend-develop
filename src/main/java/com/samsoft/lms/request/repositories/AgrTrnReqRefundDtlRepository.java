package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

@Repository
public interface AgrTrnReqRefundDtlRepository extends JpaRepository<AgrTrnReqRefundDtl, Integer>{
	
	AgrTrnReqRefundDtl findByRequestHdrReqId(Integer reqId);

}
