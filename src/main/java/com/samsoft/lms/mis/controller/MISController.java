package com.samsoft.lms.mis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.FourFinResponse;
import com.samsoft.lms.mis.dto.CollectionDownloadDto;
import com.samsoft.lms.mis.service.MISService;

@RestController
@RequestMapping("/mis")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class MISController {
	
	@Autowired
	private MISService misService;
	
	@GetMapping("/get/collectiondetails")
	public FourFinResponse<List<CollectionDownloadDto>> getColllectionDetails() {
		FourFinResponse<List<CollectionDownloadDto>> response = new FourFinResponse<List<CollectionDownloadDto>>();
		
		try {
			List<CollectionDownloadDto> collectionDtoList = misService.getCollecttionData();
			
			if(collectionDtoList != null) {
				response.setHttpStatus(HttpStatus.OK);
				response.setResponseCode(HttpStatus.OK.value());
				response.setData(collectionDtoList);
			} else {
				response.setHttpStatus(HttpStatus.NOT_FOUND);
				response.setResponseCode(HttpStatus.NOT_FOUND.value());
				response.setResponseMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
				response.setData(collectionDtoList);
			}
			
		} catch (Exception e) {
			response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			response.setResponseMessage(e.getMessage());
			e.printStackTrace();
		}
		
		return response;
	}
	
}
