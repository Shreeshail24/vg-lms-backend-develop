package com.samsoft.lms.newux.analytics.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PowerBIAnalyticsReqDto {

    private String tenantIdentifier;
    private String userToken;
    private String reportToken;
}
