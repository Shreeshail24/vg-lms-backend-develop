package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportSMALargeBorrower;

@Repository
public interface ReportSMALargeBorrowerRepository extends JpaRepository<ReportSMALargeBorrower, String> {
    @Query (value= "select * from v_lms_rep_SMA_largeborrower where nDPD >= :fromDPD and nDPD <= :toDPD and sServBranch = :servBranch and OutstandingAmount >= :fromoutstandingAmount and OutstandingAmount <= :tooutstandingAmount", nativeQuery=true)
    public List<ReportSMALargeBorrower> FindByDPDandBranchandOSAmount(@Param("servBranch") String servBranch, @Param("fromDPD") Integer  fromDPD, @Param("toDPD") Integer toDPD, @Param("fromoutstandingAmount") Double fromoutstandingAmount, @Param("tooutstandingAmount") Double tooutstandingAmount);

}
