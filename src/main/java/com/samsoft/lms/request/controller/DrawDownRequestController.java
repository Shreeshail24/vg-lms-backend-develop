package com.samsoft.lms.request.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.samsoft.lms.agreement.exceptions.AgreementDataNotFoundException;
import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.request.dto.*;
import com.samsoft.lms.request.services.DrawDownRequestService;
import com.samsoft.lms.request.services.DrawdownRequestWorklistService;

import javax.validation.Valid;

@RestController
@Api(tags = "DrawDown request RESTful Services", value = "DrawDownRequestController")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in" , "https://qa-losone.4fin.in"}, allowedHeaders = "*")
@RequestMapping(value = "/drawdownrequest")
@Validated
public class DrawDownRequestController {

	@Autowired
	private DrawDownRequestService downRequestService;

	@Autowired
	private DrawdownRequestWorklistService reqWorklistService;

	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> createDrawdownRequest(@Valid @RequestBody DrawDownRequestDto downRequestDto)
			throws Exception {
		Boolean requestHeader = true;
		try {
			requestHeader = downRequestService.createDrawdownRequest(downRequestDto);

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return ResponseEntity.ok(requestHeader);

	}

//	@PostMapping(value = "/createLas", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Boolean> createLasDrawdownRequest(@Valid @RequestBody DrawDownRequestDto downRequestDto)
//			throws Exception {
//		Boolean requestHeader = true;
//		try {
//			requestHeader = downRequestService.createLasDrawdownRequest(downRequestDto);
//
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		}
//		return ResponseEntity.ok(requestHeader);
//
//	}

	@PostMapping(value = "/getdetail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DrawDownGetRequestDto getDrawDownRequest(@RequestBody DrawDownGetDto downGetDto) throws Exception {

		DrawDownGetRequestDto downResponseDto = null;
		try {
			downResponseDto = downRequestService.getDrawDownRequestDetail(downGetDto);

		} catch (AgreementDataNotFoundException e) {
			throw new AgreementDataNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw e;
		}

		return downResponseDto;
	}

	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public FourFinResponse<String> updateDrawDownRequest(@Valid @RequestBody DrawDownUpdateDto downUpdateDto)
			throws Exception {
		FourFinResponse<String> response = new FourFinResponse<>();
		try {
			String result = downRequestService.updateDrawDownRequest(downUpdateDto);
			if (result.equalsIgnoreCase("success")) {
				response.setHttpStatus(HttpStatus.OK);
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
				response.setData(result);
			} else {
				response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			}

		} catch (Exception e) {
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			response.setData(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

//	@PostMapping(value = "/updateLas", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public FourFinResponse<String> updateLasDrawDownRequest(@Valid @RequestBody DrawDownUpdateDto downUpdateDto)
//			throws Exception {
//		FourFinResponse<String> response = new FourFinResponse<>();
//		try {
//			String result = downRequestService.updateLasDrawDownRequest(downUpdateDto);
//			if (result.equalsIgnoreCase("success")) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
//				response.setData(result);
//			} else {
//				response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//				response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			}
//
//		} catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			response.setData(e.getMessage());
//			e.printStackTrace();
//		}
//		return response;
//	}
//
//	@PostMapping(value = "/updateLasDrawDownUtr", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public FourFinResponse<Boolean> updateLasDrawDownUtr(@Valid @RequestBody LasDrawDownRequestDto downRequestDto)
//			throws Exception {
//		FourFinResponse<Boolean> response = new FourFinResponse<>();
//		try {
//			Boolean result = downRequestService.updateLasDrawDownUtr(downRequestDto);
//			if (result) {
//				response.setHttpStatus(HttpStatus.OK);
//				response.setResponseCode(HttpStatus.OK.value());
//				response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
//				response.setData(result);
//			} else {
//				response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//				response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			}
//
//		} catch (Exception e) {
//			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			response.setData(null);
//			e.printStackTrace();
//		}
//		return response;
//	}

	@GetMapping(value = "/getAllDrawdownRequestId", produces = MediaType.APPLICATION_JSON_VALUE)
	public AllDrawdownRequestIdMainDto getAllDrawdownRequestId(@RequestParam String statusValue, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
		try {
			Character status = Character.valueOf(statusValue.charAt(0));
			return reqWorklistService.getAllDrawdownRequestId(status, pageNo, pageSize);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping(value = "/getAllDrawdownRequestIdSearch", produces = MediaType.APPLICATION_JSON_VALUE)
	public AllDrawdownRequestIdMainDto getAllDrawdownRequestIdSearch(@RequestParam String mastAgrId, @RequestParam String statusValue) {
		try {
			
			Character status = Character.valueOf(statusValue.charAt(0));
			return reqWorklistService.getAllDrawdownRequestIdSearch(mastAgrId, status);
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping(value = "/updateUtrNoAfterApproval", produces = MediaType.APPLICATION_JSON_VALUE)
	public FourFinResponse<String> updateUtrNoAfterApproval(@RequestParam String mastAgrId, @RequestParam String utrNo) {
		FourFinResponse<String> response = new FourFinResponse<>();
		try {
			String result = downRequestService.updateUtrNoAfterApproval(mastAgrId, utrNo);
			if (result.equalsIgnoreCase("success")) {
				response.setHttpStatus(HttpStatus.OK);
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
				response.setData(result);
			} else {
				response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
				response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			}
		} catch (Exception e) {
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			response.setData(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping(value = "/getalldrawdownrequestsbymasteragrid", produces = MediaType.APPLICATION_JSON_VALUE)
	public AllDrawdownRequestIdMainDto getAllDrawdownRequestsByMasterAgrId(@RequestParam String mastAgrId) throws Exception {
		try {
			return reqWorklistService.getAllDrawdownRequestsByMasterAgrId(mastAgrId);
		} catch (Exception e) {
			throw e;
		}

	}
}
