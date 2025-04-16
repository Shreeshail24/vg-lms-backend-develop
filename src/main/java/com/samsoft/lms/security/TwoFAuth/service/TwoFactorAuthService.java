//package com.samsoft.lms.security.TwoFAuth.service;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.samsoft.lms.security.TwoFAuth.dto.request.Validate2FACodeRequestDto;
//import com.samsoft.lms.security.TwoFAuth.dto.response.Generate2FAQRResponse;
//import com.samsoft.lms.security.TwoFAuth.dto.response.TwoFAuthResponse;
//import com.samsoft.lms.security.token.jwt.util.JwtTokenUtil;
//import com.samsoft.lms.security.token.jwt.util.TokenType;
//import com.samsoft.lms.security.userLogin.dto.LoginLogsReqDto;
//import com.samsoft.lms.security.userLogin.dto.LoginLogsResDto;
//import com.samsoft.lms.security.userLogin.entity.User;
//import com.samsoft.lms.security.userLogin.enums.LoginLogsEnum;
//import com.samsoft.lms.security.userLogin.enums.LoginLogsFailureReasonEnum;
//import com.samsoft.lms.security.userLogin.repository.UserRepository;
//import com.samsoft.lms.security.userLogin.service.LoginLogsService;
//import com.samsoft.lms.security.userLogin.service.UsersRolesService;
//
//import dev.samstevens.totp.code.CodeVerifier;
//import dev.samstevens.totp.qr.QrData;
//import dev.samstevens.totp.qr.QrDataFactory;
//import dev.samstevens.totp.secret.SecretGenerator;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@Slf4j
//public class TwoFactorAuthService {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private SecretGenerator secretGenerator;
//
//	@Autowired
//	private QrDataFactory qrDataFactory;
//
//	@Autowired
//	private CodeVerifier verifier;
//
//	@Autowired
//	private LoginLogsService LoginLogsService;
//	
//	@Autowired
//	private UsersRolesService usersRolesService;
//	
//	@Autowired
//	private JwtTokenUtil jwtTokenUtil;
//
//	@Value("${twoFactorAuthServerName}")
//	private String twoFactorAuthServerName;
//
//	public Generate2FAQRResponse generate2FAQR(String username) throws Exception {
//		try {
//			User user = userRepository.findByUserId(username);
//			Generate2FAQRResponse generate2faqrResponse = new Generate2FAQRResponse();
//
//			if (user != null) {
//
//				// Generate and store the secret
//				String secret = secretGenerator.generate();
//
//				QrData data = qrDataFactory.newBuilder().label(username).secret(secret).issuer("4Fin - 2FA-" + twoFactorAuthServerName).build();
//
//				user.setGoogleKey(secret);
//				userRepository.save(user);
//
//				generate2faqrResponse.setOtpAuthURL(data.getUri());
//				generate2faqrResponse.setStatus(Boolean.TRUE);
//
//				return generate2faqrResponse;
//			}
//
//			return null;
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
////			log.error("Error: " + e);
////			log.info("Method: generate2FAQR");
//			throw e;
//		}
//	}
//
//	public TwoFAuthResponse validate2FACode(Validate2FACodeRequestDto body,HttpServletRequest request) throws Exception {
//		try {
////			log.info("validate2FACode==========>" + body);
//			User user = userRepository.findByUserId(body.getUsername());
//			LoginLogsReqDto loginLogsReqDto = new LoginLogsReqDto();
//			if (user != null) {
//				String userIP=request.getHeader("user-ip");
//				String userLat=request.getHeader("user-lat");
//				String userLong=request.getHeader("user-long");
//				String userCountry=request.getHeader("user-country");
//				
//				String role = usersRolesService.getUsersRole(user.getUserId());
//
//				loginLogsReqDto.setLoginType(LoginLogsEnum.TWOFAUTH.getType());
//				loginLogsReqDto.setUserid(body.getUsername());
//				
////				loginLogsReqDto.setFailureReason(failureReason);
//				loginLogsReqDto.setUserIP(userIP);
//				loginLogsReqDto.setUserLat(userLat);
//				loginLogsReqDto.setUserLong(userLong);
//				loginLogsReqDto.setUserCountry(userCountry);
//				
//				if (verifier.isValidCode(user.getGoogleKey(), body.getCode())) {
//					
//					final String accessToken = jwtTokenUtil.generateToken(user.getUserId(), TokenType.ACCESS);
//					final String refreshToken = jwtTokenUtil.generateToken(user.getUserId(), TokenType.REFRESH);
////					log.info("accessToken===>"+accessToken);
////					log.info("refreshToken===>"+refreshToken);
//					
//					loginLogsReqDto.setSuccessYN('Y');
//					
//					// Save Login Logs
//					LoginLogsResDto loginLogsResDto = LoginLogsService.saveLoginLogs(loginLogsReqDto);
////					LoginLogsResDto loginLogsResDto = this.saveLoginLogs(body.getUsername(), 'Y', null, LoginLogsEnum.TWOFAUTH.getType());
////					log.info("loginLogsResDto ===> "+ loginLogsResDto);
//					
//					return new TwoFAuthResponse(Boolean.TRUE, loginLogsResDto.getLoginKey(), accessToken, refreshToken, role);
//				}
//				loginLogsReqDto.setSuccessYN('N');
//				loginLogsReqDto.setFailureReason(LoginLogsFailureReasonEnum.INVALIDCODE.getFailureReason());
//				
//				LoginLogsService.saveLoginLogs(loginLogsReqDto);
//
//				// Save Login Logs
////				this.saveLoginLogs(body.getUsername(), 'N', LoginLogsFailureReasonEnum.INVALIDCODE.getFailureReason(), LoginLogsEnum.TWOFAUTH.getType());
//
//				return new TwoFAuthResponse(Boolean.FALSE, null, null, null, null);
//			}
//
//			loginLogsReqDto.setFailureReason(LoginLogsFailureReasonEnum.USERNOTFOUND.getFailureReason());
//			loginLogsReqDto.setSuccessYN('N');
//			LoginLogsService.saveLoginLogs(loginLogsReqDto);
//			// Save Login Logs
////			this.saveLoginLogs(body.getUsername(), 'N', LoginLogsFailureReasonEnum.USERNOTFOUND.getFailureReason(), LoginLogsEnum.TWOFAUTH.getType());
//
//			return new TwoFAuthResponse(Boolean.FALSE, null, null, null, null);
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
////			log.error("Error: " + e);
////			log.info("Method: generate2FAQR");
//
//			// Save Login Logs
//			this.saveLoginLogs(body.getUsername(), 'N', LoginLogsFailureReasonEnum.SERVERERROR.getFailureReason(), LoginLogsEnum.TWOFAUTH.getType());
//			
//			throw e;
//		}
//	}
//
//	public Boolean twoFAuthSecretKeyExists(String username) throws Exception {
//		try {
//
//			User user = userRepository.findByUserId(username);
//
//			if (user != null) {
//
//				if (user.getGoogleKey() != null) {
//					return Boolean.TRUE;
//				}
//
//				return Boolean.FALSE;
//			}
//
//			return Boolean.FALSE;
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
////			log.error("Error: " + e);
////			log.info("Method: twoFAuthSecretKeyExists");
//			throw e;
//		}
//	}
//
//	protected LoginLogsResDto saveLoginLogs(String userid, Character successYN, String failureReason, String loginType) throws Exception {
//
//		LoginLogsReqDto loginLogsReqDto = new LoginLogsReqDto();
//		loginLogsReqDto.setLoginType(loginType);
//		loginLogsReqDto.setUserid(userid);
//		loginLogsReqDto.setSuccessYN(successYN);
//		loginLogsReqDto.setFailureReason(failureReason);
//
//		LoginLogsResDto loginLogsResDto = LoginLogsService.saveLoginLogs(loginLogsReqDto);
//		
//		return loginLogsResDto;
//
//	}
//}
