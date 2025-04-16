package com.samsoft.lms.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrInsuranceDetail;

@Repository
public interface AgrInsuranceDetailRepository extends JpaRepository<AgrInsuranceDetail, Integer> {
    @Query(value = "select * from agr_insurance_details Where sCustomerId= :sCustomerId", nativeQuery = true)
    public  AgrInsuranceDetail findBysCustomerId(@Param("sCustomerId") String sCustomerId);
}
