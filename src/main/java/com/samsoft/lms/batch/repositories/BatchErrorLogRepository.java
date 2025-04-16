package com.samsoft.lms.batch.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.BatchErrorLog;
@Repository
public interface BatchErrorLogRepository extends JpaRepository<BatchErrorLog, Integer>{
	
	@Modifying
	@Transactional
	@Query(value="delete from  BatchErrorLog where fileName = :fileName")
	Integer deleteAllFileData(@Param("fileName") String fileName);

}
