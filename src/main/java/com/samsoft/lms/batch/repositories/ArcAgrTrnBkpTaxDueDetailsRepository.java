package com.samsoft.lms.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.entities.ArcAgrTrnBkpTaxDueDetails;

@Repository
public interface ArcAgrTrnBkpTaxDueDetailsRepository extends JpaRepository<ArcAgrTrnBkpTaxDueDetails, Integer> {

	@Transactional
	@Modifying
	void deleteAllByBkpDueDetailBkpLoanDetailsBkpSummaryBackupId(Integer backupId);

}
