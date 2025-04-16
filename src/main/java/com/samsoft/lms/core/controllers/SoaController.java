package com.samsoft.lms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.Soa;
import com.samsoft.lms.core.services.SoaService;

@RestController
@RequestMapping("/soa")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class SoaController {

	@Autowired
	private SoaService soa;

	@GetMapping("/generate")
	public ResponseEntity<Soa> generateSoa(@RequestParam String mastAgrId) throws Exception {
		Soa result = new Soa();
		try {
			result = soa.generateSoa(mastAgrId);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
}
