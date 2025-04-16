package com.samsoft.lms.core.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.services.InterestService;

@RestController
@RequestMapping("/interest")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class InterestController {

	@Autowired
	private InterestService interestService;
	@Autowired
	private Environment env;

	@GetMapping(value = "/getInterestAmount", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getInterestAmount(@RequestParam Double amount, @RequestParam Float interestRate,
			@RequestParam String interestBasis, @RequestParam String interestStartDate,
			@RequestParam String interestEndDate) {

		Double intAmount = 0d;
		String dateFormat = env.getProperty("lms.global.date.format");
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		/*
		 * LocalDate dtStartDate = null, dtEndDate = null; try { dtStartDate =
		 * Instant.ofEpochMilli(sdf.parse(interestStartDate).getTime()).atZone(ZoneId.
		 * systemDefault()) .toLocalDate(); dtEndDate =
		 * Instant.ofEpochMilli(sdf.parse(interestEndDate).getTime()).atZone(ZoneId.
		 * systemDefault()) .toLocalDate();
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		Date dtStartDate = null, dtEndDate = null;
		try {
			dtStartDate = new SimpleDateFormat(dateFormat).parse(interestStartDate);
			dtEndDate = new SimpleDateFormat(dateFormat).parse(interestEndDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			intAmount = interestService.getInterestAmount(amount, interestRate, interestBasis, dtStartDate, dtEndDate);
			ResponseEntity.ok(intAmount);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(intAmount);

	}
}
