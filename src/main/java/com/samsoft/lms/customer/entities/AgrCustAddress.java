package com.samsoft.lms.customer.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.samsoft.lms.common.utils.EncryptionUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agr_cust_addr")
public class AgrCustAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nAddrSeqNo", length = 20)
	private Integer addrSeqNo;
	
	@Column(name = "sCustomerID", length = 20)
	private String customerId;

	@Column(name = "sAddrType", length = 20)
	private String addrType;

	@Column(name = "sAddressLine1")
	private String addressLine1;

	@Column(name = "sAddressLine2")
	private String addressLine2;
	
	@Column(name = "sAddressLine3")
	private String addressLine3;

	@Column(name = "sLandMark")
	private String landMark;

	@Column(name = "sCity", length = 20)
	private String city;

	@Column(name = "sState", length = 20)
	private String state;

	@Column(name = "sPinCode", length = 6)
	private String pinCode;

	@Column(name = "sMobile", length = 20)
	private String mobile;
	
	@Column(name = "sPrefferedAddress")
	private String prefferedAddress;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="nCustInternalID")
	private AgrCustomer customer;

	

//	public String getAddressLine1() {
////		return addressLine1;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.addressLine1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAddressLine1(String addressLine1) {
////		this.addressLine1 = addressLine1;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.addressLine1 = encryptionUtil.encrypt(addressLine1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAddressLine2() {
////		return addressLine2;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.addressLine2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAddressLine2(String addressLine2) {
////		this.addressLine2 = addressLine2;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.addressLine2 = encryptionUtil.encrypt(addressLine2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAddressLine3() {
////		return addressLine3;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.addressLine3);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAddressLine3(String addressLine3) {
////		this.addressLine3 = addressLine3;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.addressLine3 = encryptionUtil.encrypt(addressLine3);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
