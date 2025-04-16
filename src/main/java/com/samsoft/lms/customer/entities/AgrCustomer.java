package com.samsoft.lms.customer.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.common.utils.EncryptionUtil;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agr_customers")
//@NamedNativeQuery(name="AgrCustomer.findByMasterAgrMastAgrIdOrderByDtUserDateDesc",
//query="select sCustomerId AS customerId,sMobile AS mobile,sCustCategory AS custCategory,"
//		+ "nCustInternalID AS custInternalId,sFirstName AS firstName,sMiddleName AS middleName,"
//		+ "sLastName AS lastName, dUserDate AS dtUserDate from agr_customers where sMastAgrId =:mastAgrId order by dUserDate desc",resultSetMapping="Mapping.AgrCustomerResponseDto")
//@SqlResultSetMapping(name="Mapping.AgrCustomerResponseDto",
//	classes= @ConstructorResult(targetClass = AgrCustomerResponseDto.class,
//			columns = {
//					@ColumnResult(name="customerId"),
//					@ColumnResult(name="mobile"),
//					@ColumnResult(name="custCategory"),
//					@ColumnResult(name="custInternalId"),
//					@ColumnResult(name="firstName"),
//					@ColumnResult(name="middleName"),
//					@ColumnResult(name="lastName"),
//					@ColumnResult(name="dtUserDate")
//			}) )

public class AgrCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nCustInternalID", length = 20)
	private Integer custInternalId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

