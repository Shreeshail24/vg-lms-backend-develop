package com.samsoft.lms.approvalSettings.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApprovalSettingDto {
private Integer approvalSettingId;
private String flowType;
private String userId;
private String requestType;
private String dtUserDateTime;
private String CreatedBy;
private String dtLastUpdated;
private String LastUpdatedBy;
private String requestTypeDesc;
private String flowTypeDesc;
}
