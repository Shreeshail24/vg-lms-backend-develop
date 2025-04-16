package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrProduct;

@Repository
public interface AgrProductRepository extends JpaRepository<AgrProduct, Integer> {
	AgrProduct findByMasterAgreementMastAgrId(String masterAgreement);
}
