package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRescheduleDtl;

@Repository
public interface AgrTrnReqRescheduleDtlRepository extends JpaRepository<AgrTrnReqRescheduleDtl, Integer>{

	AgrTrnReqRescheduleDtl findByMasterAgrIdAndRequestHdrReqId(String mastAgrId, int reqId);
}
