package com.samsoft.lms.customer.dto;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSearchDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerId;
	private String title;
	private String custCategory;
	private String firstName;
	private String lastName;
	private String middleName;
	private String mobile;
	private String customerType;
	private Integer custInternalId;
	private String alternateMobile;
	private String emailId;
	private String fax;
	private String landLine;
	private String alternateLandline;
	private String homeBranch;
	private String gstin;
	private String aadharNo;
	private String pan;
	private String residenceStatus;
	private String prefAddr;
	private String gender;
	private String maritalStatus;
	private String status;
	private String prefferedContactTimeFrom;
	private String prefferedContactTimeTo;
	private String mastAgrId;
	private String customerName;
	
	private Double loanAmount=0.0;
	private Double emiAmount=0.0;
	private String dtNextInstallment;
	private String dtDisbursmentDate;
	
//	public String getCustomerId() {
//		return customerId;
//	}
//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}
//	public String getTitle() {
//		return title;
//	}
//	public void setTitle(String title) {
//		this.title = title;
//	}
//	public String getCustCategory() {
//		return custCategory;
//	}
//	public void setCustCategory(String custCategory) {
//		this.custCategory = custCategory;
//	}
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	public String getMiddleName() {
//		return middleName;
//	}
//	public void setMiddleName(String middleName) {
//		this.middleName = middleName;
//	}
//	public String getMobile() {
//		return mobile;
//	}
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//	public String getCustomerType() {
//		return customerType;
//	}
//	public void setCustomerType(String customerType) {
//		this.customerType = customerType;
//	}
//	public Integer getCustInternalId() {
//		return custInternalId;
//	}
//	public void setCustInternalId(Integer custInternalId) {
//		this.custInternalId = custInternalId;
//	}
//	public String getAlternateMobile() {
//		return alternateMobile;
//	}
//	public void setAlternateMobile(String alternateMobile) {
//		this.alternateMobile = alternateMobile;
//	}
//	public String getEmailId() {
//		return emailId;
//	}
//	public void setEmailId(String emailId) {
//		this.emailId = emailId;
//	}
//	public String getFax() {
//		return fax;
//	}
//	public void setFax(String fax) {
//		this.fax = fax;
//	}
//	public String getLandLine() {
//		return landLine;
//	}
//	public void setLandLine(String landLine) {
//		this.landLine = landLine;
//	}
//	public String getAlternateLandline() {
//		return alternateLandline;
//	}
//	public void setAlternateLandline(String alternateLandline) {
//		this.alternateLandline = alternateLandline;
//	}
//	public String getHomeBranch() {
//		return homeBranch;
//	}
//	public void setHomeBranch(String homeBranch) {
//		this.homeBranch = homeBranch;
//	}
//	public String getGstin() {
//		return gstin;
//	}
//	public void setGstin(String gstin) {
//		this.gstin = gstin;
//	}
//	public String getAadharNo() {
//		return aadharNo;
//	}
//	public void setAadharNo(String aadharNo) {
//		this.aadharNo = aadharNo;
//	}
//	public String getPan() {
//		return pan;
//	}
//	public void setPan(String pan) {
//		this.pan = pan;
//	}
//	public String getResidenceStatus() {
//		return residenceStatus;
//	}
//	public void setResidenceStatus(String residenceStatus) {
//		this.residenceStatus = residenceStatus;
//	}
//	public String getPrefAddr() {
//		return prefAddr;
//	}
//	public void setPrefAddr(String prefAddr) {
//		this.prefAddr = prefAddr;
//	}
//	public String getGender() {
//		return gender;
//	}
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//	public String getMaritalStatus() {
//		return maritalStatus;
//	}
//	public void setMaritalStatus(String maritalStatus) {
//		this.maritalStatus = maritalStatus;
//	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
//	public String getPrefferedContactTimeFrom() {
//		return prefferedContactTimeFrom;
//	}
//	public void setPrefferedContactTimeFrom(String prefferedContactTimeFrom) {
//		this.prefferedContactTimeFrom = prefferedContactTimeFrom;
//	}
//	public String getPrefferedContactTimeTo() {
//		return prefferedContactTimeTo;
//	}
//	public void setPrefferedContactTimeTo(String prefferedContactTimeTo) {
//		this.prefferedContactTimeTo = prefferedContactTimeTo;
//	}
//	public String getMastAgrId() {
//		return mastAgrId;
//	}
//	public void setMastAgrId(String mastAgrId) {
//		this.mastAgrId = mastAgrId;
//	}
//	public String getCustomerName() {
//		return customerName;
//	}
//	public void setCustomerName(String customerName) {
//		this.customerName = customerName;
//	}

}
