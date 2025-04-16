package com.samsoft.lms.core.repositories;


import java.util.List;

import org.springframework.data.domain.Pageable;

import com.samsoft.lms.core.entities.VAgrTranHistory;

public interface VArgTranHistoryRepository extends ReadOnlyRepositoryTranHistory<VAgrTranHistory, String>{

	List<VAgrTranHistory> findAllByMastAgrId( String masterAgreement);
	
	List<VAgrTranHistory> findByMastAgrIdAndTranId( String masterAgreement, Integer TranId);

	List<VAgrTranHistory> findAllByMastAgrIdOrderByTranDtlIdAsc(String masterAgreement);


}
