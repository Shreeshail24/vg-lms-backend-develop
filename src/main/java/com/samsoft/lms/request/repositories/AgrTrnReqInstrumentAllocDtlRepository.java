package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqInstrumentAllocDtl;

@Repository
public interface AgrTrnReqInstrumentAllocDtlRepository extends JpaRepository<AgrTrnReqInstrumentAllocDtl, Integer> {
	AgrTrnReqInstrumentAllocDtl findByInstrumentRequestHdrReqId(Integer reqId);

	List<AgrTrnReqInstrumentAllocDtl> findByInstrumentInstrumetSrNo(Integer instrumentSrNo);
}
