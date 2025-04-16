//package com.samsoft.lms.security.userLogin.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "sec_external_login_attempts")
//public class ExternalLoginAttempts {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "iextloginkey", unique = true, nullable = false)
//	private Integer extLoginKey;
//	
//	@Column(name = "szemail", nullable = false)
//	private String email;
//	
//	@Column(name = "dtdatetime")
//	private Date dateTime;
//	
//	@Column(name="szipaddress")
//	private String ipAddress;
//	
//	@Column(name="szlatitute")
//	private String lat;
//	
//	@Column(name="szlongitude")
//	private String longitude;
//	
//	@Column(name="szcountry")
//	private String country;
//}
