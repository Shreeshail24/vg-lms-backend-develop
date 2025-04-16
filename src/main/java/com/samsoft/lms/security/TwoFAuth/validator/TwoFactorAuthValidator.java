//package com.samsoft.lms.security.TwoFAuth.validator;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import com.samsoft.lms.common.enums.ExceptionMessageEnum;
//import com.samsoft.lms.core.exceptions.InvalidInputException;
//import com.samsoft.lms.security.TwoFAuth.dto.request.Validate2FACodeRequestDto;
//
//@Component
//public class TwoFactorAuthValidator {
//	
//	public Boolean validate2FACodeRequest(Validate2FACodeRequestDto body) throws InvalidInputException {
//		if(body == null) {
//			throw new InvalidInputException(ExceptionMessageEnum.INPUT_EMPTY.getMessage(), HttpStatus.BAD_REQUEST);
//		} else if(StringUtils.isEmpty(body.getUsername())) {
//			throw new InvalidInputException(ExceptionMessageEnum.INVALID_USERID.getMessage(), HttpStatus.BAD_REQUEST);
//		} else if(StringUtils.isEmpty(body.getCode())) {
//			throw new InvalidInputException(ExceptionMessageEnum.INVALID_OTP.getMessage(), HttpStatus.BAD_REQUEST);
//		} else {
//			return Boolean.TRUE;
//		}
//	}
//}
