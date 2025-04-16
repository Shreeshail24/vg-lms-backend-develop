package com.samsoft.lms.las.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.samsoft.lms.las.exception.FourFinException;

import java.util.Arrays;

@Component
@Slf4j
public class FileUploadValidator {

    public void validateFileUpload(MultipartFile file, String userId) throws FourFinException {
        log.info("in validateFileUpload");
        if (null == file || file.isEmpty()) {
            throw new FourFinException("Please select file to upload");
        } else if (null == userId || userId.isEmpty()) {
            throw new FourFinException("User Id cannot be null or empty");
        }

        String checkFileName = file.getOriginalFilename();
        String fileNameWithoutExtension = checkFileName.substring(0, checkFileName.lastIndexOf('.'));
        String fileExtension = checkFileName.substring(checkFileName.lastIndexOf('.'));
        for (int i = 0; i < fileNameWithoutExtension.length(); i++) {
            int asciiVal = (int) fileNameWithoutExtension.charAt(i);
            if ((asciiVal >= 32 && asciiVal <= 47) || (asciiVal >= 58 && asciiVal <= 64)
                    || (asciiVal >= 91 && asciiVal <= 96) || (asciiVal >= 123 && asciiVal <= 126)) {
                log.error("Special Character is not allowed in the filename");
                throw new FourFinException("Special character is not allowed in the filename");
            }
        }
        if (!Arrays.asList(".xls", ".xlsx").contains(fileExtension)) {
            log.error("Not a valid file");
            throw new FourFinException("Not a Valid File");
        }

    }
}
