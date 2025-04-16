package com.samsoft.lms.transaction.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.VLmsGlSysTranIntAccrual;

@Repository
public interface VLmsGlSysTranIntAccrualRepository extends JpaRepository<VLmsGlSysTranIntAccrual, Integer> {

	List<VLmsGlSysTranIntAccrual> findByGlGeneratedYnAndDtTranDateBetweenAndMastAgrIdInAndTranAmountGreaterThanOrderByMastAgrId(
			String gLGeneratedYn, Date from, Date to, List<String> masterAgr, Double tranAmount);
}
