package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabOrganization;

@Repository
public interface TabOrganizationRepository extends JpaRepository<TabOrganization, String>{
	List<TabOrganization> findByOrgId(String orgId);
	
	
}
