package com.samsoft.lms.instrument.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.TrnInsBatchHdr;

@Repository
public interface TrnInsBatchHdrRepository extends JpaRepository<TrnInsBatchHdr, Integer>{
	
	TrnInsBatchHdr findByBatchIdAndBatchStatusIn(Integer batchId, List<String> batchStatus);
	
	List<TrnInsBatchHdr> findByBatchStatus(String batchStatus);
	 
	List<TrnInsBatchHdr> findByInstrumentTypeAndBatchStatusAndDtBatchDateBetween(String instrumentType, String batchStatus, Date fromDate, Date toDate);

	List<TrnInsBatchHdr> findByInstrumentTypeAndBatchStatus(String instrumentType, String batchStatus);
}
