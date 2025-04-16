package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.CustomerUnutilizedLimit;



@Repository
public interface CustomerUnutilizedLimitRepository extends JpaRepository<CustomerUnutilizedLimit, String> {
    @Query(value ="select * from v_lms_rep_od_cust_unutilised_limit Where sCustomerId = :customerId order by dLimitSanctioned", nativeQuery = true)
    public  List<CustomerUnutilizedLimit> findAllByCustomerId(String customerId);
}