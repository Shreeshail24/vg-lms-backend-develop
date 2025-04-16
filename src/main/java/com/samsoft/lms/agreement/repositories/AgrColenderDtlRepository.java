package com.samsoft.lms.agreement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrColenderDtl;

@Repository
public interface AgrColenderDtlRepository extends JpaRepository<AgrColenderDtl, Integer>{
	
	List<AgrColenderDtl> findByMasterAgrMastAgrId(String mastAgrId);
	AgrColenderDtl findByCoLendId(Integer coLendId);
	AgrColenderDtl findByMasterAgrMastAgrIdAndInstrumentPresenterYn(String mastAgrId, String instrumentPresenterYn);


}
