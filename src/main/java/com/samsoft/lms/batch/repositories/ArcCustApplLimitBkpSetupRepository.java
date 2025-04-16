package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.ArcCustApplLimitBkpSetup;

@Repository
public interface ArcCustApplLimitBkpSetupRepository extends JpaRepository<ArcCustApplLimitBkpSetup, Integer> {

	List<ArcCustApplLimitBkpSetup> findByBkpSummaryBackupId(Integer backupId);
}
