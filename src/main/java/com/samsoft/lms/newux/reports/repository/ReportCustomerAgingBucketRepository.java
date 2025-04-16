package com.samsoft.lms.newux.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samsoft.lms.newux.reports.entity.ReportCustomerAgingBucket;

@Repository
public interface ReportCustomerAgingBucketRepository extends JpaRepository<ReportCustomerAgingBucket, String> {
}
