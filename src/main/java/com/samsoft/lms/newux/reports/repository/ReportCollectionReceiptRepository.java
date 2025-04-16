package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportCollectionReceipt;

import java.util.List;
@Repository
public interface ReportCollectionReceiptRepository extends JpaRepository<ReportCollectionReceipt,String> {
    @Query(value="select * from v_lms_rep_recipts_new Where dReceiptDate >= :fromDate and dReceiptDate <= :toDate",nativeQuery = true)
    public List<ReportCollectionReceipt> findByDtInstallmentDate(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
