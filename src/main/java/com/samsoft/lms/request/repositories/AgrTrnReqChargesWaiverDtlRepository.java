package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqChargesWaiverDtl;



@Repository
public interface AgrTrnReqChargesWaiverDtlRepository extends JpaRepository<AgrTrnReqChargesWaiverDtl, Integer>{

	List<AgrTrnReqChargesWaiverDtl> findByRequestHdrReqId(Integer reqId);

}
