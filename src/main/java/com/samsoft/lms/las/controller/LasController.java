package com.samsoft.lms.las.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.samsoft.lms.aws.dto.DocumentViewResDto;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.las.dto.request.UploadExcelReqDto;
import com.samsoft.lms.las.dto.request.UploadLasExcelReqDto;
import com.samsoft.lms.las.dto.response.GetLasDetailsResDto;
import com.samsoft.lms.las.dto.response.GetLasPortfolioSummaryResDto;
import com.samsoft.lms.las.dto.response.LatestValuationSharesResDto;
import com.samsoft.lms.las.dto.response.UploadLasTrailResDto;
import com.samsoft.lms.las.services.LasService;

import java.util.List;

@RestController
@RequestMapping(value = "/las")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class LasController {

    @Autowired
    private LasService lasService;

    @PostMapping(value = "/uploadLasExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<Boolean> uploadLasExcel(@RequestParam("file") MultipartFile file,  @RequestParam("userId") String userId) throws Exception {
        FourFinResponse<Boolean> fourFinResponse = new FourFinResponse<Boolean>();

        try {
            Boolean response = lasService.uploadLasExcel(file, userId);
            if(response) {
                fourFinResponse.setHttpStatus(HttpStatus.OK);
                fourFinResponse.setResponseCode(HttpStatus.OK.value());
                fourFinResponse.setResponseMessage("Excel Uploaded Successfully.");
                fourFinResponse.setData(true);
            } else {
                fourFinResponse.setHttpStatus(HttpStatus.NOT_FOUND);
                fourFinResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
                fourFinResponse.setResponseMessage("Excel not Uploaded.");
                fourFinResponse.setData(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fourFinResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            fourFinResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            fourFinResponse.setResponseMessage("Excel not Uploaded.");
            fourFinResponse.setData(null);

            throw e;
        }
        return fourFinResponse;
    }

    @PostMapping(value = "/updateLas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<Boolean> updateLas(@RequestParam("mastAgrId") String mastAgrId, @RequestParam("amount") Double amount, @RequestParam("type") String type) throws Exception {
        FourFinResponse<Boolean> fourFinResponse = new FourFinResponse<Boolean>();

        try {
            Boolean response = lasService.updateLas(mastAgrId, amount, type);
            if(response) {
                fourFinResponse.setHttpStatus(HttpStatus.OK);
                fourFinResponse.setResponseCode(HttpStatus.OK.value());
                fourFinResponse.setResponseMessage("Record Updated Successfully.");
                fourFinResponse.setData(true);
            } else {
                fourFinResponse.setHttpStatus(HttpStatus.NOT_FOUND);
                fourFinResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
                fourFinResponse.setResponseMessage("Record not Updated.");
                fourFinResponse.setData(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            fourFinResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            fourFinResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            fourFinResponse.setResponseMessage("Record not Updated.");
            fourFinResponse.setData(null);

            throw e;
        }
        return fourFinResponse;
    }

    @GetMapping(value = "latestValuationShares")
    public FourFinResponse<LatestValuationSharesResDto> latestValuationShares(@RequestParam("mastAgrId") String mastAgrId) {
        FourFinResponse<LatestValuationSharesResDto> response = new FourFinResponse<>();

        try {

            LatestValuationSharesResDto res = lasService.latestValuationShares(mastAgrId);

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
            e.printStackTrace();
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(e.getMessage());
            response.setData(null);
        }

        return response;
    }

    @GetMapping(value = "getLasDetails")
    public FourFinResponse<GetLasDetailsResDto> getLasDetails(@RequestParam("mastAgrId") String mastAgrId) {
        FourFinResponse<GetLasDetailsResDto> response = new FourFinResponse<>();

        try {

            GetLasDetailsResDto res = lasService.getLasDetails(mastAgrId);

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
            e.printStackTrace();
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(e.getMessage());
            response.setData(null);
        }

        return response;
    }

    @GetMapping(value = "getLasPortfolioSummary")
    public FourFinResponse<GetLasPortfolioSummaryResDto> getLasPortfolioSummary() {
        FourFinResponse<GetLasPortfolioSummaryResDto> response = new FourFinResponse<>();

        try {

            GetLasPortfolioSummaryResDto res = lasService.getLasPortfolioSummary();

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
            e.printStackTrace();
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(e.getMessage());
            response.setData(null);
        }

        return response;
    }

    @GetMapping(value = "getLatestLasUploadExcelTrail")
    public FourFinResponse<List<UploadLasTrailResDto>> getLatestLasUploadExcelTrail() {
        FourFinResponse<List<UploadLasTrailResDto>> response = new FourFinResponse<>();

        try {

            List<UploadLasTrailResDto> res = lasService.getLatestLasUploadExcelTrail();

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
            e.printStackTrace();
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setResponseMessage(e.getMessage());
            response.setData(null);
        }

        return response;
    }
}
