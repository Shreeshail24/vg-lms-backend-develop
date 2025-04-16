package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.CustApplLimitSetup;

@Repository
public interface CustApplLimitSetupRepository extends JpaRepository<CustApplLimitSetup, Integer> {

	CustApplLimitSetup findByOriginationApplnNoAndCustomerIdAndProductCode(String originationApplnNo, String customerId, String productCode);

	List<CustApplLimitSetup> findByCustomerId(String customerId);

	CustApplLimitSetup findByCustApplLimitId(Integer custApplLimitId);
	
	CustApplLimitSetup findByOriginationApplnNo(String originationApplnNo);

	List<CustApplLimitSetup> findByAgencyId(Integer agencyId);

}
