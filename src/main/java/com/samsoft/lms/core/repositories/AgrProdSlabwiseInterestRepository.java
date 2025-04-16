package com.samsoft.lms.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.core.entities.AgrProdSlabwiseInterest;

@Repository
public interface AgrProdSlabwiseInterestRepository extends JpaRepository<AgrProdSlabwiseInterest, Integer> {

	List<AgrProdSlabwiseInterest> findByMasterAgrId(String mastAgrId);

	@Query(value = "SELECT * FROM agr_prod_slabwise_interest agrprodsla0_ WHERE agrprodsla0_.sMasterAgrId = :mastAgrId AND :dpdTo between agrprodsla0_.nTenorFrom and agrprodsla0_.nTenorTo", nativeQuery = true)
	AgrProdSlabwiseInterest fetchNewSlab(@Param("mastAgrId") String mastAgrId, @Param("dpdTo") int dpdTo);

	AgrProdSlabwiseInterest findByMasterAgrIdAndIntSlabId(String mastAgrId, int intSlabId);

	AgrProdSlabwiseInterest findByMasterAgrIdAndIntSlabIdLessThan(String mastAgrId, Integer slabId);

	AgrProdSlabwiseInterest findByMasterAgrIdAndSlabAdjustedAndIntSlabIdLessThan(String mastAgrId, String slabAdjusted,
			Integer slabId);
	
	AgrProdSlabwiseInterest findFirstByMasterAgrIdOrderByIntSlabIdDesc(String mastAgrId);
}
