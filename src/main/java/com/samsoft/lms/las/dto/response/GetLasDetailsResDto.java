package com.samsoft.lms.las.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetLasDetailsResDto {

    private String mastAgrId;
    private Double fmv;
    private Double drawingPower;
    private Double utilizedLimit;
    private Double limitSanctionAmount;
    private Double ltv;
    private Double availableLimit;
    private Double actualLtv;
    private String customerName;
    private String businessDate;
}
