package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.core.dto.RuleDetailsDto;
import com.samsoft.lms.core.entities.VMstPayAppRuleSet;

@Repository
@Transactional
public interface VMstPayAppRuleSetRepository extends JpaRepository<VMstPayAppRuleSet, String> {

	@Query(nativeQuery = true)
	List<RuleDetailsDto> getRuleDetails(@Param("masterAgreement") String masterAgreement,
			@Param("ruleId") String ruleId, @Param("paymentFor") String paymentFor);

	@Query(nativeQuery = true)
	List<RuleDetailsDto> getRuleDetailsLoanManual(@Param("masterAgreement") String masterAgreement,
			@Param("ruleId") String ruleId, @Param("paymentFor") String paymentFor, @Param("loanId") String loanId);

	@Query(nativeQuery = true)
	List<RuleDetailsDto> getRuleDetailsChargesManual(@Param("masterAgreement") String masterAgreement,
			@Param("ruleId") String ruleId, @Param("paymentFor") String paymentFor,
			@Param("tranCategory") String tranCategory, @Param("tranHead") String tranHead);

}
