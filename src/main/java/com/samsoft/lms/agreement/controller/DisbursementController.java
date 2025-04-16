package com.samsoft.lms.agreement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.agreement.dto.*;
import com.samsoft.lms.agreement.exceptions.AgreementBadRequestException;
import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.agreement.exceptions.AgreementInternalServerError;
import com.samsoft.lms.agreement.services.DisbursementService;

@RestController
@RequestMapping(value = "/interface")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in" , "https://qa-losone.4fin.in"}, allowedHeaders = "*")
public class DisbursementController {

	@Autowired
	private DisbursementService disbursementService;

	@PostMapping(value = "/disbursement", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto loanBoaring(@RequestBody DisbursementBoardingDto disbursementBoarding) {
		try {
			String result = disbursementService.disbursementApi(disbursementBoarding);
			return new AgreementResponseDto(HttpStatus.OK.value(), result);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new AgreementInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/disbursementOdFirst", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto disbursementOdFirst(@RequestBody DisbursementBoardingOdFirstDto disbursementBoarding) {
		try {
			//String result = disbursementService.executeOdDisbursementApi(executeDisbursementBoardingOdFirstDto);

			//Commented by bhagyashri kadam
			//Instead of disbursementOdFirstApi calling executeOdDisbursementApi method because we are checking all drawdown request against that master agreement id
			//If any drawdown requests are available then we are calling disbursementOdSubsequent method  otherwise we are calling disbursementOdFirstApi method
			String result = disbursementService.disbursementOdFirstApi(disbursementBoarding);

			return new AgreementResponseDto(HttpStatus.OK.value(), result);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new AgreementInternalServerError(e.getMessage());
		}
	}

	@PostMapping(value = "/disbursementOdSubsequent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto disbursementOdSubsequentApi(
			@RequestBody DisbursementBoardingOdSubsequentDto disbursementBoarding) throws Exception {
		try {
			String result = disbursementService.disbursementOdSubsequentApi(disbursementBoarding);
			return new AgreementResponseDto(HttpStatus.OK.value(), result);
		} catch (AgreementDataNotFoundException e) {
			throw e;
		} catch (AgreementBadRequestException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping(value = "/postEcsInstrument", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public AgreementResponseDto postEcsInstruments(@RequestParam String mastAgrId) {
		try {
			String result = disbursementService.postEcsInstruments(mastAgrId);
			return new AgreementResponseDto(HttpStatus.OK.value(), result);
		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new AgreementInternalServerError(e.getMessage());
		}
	}
}
