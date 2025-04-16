package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.VAgrInterestAccrualHistory;

@Repository
public interface VAgrInterestAccrualHistoryRepository
		extends ReadOnlyRepositoryForTranAccrualHistory<VAgrInterestAccrualHistory, String> {

	List<VAgrInterestAccrualHistory> findAllByMastAgrIdAndLoanIdAndDtTranDateBetween(String masterAgreementId,
			String loanId, Date fromDate, Date toDate, Pageable paging);
	
	List<VAgrInterestAccrualHistory> findAllByMastAgrIdAndLoanId(String masterAgreementId,
			String loanId);

}
