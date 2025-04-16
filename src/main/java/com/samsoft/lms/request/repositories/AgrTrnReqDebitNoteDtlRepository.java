package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqDebitNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnRequestHdr;

@Repository
public interface AgrTrnReqDebitNoteDtlRepository extends JpaRepository<AgrTrnReqDebitNoteDtl, Integer>{
	
	AgrTrnReqDebitNoteDtl findByRequestHdrReqId(Integer reqId);

}
