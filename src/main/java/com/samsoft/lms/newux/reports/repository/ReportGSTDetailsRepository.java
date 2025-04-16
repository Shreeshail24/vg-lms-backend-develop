package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportGSTDetails;

import java.util.List;

@Repository
public interface ReportGSTDetailsRepository extends JpaRepository<ReportGSTDetails, String> {

    @Query(value = "select * from v_lms_rpt_GST_Details Where dTranDate between :fromDate and :toDate", nativeQuery = true)
    public List<ReportGSTDetails> findByDTranDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
