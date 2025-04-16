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
public class TabUploadExcelReqDto {

    private String fileName;
    private String fileUrl;
    private String userId;
}
