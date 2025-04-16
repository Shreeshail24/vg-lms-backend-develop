package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepositoryPaymentApp<T, ID> extends Repository<T, ID> {
 
    List<T> findByDecKeyNotNullOrderByColumnName();

}