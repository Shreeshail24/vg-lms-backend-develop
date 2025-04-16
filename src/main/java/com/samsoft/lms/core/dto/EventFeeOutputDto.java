package com.samsoft.lms.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.samsoft.lms.transaction.dto.GstListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFeeOutputDto {
	private String feeCode;
	private Double feeAmount;
	List<GstListDto> gstList = new ArrayList<GstListDto>();
}
