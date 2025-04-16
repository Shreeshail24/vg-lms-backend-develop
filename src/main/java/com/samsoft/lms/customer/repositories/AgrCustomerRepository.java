package com.samsoft.lms.customer.repositories;

import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.customer.entities.AgrCustomer;



@Repository
public interface AgrCustomerRepository extends JpaRepository<AgrCustomer, String>{

//	List<AgrCustomer> findAllByCustomerIdOrderByDtUserDateDesc(String customerId);
	List<AgrCustomer> findAllByCustomerIdIgnoreCaseOrderByDtUserDateDesc(String customerId);
	List<AgrCustomer> findByMasterAgrMastAgrIdOrderByDtUserDateDesc(String mastAgrId, Pageable paging);
	List<AgrCustomer> findByMasterAgrMastAgrIdOrderByDtUserDateDesc(String mastAgrId);
	List<AgrCustomer> findAllByMobileOrderByDtUserDateDesc(String mobile);
//	List<AgrCustomer> findAllByPanOrderByDtUserDateDesc(String pan);
	List<AgrCustomer> findAllByPanIgnoreCaseOrderByDtUserDateDesc(String pan);
	
	AgrCustomer findFirstByCustomerId(String customerId);
	
	AgrCustomer findByMasterAgrMastAgrIdAndCustomerType(String mastAgrId, String customerType);
	
	List<AgrCustomer> findAllByMasterAgrOriginationApplnNoOrderByDtUserDateDesc(String originationApplnNo);
	
//	@Query(nativeQuery=true)
//	public List<AgrCustomerResponseDto> findByMasterAgrMastAgrIdOrderByDtUserDateDesc(String mastAgrId);
//	
//	List<AgrCustomer> findByMasterAgrMastAgrIdOrderByDtUserDateDesc(String mastAgrId);
	
//	List<AgrCustomer> findByMasterAgrMastAgrIdOrderByDtUserDateDesc(String mastAgrId);

	AgrCustomer findByMasterAgrMastAgrIdAndPanAndCustomerType(String mastAgrId, String pan, String customerType);

	AgrCustomer findByMasterAgrMastAgrIdAndCustomerIdAndCustomerType(String mastAgrId, String customerId, String customerType);

	AgrCustomer findByCustInternalId(Integer custInternalId);
	
	
//	List<AgrCustomer> findAllByFirstNameIgnoreCaseOrderByFirstNameDesc(String firstName);
	List<AgrCustomer> findAllByAadharNoOrderByFirstNameDesc(String aadharNo);
	List<AgrCustomer> findAllByCustCategoryOrderByFirstNameDesc(String custCategory);
	List<AgrCustomer> findAllByCustomerTypeOrderByFirstNameDesc(String custType);
	List<AgrCustomer> findAllByStatusOrderByFirstNameDesc(String status);
	List<AgrCustomer> findAllByLastNameOrderByLastNameDesc(String lastName);
	
	
    @Query("SELECT c FROM AgrCustomer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) ORDER BY c.firstName DESC")
    List<AgrCustomer> findAllByFirstNameContainingIgnoreCaseOrderByFirstNameDesc(@Param("firstName") String firstName);
	AgrCustomer findByCustomerId(String customerId);


	
}
