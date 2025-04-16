//package com.samsoft.lms.security.userLogin.service;
//
//import java.util.Date;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import com.samsoft.lms.security.userLogin.dto.ExternalLoginAttemptsReqDto;
//import com.samsoft.lms.security.userLogin.entity.ExternalLoginAttempts;
//import com.samsoft.lms.security.userLogin.repository.ExternalLoginAttemptsRepository;
//import com.samsoft.lms.util.PlatformUtils;
//
//@Service
//@Slf4j
//public class ExternalLoginAttemptsService {
//	
//	@Autowired
//	private ExternalLoginAttemptsRepository externalLoginAttemptsRepository;
//	
//	@Autowired
//	private PlatformUtils platFormUtils;
//	
//	public Boolean saveExternalLoginAttempts(ExternalLoginAttemptsReqDto externalLoginAttemptsReqDto,HttpServletRequest request) throws Exception {
//		
//		try {
//			
//			log.info("externalLoginAttemptsReqDto===> " + externalLoginAttemptsReqDto);
//			
//			String userIP=request.getHeader("user-ip");
//			String userLat=request.getHeader("user-lat");
//			String userLong=request.getHeader("user-long");
//			String userCountry=request.getHeader("user-country");
//			
//			
//			ExternalLoginAttempts externalLoginAttempts = new ExternalLoginAttempts();
//			externalLoginAttempts.setEmail(externalLoginAttemptsReqDto.getEmail());
//			externalLoginAttempts.setDateTime(new Date());
//			externalLoginAttempts.setIpAddress(userIP);
//			externalLoginAttempts.setLat(userLat);
//			externalLoginAttempts.setLongitude(userLong);
//			externalLoginAttempts.setCountry(userCountry);
//			externalLoginAttemptsRepository.save(externalLoginAttempts);
//			
//			
//			log.info("saved ExternalLoginAttempts===> " + externalLoginAttempts);
//			log.info("sending mail while login by unauthorized user!!!!!!!");
//
//			String serverBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
//			String[] arrOfStr = serverBaseUrl.split("://", 2);
//			log.info("serverBaseUrl: {}, arrOfStr: {}", serverBaseUrl, arrOfStr);
//			if(!arrOfStr[0].contains("qa")) {
//				platFormUtils.sendUnauthorizedUserMail(externalLoginAttemptsReqDto.getEmail(),userIP);
//			}
//			log.info("saved ExternalLoginAttempts===> " + externalLoginAttempts);
//			
//			return Boolean.TRUE;
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			log.info("Method name: saveExternalLoginAttempts");
//			log.info("Request params: " + externalLoginAttemptsReqDto);
//			log.error("Error: " + e);
//			
//			throw e;
//		}
//		
//	}
//}
