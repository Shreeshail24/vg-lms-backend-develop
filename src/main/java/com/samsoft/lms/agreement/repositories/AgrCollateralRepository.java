package com.samsoft.lms.agreement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrCollateral;

@Repository
public interface AgrCollateralRepository extends JpaRepository<AgrCollateral, Integer>{
	
	List<AgrCollateral> findByMastAgrMastAgrId(String mastAgrId);

}
