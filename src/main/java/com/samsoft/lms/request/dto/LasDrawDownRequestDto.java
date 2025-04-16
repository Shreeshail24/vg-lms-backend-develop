package com.samsoft.lms.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LasDrawDownRequestDto {
    private String masterAggrId;
    private String requestDate;
    private Double requestedAmount;
//    private String requestStatus;

//    private Double limitSanAmount;
//    private Double utilizedLimit;
//    private Double availableLimit;
//    private Double totalDues;
//    private Double totalOverDues;
//
//    private Double approvedAmount;
//    private String remarksRequest;
//    private String userIdRequest;
//    private String endUse;
//
//    private String userId;

}
