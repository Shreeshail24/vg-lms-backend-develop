package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportProvisionDetails;
import com.samsoft.lms.newux.reports.repository.ReportProvisionDetailsRepository;

import java.util.List;

@Service
@Slf4j
public class ReportProvisionDetailsService {

    @Autowired
    private ReportProvisionDetailsRepository reportProvisionDetailsRepository;


    public List<ReportProvisionDetails> getReportProvisionDetails(String fromDate, String toDate) throws Exception {

        try {
            log.info("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            return reportProvisionDetailsRepository.findByProvisionDate(fromDate, toDate);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportProvisionDetails");
            log.error("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            log.error("Error: " + e.getMessage());
            throw e;
        }

    }
}
