package com.samsoft.lms.las.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tab_upload_excel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabUploadExcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iuploadlasid", nullable = false, unique = true)
    private Integer uploadLasId;

    @Column(name = "szfilename")
    private String fileName;

    @Column(name = "szfileurl")
    private String fileUrl;

    @Column(name = "szuserid", length = 200)
    private String userId;

    @Column(name = "dtcreateddatetime")
    private Date createdDateTime;

    @Column(name = "dtupdateddatetime")
    private Date updatedDateTime;

}
