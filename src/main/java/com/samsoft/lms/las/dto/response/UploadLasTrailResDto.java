package com.samsoft.lms.las.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadLasTrailResDto {

    private String mastAgrId;
    private String isin;
    private String nameOfShare;
    private Double priceOfShare;
    private Integer quantityOfShare;
}
