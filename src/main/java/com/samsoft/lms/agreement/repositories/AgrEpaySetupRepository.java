package com.samsoft.lms.agreement.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrEpaySetup;

@Repository
public interface AgrEpaySetupRepository extends JpaRepository<AgrEpaySetup, Integer> {

	List<AgrEpaySetup> findAllByMastAgreementMastAgrIdAndActive(String mastAgrId, String active);
	
	List<AgrEpaySetup> findAllByMastAgreementMastAgrId(String mastAgrId);
	
	List<AgrEpaySetup> findAllByMastAgreementMastAgrIdAndActiveAndDtFromDateLessThanEqualAndDtToDateGreaterThanEqual(String mastAgrId, String active, Date businessDate, Date businessDate1);

}
