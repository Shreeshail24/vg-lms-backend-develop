package com.samsoft.lms.transaction.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnLimitDtls;

@Repository
public interface AgrTrnLimitDtlsRepository extends JpaRepository<AgrTrnLimitDtls, Integer> {

	List<AgrTrnLimitDtls> findByMasterAgreementMastAgrIdOrderByLimitTranId(String mastAgrId);

}
