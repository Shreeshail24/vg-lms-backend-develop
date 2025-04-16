package com.samsoft.lms.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.TabProvisionSetupDtl;

@Repository
public interface TabProvisionSetupDtlRepository extends JpaRepository<TabProvisionSetupDtl, Integer>{

}
