package com.samsoft.lms.odMgmt.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.dto.CustApplicationProdLimitGetDto;
import com.samsoft.lms.core.dto.CustApplicationProdLimitSetDto;
import com.samsoft.lms.odMgmt.services.SupplierFinanceService;

@RestController
@RequestMapping("/supplierFinance")
@CrossOrigin(origins = { "https://lms.4fin.in/", "http://localhost:3000", "https://qa-lms.4fin.in",
		"https://qa-losone.4fin.in" }, allowedHeaders = "*")
public class SupplierFinanceController {

	@Autowired
	private SupplierFinanceService supplierService;

	@GetMapping("/getCustApplicationProdLimit")
	public ResponseEntity<CustApplicationProdLimitGetDto> getCustApplicationProdLimit(
			@RequestParam Integer custApplLimitId) throws Exception {
		try {
			CustApplicationProdLimitGetDto custLimit = supplierService.getCustApplicationProdLimit(custApplLimitId);
			return ResponseEntity.ok(custLimit);
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/setCustApplicationProdLimit")
	public ResponseEntity<Integer> setCustApplicationProdLimit(@RequestBody CustApplicationProdLimitSetDto limitDto)
			throws Exception {

		Integer result = null;
		try {
			result = supplierService.setCustApplicationProdLimit(limitDto);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/validateCustApplicationProdLimit")
	public ResponseEntity<String> validateCustApplicationProdLimit(@RequestParam Integer custApplLimitId,
			@RequestParam Double amount) throws Exception {
		String result = "success";
		try {
			result = supplierService.validateCustApplicationProdLimit(custApplLimitId, amount);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/getcustapplicationprodlimitbycustomerid")
	public ResponseEntity<List<CustApplicationProdLimitGetDto>> getCustApplicationProdLimitByCustomerId(@RequestParam String customerId, @RequestParam String portfolioCode, @RequestParam String mastAgrId) throws Exception {
		try {
			List<CustApplicationProdLimitGetDto> custLimitList = supplierService.getCustApplicationProdLimitByCustomerId(customerId,portfolioCode,mastAgrId);
			return ResponseEntity.ok(custLimitList);
		} catch (Exception e) {
			throw e;
		}
	}

}
