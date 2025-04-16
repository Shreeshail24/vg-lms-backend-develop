package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrRepaySchedule;

@Repository
public interface AgrRepayScheduleRepository extends JpaRepository<AgrRepaySchedule, Integer> {

	// @Query(value = "SELECT * FROM agr_repay_schedule S WHERE S.sLoanId = :loanId
	// AND S.DTINSTALLMENTDATE = :installmentDate", nativeQuery = true)
	List<AgrRepaySchedule> findByLoanIdAndDtInstallment(String loanId, Date installmentDate);

	List<AgrRepaySchedule> findByMasterAgrIdAndLoanId(String mastAgrId, String loanId);

	@Query(value = "SELECT DISTINCT count(installmentNo) FROM AgrRepaySchedule s WHERE s.loanId = :loanId and s.dtInstallment > :businessDate")
	Integer getBalanceTenor(@Param("loanId") String loanId, @Param("businessDate") Date businessDate);

	List<AgrRepaySchedule> findByMasterAgrIdOrderByRepaySchId(String masterAgrId);
	
	Page<AgrRepaySchedule> findByMasterAgrId(String mastAgrId, Pageable pageable);

	Page<AgrRepaySchedule> findByMasterAgrIdAndLoanId(String mastAgrId, String loanId, Pageable pageable);

	

	AgrRepaySchedule findByMasterAgrIdAndInstallmentNo(String masterAgrId, Integer installmentNo);

	List<AgrRepaySchedule> findByMasterAgrIdAndLoanIdAndDtInstallmentBetween(String mastAgrId, String loanId, Date from,
			Date to);

	List<AgrRepaySchedule> findByDtInstallmentBetween(Date from, Date to);

	@Modifying
	@Transactional
	void deleteByMasterAgrIdAndDtInstallmentGreaterThan(String mastAgrId, Date installmentDate);

	@Modifying
	@Transactional
	void deleteByMasterAgrIdAndInstallmentNoGreaterThan(String mastAgrId, int installmentNo);

	List<AgrRepaySchedule> findByMasterAgrIdAndDtInstallmentGreaterThanOrderByRepaySchId(String mastAgrId,
			Date businessDate);

	@Query(value = "SELECT max(installmentNo) FROM AgrRepaySchedule s WHERE s.masterAgrId = :masterAgrId and s.dtInstallment <= :businessDate")
	Integer getMaxInstallmentNo(@Param("masterAgrId") String masterAgrId, @Param("businessDate") Date businessDate);

	AgrRepaySchedule findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndPrincipalAmountGreaterThanOrderByInstallmentNoAsc(
			String mastAgrId, Date disbursementDate, Double amount);

	AgrRepaySchedule findFirstByMasterAgrIdAndDtInstallmentGreaterThanAndInterestAmountGreaterThanOrderByInstallmentNoAsc(
			String mastAgrId, Date disbursementDate, Double amount);

	AgrRepaySchedule findFirstByMasterAgrIdAndDtInstallmentLessThanOrderByInstallmentNoDesc(String mastAgrId,
			Date disbursementDate);
}
