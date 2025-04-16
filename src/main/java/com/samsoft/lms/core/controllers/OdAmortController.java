package com.samsoft.lms.core.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.OdAmortDto;
import com.samsoft.lms.core.dto.OdAmortScheduleDto;
import com.samsoft.lms.core.exceptions.AmortExceptions;
import com.samsoft.lms.core.services.OdAmort;

@RestController
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
@RequestMapping(value = "/od")
public class OdAmortController {
	@Autowired
	private OdAmort odAmort;

	@PostMapping(value = "/getOdAmort", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OdAmortScheduleDto>> getOdAmort(@RequestBody OdAmortDto odDto) {

		List<OdAmortScheduleDto> amort = new ArrayList<OdAmortScheduleDto>();

		try {
			amort = odAmort.getOdAmort(odDto);
			if (!(amort.size() > 0)) {
				throw new AmortExceptions("No record found in amort");
			}
		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return ResponseEntity.ok(amort);
	}

}
