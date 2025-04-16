package com.samsoft.lms.las.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.las.entities.TabUploadLasTrail;

import java.util.List;

@Repository
public interface TabUploadLasTrailRepository extends JpaRepository<TabUploadLasTrail, Integer> {

    List<TabUploadLasTrail> findByUploadLasIdAndMastAgrId(Integer uploadLasId, String mastAgrId);
}
