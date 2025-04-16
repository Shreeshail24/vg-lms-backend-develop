package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.dto.GetSumAmountDto;
import com.samsoft.lms.request.entities.AgrTrnReqSettlementPaySch;

@Repository
public interface AgrTrnReqSettlementPaySchRepository extends JpaRepository<AgrTrnReqSettlementPaySch, Integer> {

	@Query(value="select new com.samsoft.lms.batch.dto.GetSumAmountDto( sum(promiseAmount) as sumAmount) from AgrTrnReqSettlementPaySch where masterAgrId = :mastAgrId ")
	GetSumAmountDto getSumAmount(@Param("mastAgrId") String mastAgrId);
}
