package com.samsoft.lms.newux.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.samsoft.lms.request.entities.AgrTrnReqChargesWaiverDtl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargesWaverReqDetailsResponseDto {

	private String mastAgrId;
	private String requestDate;
	private String flowType;
	private String requestStatus;
	private String userId;
	List<ChargesWaiverParamListResponseDto> waiverParamList;
	private String customerId;
	private String customerName;
	private String reason;
	private String remark;
	private String allocatedUserId;
	
}
