package com.samsoft.lms.transaction.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.VLmsGlTranDtl;

@Repository
public interface VLmsGlTranDtlRepository extends JpaRepository<VLmsGlTranDtl, Integer> {

	List<VLmsGlTranDtl> findByTranIdAndTranAmountGreaterThanAndEventIdOrderByTranDtlId(Integer tranId, Double tranAmount, Integer eventId);

}
