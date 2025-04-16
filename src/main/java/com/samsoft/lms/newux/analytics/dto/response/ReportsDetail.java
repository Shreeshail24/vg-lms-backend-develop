package com.samsoft.lms.newux.analytics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportsDetail {

    public String reportId;
    public String reportName;
    public String embedUrl;
}
