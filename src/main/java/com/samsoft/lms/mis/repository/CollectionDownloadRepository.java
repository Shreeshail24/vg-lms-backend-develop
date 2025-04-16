package com.samsoft.lms.mis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.mis.entity.CollectionDownload;

@Repository
public interface CollectionDownloadRepository extends JpaRepository<CollectionDownload, String>{

}
