package com.samsoft.lms.document.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadExternalDocumentRequest {

    private String docCode;
    private String folderName;
    private MultipartFile file;
}