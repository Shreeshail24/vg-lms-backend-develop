package com.samsoft.lms.newux.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.dto.response.reports.GetAmortBasicDetailsReportResDto;
import com.samsoft.lms.newux.dto.response.reports.InterestIncomeResDto;
import com.samsoft.lms.newux.dto.response.reports.SOAReportResDto;
import com.samsoft.lms.newux.exceptions.LoansInternalServerError;
import com.samsoft.lms.newux.reports.entity.*;
import com.samsoft.lms.newux.reports.service.*;
import com.samsoft.lms.newux.services.ReportService;

@Slf4j
@RestController
@RequestMapping(value = "/report")
@CrossOrigin(origins = {"https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
        "https://qa-losone.4fin.in"}, allowedHeaders = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CustomerwiseOutstandingPortfolioService customerwiseOutstandingPortfolioService;

    @Autowired
    private ReportAgrOverduesService reportAgrOverduesService;

    @Autowired
    private InterestCertificateReportService interestCertificateReportService;

    @Autowired
    private ReportPosDateWiseService reportPosDateWiseService;

    @Autowired
    private ReportProvisionDetailsService reportProvisionDetailsService;

    @Autowired
    private ReportGSTDetailsService reportGSTDetailsService;

    @Autowired
    private ReportAmortBasicDetailsService reportAmortBasicDetailsService;

    @Autowired
    private ReportCustomerAgingBucketService reportCustomerAgingBucketService;

    @Autowired
    private ReportTrialBalanceService reportTrialBalanceService;

    @Autowired
    private ReportSMA2Service reportSMA2Service;

    @Autowired
    private ReportSMALargeBorrowerService reportSMALargeBorrowerService;

    @Autowired
    private ReportBillOfSupplyService reportBillOfSupplyService;

    @Autowired
    private ReportCollectionReceiptService reportCollectionReceiptService;

    @Autowired
    private PNTReportService pntReportService;

    @Autowired
    private CustomerUnutilizedLimitService customerUnutilizedLimitService;

    @GetMapping(value = "/getSOAReport", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<SOAReportResDto> soaReport(@RequestParam String mastAgrId, @RequestParam String customerId)
            throws Exception {
        FourFinResponse<SOAReportResDto> response = new FourFinResponse<>();
        try {
            SOAReportResDto res = reportService.soaReport(mastAgrId, customerId);
            response.setHttpStatus(HttpStatus.OK);
            response.setResponseCode(HttpStatus.OK.value());
            response.setData(res);

        } catch (Exception e) {

            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            throw new LoansInternalServerError(e.getMessage());
        }

        return response;
    }

    @GetMapping(value = "/getCustomerwiseOutstandingPortfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<CustomerwiseOutstandingPortfolio>> getCustomerwiseOutstandingPortfolio(
            @RequestParam String homeBranch, @RequestParam String assetClass) throws Exception {
        FourFinResponse<List<CustomerwiseOutstandingPortfolio>> response = new FourFinResponse<>();
        try {
            List<CustomerwiseOutstandingPortfolio> outstandingPortfolios = customerwiseOutstandingPortfolioService
                    .getCustomerwiseOutstandingPortfolio(homeBranch, assetClass);
            if (!outstandingPortfolios.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(outstandingPortfolios);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportAgrOverdues", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportAgrOverdues>> getReportAgrOverdues() throws Exception {
        FourFinResponse<List<ReportAgrOverdues>> response = new FourFinResponse<>();
        try {
            List<ReportAgrOverdues> agrOverdues = reportAgrOverduesService.getReportAgrOverdues();
            if (!agrOverdues.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(agrOverdues);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getInterestIncomeDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<InterestCertificateReport>> getInterestIncomeDetail(@RequestParam String fromDate,
                                                                                    @RequestParam String toDate) throws Exception {
        FourFinResponse<List<InterestCertificateReport>> response = new FourFinResponse<>();
        try {
            List<InterestCertificateReport> interestCertificateReports = interestCertificateReportService
                    .getInterestIncomeDetail(fromDate, toDate);
            if (!interestCertificateReports.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(interestCertificateReports);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportPosDateWise", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportPosDateWise>> getReportPosDateWise(@RequestParam String date) throws Exception {
        FourFinResponse<List<ReportPosDateWise>> response = new FourFinResponse<>();
        try {
            List<ReportPosDateWise> reportPosDateWiseList = reportPosDateWiseService.getReportPosDateWise(date);
            if (!reportPosDateWiseList.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(reportPosDateWiseList);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportProvisionDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportProvisionDetails>> getReportProvisionDetails(@RequestParam String fromDate,
                                                                                   @RequestParam String toDate) throws Exception {
        FourFinResponse<List<ReportProvisionDetails>> response = new FourFinResponse<>();
        try {
            List<ReportProvisionDetails> reportProvisionDetails = reportProvisionDetailsService
                    .getReportProvisionDetails(fromDate, toDate);
            if (!reportProvisionDetails.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(reportProvisionDetails);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportGSTDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportGSTDetails>> getReportGSTDetails(@RequestParam String fromDate,
                                                                       @RequestParam String toDate) throws Exception {
        FourFinResponse<List<ReportGSTDetails>> response = new FourFinResponse<>();
        try {
            List<ReportGSTDetails> reportGSTDetails = reportGSTDetailsService.getReportGSTDetails(fromDate, toDate);
            if (!reportGSTDetails.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(reportGSTDetails);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getAmortBasicDetailsReport", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<GetAmortBasicDetailsReportResDto> getAmortBasicDetailsReport(@RequestParam String mastAgrId,
                                                                                        @RequestParam String customerId) throws Exception {
        FourFinResponse<GetAmortBasicDetailsReportResDto> response = new FourFinResponse<>();
        try {
            GetAmortBasicDetailsReportResDto res = reportAmortBasicDetailsService.getAmortBasicDetailsReport(mastAgrId,
                    customerId);
            if (res != null) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportCustomerAgingBucket", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportCustomerAgingBucket>> getReportCustomerAgingBucket() throws Exception {
        FourFinResponse<List<ReportCustomerAgingBucket>> response = new FourFinResponse<>();
        try {
            List<ReportCustomerAgingBucket> res = reportCustomerAgingBucketService.getReportCustomerAgingBucket();
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportTrialBalance", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportTrialBalance>> getReportTrialBalance(@RequestParam String fromDate,
                                                                           @RequestParam String toDate) throws Exception {
        FourFinResponse<List<ReportTrialBalance>> response = new FourFinResponse<>();
        try {
            List<ReportTrialBalance> res = reportTrialBalanceService.getReportTrialBalance(fromDate, toDate);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getReportsma2", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportSMA2>> getReportSMA2(@RequestParam Integer fromDPD, @RequestParam Integer toDPD,
                                                           @RequestParam String servBranch) throws Exception {
        FourFinResponse<List<ReportSMA2>> response = new FourFinResponse<>();
        try {
            List<ReportSMA2> res = reportSMA2Service.getSMA2Report(fromDPD, toDPD, servBranch);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @GetMapping(value = "/getsmalargeborrower", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportSMALargeBorrower>> getReportSMALargeborrower(@RequestParam String servBranch,
                                                                                   @RequestParam Integer fromDPD, @RequestParam Integer toDPD, @RequestParam Double fromoutstandingAmount,
                                                                                   @RequestParam Double tooutstandingAmount) throws Exception {
        FourFinResponse<List<ReportSMALargeBorrower>> response = new FourFinResponse<>();
        try {
            List<ReportSMALargeBorrower> res = reportSMALargeBorrowerService.getReportSMALargeBorrow(servBranch,
                    fromDPD, toDPD, fromoutstandingAmount, tooutstandingAmount);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/getbillofsupply", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportBillOfSupply>> getReportBillOfSupply(@RequestParam String mID,
                                                                           @RequestParam String installmentDt) throws Exception {
        FourFinResponse<List<ReportBillOfSupply>> response = new FourFinResponse<>();
        try {
            List<ReportBillOfSupply> res = reportBillOfSupplyService.getReportBillOfSupply(mID, installmentDt);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/pntreport", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<PNTReportDetails>> findByBatchIdorCustomerId(@RequestParam(required = false) Integer batchId,
                                                                             @RequestParam(required = false) String mastAgrId) throws Exception {
        FourFinResponse<List<PNTReportDetails>> response = new FourFinResponse<>();
        try {
            List<PNTReportDetails> res = pntReportService.findByBatchIdOrMastAgrId(batchId, mastAgrId);
            log.info("Report ReportPNT ==>" + res );
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/customersunutilizedlimit", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<CustomerUnutilizedLimit>> getCustomerUnutilizedLimitByCustomerId(@RequestParam String customerId) throws Exception {
        FourFinResponse<List<CustomerUnutilizedLimit>> response = new FourFinResponse<>();

        try {
            List<CustomerUnutilizedLimit> res = customerUnutilizedLimitService.getCustomerUnutilizedLimitByCustomerId(customerId);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }

        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = "/collectionreceipt", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<List<ReportCollectionReceipt>>
    getReportCollectionReceipt(@RequestParam String fromDate, @RequestParam
    String toDate) throws Exception {
        FourFinResponse<List<ReportCollectionReceipt>> response = new
                FourFinResponse<>();
        try {
            List<ReportCollectionReceipt> res =
                    reportCollectionReceiptService.getReportCollectionReceipt(fromDate, toDate);
            if (!res.isEmpty()) {
                response.setHttpStatus(HttpStatus.OK);
                response.setResponseCode(HttpStatus.OK.value());
                response.setData(res);
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                response.setData(null);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}
