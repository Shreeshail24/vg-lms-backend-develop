package com.samsoft.lms.batch.repositories;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepositoryLoanProduct<T, ID> extends Repository<T, ID> {
 
    List<T> findAllByCustomerId(String inputParameter);

}
