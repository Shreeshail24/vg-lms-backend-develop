package com.samsoft.lms.batch.dto;

import java.time.LocalDateTime;

import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EodStatusDto {

	private Integer eodSrNo;
	private String customerId;
	private String processName;
	private String processStatus;
	private String businessDate;
	private LocalDateTime executedTime;
}
