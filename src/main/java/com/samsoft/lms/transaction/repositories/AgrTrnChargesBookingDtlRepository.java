package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnChargesBookingDtl;

@Repository
public interface AgrTrnChargesBookingDtlRepository extends JpaRepository<AgrTrnChargesBookingDtl, Integer>{

}
