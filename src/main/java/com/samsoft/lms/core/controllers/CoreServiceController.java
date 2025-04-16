package com.samsoft.lms.core.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.EventBaseChagesCalculationOutputDto;
import com.samsoft.lms.core.exceptions.AmortExceptions;
import com.samsoft.lms.core.services.CoreServices;
import com.samsoft.lms.core.services.InterestService;

@RestController
@RequestMapping("/coreservice")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in" , "https://qa-losone.4fin.in"}, allowedHeaders = "*")
public class CoreServiceController {
	@Autowired
	private CoreServices coreService;
	@Autowired
	private InterestService intService;

	@PostMapping(value = "/getEventBaseChargesCalculation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EventBaseChagesCalculationOutputDto> getEventBaseChargesCalculation(
			@RequestParam String mastAgrId, @RequestParam Integer instrumentId, @RequestParam String feeEvent,
			@RequestParam String bookFlag, Double basicAmount, String reversalYn) {
		EventBaseChagesCalculationOutputDto eventBaseChargesCalculation = null;

		try {
			eventBaseChargesCalculation = coreService.getEventBaseChargesCalculation(mastAgrId, instrumentId, feeEvent,
					bookFlag, basicAmount, reversalYn);

		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return ResponseEntity.ok(eventBaseChargesCalculation);
	}

	@GetMapping(value = "/getInterestDays", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int getInterestDays(@RequestParam String interestBasis, @RequestParam String toDate,
			@RequestParam String fromDate) {
		int result = 0;
		try {
			LocalDate dtStartDate = LocalDate.parse(fromDate);
			LocalDate dtEndDate = LocalDate.parse(toDate);
			result = intService.getInterestDays(interestBasis, dtStartDate, dtEndDate);

		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return result;
	}

}
