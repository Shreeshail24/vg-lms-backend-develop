package com.samsoft.lms.instrument.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.TrnInsBatchInstruments;

@Repository
public interface TrnInsBatchInstrumentsRepository extends JpaRepository<TrnInsBatchInstruments, Integer> {

	List<TrnInsBatchInstruments> findByBatchHdrBatchId(Integer batchId);
	
	List<TrnInsBatchInstruments> findByBatchHdrBatchStatusNot(String batchStatus);

}
