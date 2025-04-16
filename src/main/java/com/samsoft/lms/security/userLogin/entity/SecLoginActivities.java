//package com.samsoft.lms.security.userLogin.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
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
//@Table(name = "sec_login_activities")
//@IdClass(SecLoginActivitiesCompositeKey.class)
//public class SecLoginActivities {
//	
//	@Id
//	@Column(name = "iloginkey", nullable = false, unique = true)
//	private Integer loginKey;
//	
//	@Id
//	@Column(name = "szactivitycode", nullable = false, unique = true)
//	private String activityCode;
//	
//	@Id
//	@Column(name = "szactioncode", nullable = false, unique = true)
//	private String actionCode;
//	
//	@Id
//	@Column(name = "dtactdatetime", nullable = false, unique = true)
//	private Date actDateTime;
//	
//	@Column(name = "jsondata", columnDefinition = "JSON")
//	private String jsonData;
//	
//	@Column(name = "ileadno")
//	private Integer leadNo;
//	
//	@Column(name = "icreditapplicantid")
//	private Integer creditApplicantId;
//}
