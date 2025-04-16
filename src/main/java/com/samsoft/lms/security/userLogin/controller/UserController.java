//package com.samsoft.lms.security.userLogin.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.samsoft.lms.common.enums.ResponseMessageEnum;
//import com.samsoft.lms.core.dto.FourFinResponse;
//import com.samsoft.lms.security.token.jwt.service.JwtUserDetailsService;
//import com.samsoft.lms.security.token.jwt.util.JwtTokenUtil;
//import com.samsoft.lms.security.token.jwt.util.TokenType;
//import com.samsoft.lms.security.userLogin.dto.RefreshTokenRequest;
//import com.samsoft.lms.security.userLogin.dto.RefreshTokenResponse;
//import com.samsoft.lms.security.userLogin.dto.UserDetailDto;
//import com.samsoft.lms.security.userLogin.dto.UserLoginRequestDto;
//import com.samsoft.lms.security.userLogin.dto.ValidateTokenRequest;
//import com.samsoft.lms.security.userLogin.service.UserService;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.SignatureException;
//import io.swagger.annotations.Api;
//@RestController
//@Api(tags = "User Login RESTful Services", value = "UserController")
////@CrossOrigin(origins = {"https://loans.4fin.in","http://localhost:3000", "https://lms.4fin.in"} ,allowedHeaders = "*")
//@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in", "https://qa-collection.4fin.in", "https://fftest.fintaar.ai", "http://yarnbizqa.4fin.in", "http://yarnbizqalms.4fin.in"} ,allowedHeaders = "*")
////@Slf4j
//public class UserController {
//	
//	@Autowired
//	private UserService userService;
//	
//	
////	@Autowired
////	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	private JwtTokenUtil jwtTokenUtil;
//
//	@Autowired
//	private JwtUserDetailsService userDetailsService;
//	
////	@Autowired
////	private AuthenticationManager authenticationManager;
//
//	public static final Integer JWT_TOKEN_VALIDITY = 40;
//
//	public static final Integer JWT_REFRESH_TOKEN_VALIDITY = 60;
//	
//	@PostMapping("/validateUser")
//	public FourFinResponse<UserDetailDto> validateUser(@RequestBody UserLoginRequestDto userLogin) {
//		FourFinResponse<UserDetailDto> response = new FourFinResponse<>();
//		
//		try {
//			UserDetailDto userDetailDto = userService.validateUser(userLogin);
//			
//			if(userDetailDto != null) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(userDetailDto);
//			} else {
//				response.setHttpStatus(HttpStatus.NOT_FOUND);
//				response.setResponseCode(HttpStatus.NOT_FOUND.value());
//				response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//				response.setData(userDetailDto);
//			}
//			
//		} catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			response.setResponseMessage(e.getMessage());
//		}
//		
//		return response;
//	}
//	
//	
////	@PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
////	public FourFinResponse<AuthResponse> authenticateUser(@RequestBody JwtRequest userLogin) {
////		FourFinResponse<AuthResponse> response = new FourFinResponse<>();
////
////		try {
////				
//////				authenticate(userLogin.getUsername(), userLogin.getPassword());
//////				final UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin.getUsername());
////				
//////				log.info("userDetails===>"+userDetails);
////				
////				final String accessToken = jwtTokenUtil.generateToken(userLogin.getUsername(), TokenType.ACCESS);
////				log.info("accessToken===>"+accessToken);
////				
//////				final String refreshToken = jwtTokenUtil.generateToken(userDetails, TokenType.REFRESH);
//////				log.info("refreshToken===>"+refreshToken);
////
////				AuthResponse data = new AuthResponse(Boolean.TRUE, null,
////						userLogin.getUsername(), accessToken, null, JWT_TOKEN_VALIDITY);
////				response.setHttpStatus(HttpStatus.OK);
////				response.setResponseMessage(ResponseMessageEnum.SUCCESS.getMessage());
////				response.setResponseCode(HttpStatus.OK.value());
////				response.setData(data);
////
////		} catch (Exception e) {
////			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
////			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
////			response.setResponseMessage(e.getMessage());
////		}
////		return response;
////	}
//	
//	@PostMapping(value = "/refreshToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public FourFinResponse<RefreshTokenResponse> generateAccessToken(
//			@RequestBody RefreshTokenRequest refreshTokenRequest) {
//		FourFinResponse<RefreshTokenResponse> response = new FourFinResponse<RefreshTokenResponse>();
//		try {
//			final UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenRequest.getUsername());
//
////			if (jwtTokenUtil.validateToken(refreshTokenRequest.getRefreshKey(), refreshTokenRequest.getUsername())) {
//			if (jwtTokenUtil.validateToken(refreshTokenRequest.getRefreshKey(),refreshTokenRequest.getUsername())) {
////				final String accessToken = jwtTokenUtil.generateToken(userDetails, TokenType.ACCESS);
////				final String refreshToken = jwtTokenUtil.generateToken(userDetails, TokenType.REFRESH);
//				final String accessToken = jwtTokenUtil.generateToken(userDetails.getUsername(), TokenType.ACCESS);
//				final String refreshToken = jwtTokenUtil.generateToken(userDetails.getUsername(), TokenType.REFRESH);
//				RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(accessToken, refreshToken,
//						JWT_TOKEN_VALIDITY);
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseMessage(ResponseMessageEnum.SUCCESS.getMessage());
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(refreshTokenResponse);
//			} else {
//				response.setHttpStatus(HttpStatus.UNAUTHORIZED);
//				response.setResponseMessage(ResponseMessageEnum.INVALID_TOKEN.getMessage());
//				response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
//			}
//
//		}catch (SignatureException e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(ResponseMessageEnum.INVALID_TOKEN.getMessage());
//		}catch(ExpiredJwtException e) {
//			response.setHttpStatus(HttpStatus.UNAUTHORIZED);
//			response.setResponseMessage(ResponseMessageEnum.INVALID_TOKEN.getMessage());
//			response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
//		}
//		catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(e.getMessage());
//		}
//		return response;
//	}
//	
//	@PostMapping(value = "/validateAccessToken", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public FourFinResponse<Boolean> validateAccessToken(
//			@RequestBody ValidateTokenRequest validateTokenRequest) {
//		FourFinResponse<Boolean> response = new FourFinResponse<Boolean>();
//		try {
//
//			if (jwtTokenUtil.validateToken(validateTokenRequest.getAccessKey(),validateTokenRequest.getUsername())) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseMessage(ResponseMessageEnum.SUCCESS.getMessage());
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(Boolean.TRUE);
//			} else {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseMessage(ResponseMessageEnum.SUCCESS.getMessage());
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(Boolean.FALSE);
//			}
//
//		}catch (SignatureException e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(ResponseMessageEnum.INVALID_TOKEN.getMessage());
//		}catch(ExpiredJwtException e) {
//			response.setHttpStatus(HttpStatus.UNAUTHORIZED);
//			response.setResponseMessage(ResponseMessageEnum.INVALID_TOKEN.getMessage());
//			response.setResponseCode(HttpStatus.UNAUTHORIZED.value());
//		}
//		catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(e.getMessage());
//		}
//		return response;
//	}
//	
//	
////	private void authenticate(String username, String password) throws Exception {
////		try {
////			 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
////		} catch (DisabledException e) {
////			log.error("authenticate ==>"+e);
////			throw new Exception("USER_DISABLED", e);
////		} catch (BadCredentialsException e) {
////			log.error("authenticate ==>"+e);
////			throw new Exception("INVALID_CREDENTIALS", e);
////		}
////	}
//
//	@GetMapping("/logoutUser")
//	public FourFinResponse<Boolean> logoutUser(@RequestParam String userId) {
//		FourFinResponse<Boolean> response = new FourFinResponse<>();
//		
//		try {
//			Boolean res = userService.logoutUser(userId);
//			
//			if(res) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(res);
//			} else {
//				response.setHttpStatus(HttpStatus.NOT_FOUND);
//				response.setResponseCode(HttpStatus.NOT_FOUND.value());
//				response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//				response.setData(res);
//			}
//			
//		} catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			response.setResponseMessage(e.getMessage());
//		}
//		
//		return response;
//	}
//	
//	@GetMapping("/userExists")
//	public FourFinResponse<Boolean> userExists(@RequestParam String userId,HttpServletRequest request) {
//		FourFinResponse<Boolean> response = new FourFinResponse<>();
//		
//		try {
//			Boolean res = userService.userExists(userId,request);
//			
//			if(res) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setData(res);
//			} else {
//				response.setHttpStatus(HttpStatus.NOT_FOUND);
//				response.setResponseCode(HttpStatus.NOT_FOUND.value());
//				response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
//				response.setData(res);
//			}
//			
//		} catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			response.setResponseMessage(e.getMessage());
//		}
//		
//		return response;
//	}
//  
//}
