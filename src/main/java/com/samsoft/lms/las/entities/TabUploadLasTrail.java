package com.samsoft.lms.las.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tab_upload_las_trail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TabUploadLasTrailId.class)
public class TabUploadLasTrail {

    @Id
    @Column(name = "iuploadlasid", nullable = false)
    private Integer uploadLasId;

    @Id
    @Column(name = "sMastAgrId", nullable = false   )
    private String mastAgrId;

    @Id
    @Column(name = "szisin", unique = true)
    private String isin;

    @Column(name = "sznameofshare")
    private String nameOfShare;

    @Column(name = "dpriceofshare")
    private Double priceOfShare;

    @Column(name = "iquantityofshare")
    private Integer quantityOfShare;

    @Column(name = "szerrordesc")
    private String errorDesc;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;

}
