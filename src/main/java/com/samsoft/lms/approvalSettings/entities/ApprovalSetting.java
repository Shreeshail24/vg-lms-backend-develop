package com.samsoft.lms.approvalSettings.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "approval_setting")
public class ApprovalSetting {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "nApprovalSettingId")
	private Integer approvalSettingId;
	
	@Column(name = "sFlowType", length = 200)
	private String flowType;
	
	@Column(name = "sUserId", length = 200)
	private String userId;
	
	@Column(name = "sRequestType", length = 200)
	private String requestType;
	
    @Column(name="dCreatedDate")
    @Temporal(TemporalType.DATE)
    private Date dtUserDateTime;
    
    @Column(name="CreatedBy" , length = 200)
	private String CreatedBy;

    @Column(name="dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();
    
    @Column(name="sLastUpdatedBy" , length = 200)
	private String LastUpdatedBy;
    
    @Column(name="sRequestTypeDesc")
	private String requestTypeDesc;
    
    @Column(name="sFlowTypeDesc")
   	private String flowTypeDesc;
    
    

}
