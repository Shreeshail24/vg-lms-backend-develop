package com.samsoft.lms.request.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Data
@NoArgsConstructor
@ToString
public class RestructureReqDtoMain {

	private String mastAgrId;
	private String dtRequest;
	private String flowType;
	private String requestStatus;
	private String reason;
	private String remark;
	private String userId;
	private String loanId;
	private double netReceivable;
	private double dreAmount;
	private double newFinanceAmount;
	private String newAssetClass;
	private int newCycleDay;
	private String newRepayFrequency;
	private float newInterestRate;
	private float newIndexRate;
	private float newSpreadRate;
	private float newOffsetRate;
	private String tranType;
	private String changeFactor;
	private int newTenor;
	private double newEmi;
	private String newInstallmentStartDate;
	private String restructureReason;
	private String restructureRemark;
	private List<RestructureDtlList> restructureList;
	private List<RestructureVariationList> variationList;
	private List<EstScheduleList> estScheduleList;
}
