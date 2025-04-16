package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqEstRepaySchedule;

@Repository
public interface AgrTrnReqEstRepayScheduleRepository extends JpaRepository<AgrTrnReqEstRepaySchedule, Integer>{

}
