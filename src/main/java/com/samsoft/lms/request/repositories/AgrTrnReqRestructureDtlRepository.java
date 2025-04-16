package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqRestructureDtl;

@Repository
public interface AgrTrnReqRestructureDtlRepository extends JpaRepository<AgrTrnReqRestructureDtl, Integer> {

	List<AgrTrnReqRestructureDtl> findByRequestHdrReqIdAndCapitalizeAmountGreaterThan(int reqId,
			double capitalizaAmount);

	List<AgrTrnReqRestructureDtl> findByRequestHdrReqIdAndWaiveAmountGreaterThan(int reqId, double waiverAmount);
}
