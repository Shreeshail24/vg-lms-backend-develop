package com.samsoft.lms.core.repositories;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.core.entities.AgrTrnTaxDueDetails;

@Repository
public interface AgrTrnTaxDueDetailsRepository extends JpaRepository<AgrTrnTaxDueDetails, Integer> {

	List<AgrTrnTaxDueDetails> findByDueDetailOrderByTaxDueIdAsc(AgrTrnDueDetails dueDetail);
	
	List<AgrTrnTaxDueDetails> findByDueDetailMastAgrId(String mastAgrId);
	
	List<AgrTrnTaxDueDetails> findByDueDetailLoanId(String loanId);
	
	List<AgrTrnTaxDueDetails> findByDueDetailDueDtlId(Integer dueDtlId);

	AgrTrnTaxDueDetails findByTaxDueId(Integer taxDueId);
	
	@Modifying
	@Transactional
	Integer deleteByDueDetailDueDtlIdAndDueTaxAmountLessThanEqual(Integer dueDtlId, Double dueAmount);
	
	

}
