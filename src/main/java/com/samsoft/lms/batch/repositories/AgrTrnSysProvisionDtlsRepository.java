package com.samsoft.lms.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.AgrTrnSysProvisionDtls;

@Repository
public interface AgrTrnSysProvisionDtlsRepository extends JpaRepository<AgrTrnSysProvisionDtls, Integer>{

}
