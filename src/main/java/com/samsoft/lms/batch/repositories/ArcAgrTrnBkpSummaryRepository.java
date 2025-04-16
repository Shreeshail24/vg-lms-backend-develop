package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.ArcAgrTrnBkpSummary;

@Repository
public interface ArcAgrTrnBkpSummaryRepository extends JpaRepository<ArcAgrTrnBkpSummary, Integer> {
	List<ArcAgrTrnBkpSummary> findByCustomerIdOrderByBackupIdAsc(String customerId);
	
	@Transactional
	@Modifying
//	@Query(value = "DELETE FROM agr_trn_bkp_summary WHERE sbackupid = :backupId", nativeQuery = true)
	void deleteAllByBackupId(Integer backupId);
}
