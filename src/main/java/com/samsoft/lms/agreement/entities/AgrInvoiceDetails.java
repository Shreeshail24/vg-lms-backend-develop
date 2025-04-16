package com.samsoft.lms.agreement.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samsoft.lms.core.entities.AgrMasterAgreement;
import com.samsoft.lms.core.entities.CustApplLimitSetup;
import com.samsoft.lms.customer.entities.AgrCustomer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_invoice_details")
public class AgrInvoiceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nInvID", length = 20)
	private int invId;

	@Column(name = "sInvoiceNo", length = 20)
	private String invoiceNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "dInvoiceDate")
	private Date dtInvoiceDate;

	@Column(name = "sSupplierName", length = 200)
	private String supplierName;

	@Column(name = "nInvoiceSubTotalAmount", length = 20)
	private double invoiceSubTotalAmount;

	@Column(name = "nInvoiceTaxAmount", length = 20)
	private double invoiceTaxAmount;

	@Column(name = "nNetInvoiceAmount", length = 20)
	private double netInvoiceAmount;

	@Column(name = "nCreditDays", length = 3)
	private int creditDays;

	@Column(name = "sInvoiceImageURL", length = 200)
	private String invoiceImageURL;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sMastAgrId", nullable = false)
	@JsonIgnore
	private AgrMasterAgreement master;
}
