package com.samsoft.lms.encryption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samsoft.lms.encryption.service.EncryptionService;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "Encryption RESTful Services", value = "EncryptionController")
@RequestMapping(value = "/encryption")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EncryptionController {
	
	@Autowired
	private EncryptionService encryptionService;
	
	@PostMapping(value="/mobileNo")
	public Boolean encryptMobileNo() {
		try {
			return encryptionService.encryptMobileNo();
		}catch(Exception e) {
			System.out.println("mobile===>"+e);
		}
		return null;
	}
	
	@PostMapping(value="/gst")
	public Boolean encryptGst() {
		try {
			return encryptionService.encryptGst();
		}catch(Exception e) {
			System.out.println("gst===>"+e);
		}
		return null;
	}
	
	@PostMapping(value="/name")
	public Boolean encryptName() {
		try {
			return encryptionService.encryptName();
		}catch(Exception e) {
			System.out.println("name===>"+e);
		}
		return null;
	}
	
	@PostMapping(value="/aadhaar")
	public Boolean encryptAadhaar() {
		try {
			return encryptionService.encryptAadhaar();
		}catch(Exception e) {
			System.out.println("Aaddhar===>"+e);
		}
		return null;
	}
	
	@PostMapping(value="/address")
	public Boolean encryptAddress() {
		try {
			return encryptionService.encryptAddress();
		}catch(Exception e) {
			System.out.println("Address===>"+e);
		}
		return null;
	}
	
	@PostMapping(value="/email")
	public Boolean encryptEmail() {
		try {
			return encryptionService.encryptEmail();
		}catch(Exception e) {
			System.out.println("email===>"+e);
		}
		return null;
	}
	
}
