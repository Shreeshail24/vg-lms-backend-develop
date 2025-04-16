package com.samsoft.lms.instrument.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.instrument.entities.TrnInsBatchStatus;

@Repository
public interface TrnInsBatchStatusRepository extends JpaRepository<TrnInsBatchStatus, Integer>{

}
