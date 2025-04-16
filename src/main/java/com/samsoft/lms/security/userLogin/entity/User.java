//package com.samsoft.lms.security.userLogin.entity;
//
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "sec_users")
//@Data
////@NoArgsConstructor
////@AllArgsConstructor
//public class User {
//	
//	public User(String userId, String password, Character status, String googleKey) {
//		super();
//		this.userId = userId;
//		this.password = password;
//		this.status = status;
//		this.googleKey = googleKey;
//	}
//	
//	  public User() {
//	    }
//
//	@Id
//	@Column(name = "szuserid", length = 200)
//	private String userId;
//	
//	@Column(name = "szpwd")
//	private String password;
//	
//	@Column(name = "cstatus")
//	private Character status;
//	
//	@Column(name = "szgooglekey")
//	private String googleKey;
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public Character getStatus() {
//		return status;
//	}
//
//	public void setStatus(Character status) {
//		this.status = status;
//	}
//
//	public String getGoogleKey() {
//		return googleKey;
//	}
//
//	public void setGoogleKey(String googleKey) {
//		this.googleKey = googleKey;
//	}
//
//	public User(String userId, String googleKey) {
//		super();
//		this.userId = userId;
//		this.googleKey = googleKey;
//	}
//}
