package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.AgrTrnBkpSummary;

@Repository
public interface AgrTrnBkpSummaryRepository extends JpaRepository<AgrTrnBkpSummary, Integer> {
	List<AgrTrnBkpSummary> findByCustomerIdOrderByBackupIdAsc(String customerId);
	
	@Transactional
	@Modifying
//	@Query(value = "DELETE FROM agr_trn_bkp_summary WHERE sbackupid = :backupId", nativeQuery = true)
	void deleteAllByBackupId(Integer backupId);
}
