//package com.samsoft.lms.security.userLogin.service;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.samsoft.lms.security.userLogin.dto.ExternalLoginAttemptsReqDto;
//import com.samsoft.lms.security.userLogin.dto.LoginLogsReqDto;
//import com.samsoft.lms.security.userLogin.dto.LoginLogsResDto;
//import com.samsoft.lms.security.userLogin.dto.UserDetailDto;
//import com.samsoft.lms.security.userLogin.dto.UserLoginRequestDto;
//import com.samsoft.lms.security.userLogin.entity.User;
//import com.samsoft.lms.security.userLogin.enums.LoginLogsEnum;
//import com.samsoft.lms.security.userLogin.enums.LoginLogsFailureReasonEnum;
//import com.samsoft.lms.security.userLogin.repository.UserRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class UserService {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private UsersRolesService usersRolesService;
//
//	@Autowired
//	private LoginLogsService LoginLogsService;
//
//	@Autowired
//	private ExternalLoginAttemptsService externalLoginAttemptsService;
//
//	public UserDetailDto validateUser(UserLoginRequestDto userLogin) throws Exception {
//
//		UserDetailDto userDetailDto = null;
//		try {
//
//			User userDetails = userRepository.findByUserIdAndStatus(userLogin.getUserId(), 'A');
//
//			if (userDetails != null) {
//				String role = usersRolesService.getUsersRole(userDetails.getUserId());
//				if (role.equals("")) {
//					userDetailDto = new UserDetailDto(Boolean.FALSE, role, userDetails.getUserId());
//				} else {
//					userDetailDto = new UserDetailDto(Boolean.TRUE, role, userDetails.getUserId());
//				}
//
//				// Save Login Logs
//				this.saveLoginLogs(userLogin.getUserId(), 'Y', null, LoginLogsEnum.LOGIN.getType());
//
//			} else {
//
//				userDetailDto = new UserDetailDto(Boolean.FALSE, null, userLogin.getUserId());
//
//				// saveExternalLoginAttempts
//				externalLoginAttemptsService
//						.saveExternalLoginAttempts(new ExternalLoginAttemptsReqDto(userLogin.getUserId()),null);
//
////				// Save Login Logs
////				this.saveLoginLogs(userLogin.getUserId(), 'N',
////						LoginLogsFailureReasonEnum.USERNOTFOUND.getFailureReason(), LoginLogsEnum.LOGIN.getType());
//			}
//
//		} catch (Exception e) {
////			log.error("Method Name : validateUser");
////			log.error("Request object : " + userLogin);
////			log.error("Exception : " + e);
//			e.printStackTrace();
//
//			// Save Login Logs
//			this.saveLoginLogs(userLogin.getUserId(), 'N', LoginLogsFailureReasonEnum.SERVERERROR.getFailureReason(),
//					LoginLogsEnum.LOGIN.getType());
//
//			throw e;
//		}
//
//		return userDetailDto;
//	}
//
//	public Boolean logoutUser(String userId) throws Exception {
//
//		try {
//
//			User userDetails = userRepository.findByUserId(userId);
//
//			if (userDetails != null) {
//				if (userDetails.getStatus() == 'A') {
//					// Save Login Logs
//					this.saveLoginLogs(userId, 'Y', null, LoginLogsEnum.LOGOUT.getType());
//
//					return Boolean.TRUE;
//				} else {
//					// Save Login Logs
//					this.saveLoginLogs(userId, 'N', LoginLogsFailureReasonEnum.INACTIVEUSER.getFailureReason(),
//							LoginLogsEnum.LOGOUT.getType());
//
//					return Boolean.FALSE;
//				}
//			}
//
//			return Boolean.FALSE;
//
//		} catch (Exception e) {
////			log.error("Method Name : logoutUser");
////			log.error("Request object : " + userId);
////			log.error("Exception : " + e);
//			e.printStackTrace();
//
//			// Save Login Logs
//			this.saveLoginLogs(userId, 'N', LoginLogsFailureReasonEnum.SERVERERROR.getFailureReason(),
//					LoginLogsEnum.LOGOUT.getType());
//
//			throw e;
//		}
//
//	}
//
//	public Boolean userExists(String userId,HttpServletRequest request) throws Exception {
//
//		try {
//			User userDetails = userRepository.findByUserId(userId);
//			String userIP=request.getHeader("user-ip");
//			String userLat=request.getHeader("user-lat");
//			String userLong=request.getHeader("user-long");
//			String userCountry=request.getHeader("user-country");
//			LoginLogsReqDto loginLogsReqDto = new LoginLogsReqDto();
//			loginLogsReqDto.setLoginType(LoginLogsEnum.GOOGLEAUTH.getType());
//			loginLogsReqDto.setUserIP(userIP);
//			loginLogsReqDto.setUserLat(userLat);
//			loginLogsReqDto.setUserLong(userLong);
//			loginLogsReqDto.setUserCountry(userCountry);
//			loginLogsReqDto.setUserid(userId);
//			if (userDetails != null) {
//				if (userDetails.getStatus() == 'A') {
//					loginLogsReqDto.setSuccessYN('Y');
////					log.info("loginLogsReqDto=======>"+loginLogsReqDto);
//					LoginLogsResDto loginLogsResDto = LoginLogsService.saveLoginLogs(loginLogsReqDto);
//					// Save Login Logs
////					this.saveLoginLogs(userId, 'Y', null, LoginLogsEnum.GOOGLEAUTH.getType());
//
//					return Boolean.TRUE;
//				} else {
//
//					// Save Login Logs
//					loginLogsReqDto.setSuccessYN('N');
//					loginLogsReqDto.setFailureReason(LoginLogsFailureReasonEnum.INACTIVEUSER.getFailureReason());
//					// Save Login Logs
////					log.info("loginLogsReqDto=======>"+loginLogsReqDto);
//					LoginLogsResDto loginLogsResDto = LoginLogsService.saveLoginLogs(loginLogsReqDto);
//
////					this.saveLoginLogs(userId, 'N', LoginLogsFailureReasonEnum.INACTIVEUSER.getFailureReason(),
////							LoginLogsEnum.GOOGLEAUTH.getType());
//
//					return Boolean.FALSE;
//				}
//			} else {
//
//				// saveExternalLoginAttempts
//				externalLoginAttemptsService.saveExternalLoginAttempts(new ExternalLoginAttemptsReqDto(userId),request);
//
//				return Boolean.FALSE;
//			}
//
//		} catch (Exception e) {
////			log.error("Method Name : userExists");
////			log.error("Request object : " + userId);
////			log.error("Exception : " + e);
//			e.printStackTrace();
//			throw e;
//		}
//
//	}
//
//	protected void saveLoginLogs(String userid, Character successYN, String failureReason, String loginType)
//			throws Exception {
//
//		LoginLogsReqDto loginLogsReqDto = new LoginLogsReqDto();
//		loginLogsReqDto.setLoginType(loginType);
//		loginLogsReqDto.setUserid(userid);
//		loginLogsReqDto.setSuccessYN(successYN);
//		loginLogsReqDto.setFailureReason(failureReason);
//
//		LoginLogsService.saveLoginLogs(loginLogsReqDto);
//
//	}
//}
