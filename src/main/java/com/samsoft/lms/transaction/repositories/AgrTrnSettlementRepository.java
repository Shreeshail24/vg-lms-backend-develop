package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnSettlement;

@Repository
public interface AgrTrnSettlementRepository extends JpaRepository<AgrTrnSettlement, Integer> {

}
