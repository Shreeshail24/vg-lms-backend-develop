//package com.samsoft.lms.security.userLogin.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.samsoft.lms.security.userLogin.entity.UsersRoles;
//import com.samsoft.lms.security.userLogin.repository.UsersRolesRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class UsersRolesService {
//
//	@Autowired
//	private UsersRolesRepository usersRolesRepository;
//	
//	public String getUsersRole(String userId) throws Exception{
//		String userRole = "";
//		try {
//			UsersRoles usersRoles = usersRolesRepository.findByUserId(userId);
//			if(usersRoles != null) {
//				userRole = usersRoles.getRoleCode();
//			}
//			
//		} catch (Exception e) {
////			log.error("Method Name : getUsersRole");
////			log.error("Request userId : " + userId);
////			log.error("Exception : " + e);
//			e.printStackTrace();
//			throw e;
//		}
//		return userRole;
//	}
//}
