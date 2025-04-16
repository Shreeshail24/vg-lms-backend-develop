package com.samsoft.lms.master.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.master.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Integer> {
    public Agency findByAgencyId(Integer agencyId);
}
