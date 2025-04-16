package com.samsoft.lms.request.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqChargesBookingDtl;
import com.samsoft.lms.request.entities.AgrTrnReqCreditNoteDtl;

@Repository
public interface AgrTrnReqChargesBookingDtlRepository extends JpaRepository<AgrTrnReqChargesBookingDtl, Integer>{

	AgrTrnReqChargesBookingDtl findByRequestHdrReqId(Integer reqId);

}
