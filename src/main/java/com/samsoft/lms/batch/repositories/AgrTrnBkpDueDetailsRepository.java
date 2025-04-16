package com.samsoft.lms.batch.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.dto.DueDetailsDto;
import com.samsoft.lms.batch.entities.AgrTrnBkpDueDetails;

@Repository
public interface AgrTrnBkpDueDetailsRepository extends JpaRepository<AgrTrnBkpDueDetails, Integer> {

	@Query(nativeQuery = true)
	List<DueDetailsDto> getDueDetails(@Param("loanBackupId") Integer loanBackupId,
			@Param("dueCategory") String dueCategory);

	@Transactional
	@Modifying
	void deleteAllByBkpLoanDetailsBkpSummaryBackupId(Integer backupId);

	List<AgrTrnBkpDueDetails> findByBkpLoanDetailsLoanBackupId(Integer loanBackupId);
}
