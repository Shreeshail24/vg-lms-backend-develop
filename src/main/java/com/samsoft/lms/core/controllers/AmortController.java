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

import com.samsoft.lms.core.dto.AmortParameter;
import com.samsoft.lms.core.dto.AmortVariationDto;
import com.samsoft.lms.core.dto.GetEmiDto;
import com.samsoft.lms.core.entities.Amort;
import com.samsoft.lms.core.exceptions.AmortExceptions;
import com.samsoft.lms.core.services.CoreAmort;
import com.samsoft.lms.core.services.CoreAmortVariation;

@RestController
@RequestMapping(value = "/amort")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in","https://qa-los.4fin.in","http://10.0.153.25:80","http://10.0.153.25","http://10.0.153.25:9090", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class AmortController {

	@Autowired
	private CoreAmort coreAmort;

	@Autowired
	private CoreAmortVariation coreAmortVariation;

	@PostMapping(value = "/getAmort", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Amort>> getAmort(@RequestBody AmortParameter amortJson) {
		// Gson g = new Gson();
		List<Amort> amort = null;
		// String jsonString = g.toJson(amortJson);
		try {
			amort = coreAmort.getAmort(amortJson);
			if (!(amort.size() > 0)) {
				throw new AmortExceptions("No record found in amort");
			}
		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return ResponseEntity.ok(amort);
	}

	@PostMapping(value = "/getAmortVariation", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Amort>> getAmort(@RequestBody AmortVariationDto amortDto) {
		List<Amort> amortVariation = new ArrayList<Amort>();
		try {
			amortVariation = coreAmortVariation.getAmortVariation(amortDto);
			if (!(amortVariation.size() > 0)) {
				throw new AmortExceptions("No record found in amort");
			}
		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return ResponseEntity.ok(amortVariation);
	}

	@PostMapping(value = "/getEmi", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> getEmi(@RequestBody GetEmiDto emiDto) {
		Double result = 0d;
		try {
			result = coreAmort.roundInstallment(
					coreAmort.getEMI(emiDto.getAmount(), emiDto.getInterestRate(), emiDto.getTenor(), "",
							emiDto.getRepaymentFrequency(), emiDto.getInterestBasis(), emiDto.getAmortizationMethod()),
					"ROUND");

		} catch (Exception e) {
			throw new AmortExceptions(e.getMessage());
		}
		return ResponseEntity.ok(result);
	}

}
