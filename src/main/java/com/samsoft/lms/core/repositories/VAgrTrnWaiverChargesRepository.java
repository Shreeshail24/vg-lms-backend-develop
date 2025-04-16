package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samsoft.lms.core.entities.VAgrTrnChargesWaiver;
import com.samsoft.lms.transaction.entities.VAgrTrnChargesHistory;

public interface VAgrTrnWaiverChargesRepository extends JpaRepository<VAgrTrnChargesWaiver, Integer> {

	List<VAgrTrnChargesWaiver> findBymastAgrId(String mastAgrId);
}
