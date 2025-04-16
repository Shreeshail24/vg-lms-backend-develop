package com.samsoft.lms.las.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.las.dto.request.AgrCollateralSharesReqDto;
import com.samsoft.lms.las.entities.AgrCollateralShares;
import com.samsoft.lms.las.repositories.AgrCollateralSharesRepository;

import java.util.Date;

@Service
@Slf4j
public class AgrCollateralSharesService {

    @Autowired
    private AgrCollateralSharesRepository agrCollateralSharesRepository;

    public AgrCollateralShares saveAgrCollateralShares(AgrCollateralSharesReqDto agrCollateralSharesReqDto) throws Exception {
        AgrCollateralShares agrCollateralShares = null;

        try {

            agrCollateralShares = agrCollateralSharesRepository.findByMastAgrId(agrCollateralSharesReqDto.getMastAgrId());
            log.info("saveAgrCollateralShares===> agrCollateralShares: " + agrCollateralShares);

            if(agrCollateralShares != null) {

                agrCollateralShares.setMastAgrId(agrCollateralSharesReqDto.getMastAgrId());
                agrCollateralShares.setTrailDate(agrCollateralSharesReqDto.getTrailDate());
                agrCollateralShares.setFmv(agrCollateralSharesReqDto.getFmv());
                agrCollateralShares.setDrawingPower(agrCollateralSharesReqDto.getDrawingPower());
                agrCollateralShares.setActualLtv(agrCollateralSharesReqDto.getActualLtv());
                agrCollateralShares.setUploadLasId(agrCollateralSharesReqDto.getUploadLasId());
                agrCollateralShares.setUpdatedDateTime(new Date());
                agrCollateralShares = agrCollateralSharesRepository.save(agrCollateralShares);

            } else {

                agrCollateralShares = new AgrCollateralShares();
                agrCollateralShares.setMastAgrId(agrCollateralSharesReqDto.getMastAgrId());
                agrCollateralShares.setTrailDate(agrCollateralSharesReqDto.getTrailDate());
                agrCollateralShares.setFmv(agrCollateralSharesReqDto.getFmv());
                agrCollateralShares.setDrawingPower(agrCollateralSharesReqDto.getDrawingPower());
                agrCollateralShares.setActualLtv(agrCollateralSharesReqDto.getActualLtv());
                agrCollateralShares.setUploadLasId(agrCollateralSharesReqDto.getUploadLasId());
                agrCollateralShares.setCreatedDateTime(new Date());
                agrCollateralShares = agrCollateralSharesRepository.save(agrCollateralShares);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: saveAgrCollateralShares");
            log.error("Request: " + agrCollateralSharesReqDto);
            log.error("Error: " + e);

            throw e;
        }

        return agrCollateralShares;
    }

    public void updateUploadLasId(String mastAgrId, Integer uploadLasId) throws Exception {
        AgrCollateralShares agrCollateralShares = null;
        try {

            agrCollateralShares = agrCollateralSharesRepository.findByMastAgrId(mastAgrId);
            log.info("After updateUploadLasId===> agrCollateralShares: " + agrCollateralShares);

            if(agrCollateralShares != null) {
                agrCollateralShares.setUploadLasId(uploadLasId);
                agrCollateralSharesRepository.save(agrCollateralShares);
            }
            log.info("Before updateUploadLasId===> agrCollateralShares: " + agrCollateralShares);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: updateUploadLasId");
            log.error("Request: " + mastAgrId);
            log.error("Error: " + e);

            throw e;
        }
    }

    public AgrCollateralShares getAgrCollateralSharesByMastAgrId(String mastAgrId) throws Exception {
        try {

            return agrCollateralSharesRepository.findByMastAgrId(mastAgrId);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: getAgrCollateralSharesByMastAgrId");
            log.error("Request: " + mastAgrId);
            log.error("Error: " + e);

            throw e;
        }
    }


}
