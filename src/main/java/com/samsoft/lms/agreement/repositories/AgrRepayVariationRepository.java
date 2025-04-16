package com.samsoft.lms.agreement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrRepayVariation;

@Repository
public interface AgrRepayVariationRepository extends JpaRepository<AgrRepayVariation, Integer>{

	List<AgrRepayVariation> findByLoansLoanIdAndFromInstallmentNoGreaterThan(String loanId, int installmentNo);
	
	List<AgrRepayVariation> findByLoansLoanId(String loanId);
}
