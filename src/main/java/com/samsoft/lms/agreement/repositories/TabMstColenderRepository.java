package com.samsoft.lms.agreement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.TabMstColender;

@Repository
public interface TabMstColenderRepository extends JpaRepository<TabMstColender, Integer>{

	TabMstColender findByColenderId(Integer colenderId);
}
