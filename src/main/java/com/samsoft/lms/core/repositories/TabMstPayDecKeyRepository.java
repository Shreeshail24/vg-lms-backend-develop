package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstPayDecKey;

@Repository
public interface TabMstPayDecKeyRepository extends JpaRepository<TabMstPayDecKey, String>{

}
