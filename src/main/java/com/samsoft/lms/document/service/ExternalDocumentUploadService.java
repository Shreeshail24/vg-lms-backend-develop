package com.samsoft.lms.document.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.samsoft.lms.aws.service.AWSService;
import com.samsoft.lms.document.dto.request.UploadExternalDocumentRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
@Slf4j
public class ExternalDocumentUploadService {

    @Value("${destinationPath}")
    private String destinationPath;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.secretKey}")
    private String key;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Autowired
    private AWSService awsService;

    public String uploadExternalDocument(UploadExternalDocumentRequest uploadExternalDocumentRequest) throws Exception {

        InputStream inputStream = null;
        try {

            long epoch = new Date().getTime();
            String fileName = org.springframework.util.StringUtils
                    .getFilename(uploadExternalDocumentRequest.getFile().getOriginalFilename());
            fileName = fileName.replace(" ", "_");

            String docCode = uploadExternalDocumentRequest.getDocCode();
            if (StringUtils.isEmpty(destinationPath)) {
                throw new Exception("Destination Path can not be Empty!!");
            }

            inputStream = uploadExternalDocumentRequest.getFile().getInputStream();
            String destFolder = destinationPath + "/ " + uploadExternalDocumentRequest.getFolderName() + "/" + docCode;
            File dst = new File(destFolder);
            if (!dst.exists()) {
                Path dir = Paths.get(destFolder);
                Files.createDirectories(dir);
                log.info("destFolder is created!!");
            }

            StringBuilder sb = new StringBuilder(destFolder);
            sb.append("/").append(epoch).append("_").append(fileName);
            String locateFilePath = sb.toString();
            log.info("bucketName is: " + bucketName + " locateFilePath: " + locateFilePath);

            Path path = Paths.get(locateFilePath);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            String s3BucketUrl = awsService.uploadToS3Bucket(uploadExternalDocumentRequest.getFile().getContentType(),
                    locateFilePath);

            this.deleteLocalFileAndMkdir(destFolder);

            return s3BucketUrl;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("uploadExternalDocument :: Method: uploadExternalDocument");
            log.error("uploadExternalDocument :: Request: {}", uploadExternalDocumentRequest);
            log.error("uploadExternalDocument :: Error: {}", e.getMessage());

            throw e;
        }
    }

    private Boolean deleteLocalFileAndMkdir(String LocalfolderPath) {

        try {

            // Deleting Local Folder Path
            FileUtils.deleteDirectory(new File(LocalfolderPath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;

    }
}