//	public Integer getCustInternalId() {
//		return custInternalId;
//	}
//
//	public void setCustInternalId(Integer custInternalId) {
//		this.custInternalId = custInternalId;
//	}
//
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public String getFax() {
//		return fax;
//	}
//
//	public void setFax(String fax) {
//		this.fax = fax;
//	}
//
//	public String getHomeBranch() {
//		return homeBranch;
//	}
//
//	public void setHomeBranch(String homeBranch) {
//		this.homeBranch = homeBranch;
//	}
//
//	public String getGstin() {
//		return gstin;
//	}
//
//	public void setGstin(String gstin) {
//		this.gstin = gstin;
//	}
//
//	public String getRemark() {
//		return remark;
//	}
//
//	public void setRemark(String remark) {
//		this.remark = remark;
//	}
//
//	public String getPrefAddr() {
//		return prefAddr;
//	}
//
//	public void setPrefAddr(String prefAddr) {
//		this.prefAddr = prefAddr;
//	}
//
//	public String getPrefferedContactTimeFrom() {
//		return prefferedContactTimeFrom;
//	}
//
//	public void setPrefferedContactTimeFrom(String prefferedContactTimeFrom) {
//		this.prefferedContactTimeFrom = prefferedContactTimeFrom;
//	}
//
//	public String getPrefferedContactTimeTo() {
//		return prefferedContactTimeTo;
//	}
//
//	public void setPrefferedContactTimeTo(String prefferedContactTimeTo) {
//		this.prefferedContactTimeTo = prefferedContactTimeTo;
//	}
//
//	public String getLimitFreezYn() {
//		return limitFreezYn;
//	}
//
//	public void setLimitFreezYn(String limitFreezYn) {
//		this.limitFreezYn = limitFreezYn;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public LocalDate getDtLastUpdate() {
//		return dtLastUpdate;
//	}
//
//	public void setDtLastUpdate(LocalDate dtLastUpdate) {
//		this.dtLastUpdate = dtLastUpdate;
//	}
//
//	public LocalDate getDtUserDate() {
//		return dtUserDate;
//	}
//
//	public void setDtUserDate(LocalDate dtUserDate) {
//		this.dtUserDate = dtUserDate;
//	}
//
//	public List<AgrCustAddress> getCustAddress() {
//		return custAddress;
//	}
//
//	public void setCustAddress(List<AgrCustAddress> custAddress) {
//		this.custAddress = custAddress;
//	}

	@Column(name = "sTitle", length = 60)
	private String title;

	@Column(name = "sFirstName", length = 200)
	private String firstName;

	@Column(name = "sMiddleName", length = 200)
	private String middleName;

	@Column(name = "sLastName", length = 200)
	private String lastName;

	@Column(name = "sCustCategory", length = 20)
	private String custCategory;

//	public AgrMasterAgreement getMasterAgr() {
//		return masterAgr;
//	}
//
//	public void setMasterAgr(AgrMasterAgreement masterAgr) {
//		this.masterAgr = masterAgr;
//	}
//
//	public String getCustCategory() {
//		return custCategory;
//	}
//
//	public void setCustCategory(String custCategory) {
//		this.custCategory = custCategory;
//	}
//
//	public String getResidenceStatus() {
//		return residenceStatus;
//	}
//
//	public void setResidenceStatus(String residenceStatus) {
//		this.residenceStatus = residenceStatus;
//	}
//
//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//
//	public String getMaritalStatus() {
//		return maritalStatus;
//	}
//
//	public void setMaritalStatus(String maritalStatus) {
//		this.maritalStatus = maritalStatus;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	@Column(name = "sMobile", length = 200)
	private String mobile;

	@Column(name = "sAlternateMobile", length = 200)
	private String alternateMobile;

	@Column(name = "sEmailId")
	private String emailId;

	/*
	 * @Column(name = "sContactTimeFrom", length = 10) private String
	 * contactTimeFrom;
	 * 
	 * @Column(name = "sContactTimeTo", length = 10) private String sContactTimeTo;
	 */
	@Column(name = "sFax")
	private String fax;

	@Column(name = "sLandLine", length = 200)
	private String landLine;

	@Column(name = "sAlternateLandline", length = 200)
	private String alternateLandline;

	@Column(name = "sHomeBranch", length = 20)
	private String homeBranch;

	@Column(name = "sGSTIN", length = 200)
	private String gstin;

	@Column(name = "sAadharNo", length = 200)
	private String aadharNo;

	@Column(name = "sPAN", length = 200)
	private String pan;

	@Column(name = "sResidenceStatus", length = 20)
	private String residenceStatus;

	@Column(name = "sRemark", length = 20)
	private String remark;

	@Column(name = "sPrefAddr", length = 20)
	private String prefAddr;

	@Column(name = "sGender", length = 10)
	private String gender;

	@Column(name = "sCustomerType", length = 20)
	private String customerType;

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	@Column(name = "sMarritalStatus", length = 20)
	private String maritalStatus;

	@Column(name = "sStatus", length = 20, columnDefinition = "varchar(5) default 'L'")
	private String status="L";

	@Column(name = "sPrefferedContactTimeFrom", length = 20)
	private String prefferedContactTimeFrom;

	@Column(name = "sPrefferedContactTimeTo", length = 20)
	private String prefferedContactTimeTo;
	
	@Column(name="sLimitFreezYN", length = 2, columnDefinition = "varchar(2) default 'N'")
	private String limitFreezYn="N";

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	// @Temporal(TemporalType.DATE)
	private LocalDate dtLastUpdate = LocalDate.now();

	@Column(name = "dUserDate")
	// @Temporal(TemporalType.TIMESTAMP)
	private LocalDate dtUserDate = LocalDate.now();
	
	@Column(name = "sProfileURL",length=200)
	private String profileURL;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<AgrCustAddress> custAddress;

	/*
	 * @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "sMastAgrId") private AgrMasterAgreement mastAgr;
	 */
	
	
	public String getProfileUrl() {
        return profileURL;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileURL = profileUrl;
    }

//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getMiddleName() {
//		return middleName;
//	}
//
//	public void setMiddleName(String middleName) {
//		this.middleName = middleName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getAlternateMobile() {
//		return alternateMobile;
//	}
//
//	public void setAlternateMobile(String alternateMobile) {
//		this.alternateMobile = alternateMobile;
//	}
//
//	public String getEmailId() {
//		return emailId;
//	}
//
//	public void setEmailId(String emailId) {
//		this.emailId = emailId;
//	}
//
//	public String getLandLine() {
//		return landLine;
//	}
//
//	public void setLandLine(String landLine) {
//		this.landLine = landLine;
//	}
//
//	public String getAlternateLandline() {
//		return alternateLandline;
//	}
//
//	public void setAlternateLandline(String alternateLandline) {
//		this.alternateLandline = alternateLandline;
//	}
//
//	public String getAadharNo() {
//		return aadharNo;
//	}
//
//	public void setAadharNo(String aadharNo) {
//		this.aadharNo = aadharNo;
//	}
//
//	public String getPan() {
//		return pan;
//	}
//
//	public void setPan(String pan) {
//		this.pan = pan;
//	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;

	


//	public String getFirstName() {
////		return firstName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decodeBase64(this.firstName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setFirstName(String firstName) {
////		this.firstName = firstName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.firstName = encryptionUtil.encBase64(firstName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getMiddleName() {
////		return middleName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decodeBase64(this.middleName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setMiddleName(String middleName) {
////		this.middleName = middleName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.middleName = encryptionUtil.encBase64(middleName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getLastName() {
////		return lastName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decodeBase64(this.lastName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setLastName(String lastName) {
////		this.lastName = lastName;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.lastName = encryptionUtil.encBase64(lastName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getMobile() {
////		return mobile;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.mobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setMobile(String mobile) {
////		this.mobile = mobile;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.mobile = encryptionUtil.encrypt(mobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getEmailId() {
////		return emailId;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.emailId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setEmailId(String emailId) {
////		this.emailId = emailId;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.emailId = encryptionUtil.encrypt(emailId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getPan() {
////		return pan;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.pan);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setPan(String pan) {
////		this.pan = pan;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.pan = encryptionUtil.encrypt(pan);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAlternateMobile() {
////		return alternateMobile;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.alternateMobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAlternateMobile(String alternateMobile) {
////		this.alternateMobile = alternateMobile;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.alternateMobile = encryptionUtil.encrypt(alternateMobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAlternateLandline() {
////		return alternateLandline;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.alternateLandline);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAlternateLandline(String alternateLandline) {
////		this.alternateLandline = alternateLandline;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.alternateLandline = encryptionUtil.encrypt(alternateLandline);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getAadharNo() {
////		return aadharNo;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.aadharNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setAadharNo(String aadharNo) {
////		this.aadharNo = aadharNo;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.aadharNo = encryptionUtil.encrypt(aadharNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public String getLandLine() {
////		return landLine;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			return encryptionUtil.decrypt(this.landLine);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public void setLandLine(String landLine) {
////		this.landLine = landLine;
//		try {
//			EncryptionUtil encryptionUtil = new EncryptionUtil();
//			this.landLine = encryptionUtil.encrypt(landLine);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	
	
}
