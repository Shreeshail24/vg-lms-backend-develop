package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRecall;

@Repository
public interface AgrTrnReqRecallRepository extends JpaRepository<AgrTrnReqRecall, Integer> {

	List<AgrTrnReqRecall> findByMasterAgrIdAndRequestHdrReqId(String mastAgrId, Integer reqId);

}
