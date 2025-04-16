package com.samsoft.lms.las.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.samsoft.lms.las.dto.response.GetFilePathResDto;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class FileUploadUtil {

    @Value("${destinationPath}")
    private String destinationPath;

    public String getFileName(MultipartFile file) {
        StringBuilder sb = null;
        DateFormat df = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        String date = df.format(new Date());

        String fileName = org.springframework.util.StringUtils.getFilename(file.getOriginalFilename());
        fileName = fileName.replace(" ", "_");

        sb = new StringBuilder();
        sb.append(date).append("_").append(fileName);

        return sb.toString();
    }

    public GetFilePathResDto getFilePath(MultipartFile file) throws Exception {

        InputStream inputStream = null;
        String localFilePath = "";
        GetFilePathResDto getFilePathResDto = new GetFilePathResDto();

        try {

            inputStream = file.getInputStream();
            String destFolder = destinationPath + "/las";
            File dst = new File(destFolder);

            if (!dst.exists()) {
                Path dir = Paths.get(destFolder);
                Files.createDirectories(dir);
                log.info("Dest Folder is created!!");
            }

            localFilePath = this.getFileName(file);
            log.info("localFilePath: " + localFilePath);
            StringBuilder sb = new StringBuilder(destFolder);
            sb.append("/").append(localFilePath);
            localFilePath = sb.toString();

            Path path = Paths.get(localFilePath);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            getFilePathResDto.setLocalFilePath(localFilePath);
            getFilePathResDto.setDestFolder(destFolder);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            log.info("getFilePath: " + file);
            log.error("getFilePath Error: " + e);
            throw e;
        }

        return getFilePathResDto;
    }
}
