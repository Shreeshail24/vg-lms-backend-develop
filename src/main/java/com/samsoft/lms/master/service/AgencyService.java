package com.samsoft.lms.master.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samsoft.lms.master.entity.Agency;
import com.samsoft.lms.master.repositories.AgencyRepository;

@Service
@Slf4j
public class AgencyService {

    @Autowired
    private AgencyRepository agencyRepository;

    public Agency getAgencyByAgencyId(Integer agencyId) throws Exception{
        try{
            Agency agency = agencyRepository.findByAgencyId(agencyId);
            return agency;
        }catch(Exception e){
            e.printStackTrace();
            log.error("IN method getAgencyByAgencyId : "+e.getMessage());
            log.error("agencyId "+ agencyId.toString());
            throw e;
        }
    }

    public Agency saveAgency(Agency agency) throws Exception{
        try{
            Agency savedAgency = agencyRepository.save(agency);
            return savedAgency;
        }catch(Exception e){
            e.printStackTrace();
            log.error("IN method saveAgency : "+e.getMessage());
            log.error("agency "+ agency);
            throw e;
        }
    }
}
