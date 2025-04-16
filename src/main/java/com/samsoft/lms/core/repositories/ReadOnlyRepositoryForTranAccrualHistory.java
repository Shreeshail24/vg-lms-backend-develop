package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepositoryForTranAccrualHistory<T, ID> extends Repository<T, ID> {
 
    List<T> findAllByMastAgrIdAndLoanIdAndDtTranDateBetween(String mastAgrId, String loanId, Date fromDate, Date toDate);
	
   

 
}
