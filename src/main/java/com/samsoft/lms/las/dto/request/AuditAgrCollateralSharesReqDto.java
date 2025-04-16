package com.samsoft.lms.las.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuditAgrCollateralSharesReqDto {

    private String mastAgrId;
    private Double fmv;
    private Double drawingPower;
    private Double actualLtv;
    private Date trailDate;
    private Integer uploadLasId;
}
