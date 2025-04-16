package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChargesWaiverGridDto {
	
	private Integer tranId;
    private String tranDate;
    private String dueHead;
    private Double tranAmount;
    private Double taxAmount;
    private Double waivableAmount;
    private Integer dueDtlId;
    private Integer tranDtlId;
    private String mastAgrId;
    private String tranType;


}
