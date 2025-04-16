package com.samsoft.lms.las.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TabUploadLasTrailId implements Serializable {

    private Integer uploadLasId;
    private String mastAgrId;
    private String isin;
}
