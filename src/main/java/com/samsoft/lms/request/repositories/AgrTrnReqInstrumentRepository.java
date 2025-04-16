package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqInstrument;

@Repository
public interface AgrTrnReqInstrumentRepository extends JpaRepository<AgrTrnReqInstrument, Integer>{
	
	AgrTrnReqInstrument findByRequestHdrReqId(Integer reqId);
	
}
