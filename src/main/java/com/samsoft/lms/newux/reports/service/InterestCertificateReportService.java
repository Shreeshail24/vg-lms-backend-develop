package com.samsoft.lms.newux.reports.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.newux.reports.entity.InterestCertificateReport;
import com.samsoft.lms.newux.reports.repository.InterestCertificateReportRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InterestCertificateReportService {

    @Autowired
    private InterestCertificateReportRepository interestCertificateReportRepository;

//    @Autowired
//    private AgrTrnSysTranDtlRepository agrTrnSysTranDtlRepository;

    public List<InterestCertificateReport> getInterestIncomeDetail(String fromDate, String toDate) throws Exception {
        try {
            return interestCertificateReportRepository.findByTranDate(fromDate, toDate);
//			if (!interestCertificateReportList.isEmpty()) {
//				for (InterestCertificateReport interestCertificateReport : interestCertificateReportList) {
//					InterestIncomeResDto interestIncomeResDto = new InterestIncomeResDto();
//					interestIncomeResDto.setMastAgrId(interestCertificateReport.getMastAgrId());
//					interestIncomeResDto.setCustomerId(interestCertificateReport.getCustomerId());
//					interestIncomeResDto.setPortfolioCode(interestCertificateReport.getPortfolioCode());
//					interestIncomeResDto.setProductCode(interestCertificateReport.getProductCode());
//					interestIncomeResDto.setInterestDueAmount(interestCertificateReport.getInterestDueAmount());
//					interestIncomeResDto.setInterestPaidAmount(interestCertificateReport.getInterestPaidAmount());
//
//					Double accruedInterestAmount = agrTrnSysTranDtlRepository
//							.getTotalTranAmt(interestCertificateReport.getMastAgrId());
//					interestIncomeResDto.setAccruedInterestAmount(accruedInterestAmount);
//					incomeResDtoList.add(interestIncomeResDto);
//				}
//			}
        } catch (Exception e) {
            log.error("Error in getInterestIncomeDetail()  " + e.getMessage());
            throw e;
        }
    }
}
