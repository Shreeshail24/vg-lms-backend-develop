package com.samsoft.lms.instrument.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepositoryInstrument<T, ID> extends Repository<T, ID> {

}
