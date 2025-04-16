package com.samsoft.lms.newux.analytics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PowerBIAnalyticsResDto {

    public ReportsDetail reportsDetail;
    public String embedToken;
    public String type;
    public ReportUserConfig reportUserConfig;
    public String errorMessage;
}
