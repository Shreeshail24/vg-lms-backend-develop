package com.samsoft.lms.transaction.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.samsoft.lms.core.entities.AgrTrnTranHeader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gl_error_log")
@Data
@NoArgsConstructor
public class GlErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nSrNo", length = 20)
	private Integer srNo;

	@Column(name = "sErrorMessage", length = 4000)
	private String errorMessage;

	@Column(name = "sUserID", length = 200)
	private String userID;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	public GlErrorLog(String errorMsg) {
		this.errorMessage = errorMsg;
	}

}
