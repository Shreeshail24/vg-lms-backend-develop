package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqPrepaymentHdr;

@Repository
public interface AgrTrnReqPrepaymentHdrRepository extends JpaRepository<AgrTrnReqPrepaymentHdr, Integer>{

	AgrTrnReqPrepaymentHdr findByRequestHdrReqId(int string);
	
}
