package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportPosDateWise;
import com.samsoft.lms.newux.reports.repository.ReportPosDateWiseRepository;

import java.util.List;

@Service
@Slf4j
public class ReportPosDateWiseService {

    @Autowired
    private ReportPosDateWiseRepository reportPosDateWiseRepository;

    public List<ReportPosDateWise> getReportPosDateWise(String date) throws Exception {

        try {

            return reportPosDateWiseRepository.findByBackupDate(date);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportPosDateWiseReport");
            log.error("Request: date===> " + date);
            log.error("Error: " + e.getMessage());
            throw e;
        }

    }
}
