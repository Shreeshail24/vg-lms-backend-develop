package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportPosDateWise;

import java.util.List;

@Repository
public interface ReportPosDateWiseRepository extends JpaRepository<ReportPosDateWise, String> {

    @Query(value = "select * from v_lms_rep_pos_datewise Where dBackup= :date", nativeQuery = true)
    public List<ReportPosDateWise> findByBackupDate(@Param("date") String date);
}
