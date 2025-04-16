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

import com.samsoft.lms.core.dto.DistinctDueHeadDto;
import com.samsoft.lms.core.entities.AgrTrnDueDetails;
import com.samsoft.lms.request.dto.RestructureJpaDto;

@Repository
public interface AgrTrnDueDetailsRepository extends JpaRepository<AgrTrnDueDetails, Integer> {

	List<AgrTrnDueDetails> findByMastAgrIdAndLoanIdOrderByDtDueDate(String mastAgrId, String loanId);

	List<AgrTrnDueDetails> findByMastAgrIdOrderByDtDueDate(String mastAgrId);

	AgrTrnDueDetails findByDueDtlId(Integer dueDetailId);
	
	Page<AgrTrnDueDetails> findByMastAgrId(String mastAgrId, Pageable pageable);
	
	@Query(value = "select min(dtDueDate) from AgrTrnDueDetails where loanId = :loanId and dueCategory = 'INSTALLMENT' ")
	Date getMinInstallmentDate(@Param("loanId") String loanId);

	List<AgrTrnDueDetails> findByLoanIdAndDtDueDateAndDueCategory(String loanId, Date dtDueDate, String dueCategory);

	@Modifying
	@Transactional
	Integer deleteByMastAgrIdAndDueAmountLessThanEqual(String mastAgrId, Double dueAmount);

	List<AgrTrnDueDetails> findByMastAgrIdAndDueCategory(String mastAgrId, String dueCategory);

	List<AgrTrnDueDetails> findByDtDueDateBetween(Date fromDate, Date toDate);

	List<AgrTrnDueDetails> findByMastAgrIdAndDueCategoryAndDueHead(String mastAgrId, String dueCategory,
			String dueHead);

	List<AgrTrnDueDetails> findByMastAgrIdAndDueCategoryAndDueHeadNotIn(String mastAgrId, String dueCategory,
			List<String> dueHead);

	List<AgrTrnDueDetails> findByMastAgrIdAndDueHead(String mastAgrId, String dueHead);

	List<AgrTrnDueDetails> findByLoanIdAndDueHead(String loanId, String dueHead);

	List<AgrTrnDueDetails> findByLoanId(String loanId);

	AgrTrnDueDetails findByMastAgrIdAndDueDtlId(String mastAgrId, Integer dueDtlId);

	AgrTrnDueDetails findByTranDtlId(Integer tranDtlId);

	@Query(value = "select new com.samsoft.lms.core.dto.DistinctDueHeadDto(dueHead, dueCategory) from AgrTrnDueDetails a where a.mastAgrId=:mastAgrId")
	List<DistinctDueHeadDto> findDinstinctDueHeads(@Param("mastAgrId") String mastAgrId);

	List<AgrTrnDueDetails> findByMastAgrIdAndDueCategoryAndDtDueDateBetween(String mastAgrId, String dueCategory,
			Date fromDate, Date toDate);

	@Query(value = "select distinct sMastAgrId from agr_trn_due_dtl", nativeQuery = true)
	List<String> findAllDistinctMastAgrId();

	@Query(value = "select distinct sMastAgrId from agr_trn_due_dtl where dDueDate >= NOW() and dDueDate <= DATE(NOW() + INTERVAL 7 DAY)", nativeQuery = true)
	List<String> findAllDistinctMastAgrIdAndDtDueDate();

	@Query(value = "SELECT new com.samsoft.lms.request.dto.RestructureJpaDto (d.mastAgrId, d.dueCategory, d.dueHead, SUM(d.dueAmount) AS dueAmount, SUM(s.dueTaxAmount) as dueTaxAmount) FROM AgrTrnDueDetails d left join AgrTrnTaxDueDetails s ON d.dueDtlId = s.dueDetail.dueDtlId WHERE d.mastAgrId = :mastAgrId GROUP BY d.mastAgrId, d.dueCategory , d.dueHead")
	List<RestructureJpaDto> restructureReceivables(@Param("mastAgrId") String mastAgrId);
	
	@Query(value = "SELECT * FROM agr_trn_due_details " +
            "WHERE mast_agr_id = :mastAgrId " +
            "ORDER BY " +
            "CASE WHEN :sortDir = 'asc' THEN due_dtl_id END ASC, " +
            "CASE WHEN :sortDir = 'desc' THEN due_dtl_id END DESC",
    countQuery = "SELECT COUNT(*) FROM agr_trn_due_details WHERE mast_agr_id = :mastAgrId",
    nativeQuery = true)
Page<AgrTrnDueDetails> findByMastAgrIdWithSorting(@Param("mastAgrId") String mastAgrId, 
                                                
                                               Pageable pageable,@Param("sortDir") String sortDir);


}
