//package com.samsoft.lms.security.userLogin.service;
//
//import java.util.Date;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.samsoft.lms.security.userLogin.dto.LoginLogsReqDto;
//import com.samsoft.lms.security.userLogin.dto.LoginLogsResDto;
//import com.samsoft.lms.security.userLogin.entity.LoginLogs;
//import com.samsoft.lms.security.userLogin.repository.LoginLogsRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class LoginLogsService {
//	
//	@Autowired
//	private LoginLogsRepository loginLogsRepository;
//	
//	public LoginLogsResDto saveLoginLogs(LoginLogsReqDto loginLogsReqDto) throws Exception {
//
//		try {
//			
////			log.info("saveLoginLogs Request====> " + loginLogsReqDto);
//			
//			LoginLogs loginLogs = new LoginLogs();
//			loginLogs.setLoginType(loginLogsReqDto.getLoginType());
//			loginLogs.setUserid(loginLogsReqDto.getUserid());
//			loginLogs.setDateTime(new Date());
//			loginLogs.setSuccessYN(loginLogsReqDto.getSuccessYN());
//			loginLogs.setFailureReason(loginLogsReqDto.getFailureReason());
//			loginLogs.setCountry(loginLogsReqDto.getUserCountry());
//			loginLogs.setIpAddress(loginLogsReqDto.getUserIP());
//			loginLogs.setLat(loginLogsReqDto.getUserLat());
//			loginLogs.setLongitude(loginLogsReqDto.getUserLong());
//
//			loginLogsRepository.save(loginLogs);
////			log.info("Saved saveLoginLogs====> " + loginLogs);
//			
//			return new LoginLogsResDto(loginLogs.getLoginKey(), Boolean.TRUE);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
////			log.info("saveLoginLogs=====> " + loginLogsReqDto);
////			log.info("saveLoginLogs Error: " + e);
//			throw e;
//		}
//	}
//}
