package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqCreditNoteDtl;
import com.samsoft.lms.request.entities.AgrTrnReqDebitNoteDtl;

@Repository
public interface AgrTrnReqCreditNoteDtlRepository extends JpaRepository<AgrTrnReqCreditNoteDtl, Integer>{

	AgrTrnReqCreditNoteDtl findByRequestHdrReqId(Integer reqId);

}
