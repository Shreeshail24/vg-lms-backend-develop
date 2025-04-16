package com.samsoft.lms.las.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.repositories.AgrMasterAgreementRepository;
import com.samsoft.lms.las.dto.request.TabUploadLasTrailReqDto;
import com.samsoft.lms.las.entities.TabUploadLasTrail;
import com.samsoft.lms.las.repositories.TabUploadLasTrailRepository;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TabUploadLasTrailService {

    @Autowired
    private TabUploadLasTrailRepository tabUploadLasTrailRepository;

    private AgrMasterAgreementRepository agrMasterAgreementRepository;

    public TabUploadLasTrail saveTabUploadLasTrail(TabUploadLasTrailReqDto tabUploadLasTrailReqDto) throws Exception {

        TabUploadLasTrail tabUploadLasTrail = null;

        try {

            tabUploadLasTrail = new TabUploadLasTrail();
            tabUploadLasTrail.setUploadLasId(tabUploadLasTrailReqDto.getUploadLasId());
            tabUploadLasTrail.setMastAgrId(tabUploadLasTrailReqDto.getMastAgrId());
            tabUploadLasTrail.setIsin(tabUploadLasTrailReqDto.getIsin());
            tabUploadLasTrail.setNameOfShare(tabUploadLasTrailReqDto.getNameOfShare());
            tabUploadLasTrail.setQuantityOfShare(tabUploadLasTrailReqDto.getQuantityOfShare());
            tabUploadLasTrail.setPriceOfShare(tabUploadLasTrailReqDto.getPriceOfShare());
            tabUploadLasTrail.setCreatedDateTime(new Date());
            tabUploadLasTrailRepository.save(tabUploadLasTrail);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: saveTabUploadLasTrail");
            log.error("Request: " + tabUploadLasTrailReqDto);
            log.error("Error: " + e);

            throw e;
        }

        return tabUploadLasTrail;
    }

    public List<TabUploadLasTrail> getTabUploadLasTrail(Integer uploadLasId, String mastAgrId) throws Exception {

        try {

            return tabUploadLasTrailRepository.findByUploadLasIdAndMastAgrId(uploadLasId, mastAgrId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getTabUploadLasTrail");
            log.error("Request: uploadLasId: " + uploadLasId + " mastAgrId: " + mastAgrId);
            log.error("Error: " + e);

            throw e;
        }

    }
}
