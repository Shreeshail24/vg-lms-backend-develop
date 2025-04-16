package com.samsoft.lms.instrument.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.VTrnInsBatchInstruments;
@Repository
public interface VTrnInsBatchInstrumentsRepository extends ReadOnlyRepositoryInstrument<VTrnInsBatchInstruments, Integer>{

	List<VTrnInsBatchInstruments> findAllByBatchIdAndInstrumentStatusOrderByInstrumentNo(Integer batchId, String instrumentStatus);

}
