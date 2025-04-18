package com.samsoft.lms.las.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadLasExcelReqDto {
    private MultipartFile file;
    private String userid;
}