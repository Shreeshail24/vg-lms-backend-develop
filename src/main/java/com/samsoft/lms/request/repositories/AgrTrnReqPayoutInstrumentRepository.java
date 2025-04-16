package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqPayoutInstrument;
import com.samsoft.lms.request.entities.AgrTrnReqRefundDtl;

@Repository
public interface AgrTrnReqPayoutInstrumentRepository extends JpaRepository<AgrTrnReqPayoutInstrument, Integer>{

	AgrTrnReqPayoutInstrument findByRequestHdrReqId(Integer reqId);

}
