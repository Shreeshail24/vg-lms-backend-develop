package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.AgrTrnBkpTaxDueDetails;

@Repository
public interface AgrTrnBkpTaxDueDetailsRepository extends JpaRepository<AgrTrnBkpTaxDueDetails, Integer> {

	@Transactional
	@Modifying
	void deleteAllByBkpDueDetailBkpLoanDetailsBkpSummaryBackupId(Integer backupId);

	List<AgrTrnBkpTaxDueDetails> findByBkpDueDetailDueBackupId(Integer dueBackupId);

}
