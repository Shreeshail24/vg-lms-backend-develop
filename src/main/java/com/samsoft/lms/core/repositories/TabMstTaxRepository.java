package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstTax;

@Repository
public interface TabMstTaxRepository extends JpaRepository<TabMstTax, Integer> {
	List<TabMstTax> findByStateAndTaxCodeIn(String state, List<String> taxCode);
}
