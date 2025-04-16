package com.samsoft.lms.transaction.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.transaction.entities.AgrTrnGlTranDtl;

@Repository
public interface AgrTrnGlTranDtlRepository extends JpaRepository<AgrTrnGlTranDtl, Integer> {

	List<AgrTrnGlTranDtl> findByCustomerIdAndDownloadYnAndDtTranDateBetweenAndGlEventNotIn(String customerId,
			String downLoadYn, Date from, Date to, List<String> glEvent);
	
	List<AgrTrnGlTranDtl> findByCustomerIdAndDownloadYnAndDtTranDateBetweenAndGlEventIn(String customerId, String downLoadYn,
            Date from, Date to, List<String> glEvent);
}
