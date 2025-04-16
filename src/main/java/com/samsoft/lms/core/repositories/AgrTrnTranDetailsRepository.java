package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrTrnTranDetail;

@Repository
public interface AgrTrnTranDetailsRepository extends JpaRepository<AgrTrnTranDetail, Integer> {
	List<AgrTrnTranDetail> findByEventDtlEventId(Integer eventId);	
	AgrTrnTranDetail findByTranDtlId(Integer tranDtlId);
	List<AgrTrnTranDetail> findByMasterAgrMastAgrIdAndTranCategoryOrderByTranDtlIdAsc(String mastAgrId, String tranCategory);
		
}
