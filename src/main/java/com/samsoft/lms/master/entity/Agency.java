package com.samsoft.lms.master.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.samsoft.lms.core.entities.CustApplLimitSetup;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_agencies")
public class Agency {

    @Id
    @Column(name = "iagencyid", nullable = false)
    private Integer agencyId;

    @Column(name = "szname")
    private String name;

    @Column(name = "szfullname")
    private String fullName;

    @Column(name = "szemailid")
    private String emailId;

    @Column(name = "bimobileno")
    private Integer mobileNo;

    @Column(name = "szagencytype")
    private String agencyType;

    @Column(name = "dtcreateddatetime", nullable = false ,updatable = false)
    @CreationTimestamp
    private Date createdDateTime;

    public Agency(Integer agencyId, String name) {
        super();
        this.agencyId = agencyId;
        this.name = name;
    }
}