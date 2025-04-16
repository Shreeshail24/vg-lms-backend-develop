package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqSettlement;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoff;

@Repository
public interface AgrTrnReqSettlementRepository extends JpaRepository<AgrTrnReqSettlement, Integer> {
	AgrTrnReqSettlement findByMasterAgrIdAndRequestHdrReqId(String mastAgrId, Integer reqId);
}
