package com.samsoft.lms.core.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepositoryForWaiverCharges<T, ID> extends Repository<T, ID> {

}
