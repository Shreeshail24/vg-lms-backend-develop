package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.batch.dto.DueDetailsDto;
import com.samsoft.lms.batch.entities.ArcAgrTrnBkpDueDetails;

@Repository
public interface ArcAgrTrnBkpDueDetailsRepository extends JpaRepository<ArcAgrTrnBkpDueDetails, Integer> {
	
}
