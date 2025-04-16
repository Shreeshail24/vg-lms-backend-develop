package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnInstrument;

@Repository
public interface AgrTrnInstrumentRepository extends JpaRepository<AgrTrnInstrument, Integer>{

}
