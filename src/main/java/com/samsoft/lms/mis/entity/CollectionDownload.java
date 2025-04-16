package com.samsoft.lms.mis.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="v_lms_rep_Collection_download")
public class CollectionDownload {
	@Id
	@Column(name="sMastAgrId")
	private String mastAgrId;

	@Column(name = "sCustomerId")
	private String customerId;
	
	@Column(name="sCustomerName")
	private String customerName;
	
	@Column(name="sMobile")
	private String mobileNo;
	
	@Column(name="sEmailId")
	private String emailId;
	
	@Column(name="nLoanAmount")
	private Double loanAmount;
	
	@Column(name="sAddressLine1")
	private String addressLine1;
	
	@Column(name="sAddressLine2")
	private String addressLine2;
	
	@Column(name="sCity")
	private String city;
	
	@Column(name="sState")
	private String state;
	
	@Column(name="sCountry")
	private String country;
	
	@Column(name="sPinCode")
	private String pincode;
	
	@Column(name="NBFC")
	private String nbfc;
	
	@Column(name="sAlternateMobile")
	private String alternateMobileNo;
	
	@Column(name="sLoanId")
	private String loanId;

	@Column(name = "dtInstallmentDate")
	private Date installmentDate;
	
	@Column(name="nInstallmentNo")
	private Integer installmentNo;
	
	@Column(name="nInstallmentAmount")
	private Integer installmentAmount;
	
	@Column(name="dPrevInstallment")
	private Date prevInstallment;
	
	@Column(name="nInterestRate")
	private Double intersetRate;
	
	@Column(name="nTenor")
	private Integer tenor;
	
	@Column(name="sBankCode")
	private String bankCode;
	
	@Column(name="sAccountNo")
	private String accountNo;

	@Column(name = "dDisbDate")
	private Date disbDate;

	@Column(name = "sOriginationApplnNo")
	private String originationApplnNo;
}
