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
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "sec_login_logs")
//public class LoginLogs {
//
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "iloginkey", unique = true, nullable = false)
//	private Integer loginKey;
//	
//	@Column(name = "szlogintype")
//	private String loginType;
//	
//	@Column(name = "szuserid", length = 200)
//	private String userid;
//	
//	@Column(name = "dtdatetime")
//	private Date dateTime;
//	
//	@Column(name = "csuccessyn")
//	private Character successYN;
//	
//	@Column(name = "szfailurereason")
//	private String failureReason;
//	
//	@Column(name = "szipaddress")
//	private String ipAddress;
//	
//	@Column(name = "szlat")
//	private String lat;
//	
//	@Column(name = "szlong")
//	private String longitude;
//	
//	@Column(name = "szcountry")
//	private String country;
//
//	public Integer getLoginKey() {
//		return loginKey;
//	}
//
//	public void setLoginKey(Integer loginKey) {
//		this.loginKey = loginKey;
//	}
//
//	public String getLoginType() {
//		return loginType;
//	}
//
//	public void setLoginType(String loginType) {
//		this.loginType = loginType;
//	}
//
//	public String getUserid() {
//		return userid;
//	}
//
//	public void setUserid(String userid) {
//		this.userid = userid;
//	}
//
//	public Date getDateTime() {
//		return dateTime;
//	}
//
//	public void setDateTime(Date dateTime) {
//		this.dateTime = dateTime;
//	}
//
//	public Character getSuccessYN() {
//		return successYN;
//	}
//
//	public void setSuccessYN(Character successYN) {
//		this.successYN = successYN;
//	}
//
//	public String getFailureReason() {
//		return failureReason;
//	}
//
//	public void setFailureReason(String failureReason) {
//		this.failureReason = failureReason;
//	}
//
//	public String getIpAddress() {
//		return ipAddress;
//	}
//
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//
//	public String getLat() {
//		return lat;
//	}
//
//	public void setLat(String lat) {
//		this.lat = lat;
//	}
//
//	public String getLongitude() {
//		return longitude;
//	}
//
//	public void setLongitude(String longitude) {
//		this.longitude = longitude;
//	}
//
//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}
//}
