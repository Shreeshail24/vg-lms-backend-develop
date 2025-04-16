package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportTrialBalance;

@Repository
public interface ReportTrialBalanceRepository extends JpaRepository<ReportTrialBalance, String> {

    @Query(value = "select * from v_lms_rep_trial_balance Where dTranDate between :fromDate and :toDate", nativeQuery = true)
    public List<ReportTrialBalance> findByTranDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
