package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportBillOfSupply;

import java.util.List;

@Repository
public interface ReportBillOfSupplyRepository extends JpaRepository<ReportBillOfSupply,String> {
    @Query(value = "select * from v_lms_rep_billofsupply  WHERE  sMastAgrId = :mID and dtInstallmentDate= :installmentDt",nativeQuery = true)
    public List<ReportBillOfSupply> FindByMIDandInstallmentDt(@Param("mID") String mID, @Param("installmentDt") String installmentDt);
}
