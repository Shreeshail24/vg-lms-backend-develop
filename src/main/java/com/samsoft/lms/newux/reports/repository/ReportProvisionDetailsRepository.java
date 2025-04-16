package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samsoft.lms.newux.reports.entity.ReportProvisionDetails;

import java.util.List;

public interface ReportProvisionDetailsRepository extends JpaRepository<ReportProvisionDetails, String> {

    @Query(value = "select * from v_lms_rep_provision_details Where dProvisionDate between :fromDate and :toDate", nativeQuery = true)
    public List<ReportProvisionDetails> findByProvisionDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
