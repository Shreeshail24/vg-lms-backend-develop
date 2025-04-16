package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportGSTDetails;
import com.samsoft.lms.newux.reports.entity.ReportProvisionDetails;
import com.samsoft.lms.newux.reports.repository.ReportGSTDetailsRepository;

import java.util.List;

@Service
@Slf4j
public class ReportGSTDetailsService {

    @Autowired
    private ReportGSTDetailsRepository reportGSTDetailsRepository;

    public List<ReportGSTDetails> getReportGSTDetails(String fromDate, String toDate) throws Exception {

        try {
            log.info("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            return reportGSTDetailsRepository.findByDTranDate(fromDate, toDate);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportGSTDetails");
            log.error("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            log.error("Error: " + e.getMessage());
            throw e;
        }

    }
}
