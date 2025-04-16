package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.TabMstDepositBank;

@Repository
public interface TabMstDepositBankRepository extends JpaRepository<TabMstDepositBank, Integer> {

	List<TabMstDepositBank> findAllByActive(String active);
	
	TabMstDepositBank findByBankName(String bankName);
	
	TabMstDepositBank findByDepositBankId(String depositBankId);
}
