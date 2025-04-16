package com.samsoft.lms.batch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.batch.entities.VMstProvisionSetup;

@Repository
public interface VMstProvisionSetupRepository extends JpaRepository<VMstProvisionSetup, Integer> {

	VMstProvisionSetup findByPortfolioCdAndDpdFromLessThanEqualAndDpdToGreaterThanEqual(String portfolio, Integer dpd,
			Integer dpd1);

}
