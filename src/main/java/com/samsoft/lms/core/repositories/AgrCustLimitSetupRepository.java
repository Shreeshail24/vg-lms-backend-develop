package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrCustLimitSetup;

@Repository
public interface AgrCustLimitSetupRepository extends JpaRepository<AgrCustLimitSetup, Integer>{

	AgrCustLimitSetup findByMasterAgreementMastAgrId(String masterAgreement);
}
