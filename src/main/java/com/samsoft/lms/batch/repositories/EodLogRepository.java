package com.samsoft.lms.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.EodLog;
@Repository
public interface EodLogRepository extends JpaRepository<EodLog, Integer>{

}
