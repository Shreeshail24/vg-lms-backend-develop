package com.samsoft.lms.newux.dto.response.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetAmortBasicDetailsReportResDto {

    private String customerName;
    private String disbDate;
    private String originationApplnNo;
    private String nbfc;
}
