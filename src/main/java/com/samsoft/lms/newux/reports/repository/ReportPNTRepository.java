package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.PNTReportDetails;

@Repository
public interface ReportPNTRepository extends JpaRepository<PNTReportDetails, String> { 
    public List<PNTReportDetails> findByBatchIdOrCustomerId(@Param("batchId")  Integer batchId,
            @Param("customerId") String customerId);

    public  List<PNTReportDetails> findByBatchIdOrMastAgrId(@Param("batchId")  Integer batchId,
                                                       @Param("mastAgrId") String mastAgrId);
}
