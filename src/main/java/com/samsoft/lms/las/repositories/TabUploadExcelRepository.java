package com.samsoft.lms.las.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.las.entities.TabUploadExcel;

@Repository
public interface TabUploadExcelRepository extends JpaRepository<TabUploadExcel, Integer> {

    TabUploadExcel findByUploadLasId(Integer uploadLasId);
}
