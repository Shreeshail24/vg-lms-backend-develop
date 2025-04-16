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
@Table(name = "batch_error_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20, name = "nErrorLogId")
	private Integer erroLogId;
	
	@Column(length = 100, name = "sFileName")
	private String fileName;
	
	@Column(name = "sErrorMessage")
	private String errorMessage;
	
	@Column(name = "sOriginationNo")
	private String originationNo;
	
	@Column(name = "dBusinessDate")
	private Date businessDate;
	
	@Column(name = "dLastUpdated")	
	private LocalDateTime dtLastUpdated = LocalDateTime.now();

	@Column(name = "dUserDate")	
	private LocalDateTime dtUserDate = LocalDateTime.now();

}
