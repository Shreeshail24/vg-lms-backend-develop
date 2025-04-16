package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstOrgBranch;
@Repository
public interface TabMstOrgBranchRepository extends JpaRepository<TabMstOrgBranch, Integer>{

	TabMstOrgBranch findByOrgBrId(String orgId);
}
