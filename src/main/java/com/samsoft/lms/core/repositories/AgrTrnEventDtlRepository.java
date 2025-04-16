package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrTrnEventDtl;

import java.util.List;

@Repository
public interface AgrTrnEventDtlRepository extends JpaRepository<AgrTrnEventDtl, Integer>{

	AgrTrnEventDtl findByTranHeaderTranId(Integer tranId);
	
	List<AgrTrnEventDtl> findByTranHeaderTranIdOrderByEventId(Integer tranId);

	AgrTrnEventDtl findByEventIdAndGlGeneratedYnIn(Integer eventId, List<String> glGeneratedYn);

	List<AgrTrnEventDtl> findByTranHeaderTranIdAndGlGeneratedYnOrderByEventId(Integer tranId, String glGeneratedYn);
}
