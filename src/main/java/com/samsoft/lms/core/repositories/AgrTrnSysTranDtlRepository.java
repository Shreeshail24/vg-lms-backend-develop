package com.samsoft.lms.core.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.dto.SysTranDtlDto;
import com.samsoft.lms.core.entities.AgrTrnSysTranDtl;

@Repository
public interface AgrTrnSysTranDtlRepository extends JpaRepository<AgrTrnSysTranDtl, Integer> {

	List<AgrTrnSysTranDtl> findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanOrderByInstallmentNo(String tranType,String loanId,String adjusted,Date backupDate);
	
	List<AgrTrnSysTranDtl> findByTranTypeAndLoanIdAndAdjustedYnAndDtTranDateLessThanEqualOrderByInstallmentNo(String tranType,String loanId,String adjusted,Date backupDate);


	@Query(value = "SELECT MAX(dpd) from AgrTrnSysTranDtl A where A.tranType = :tranType AND A.loanId = :loanId AND A.adjustedYn = 'N' ")
	Integer getMaxDpd(@Param("tranType") String tranType, @Param("loanId") String loanId);
	
	@Query(value = "SELECT SUM(tranAmount) FROM AgrTrnSysTranDtl A WHERE A.tranType= :tranType and A.loanId = :loanId and A.adjustedYn = 'N' and dtTranDate < :backupDate")
	Double getSumOfTranAmountOfPenal(@Param("tranType") String tranType, @Param("loanId") String loanId, @Param("backupDate") Date backupDate);
	
	@Query(nativeQuery =  true)
	List<SysTranDtlDto> getSysTranDetails(@Param("tranType") String tranType, @Param("loanId") String loanId, @Param("backupDate") Date backupDate);

	List<AgrTrnSysTranDtl> findByMastAgrIdAndAdjustedYn(String mastAgrId, String adjusted);
	
	List<AgrTrnSysTranDtl> findByMastAgrIdAndAdjustedYnAndTranType(String mastAgrId, String adjusted, String tranType);
	
	List<AgrTrnSysTranDtl> findByMastAgrIdAndAdjustedYnAndTranTypeAndLoanId(String mastAgrId, String adjusted, String tranType, String loanId);
	
	@Query(value = "Select ifnull(sum(a.nTranAmount),0) from agr_trn_sys_tran_dtl a Where a.sMastAgrID= :mastAgrID AND sTranType='INTEREST_ACCRUAL' AND sAdjustedYn='N'", nativeQuery = true)
	Double getTotalTranAmt(@Param("mastAgrID") String mastAgrID);
	
	AgrTrnSysTranDtl findByTranId(Integer tranId);

}
