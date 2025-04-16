package com.samsoft.lms.newux.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditNoteReqDetailsResponseDto {
	
	private String mastAgrId;
	private String loanId;
	private String dtRequestDate;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String tranHead;
	private Double crNoteAmount;
	private String crNoteReason;
	private String crNoteRemark;
	private Integer installmentNo;
	private String customerId;
	private String customerName;
	private String allocatedUserId;
}
