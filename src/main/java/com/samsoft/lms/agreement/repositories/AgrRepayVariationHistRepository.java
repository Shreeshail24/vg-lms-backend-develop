package com.samsoft.lms.agreement.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrRepayVariationHist;

@Repository
public interface AgrRepayVariationHistRepository extends JpaRepository<AgrRepayVariationHist, Integer> {

	@Query(value = "SELECT max(seqNo) FROM AgrRepayVariationHist s WHERE s.loanId = :loanId ")
	Integer getMaxSeqNo(@Param("loanId") String loanId);

}
