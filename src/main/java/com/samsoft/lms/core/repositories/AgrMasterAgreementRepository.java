package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrMasterAgreement;

import javax.persistence.NamedNativeQuery;

@Repository
public interface AgrMasterAgreementRepository extends JpaRepository<AgrMasterAgreement, Integer> {

	List<AgrMasterAgreement> findAllByOrderByDtUserDateDesc(Pageable paging);

	List<AgrMasterAgreement> findByPortfolioCodeInOrderByDtUserDateDesc(List<String> portfolio, Pageable paging);

	List<AgrMasterAgreement> findByMastAgrIdAndPortfolioCodeInOrderByDtUserDateDesc(String mastAgrId,
			List<String> portfolio, Pageable paging);

	List<AgrMasterAgreement> findByPortfolioCodeInOrderByDtUserDateDesc(List<String> portfolio);

	AgrMasterAgreement findByMastAgrId(String mastAgrId);

	List<AgrMasterAgreement> findAllByCustomerId(String customerId);

	AgrMasterAgreement findByOriginationApplnNo(String originationApplnNo);

	List<AgrMasterAgreement> findAllByCustomerIdAndExcessAmountGreaterThanAndAgreementStatus(String customerId,
			Double exccess, String status);

	AgrMasterAgreement findByMastAgrIdAndDtNextDrop(String mastAgrId, Date nextDrop);

	@Query(value = "select sMastAgrId from agr_master_agreement where sCustomerId = :customerId", nativeQuery = true)
	List<String> findMastAgrIdListByCustomerId(String customerId);

	@Query(value = "select sum(nLoanAmount) from agr_master_agreement where sCustomerId = :customerId", nativeQuery = true)
	Double getTotalLoanAmount(String customerId);

	@Query(value = "select count(*) from agr_master_agreement where sCustomerId = :customerId and sAgreementStatus = 'L'", nativeQuery = true)
	Integer getActiveLoans(String customerId);

	@Query(value = "select dNextInstallment from agr_master_agreement where sCustomerId = :customerId and sAgreementStatus = 'L'", nativeQuery = true)
	List<Date> getNextInstallmentDates(String customerId);

	@Query(value = "select count(*) from agr_master_agreement where sAgreementStatus = :agreementStatus", nativeQuery = true)
	Integer getActiveLoansByAgreementStatus(String agreementStatus);

	@Query(value = "select count(*) from agr_master_agreement", nativeQuery = true)
	Integer getTotalLoans();

	@Query(value = "select * from agr_master_agreement ma left join agr_disbursement disb on disb.sMastAgrId = (select adisb.sMastAgrId from agr_disbursement adisb where ma.sMastAgrId = adisb.sMastAgrId limit 1) where ma.sAgreementStatus = :agreementStatus group by ma.sMastAgrId order by disb.dDisbDate desc", nativeQuery = true)
	List<AgrMasterAgreement> findByAgreementStatus(String agreementStatus);

	List<AgrMasterAgreement> findDistinctByCustomerId(String customerId);

	List<AgrMasterAgreement> findByCustomerIdAndExcessAmountAndTotalDuesAndAgreementStatus(String mastAgrId,
			Double excess, Double totalDues, String status);

	List<AgrMasterAgreement> findByCustomerIdAndAgreementStatusAndMastAgrIdIn(String customerId, String agreementStatus,
			List<String> mastAgrId);

	@Query(value = "select * from agr_master_agreement ma left join agr_disbursement disb on disb.sMastAgrId = (select adisb.sMastAgrId from agr_disbursement adisb where ma.sMastAgrId = adisb.sMastAgrId limit 1) order by disb.dDisbDate desc", nativeQuery = true)
	List<AgrMasterAgreement> getAllOrderByDisbDate();
	
	List<AgrMasterAgreement> findAllByOriginationApplnNo(String originationApplnNo);

	List<AgrMasterAgreement> findByCustomerIdAndPortfolioCodeIn(String customerId, List<String> portfolio);

	List<AgrMasterAgreement> findByOriginationApplnNoAndPortfolioCodeIn(String originationApplnNo, List<String> portfolio);

	List<AgrMasterAgreement> findByVirtualIdIsNull();

	AgrMasterAgreement findByVirtualId(String virtualId);

	List<AgrMasterAgreement> findByVpaIdIsNull();
	
	
	@Query(value = "SELECT DISTINCT s.sMastAgrId FROM agr_master_agreement s where s.sAgreementStatus='L' and s.sMastAgrId not in (select a.sMastAgrId from agr_loans a)", nativeQuery = true)
	List<String> findAllMasterAgreement();

}
