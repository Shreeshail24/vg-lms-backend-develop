package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samsoft.lms.core.entities.AgrLoans;

public interface AgrLoansRepository extends JpaRepository<AgrLoans, String>{

	@Query(value = "SELECT DISTINCT A.customerId FROM agr_loans A WHERE (A.sLoanAdditionalStatus is null OR A.sLoanAdditionalStatus = '' OR A.sLoanAdditionalStatus = 'SETTLED')", nativeQuery = true)
	List<String> findAllCustomers();
	
	AgrLoans findByLoanId(String loanId);
	
	AgrLoans findByMasterAgreementMastAgrIdAndLoanId(String mastAgrId, String loanId );
	
	@Query(value="select a.* from agr_loans a, agr_master_agreement m where a.sMastAgrId= m.sMastAgrId and m.sMastAgrId = :mastAgrId",nativeQuery=true)
	List<AgrLoans> getLoanIdByMasterAgreement(String mastAgrId);
		
	List<AgrLoans> findByMasterAgreementMastAgrId(String mastAgrId);
	
	List<AgrLoans> findByMasterAgreementMastAgrIdAndLoanAdditionalStatusIsNullOrderByTotalDues(String mastAgrId);
	
	List<AgrLoans> findByCustomerIdAndLoanAdditionalStatusIsNull(String customerId);
	List<AgrLoans> findByCustomerIdAndLoanAdditionalStatus(String customerId, String status);
	
	List<AgrLoans> findByMasterAgreementMastAgrIdAndBalTenorAndLoanAdditionalStatusIsNull(String customerId, Integer balTenor);
	
	List<AgrLoans> findByMasterAgreementMastAgrIdAndLoanAdditionalStatusIsNullOrLoanAdditionalStatusNotIn(String customerId, List<String> status);

	List<AgrLoans> findByLoanAdditionalStatus(String loanAdditionalStatus);
	
	List<AgrLoans> findByLoanAdditionalStatusAndMasterAgreementAgreementStatusAndMasterAgreementCustomerId(String loanAdditionalStatus, String agreementStatus, String customerId);

}
