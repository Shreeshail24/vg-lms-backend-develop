package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportSMA2;
import com.samsoft.lms.newux.reports.repository.ReportSMA2Repository;

import java.util.List;

@Service
@Slf4j
public class ReportSMA2Service {

    @Autowired
    private ReportSMA2Repository reportSMA2Repository;
    public List<ReportSMA2> getSMA2Report(Integer fromDPD, Integer toDPD, String servBranch) throws Exception {
        try {
            log.info("SMA2 Form Data ==>"+"From DPD"+fromDPD+"toDPD"+"servBranch"+servBranch);
            return reportSMA2Repository.findByfromDPDtoDPDandServBranch(servBranch,fromDPD,toDPD);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportTrialBalance");
            log.error("Request: servBranch===> " + servBranch + " fromDPD===> " + fromDPD + " toDPD===> " + toDPD);
            log.error("Error: " + e.getMessage());
            throw e;
        }

    }
}
