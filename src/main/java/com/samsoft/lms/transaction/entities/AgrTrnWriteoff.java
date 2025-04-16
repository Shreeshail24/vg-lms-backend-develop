package com.samsoft.lms.transaction.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrTrnTranHeader;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_trn_writeoff")
public class AgrTrnWriteoff {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nWriteoffID", length = 20)
	private Integer writeoffId;

	@Column(name = "sMasterAgrID", length = 20)
	private String masterAgrId;

	@Column(name = "sLoanID", length = 20)
	private String loanId;

	@Column(name = "dTranDate")
	private Date dtTranDate;

	@Column(name = "sWriteoffPurpose", length = 20)
	private String writeoffPurpose;

	@Column(name = "sReasonCode", length = 20)
	private String reasonCode;

	@Column(name = "sRemark", length = 200)
	private String remark;

	@Column(name = "nWriteoffAmount", length = 20)
	private Double writeoffAmount;

	@Column(name = "sSystemorManual", length = 20)
	private String systemorManual;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "nTranId", nullable = false)
	@JsonIgnore
	private AgrTrnTranHeader hdr;
}
