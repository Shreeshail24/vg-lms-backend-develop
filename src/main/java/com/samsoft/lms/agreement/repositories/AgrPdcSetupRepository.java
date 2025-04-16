package com.samsoft.lms.agreement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrPdcSetup;
@Repository
public interface AgrPdcSetupRepository extends JpaRepository<AgrPdcSetup, Integer>{

}
