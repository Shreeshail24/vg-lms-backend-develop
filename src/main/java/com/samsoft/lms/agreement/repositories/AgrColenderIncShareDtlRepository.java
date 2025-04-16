package com.samsoft.lms.agreement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.agreement.entities.AgrColenderIncShareDtl;

@Repository
public interface AgrColenderIncShareDtlRepository extends JpaRepository<AgrColenderIncShareDtl, Integer> {

	AgrColenderIncShareDtl findByColenderCoLendIdAndFeeCode(int colenderId, String feeCode);

}
