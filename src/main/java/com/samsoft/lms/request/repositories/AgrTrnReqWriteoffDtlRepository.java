package com.samsoft.lms.request.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.request.entities.AgrTrnReqWriteoffDtl;

@Repository
public interface AgrTrnReqWriteoffDtlRepository extends JpaRepository<AgrTrnReqWriteoffDtl, Integer> {

	List<AgrTrnReqWriteoffDtl> findByWriteoffIdWriteoffId(Integer writeoffId);

}
