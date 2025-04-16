package com.samsoft.lms.newux.reports.service; 

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.PNTReportDetails;
import com.samsoft.lms.newux.reports.repository.ReportPNTRepository;

import java.util.List;

@Service
@Slf4j
public class PNTReportService {
    @Autowired
    private ReportPNTRepository reportPNTrepository;

    public List<PNTReportDetails> findByBatchIdOrMastAgrId(Integer batchId,String mastAgrId) throws Exception {
        try{
            log.info("Report ReportPNT ==>" + "batchId" + batchId + "customerId" + mastAgrId );

//            return reportPNTrepository.findByBatchIdOrMastAgrId(batchId,customerId);
            return reportPNTrepository.findByBatchIdOrMastAgrId(batchId,mastAgrId);
        }
        catch(Exception e){
            e.printStackTrace();
            log.error("Method: ReportPNT");
            log.error("Report ReportPNT ==>" + "batchId" + batchId + "customerId" + mastAgrId );
            log.error("Error: " + e.getMessage());
            throw e;

        }
    }
    
}
