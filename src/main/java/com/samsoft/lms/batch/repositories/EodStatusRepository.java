package com.samsoft.lms.batch.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.EodStatus;
import com.samsoft.lms.core.entities.TabOrganization;
@Repository
public interface EodStatusRepository extends JpaRepository<EodStatus, Integer>{
	List<EodStatus> findAllByBusinessDateAndProcessName(Date businessDate, String processName, Pageable pageable);
	
	List<EodStatus> findAllByBusinessDate(Date businessDate,Pageable pageable);
	
	List<EodStatus> findByBusinessDate(Date businessDate);
	
	
}
