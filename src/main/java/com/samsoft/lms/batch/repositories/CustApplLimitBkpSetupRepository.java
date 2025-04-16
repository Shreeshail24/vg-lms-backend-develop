package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.CustApplLimitBkpSetup;

@Repository
public interface CustApplLimitBkpSetupRepository extends JpaRepository<CustApplLimitBkpSetup, Integer> {

	List<CustApplLimitBkpSetup> findByBkpSummaryBackupId(Integer backupId);
}
