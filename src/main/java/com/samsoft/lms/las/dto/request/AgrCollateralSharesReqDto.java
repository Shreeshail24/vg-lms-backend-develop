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
public class AgrCollateralSharesReqDto {

    private String mastAgrId;
    private Date trailDate;
    private Double fmv;
    private Double drawingPower;
    private Double actualLtv;
    private Integer uploadLasId;
}
