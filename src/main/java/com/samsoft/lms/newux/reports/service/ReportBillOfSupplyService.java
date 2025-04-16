package com.samsoft.lms.newux.reports.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.ReportBillOfSupply;
import com.samsoft.lms.newux.reports.repository.ReportBillOfSupplyRepository;

import java.util.List;

@Service
@Slf4j
public class ReportBillOfSupplyService {
    @Autowired
    private ReportBillOfSupplyRepository reportBillOfSupplyRepository;

    public List<ReportBillOfSupply> getReportBillOfSupply(String mID, String installmentDt) throws Exception {
        try{
            log.info("Report Bill Of Supplyr Form Data ==>" + "mID" + mID + "Installment Date" + installmentDt );
            return reportBillOfSupplyRepository.FindByMIDandInstallmentDt(mID, installmentDt);

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Method: ReportBillOfSupply");
            log.error("Report Bill Of Supplyr Form Data ==>" + "mID" + mID + "From DPD" + installmentDt );
            log.error("Error: " + e.getMessage());
            throw e;
        }
    }
}
