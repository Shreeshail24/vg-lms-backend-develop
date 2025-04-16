package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrTrnTranHeader;

@Repository
public interface AgrTrnTranHeaderRepository extends JpaRepository<AgrTrnTranHeader, Integer> {

	AgrTrnTranHeader findByMasterAgrMastAgrIdAndIntrumentId(String mastAgrId, Integer instrumentId);

	List<AgrTrnTranHeader> findByTranDateBetweenAndMasterAgrMastAgrIdInOrderByMasterAgrMastAgrIdAscTranIdAsc(Date from, Date to, List<String> mastAgrIdList);

}
