package com.samsoft.lms.agreement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrDisbursement;

@Repository
public interface AgrDisbursementRepository extends JpaRepository<AgrDisbursement, Integer> {
	AgrDisbursement findByMastAgrMastAgrId(String mastAgrId);	
	AgrDisbursement findFirstByMastAgrMastAgrIdOrderByDisbIdAsc(String mastAgrId);
	AgrDisbursement findFirstByMastAgrMastAgrIdOrderByDisbIdDesc(String mastAgrId);

	AgrDisbursement findFirstByMastAgrMastAgrIdOrderByDtDisbDateDesc(String mastAgrId);


}
