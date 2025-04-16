package com.samsoft.lms.newux.reports.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samsoft.lms.newux.reports.entity.InterestCertificateReport;

public interface InterestCertificateReportRepository extends JpaRepository<InterestCertificateReport, String> {

//	@Query(value = "select c.sMastAgrId,c.sCustomerId,c.sPortfolioCode,c.sProductCode,sum(c.InterestDueAmount),sum(c.InterestPaidAmount) from v_lms_rep_interest_certificate c Where c.dTranDate between :fromDate and :toDate Group by c.sMastAgrId,c.sCustomerId,c.sPortfolioCode,c.sProductCode having (sum(c.InterestDueAmount) >0 ) ", nativeQuery = true)
	@Query(value = "select * from v_lms_rep_interest_certificate Where dTranDate between :fromDate and :toDate", nativeQuery = true)
	public List<InterestCertificateReport> findByTranDate(@Param("fromDate") String fromDate,
			@Param("toDate") String toDate);
}
