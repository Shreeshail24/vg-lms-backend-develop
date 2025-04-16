package com.samsoft.lms.request.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_t_drawdown_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawdownRequest {

	@Id
	@Column(name = "irequestid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestId;

	@Column(name = "sMastAgrId", length = 12)
	private String mastAgrId;

	@Column(name = "nLimitSanctionAmount")
	private Double limitSanctionAmount;

	@Column(name = "nUtilizedLimit")
	private Double utilizedLimit;

	@Column(name = "nAvailableLimit")
	private Double availableLimit;

	@Column(name = "nTotalDues")
	private Double totalDues;

	@Column(name = "nTotalOverDues")
	private Double totalOverDues;

	@Column(name = "nRequestedAmount")
	private Double requestedAmount;

	@Column(name = "nApprovedAmount")
	private Double approvedAmount;

	@Column(name = "irejectreasoncode")
	private Integer rejectReasonCode;

	@Column(name = "szremarksrequest", length = 2000)
	private String remarksRequest;

	@Column(name = "szremarksapproval", length = 2000)
	private String remarksApproval;
	
	@Column(name = "szenduse", length = 2000)
	private String endUse;

	@Column(name = "dtrequesteddatetime")
	private Date requestedDateTime;

	@Column(name = "dtdecisiondatetime")
	private Date decisionDateTime;
	
	@Column(name = "dPrinStart")
	private Date dtPrinStart;
	
	@Column(name = "sDisbAccNo", length = 20)
	private String disbAccNo;
	
	@Column(name = "sIfsc", length = 20)
	private String ifsc;
	
	@Column(name = "sRepayFreq", length = 20)
	private String repayFreq;

	@Column(name = "szuseridrequest", length = 100)
	private String useridRequest;

	@Column(name = "szuseriddecision", length = 100)
	private String userIdDecision;

	@Column(name = "cstatus")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false, insertable = false, updatable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgr;

	public DrawdownRequest(String mastAgrId, Double limitSanctionAmount, Double utilizedLimit, Double availableLimit,
			Double totalDues, Double totalOverDues, Double requestedAmount, String remarksRequest,
			Date requestedDateTime, String useridRequest, Character status, String endUse) {
		super();
		this.mastAgrId = mastAgrId;
		this.limitSanctionAmount = limitSanctionAmount;
		this.utilizedLimit = utilizedLimit;
		this.availableLimit = availableLimit;
		this.totalDues = totalDues;
		this.totalOverDues = totalOverDues;
		this.requestedAmount = requestedAmount;
		this.remarksRequest = remarksRequest;
		this.requestedDateTime = requestedDateTime;
		this.useridRequest = useridRequest;
		this.status = status;
		this.endUse = endUse;
	}


	public DrawdownRequest(Integer requestId, String mastAgrId, Double limitSanctionAmount, Double utilizedLimit, Double availableLimit,
						   Double totalDues, Double totalOverDues, Double requestedAmount, String remarksRequest,
						   Date requestedDateTime, String useridRequest, Character status, String endUse) {
		super();
		this.requestId = requestId;
		this.mastAgrId = mastAgrId;
		this.limitSanctionAmount = limitSanctionAmount;
		this.utilizedLimit = utilizedLimit;
		this.availableLimit = availableLimit;
		this.totalDues = totalDues;
		this.totalOverDues = totalOverDues;
		this.requestedAmount = requestedAmount;
		this.remarksRequest = remarksRequest;
		this.requestedDateTime = requestedDateTime;
		this.useridRequest = useridRequest;
		this.status = status;
	}

}
