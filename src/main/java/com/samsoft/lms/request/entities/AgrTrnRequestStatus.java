package com.samsoft.lms.request.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_request_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnRequestStatus {
	//  not done
	@Id
	@Column(name = "nReqStatusID", length = 20)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reqStatusId;
	
	@Column(name = "dReqChangeDate")
	@Temporal(TemporalType.DATE)
	private Date dtReqChangeDate;
	
	@Column(name = "sReqStatus", length = 20)
	private String reqStatus;
	
	@Column(name = "sReason", length = 20)
	private String reason;
	
	@Column(name = "sRemark", length = 20)
	private String remark;
	
	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();
	
	/*@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="nReqID")
	private AgrTrnRequestHdr agrTrnRequestHdr;*/
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "nReqId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrTrnRequestHdr requestHdr;
}
