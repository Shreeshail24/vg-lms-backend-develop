package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnWriteoff;

@Repository
public interface AgrTrnWriteoffRepository extends JpaRepository<AgrTrnWriteoff, Integer> {

}
