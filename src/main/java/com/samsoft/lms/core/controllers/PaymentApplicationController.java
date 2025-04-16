package com.samsoft.lms.core.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.core.services.PaymentApplicationServices;
import com.samsoft.lms.core.services.PaymentAutoApportionmentService;
import com.samsoft.lms.core.services.PaymentReversalService;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = {"https://lms.4fin.in/","http://localhost:3000", "https://qa-lms.4fin.in", "https://qa-losone.4fin.in"} ,allowedHeaders = "*")
public class PaymentApplicationController {

	@Autowired
	private PaymentApplicationServices paymentService;
	
	@Autowired
	private PaymentReversalService paymentRevService;

	@Autowired
	private Environment env;
	
	@Autowired
	private PaymentAutoApportionmentService autoApportionment;

	@PostMapping("/autoPaymentApportionment")
	public ResponseEntity<String> autoPaymentApportionment(String mastAgrId, Integer instrumentId, String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtTran = sdf.parse(tranDate);
			result = autoApportionment.agreementAutoApportionmentPaymentApplication(mastAgrId, instrumentId, dtTran);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/manualLoanApportionment")
	public ResponseEntity<String> manualLoanApportionmentPaymentApplication(String mastAgrId, Integer instrumentId, String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtTran = sdf.parse(tranDate);
			result = paymentService.manualLoanApportionmentPaymentApplication(mastAgrId, instrumentId, dtTran);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/manualChargesApportionmentPaymentApplication")
	public ResponseEntity<String> manualChargesApportionmentPaymentApplication(String mastAgrId, Integer instrumentId, String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtTran = sdf.parse(tranDate);
			result = paymentService.manualChargesApportionmentPaymentApplication(mastAgrId, instrumentId, dtTran);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/paymentRelease")
	public ResponseEntity<String> paymentRelease(String customerId, String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtTran = sdf.parse(tranDate);
			result = paymentService.paymentRelease(customerId, dtTran);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/paymentReversal")
	public ResponseEntity<String> paymentReversal(String mastAgrId, Integer instrumentId, String tranDate) throws Exception {
		String result = "success";
		try {
			String dateFormat = env.getProperty("lms.global.date.format");
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dtTran = sdf.parse(tranDate);
			result = paymentRevService.paymentReversal(mastAgrId, instrumentId, dtTran);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			throw e;
		}
	}
}
