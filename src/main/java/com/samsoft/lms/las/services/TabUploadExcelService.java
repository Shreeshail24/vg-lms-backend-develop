package com.samsoft.lms.las.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.las.dto.request.TabUploadExcelReqDto;
import com.samsoft.lms.las.entities.TabUploadExcel;
import com.samsoft.lms.las.repositories.TabUploadExcelRepository;

import java.util.Date;

@Service
@Slf4j
public class TabUploadExcelService {

    @Autowired
    private TabUploadExcelRepository tabUploadExcelRepository;

    public TabUploadExcel saveTabUploadExcel(TabUploadExcelReqDto tabUploadExcelReqDto) throws Exception {
        TabUploadExcel tabUploadExcel = null;

        try {
            tabUploadExcel = new TabUploadExcel();
            tabUploadExcel.setFileName(tabUploadExcelReqDto.getFileName());
            tabUploadExcel.setFileUrl(tabUploadExcelReqDto.getFileUrl());
            tabUploadExcel.setUserId(tabUploadExcelReqDto.getUserId());
            tabUploadExcel.setCreatedDateTime(new Date());
            tabUploadExcel = tabUploadExcelRepository.save(tabUploadExcel);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: saveTabUploadExcel");
            log.error("Request: " + tabUploadExcelReqDto);
            log.error("Error: "+ e);

            throw e;
        }

        return tabUploadExcel;
    }

    public TabUploadExcel getTabUploadExcelByUploadLasId(Integer uploadLasId) throws Exception {

        try {

            return tabUploadExcelRepository.findByUploadLasId(uploadLasId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getTabUploadExcelByUploadLasId");
            log.error("Request: " + uploadLasId);
            log.error("Error: "+ e);

            throw e;
        }
    }
}
