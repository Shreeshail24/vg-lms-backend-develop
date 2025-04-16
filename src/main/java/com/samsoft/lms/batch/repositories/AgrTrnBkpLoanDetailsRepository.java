package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.AgrTrnBkpLoanDetails;

@Repository
public interface AgrTrnBkpLoanDetailsRepository extends JpaRepository<AgrTrnBkpLoanDetails, Integer> {
	List<AgrTrnBkpLoanDetails> findAllByBkpSummaryBackupId(Integer backupId);
	
	@Transactional
	@Modifying
	void deleteAllByBkpSummaryBackupId(Integer backupId);

}
