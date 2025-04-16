package com.samsoft.lms.newux.analytics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.newux.analytics.dto.response.PowerBIAnalyticsResDto;
import com.samsoft.lms.newux.analytics.services.AnalyticsService;
import com.samsoft.lms.newux.dto.response.getloans.GetAllLoansResDto;
import com.samsoft.lms.newux.exceptions.LoansInternalServerError;
import com.samsoft.lms.newux.exceptions.LoansNotFoundException;

@RestController
@RequestMapping(value = "/analytics")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping(value = "/powerBIAnalytics", produces = MediaType.APPLICATION_JSON_VALUE)
    public FourFinResponse<PowerBIAnalyticsResDto> powerBIAnalytics() throws Exception {
        FourFinResponse<PowerBIAnalyticsResDto> response = new FourFinResponse<>();
        try {
            PowerBIAnalyticsResDto res = analyticsService.powerBIAnalytics();
            if(res != null) {
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
            throw new LoansInternalServerError(e.getMessage());
        }

        return response;
    }
}
