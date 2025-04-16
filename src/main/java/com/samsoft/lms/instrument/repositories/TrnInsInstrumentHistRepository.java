package com.samsoft.lms.instrument.repositories;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.TrnInsInstrument;
import com.samsoft.lms.instrument.entities.TrnInsInstrumentHist;

@Repository
public interface TrnInsInstrumentHistRepository extends JpaRepository<TrnInsInstrumentHist, Integer>{


}
