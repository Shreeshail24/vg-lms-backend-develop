package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqSettlementDtl;
import com.samsoft.lms.request.entities.AgrTrnReqWriteoffDtl;

@Repository
public interface AgrTrnReqSettlementDtlRepository extends JpaRepository<AgrTrnReqSettlementDtl, Integer>{

	List<AgrTrnReqSettlementDtl> findBySettlementIdSettlementId(Integer settlementId);
}
