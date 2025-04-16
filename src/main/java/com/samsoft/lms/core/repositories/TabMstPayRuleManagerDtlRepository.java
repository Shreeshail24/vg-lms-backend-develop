package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.TabMstPayRuleManagerDtl;

@Repository
public interface TabMstPayRuleManagerDtlRepository extends JpaRepository<TabMstPayRuleManagerDtl, Integer> {
	@Transactional
	@Query(value = "SELECT s.sPayRuleID FROM tab_mst_pay_rule_manager_dtl s WHERE IFNULL(sDec1Val, '0') = IFNULL(:value1 , 0) and IFNULL(sDec2Val, '0') = IFNULL(:value2, 0) and IFNULL(sDec3Val, '0') = IFNULL(:value3, 0) and IFNULL(sDec4Val, '0') = IFNULL(:value4, 0) and IFNULL(sDec5Val, '0') = IFNULL(:value5, 0) LIMIT 1 ", nativeQuery = true)
	String getRuleId(@Param("value1") String value1, @Param("value2") String value2, @Param("value3") String value3,
			@Param("value4") String value4, @Param("value5") String value5);
}
