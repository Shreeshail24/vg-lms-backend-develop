package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.GlErrorLog;

@Repository
public interface GlErrorLogRepository extends JpaRepository<GlErrorLog, Integer> {

}
