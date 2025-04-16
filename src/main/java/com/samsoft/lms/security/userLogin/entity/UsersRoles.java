//package com.samsoft.lms.security.userLogin.entity;
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
////@NoArgsConstructor
////@AllArgsConstructor
//@Entity
//@Table(name = "sec_user_roles")
//@IdClass(UsersRolesCompositeKey.class)
//public class UsersRoles {
//	
//	@Id
//	@Column(name = "szuserid", nullable = false, unique = true, length = 200)
//	private String userId;
//
//	@Id
//	@Column(name = "szrolecode", nullable = false, unique = true)
//	private String roleCode;
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public String getRoleCode() {
//		return roleCode;
//	}
//
//	public void setRoleCode(String roleCode) {
//		this.roleCode = roleCode;
//	}
//}
