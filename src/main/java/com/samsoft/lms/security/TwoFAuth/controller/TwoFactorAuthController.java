//package com.samsoft.lms.security.TwoFAuth.controller;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.samsoft.lms.common.enums.ResponseMessageEnum;
//import com.samsoft.lms.core.dto.FourFinResponse;
//import com.samsoft.lms.security.TwoFAuth.dto.request.Validate2FACodeRequestDto;
//import com.samsoft.lms.security.TwoFAuth.dto.response.Generate2FAQRResponse;
//import com.samsoft.lms.security.TwoFAuth.dto.response.TwoFAuthResponse;
//import com.samsoft.lms.security.TwoFAuth.service.TwoFactorAuthService;
//import com.samsoft.lms.security.TwoFAuth.validator.TwoFactorAuthValidator;
//
//import io.swagger.annotations.Api;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@Api(tags = "Two Factor Auth RESTful Services", value = "TwoFactorAuthController")
//@RequestMapping("/TwoFAuth")
//@Slf4j
////@CrossOrigin(origins = { "https://loans.4fin.in", "http://localhost:3000",
////		"https://lms.4fin.in" }, allowedHeaders = "*")
//@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in", "https://qa-collection.4fin.in", "https://fftest.fintaar.ai", "http://yarnbizqa.4fin.in", "http://yarnbizqalms.4fin.in"} ,allowedHeaders = "*")
//public class TwoFactorAuthController {
//
//	@Autowired
//	private TwoFactorAuthService twoFactorAuthService;
//
//	@Autowired
//	private TwoFactorAuthValidator twoFactorAuthValidator;
//
//	@GetMapping("/generate2FAQR/{username}")
//	public FourFinResponse<Generate2FAQRResponse> generate2FAQR(@PathVariable String username,
//			HttpServletResponse response) {
//
//		FourFinResponse<Generate2FAQRResponse> fourFinResponse = new FourFinResponse<Generate2FAQRResponse>();
//
//		try {
//			Generate2FAQRResponse generate2faqrResponse = twoFactorAuthService.generate2FAQR(username);
//
//			if (generate2faqrResponse != null) {
//
//				fourFinResponse.setHttpStatus(HttpStatus.OK);
//				fourFinResponse.setResponseCode(HttpStatus.OK.value());
//				fourFinResponse.setResponseMessage(ResponseMessageEnum.DATA_GET_SUCCESS.getMessage());
//				fourFinResponse.setData(generate2faqrResponse);
//			} else {
//				fourFinResponse.setHttpStatus(HttpStatus.NOT_FOUND);
//				fourFinResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
//				fourFinResponse.setResponseMessage(ResponseMessageEnum.DATA_NOT_FOUND.getMessage());
//			}
//		} catch (Exception e) {
//			fourFinResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			fourFinResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			fourFinResponse.setResponseMessage(e.getMessage());
//		}
//
//		return fourFinResponse;
//	}
//
//	@PostMapping("/validate2FACode")
//	public FourFinResponse<TwoFAuthResponse> validate2FACode(@RequestBody Validate2FACodeRequestDto body,HttpServletRequest request)
//			throws Exception {
//
//		FourFinResponse<TwoFAuthResponse> fourFinResponse = new FourFinResponse<TwoFAuthResponse>();
//
//		try {
//
//			twoFactorAuthValidator.validate2FACodeRequest(body);
//
//			TwoFAuthResponse twoFAuthResponse = twoFactorAuthService.validate2FACode(body,request);
////			log.info("twoFAuthResponse=========>" + twoFAuthResponse);
//			if (twoFAuthResponse != null) {
//				fourFinResponse.setHttpStatus(HttpStatus.OK);
//				fourFinResponse.setResponseCode(HttpStatus.OK.value());
//				if (twoFAuthResponse.isValid()) {
//					fourFinResponse.setResponseMessage(ResponseMessageEnum.OTP_VERIFIED.getMessage());
//				} else {
//					fourFinResponse.setResponseMessage(ResponseMessageEnum.OTP_WRONG.getMessage());
//				}
//				fourFinResponse.setData(twoFAuthResponse);
//			} else {
//				fourFinResponse.setHttpStatus(HttpStatus.NOT_FOUND);
//				fourFinResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
//				fourFinResponse.setResponseMessage(ResponseMessageEnum.OTP_WRONG.getMessage());
//				fourFinResponse.setData(twoFAuthResponse);
//			}
//
//		} catch (Exception e) {
//			fourFinResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			fourFinResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			fourFinResponse.setResponseMessage(e.getMessage());
//		}
//
//		return fourFinResponse;
//	}
//
//	@GetMapping("/twoFAuthSecretKeyExists/{username}")
//	public FourFinResponse<Boolean> twoFAuthSecretKeyExists(@PathVariable String username) throws Exception {
//
//		FourFinResponse<Boolean> fourFinResponse = new FourFinResponse<Boolean>();
//
//		try {
//
//			Boolean res = twoFactorAuthService.twoFAuthSecretKeyExists(username);
//
//			if (res) {
//
//				fourFinResponse.setHttpStatus(HttpStatus.OK);
//				fourFinResponse.setResponseCode(HttpStatus.OK.value());
//				fourFinResponse.setData(res);
//			} else {
//				fourFinResponse.setHttpStatus(HttpStatus.NOT_FOUND);
//				fourFinResponse.setResponseCode(HttpStatus.NOT_FOUND.value());
//				fourFinResponse.setData(res);
//			}
//
//		} catch (Exception e) {
//			fourFinResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			fourFinResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			fourFinResponse.setResponseMessage(e.getMessage());
//		}
//
//		return fourFinResponse;
//	}
//
//}
