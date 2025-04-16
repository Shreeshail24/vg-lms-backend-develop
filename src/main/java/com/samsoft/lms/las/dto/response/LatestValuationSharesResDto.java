package com.samsoft.lms.las.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.samsoft.lms.aws.dto.DocumentViewResDto;
import com.samsoft.lms.las.entities.TabUploadLasTrail;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LatestValuationSharesResDto {
    private String mastAgrId;
    private List<TabUploadLasTrail> uploadLasTrails;
    private Double fmv;
}
