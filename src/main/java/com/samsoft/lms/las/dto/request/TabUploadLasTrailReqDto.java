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
public class TabUploadLasTrailReqDto {

    private Integer uploadLasId;
    private String mastAgrId;
    private String isin;
    private String nameOfShare;
    private Double priceOfShare;
    private Integer quantityOfShare;
}
