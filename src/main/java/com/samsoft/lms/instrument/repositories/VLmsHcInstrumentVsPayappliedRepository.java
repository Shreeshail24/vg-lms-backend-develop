package com.samsoft.lms.instrument.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.VLmsHcInstrumentVsPayapplied;

@Repository
public interface VLmsHcInstrumentVsPayappliedRepository extends JpaRepository<VLmsHcInstrumentVsPayapplied, Integer>{

    List<VLmsHcInstrumentVsPayapplied> findByBatchId(Integer batchId);
}