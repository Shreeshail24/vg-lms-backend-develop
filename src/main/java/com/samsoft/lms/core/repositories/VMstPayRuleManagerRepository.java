package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.VMstPayRuleManager;

@Repository
public interface VMstPayRuleManagerRepository extends ReadOnlyRepositoryPaymentApp<VMstPayRuleManager, String>{

    List<VMstPayRuleManager> findByDecKeyNotNullOrderByColumnName();

}
