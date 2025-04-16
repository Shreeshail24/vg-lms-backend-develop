//package com.samsoft.lms.security.userLogin.service;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.samsoft.lms.security.userLogin.dto.SaveSecLoginActivitiesReqDto;
//import com.samsoft.lms.security.userLogin.entity.SecLoginActivities;
//import com.samsoft.lms.security.userLogin.repository.SecLoginActivitiesRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class SecLoginActivitiesService {
//
//	@Autowired
//	private SecLoginActivitiesRepository secLoginActivitiesRepository;
//	
//	public Boolean saveSecLoginActivities(SaveSecLoginActivitiesReqDto<?> saveSecLoginActivitiesReqDto) throws Exception {
//		
//		ObjectMapper mapper = new ObjectMapper();
//		
//		try {
//			
//			log.info("saveSecLoginActivitiesReqDto====> " + saveSecLoginActivitiesReqDto);
//			
//			SecLoginActivities secLoginActivities = new SecLoginActivities();
//			secLoginActivities.setLoginKey(saveSecLoginActivitiesReqDto.getLoginKey());
//			secLoginActivities.setActivityCode(saveSecLoginActivitiesReqDto.getActivityCode());
//			secLoginActivities.setActionCode(saveSecLoginActivitiesReqDto.getActionCode());
//			secLoginActivities.setActDateTime(new Date());
//			secLoginActivities.setJsonData(mapper.writeValueAsString(saveSecLoginActivitiesReqDto.getJsonData()));
//			secLoginActivities.setLeadNo(saveSecLoginActivitiesReqDto.getLeadNo());
//			secLoginActivities.setCreditApplicantId(saveSecLoginActivitiesReqDto.getCreditApplicantId());
//			
//			secLoginActivitiesRepository.save(secLoginActivities);
//			log.info("Saved secLoginActivities===> " + secLoginActivities);
//			
//			return Boolean.TRUE;
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			
//			log.info("Method Name: saveSecLoginActivities");
//			log.info("Request Params: " + saveSecLoginActivitiesReqDto);
//			log.error("saveSecLoginActivities Error: " + e);
//			
//			throw e;
//		}
//		
//	}
//}
