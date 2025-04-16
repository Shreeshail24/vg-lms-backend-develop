package com.samsoft.lms.batch.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eod_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EodStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Integer eodSrNo;
	@Column(length = 20)
	private String customerId;
	@Column(length = 200)
	private String processName;
	@Column(length = 20)
	private String processStatus;
	
	private Date businessDate;
	private LocalDateTime dtUpdatedTime = LocalDateTime.now();
}
