package com.samsoft.lms.las.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audit_agr_collateral_shares")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditAgrCollateralShares {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iauditid", nullable = false, unique = true)
    private Integer auditId;

    @Column(name = "szmastagrid", nullable = false, unique = true)
    private String mastAgrId;

    @Column(name = "dfmv")
    private Double fmv;

    @Column(name = "ddrawingpower")
    private Double drawingPower;

    @Column(name = "dactualltv")
    private Double actualLtv;

    @Column(name = "dttraildate")
    private Date trailDate;

    @Column(name = "iuploadlasid")
    private Integer uploadLasId;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;


}
