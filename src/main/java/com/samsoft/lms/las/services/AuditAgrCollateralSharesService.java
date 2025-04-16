package com.samsoft.lms.las.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsoft.lms.las.dto.request.AuditAgrCollateralSharesReqDto;
import com.samsoft.lms.las.entities.AuditAgrCollateralShares;
import com.samsoft.lms.las.repositories.AuditAgrCollateralSharesRepository;

import java.util.Date;

@Service
@Slf4j
public class AuditAgrCollateralSharesService {

    @Autowired
    private AuditAgrCollateralSharesRepository auditAgrCollateralSharesRepository;

    @Transactional
    public AuditAgrCollateralShares saveAuditAgrCollateralShares(AuditAgrCollateralSharesReqDto auditAgrCollateralSharesReqDto) throws Exception {

        AuditAgrCollateralShares auditAgrCollateralShares = null;

        try {

            auditAgrCollateralShares = new AuditAgrCollateralShares();
            auditAgrCollateralShares.setMastAgrId(auditAgrCollateralSharesReqDto.getMastAgrId());
            auditAgrCollateralShares.setFmv(auditAgrCollateralSharesReqDto.getFmv());
            auditAgrCollateralShares.setDrawingPower(auditAgrCollateralSharesReqDto.getDrawingPower());
            auditAgrCollateralShares.setActualLtv(auditAgrCollateralSharesReqDto.getActualLtv());
            auditAgrCollateralShares.setTrailDate(auditAgrCollateralSharesReqDto.getTrailDate());
            auditAgrCollateralShares.setUploadLasId(auditAgrCollateralSharesReqDto.getUploadLasId());
            auditAgrCollateralShares.setCreatedDateTime(new Date());

            auditAgrCollateralShares = auditAgrCollateralSharesRepository.save(auditAgrCollateralShares);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Method: saveAuditAgrCollateralShares");
            log.error("Request: " + auditAgrCollateralSharesReqDto);
            log.error("Error: " + e);

            throw e;
        }

        return auditAgrCollateralShares;
    }
}
