package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.VAgrSoa;

@Repository
public interface VAgrSoaRepository extends JpaRepository<VAgrSoa, Integer> {
	List<VAgrSoa> findByMastAgrIdOrderByTranIdAsc(String mastAgrId);
}
