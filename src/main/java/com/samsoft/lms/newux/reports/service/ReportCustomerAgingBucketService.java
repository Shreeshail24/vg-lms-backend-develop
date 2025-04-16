package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportCustomerAgingBucket;
import com.samsoft.lms.newux.reports.entity.ReportGSTDetails;
import com.samsoft.lms.newux.reports.repository.ReportCustomerAgingBucketRepository;

import java.util.List;

@Service
@Slf4j
public class ReportCustomerAgingBucketService {

    @Autowired
    private ReportCustomerAgingBucketRepository reportCustomerAgingBucketRepository;

    public List<ReportCustomerAgingBucket> getReportCustomerAgingBucket() throws Exception {

        try {

            return reportCustomerAgingBucketRepository.findAll();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportCustomerAgingBucket");
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
}
