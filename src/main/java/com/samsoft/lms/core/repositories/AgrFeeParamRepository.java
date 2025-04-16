package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrFeeParam;

@Repository
public interface AgrFeeParamRepository extends JpaRepository<AgrFeeParam, Integer>{

	//Page<AgrFeeParam> findByMasterAgreementMastAgrId(String masterAgreement, Pageable page);
	
	List<AgrFeeParam> findByMasterAgreementMastAgrId(String masterAgreement);
	
	AgrFeeParam findByMasterAgreementMastAgrIdAndFeeCode(String masterAgreement, String feeCode);
	
	List<AgrFeeParam> findByFeeEventAndMasterAgreementMastAgrId(String feeEvent, String mastAgrId);
}
