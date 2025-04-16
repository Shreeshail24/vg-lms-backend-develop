package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqWriteoff;

@Repository
public interface AgrTrnReqWriteoffRepository extends JpaRepository<AgrTrnReqWriteoff, Integer>{

	AgrTrnReqWriteoff findByMasterAgrIdAndRequestHdrReqId(String mastAgrId, Integer reqId);
}
