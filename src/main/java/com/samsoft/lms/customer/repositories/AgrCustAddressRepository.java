package com.samsoft.lms.customer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.customer.entities.AgrCustAddress;

@Repository
public interface AgrCustAddressRepository extends JpaRepository<AgrCustAddress, Integer> {

	List<AgrCustAddress> findByCustomerCustInternalId(Integer custInternalId);

	List<AgrCustAddress> findByCustomerId(String customerId);

	List<AgrCustAddress> findByCustomerIdAndAddrType(String customerId,String addrType);
}
