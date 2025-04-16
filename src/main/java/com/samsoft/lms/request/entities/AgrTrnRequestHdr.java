package com.samsoft.lms.request.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeAndStatusResponseDto;
import com.samsoft.lms.newux.dto.response.SrWorklistByActivityCodeResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_trn_request_hdr")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NamedNativeQuery(name="AgrTrnRequestHdr.getAllSRByActivityCode",
query="select a.nReqId as reqId, a.sActivityCode as activityCode, a.dLastUpdated as dtLastUpdated, a.dRequest as dtRequest, a.dUserDate as dtUserDate, a.sFlowType as flowType, a.sReason as reason, a.sRemark as remark, a.sReqStatus as reqStatus, a.sUserID as userId, a.sMastAgrId as mastAgrId, concat(c.sFirstName, ' ', ifnull(c.sMiddleName, ''), ' ', ifnull(c.sLastName, '')) as customerName, c.sMobile as mobile, c.sCustomerId as customerId from agr_trn_request_hdr a, agr_master_agreement ma left join agr_customers c on ma.sCustomerId = c.sCustomerId where a.sMastAgrId=ma.sMastAgrId and sActivityCode = :activityCode group by a.nReqId order by a.nReqId desc",
resultSetMapping="Mapping.SrWorklistByActivityCodeResponseDto")
@SqlResultSetMapping(name="Mapping.SrWorklistByActivityCodeResponseDto",
	classes= @ConstructorResult(targetClass = SrWorklistByActivityCodeResponseDto.class,
			columns = {
					@ColumnResult(name="reqId"),
					@ColumnResult(name="activityCode"),
					@ColumnResult(name="dtLastUpdated"),
					@ColumnResult(name="dtRequest"),
					@ColumnResult(name="dtUserDate"),
					@ColumnResult(name="flowType"),
					@ColumnResult(name="reason"),
					@ColumnResult(name="remark"),
					@ColumnResult(name="reqStatus"),
					@ColumnResult(name="userId"),
					@ColumnResult(name="mastAgrId"),
					@ColumnResult(name="customerName"),
					@ColumnResult(name="mobile"),
					@ColumnResult(name="customerId")
			}) )

@NamedNativeQuery(name="AgrTrnRequestHdr.getAllSRByActivityCodeAndStatus",
query="select a.nReqId as reqId, a.sActivityCode as activityCode, a.dLastUpdated as dtLastUpdated, a.dRequest as dtRequest, a.dUserDate as dtUserDate, a.sFlowType as flowType, a.sReason as reason, a.sRemark as remark, a.sReqStatus as reqStatus, a.sUserID as userId, a.sMastAgrId as mastAgrId, concat(c.sFirstName, ' ', ifnull(c.sMiddleName, ''), ' ', ifnull(c.sLastName, '')) as customerName, c.sMobile as mobile, c.sCustomerId as customerId from agr_trn_request_hdr a, agr_master_agreement ma left join agr_customers c on ma.sCustomerId = c.sCustomerId where a.sMastAgrId=ma.sMastAgrId and sActivityCode = :activityCode and (sReqStatus = :status or sReqStatus = :otherStatus) group by a.nReqId order by a.nReqId desc",
resultSetMapping="Mapping.SrWorklistByActivityCodeAndStatusResponseDto")
@SqlResultSetMapping(name="Mapping.SrWorklistByActivityCodeAndStatusResponseDto",
	classes= @ConstructorResult(targetClass = SrWorklistByActivityCodeAndStatusResponseDto.class,
			columns = {
					@ColumnResult(name="reqId"),
					@ColumnResult(name="activityCode"),
					@ColumnResult(name="dtLastUpdated"),
					@ColumnResult(name="dtRequest"),
					@ColumnResult(name="dtUserDate"),
					@ColumnResult(name="flowType"),
					@ColumnResult(name="reason"),
					@ColumnResult(name="remark"),
					@ColumnResult(name="reqStatus"),
					@ColumnResult(name="userId"),
					@ColumnResult(name="mastAgrId"),
					@ColumnResult(name="customerName"),
					@ColumnResult(name="mobile"),
					@ColumnResult(name="customerId")
			}) )

public class AgrTrnRequestHdr implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8228337324192887454L;

	@Id
	@Column(name = "nReqId", length = 20)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reqId;
	
	@NotNull
	@Column(name = "dRequest", length = 20)
	@Temporal(TemporalType.DATE)
	private Date dtRequest;
	
	@Column(name = "sActivityCode", length = 20)
	private String activityCode;
	
	@Column(name = "sReqStatus", length = 20)
	private String reqStatus;
	
	@Column(name = "sFlowType", length = 20)
	private String flowType;
	
	@Column(name = "sReason", length = 20)
	private String reason;
	
	@Column(name = "sRemark", length = 20)
	private String remark;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "szDocUrl", length = 255)
	private String docUrl;

	@Column(name = "dLastUpdated")	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")	
	private LocalDate dtUserDate = LocalDate.now();
	
/*	@OneToMany(cascade = CascadeType.ALL, mappedBy="requestHdr", fetch = FetchType.LAZY)
	private List<AgrTrnChargesBookingDtl> chargesBooking;*/
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="requestHdr", fetch = FetchType.LAZY)
	private List<AgrTrnRequestStatus> requestStatus;
	
	@OneToMany(cascade= CascadeType.ALL, mappedBy = "requestHdr", fetch = FetchType.LAZY)
	private List<AgrTrnReqInstrument> instruments;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrMasterAgreement masterAgreement;
}
