package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samsoft.lms.batch.entities.LoanProductView;

public interface LoanProductViewRepository extends ReadOnlyRepositoryLoanProduct<LoanProductView, String> {

	List<LoanProductView> findAllByCustomerId(@Param("customerId") String customerId);
}
