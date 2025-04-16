package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstLookups;

@Repository
public interface TabMstLookupsRepository extends JpaRepository<TabMstLookups, Integer> {

	List<TabMstLookups> findAllByLookupType(String lookupType);
	List<TabMstLookups> findByActiveOrderByLookupType(String active);
	TabMstLookups findByLookupTypeAndCode(String lookupType, String code);
 }
