package com.samsoft.lms.transaction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.VMstGlConfig;

@Repository

public interface VMstGlConfigRepository extends JpaRepository<VMstGlConfig, Integer> {

	VMstGlConfig findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranchAndNbfc(String portfolio,
			String glEvent, String tranCategory, String tranHead, String servBranch, String nbfc);

	VMstGlConfig findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndServBranch(String portfolio, String glEvent,
			String tranCategory, String tranHead, String servBranch);

	VMstGlConfig findByPortfolioCodeAndGlEventAndTranCategoryAndTranHead(String portfolio, String glEvent,
			String tranCategory, String tranHead);
	
	VMstGlConfig findByPortfolioCodeAndGlEventAndTranCategoryAndTranHeadAndNbfc(String portfolio,
			String glEvent, String tranCategory, String tranHead, String nbfc);

}
