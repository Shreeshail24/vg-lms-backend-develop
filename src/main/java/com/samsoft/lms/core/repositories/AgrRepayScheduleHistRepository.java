package com.samsoft.lms.core.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrRepayScheduleHist;

@Repository
public interface AgrRepayScheduleHistRepository extends JpaRepository<AgrRepayScheduleHist, Integer> {

	@Query(value="select max(seqNo) from AgrRepayScheduleHist where masterAgrId = :masterAgrId and dtInstallment > :businessDate")
	Integer getMaxSeqNo(@Param("masterAgrId") String masterAgrId, @Param("businessDate") Date businessDate);
	

}
