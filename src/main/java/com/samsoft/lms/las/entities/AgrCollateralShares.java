package com.samsoft.lms.las.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.samsoft.lms.core.entities.AgrMasterAgreement;

import java.util.Date;

@Entity
@Table(name = "agr_collateral_shares")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrCollateralShares {

    @Id
    @Column(name = "szmastagrid", nullable = false, unique = true)
    private String mastAgrId;

    @Column(name = "dttraildate")
    private Date trailDate;

    @Column(name = "dfmv")
    private Double fmv;

    @Column(name = "ddrawingpower")
    private Double drawingPower;

    @Column(name = "dactualltv")
    private Double actualLtv;

    @Column(name = "iuploadlasid")
    private Integer uploadLasId;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;

}
