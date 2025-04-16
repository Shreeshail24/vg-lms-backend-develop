package com.samsoft.lms.agreement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.TabMstMasterAgreementConf;

@Repository
public interface TabMstMasterAgreementConfRepository extends JpaRepository<TabMstMasterAgreementConf, Integer> {
	List<TabMstMasterAgreementConf> findAllByPortfolioCodeAndBranchCode(String PortfolioCode, String branchCode);
}
