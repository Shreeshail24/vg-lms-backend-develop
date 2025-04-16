package com.samsoft.lms.batch.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.samsoft.lms.batch.dto.DueDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arc_agr_trn_bkp_due_dtls")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArcAgrTrnBkpDueDetails {

	@Id
	@Column(name = "nDueBackupId", length = 20)
	private Integer dueBackupId;

	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;

	@Column(name = "sLoanId", length = 20)
	private String loanId;

	@Column(name = "dDueDate")
	@Temporal(TemporalType.DATE)
	private Date dtDueDate;

	@Column(name = "sDueCategory", length = 50)
	private String dueCategory;

	@Column(name = "sDueHead", length = 20)
	private String dueHead;

	@Column(name = "nDueAmount", length = 20)
	private Double dueAmount;

	@Column(name = "nInstallmentNo", length = 3)
	private Integer installmentNo;

	@Column(name = "nDPD", length = 3)
	private Integer dpd;

	@Column(name = "dLastUpdated")

	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")

	private LocalDate dtUserDate = LocalDate.now();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bkpDueDetail", fetch = FetchType.LAZY)
	private List<ArcAgrTrnBkpTaxDueDetails> bkpTaxDueDetails;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "sLoanBackUpId")
	private ArcAgrTrnBkpLoanDetails bkpLoanDetails;

}
