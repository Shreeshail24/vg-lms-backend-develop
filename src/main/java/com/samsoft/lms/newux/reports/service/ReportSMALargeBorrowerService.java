package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportSMALargeBorrower;
import com.samsoft.lms.newux.reports.repository.ReportSMALargeBorrowerRepository;

import java.util.List;

@Service
@Slf4j
public class ReportSMALargeBorrowerService {
    @Autowired
    private ReportSMALargeBorrowerRepository reportSMALargeBorrowerRepository;
    public List<ReportSMALargeBorrower> getReportSMALargeBorrow(String servBranch, Integer fromDPD, Integer toDPD, Double fromoutstandingAmount, Double tooutstandingAmount) throws Exception {
        try {
            log.info("SMA2 Large borrower Form Data ==>" + "servBranch" + servBranch + "From DPD" + fromDPD + "toDPD" + toDPD + "fromoutstandingAmount" + fromoutstandingAmount + "tooutstandingAmount" + tooutstandingAmount);
            return reportSMALargeBorrowerRepository.FindByDPDandBranchandOSAmount(servBranch, fromDPD, toDPD, fromoutstandingAmount, tooutstandingAmount);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getReportTrialBalance");
            log.error("SMA2 Large borrower Form Data ==>" + "servBranch" + servBranch + "From DPD" + fromDPD + "toDPD" + toDPD + "fromoutstandingAmount" + fromoutstandingAmount + "tooutstandingAmount" + tooutstandingAmount);
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }

}
