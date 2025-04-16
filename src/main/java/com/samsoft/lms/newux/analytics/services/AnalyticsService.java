package com.samsoft.lms.newux.analytics.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.samsoft.lms.newux.analytics.dto.request.PowerBIAnalyticsReqDto;
import com.samsoft.lms.newux.analytics.dto.response.PowerBIAnalyticsResDto;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsService {

    public static final String tenantIdentifier = "79FDC495-A632-ED11-AB5F-6045BD735970";
    public static final String userToken = "$2b$10$owV4.lJwtd10AHsfIOljPObHdyVlPi0Z3VYoJXjL97CaUoYqOKnHe";
    public static final String reportToken = "$2b$10$owV4.lJwtd10AHsfIOljPO45y.YY3LgzIQTs24OkSkTkuIHQIzcmG";

    public PowerBIAnalyticsResDto powerBIAnalytics() throws Exception {

        PowerBIAnalyticsReqDto powerBIAnalyticsReqDto = null;
        try {
            powerBIAnalyticsReqDto = new PowerBIAnalyticsReqDto();
            powerBIAnalyticsReqDto.setTenantIdentifier(tenantIdentifier);
            powerBIAnalyticsReqDto.setUserToken(userToken);
            powerBIAnalyticsReqDto.setReportToken(reportToken);

            String requestBody = new GsonBuilder().serializeNulls().create().toJson(powerBIAnalyticsReqDto);

            log.info("powerBIAnalytics Request: " + requestBody);

            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post("http://20.204.112.166:3000/powerbi/embedfortenant")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(requestBody)
                    .asString();

            PowerBIAnalyticsResDto powerBIAnalyticsResDto = new Gson().fromJson(response.getBody(), PowerBIAnalyticsResDto.class);

            log.info("response===> " + response.getBody() + ", Status ===> " + response.getStatus());

            if (response.getStatus() == HttpStatus.SC_CREATED) {
                log.info("PowerBIAnalyticsResDto===>" + powerBIAnalyticsResDto);

                return powerBIAnalyticsResDto;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Method: powerBIAnalytics");
            log.info("Request: " + powerBIAnalyticsReqDto);
            log.error("Error: " + e);
            throw e;
        }

        return null;
    }
}
