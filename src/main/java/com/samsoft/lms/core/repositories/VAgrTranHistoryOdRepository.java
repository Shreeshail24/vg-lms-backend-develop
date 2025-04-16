package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.VAgrTranHistoryOd;

@Repository
public interface VAgrTranHistoryOdRepository extends JpaRepository<VAgrTranHistoryOd, Integer>{
	List<VAgrTranHistoryOd> findAllByMastAgrIdAndDtTranDateBetweenOrderByTranDtlIdAsc(String mastAgrId,Date fromDate, Date toDate, Pageable pageable);
}
