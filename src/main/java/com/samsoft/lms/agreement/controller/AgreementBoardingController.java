package com.samsoft.lms.agreement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.agreement.dto.AgreementBoardingDto;
import com.samsoft.lms.agreement.dto.AgreementBoardingOdDto;
import com.samsoft.lms.agreement.dto.AgreementBoardingSFDto;
import com.samsoft.lms.agreement.dto.AgreementResponseDto;
import com.samsoft.lms.agreement.dto.AgreementValidationDto;
import com.samsoft.lms.agreement.services.AgreementBoardingService;
import com.samsoft.lms.core.exceptions.CoreBadRequestException;
import com.samsoft.lms.core.exceptions.CoreDataNotFoundException;
import com.samsoft.lms.core.exceptions.CoreInternalServerError;

@RestController
@RequestMapping("/interface")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class AgreementBoardingController {

	@Autowired
	private AgreementBoardingService interfaceService;

	@PostMapping(value = "/agreementBoarding", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto agreementBoarding(@RequestBody AgreementBoardingDto agreementBoardingParameter)
			throws Exception {
		try {
			String mastAgrId = interfaceService.agreementBoarding(agreementBoardingParameter);
			return new AgreementResponseDto(HttpStatus.OK.value(), mastAgrId);
		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}
	
	@PostMapping(value = "/agreementBoardingOd", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto agreementBoardingOd(@RequestBody AgreementBoardingOdDto agreementBoardingParameter)
			throws Exception {
		try {
			String mastAgrId = interfaceService.agreementBoardingOd(agreementBoardingParameter);
			return new AgreementResponseDto(HttpStatus.OK.value(), mastAgrId);
		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping(value = "/agreementValidation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AgreementValidationDto> agreementValidation(
			@RequestBody AgreementBoardingDto agreementBoardingParameter) {
		List<AgreementValidationDto> result = new ArrayList<AgreementValidationDto>();
		try {
			result = interfaceService.agreementValidation(agreementBoardingParameter);
			return result;
		} catch (CoreDataNotFoundException e) {
			throw new CoreDataNotFoundException(e.getMessage());
		} catch (CoreBadRequestException e) {
			throw new CoreBadRequestException(e.getMessage());
		} catch (Exception e) {
			throw new CoreInternalServerError(e.getMessage());
		}

	}
	
	@PostMapping(value = "/agreementBoardingSF", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto agreementBoardingSF(@RequestBody AgreementBoardingSFDto agreementBoardingParameter)
			throws Exception {
		try {
			String mastAgrId = interfaceService.agreementBoardingSF(agreementBoardingParameter);
			return new AgreementResponseDto(HttpStatus.OK.value(), mastAgrId);
		} catch (CoreDataNotFoundException e) {
			throw e;
		} catch (CoreBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}

	}
}
