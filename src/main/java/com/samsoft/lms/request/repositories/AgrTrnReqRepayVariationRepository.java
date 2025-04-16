package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRepayVariation;

@Repository
public interface AgrTrnReqRepayVariationRepository extends JpaRepository<AgrTrnReqRepayVariation, Integer> {

	List<AgrTrnReqRepayVariation> findByMasterAgrIdAndRequestHdrReqId(String mastAgrId, int reqId);
}
