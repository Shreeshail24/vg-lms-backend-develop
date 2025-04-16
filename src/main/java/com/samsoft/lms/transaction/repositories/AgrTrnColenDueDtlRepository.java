package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnColenDueDtl;

@Repository
public interface AgrTrnColenDueDtlRepository extends JpaRepository<AgrTrnColenDueDtl, Integer>{

}
