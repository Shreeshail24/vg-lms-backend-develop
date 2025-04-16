package com.samsoft.lms.batch.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agr_trn_bkp_summary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgrTrnBkpSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sBackUpId", length = 20)
	private int backupId;

	@Column(name = "dBackup")
	@Temporal(TemporalType.DATE)
	private Date dtBackup;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "dLastUpdated")

	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")

	private LocalDate dtUserDate = LocalDate.now();
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bkpSummary", fetch = FetchType.LAZY)
	private List<AgrTrnBkpLoanDetails> bkpLoanDetails;

	// Supplier Finance Changes Start

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bkpSummary", fetch = FetchType.LAZY)
	private List<CustApplLimitBkpSetup> custApplLimitSetup;

	// Supplier Finance Changes End

}
