package com.samsoft.lms.las.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.las.entities.AgrCollateralShares;

@Repository
public interface AgrCollateralSharesRepository extends JpaRepository<AgrCollateralShares, String> {

    AgrCollateralShares findByMastAgrId(String mastAgrId);

}
