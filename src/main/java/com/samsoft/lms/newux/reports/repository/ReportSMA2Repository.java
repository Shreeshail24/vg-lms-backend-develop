package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportSMA2;

import java.util.List;

@Repository
public interface ReportSMA2Repository extends JpaRepository<ReportSMA2, String> {

    @Query(value = "select * from v_lms_rep_SMA2 where nDPD >= :fromDPD and nDPD <= :toDPD and sServBranch = :servBranch", nativeQuery = true)
    public List<ReportSMA2> findByfromDPDtoDPDandServBranch(@Param("servBranch") String servBranch, @Param("fromDPD") Integer  fromDPD, @Param("toDPD") Integer toDPD);
}
