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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="eod_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EodLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private Integer erroLogId;
	@Column(length = 20)
	private String customer;
	@Column(length = 20)
	private String loanId;
	@Column(length = 20)
	private String component;	
	private String errorMessage;	
	private LocalDateTime timeStamp = LocalDateTime.now();
	private Date businessDate;

}
