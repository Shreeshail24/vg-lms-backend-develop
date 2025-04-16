package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportCollectionReceipt;
import com.samsoft.lms.newux.reports.repository.ReportCollectionReceiptRepository;

import java.util.List;

@Service
@Slf4j
public class ReportCollectionReceiptService {
    @Autowired
    private ReportCollectionReceiptRepository reportCollectionReceiptRepository;

    public List<ReportCollectionReceipt> getReportCollectionReceipt(String fromDate, String toDate) throws Exception {
        try{
            log.info("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            return reportCollectionReceiptRepository.findByDtInstallmentDate(fromDate, toDate);
        }catch(Exception e){
            e.printStackTrace();
            log.error("Method: getReportProvisionDetails");
            log.error("Request: fromDate===> " + fromDate + " toDate===> " + toDate);
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
}
