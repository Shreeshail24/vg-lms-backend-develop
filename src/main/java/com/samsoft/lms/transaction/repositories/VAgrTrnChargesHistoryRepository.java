package com.samsoft.lms.transaction.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.VAgrTrnChargesHistory;

@Repository
public interface VAgrTrnChargesHistoryRepository extends JpaRepository<VAgrTrnChargesHistory, Integer> {

	List<VAgrTrnChargesHistory> findByMastAgrId(String mastAgrId);
}
