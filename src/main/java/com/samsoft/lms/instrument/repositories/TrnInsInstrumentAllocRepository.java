package com.samsoft.lms.instrument.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.TrnInsInstrumentAlloc;

@Repository
public interface TrnInsInstrumentAllocRepository extends JpaRepository<TrnInsInstrumentAlloc, Integer> {

	List<TrnInsInstrumentAlloc> findByInstrumentInstrumentIdAndInstrumentMasterAgrAndInstrumentInstrumentStatus(Integer instrumentId, String mastAgrId, String instrumentStatus);

	List<TrnInsInstrumentAlloc> findByInstrumentInstrumentIdAndTranHeadNotNull(Integer instrumentId);

	List<TrnInsInstrumentAlloc> findByInstrumentInstrumentIdAndApportionAmountGreaterThanAndTranHeadNotNull(Integer instrumentId, Double approtionmentAmount);
}
