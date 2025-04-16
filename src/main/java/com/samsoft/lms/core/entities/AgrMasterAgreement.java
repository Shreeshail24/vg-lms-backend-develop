
package com.samsoft.lms.core.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "agr_master_agreement")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AgrMasterAgreement {

	@Id
	@Column(name = "sMastAgrId", length = 20)
	private String mastAgrId;

	@Column(name = "sCustomerId", length = 20)
	private String customerId;

	@Column(name = "s4FinAgrNo", length = 20)
	private String fourFinAgrNo;

	@Column(name = "sOriginationApplnNo", length = 20)
	private String originationApplnNo;

	@Column(name = "sPortfolioCode", length = 20)
	private String portfolioCode;

	@Column(name = "sProductCode", length = 20)
	private String productCode;

	@Column(name = "sHomeBranch", length = 20)
	private String homeBranch;

	@Column(name = "sServBranch", length = 20)
	private String servBranch;

	@Column(name = "sHomeState", length = 20)
	private String homeState;

	@Column(name = "sServState", length = 20)
	private String servState;

	@Column(name = "sGstExempted", length = 20)
	private String gstExempted;

	@Column(name = "sAgreementStatus", columnDefinition = "varchar(5) default 'L'")
	private String agreementStatus;

	@Column(name = "sPrefRepayMode", length = 20)
	private String prefRepayMode;

	@Column(name = "sTdsCode", length = 20)
	private String tdsCode;

	@Column(name = "nTdsRate", length = 20, columnDefinition = "FLOAT default 0")
	private Float tdsRate = 0.0f;

	@Column(name = "sAssetClass", length = 20)
	private String assetClass;

	@Column(name = "sNpaStatus", length = 20)
	private String npaStatus;

	@Column(name = "nDpd", length = 20)
	private Integer dpd = 0;

	@Column(name = "nSanctionedAmount", length = 20)
	private Double sanctionedAmount;

	@Column(name = "nLoanAmount", length = 20)
	private Double loanAmount = 0.0;

	@Column(name = "nUnbilledPrincipal", length = 20)
	private Double unbilledPrincipal = 0.0;

	@Column(name = "nExcessAmount", length = 20)
	private Double excessAmount = 0.0;
	
	
	@Column(name = "nTdsExcessAmount", length = 20)
	private Double tdsExcessAmount = 0.0;

	@Column(name = "nTotalDues", length = 20)
	private Double totalDues = 0.0;

	@Column(name = "nTaxDues", length = 20)
	private Double taxDues = 0.0;

	@Column(name = "nPrincipalDues", length = 20)
	private Double principalDues = 0.0;

	@Column(name = "nInterestDues", length = 20)
	private Double interestDues = 0.0;

	@Column(name = "nPenalDues", length = 20)
	private Double penalDues = 0.0;

	@Column(name = "nOtherChargesDues", length = 20)
	private Double otherChargesDues = 0.0;

	@Column(name = "nOtherTaxDues", length = 20)
	private Double otherTaxDues = 0.0;

	@Column(name = "dPrevInstallment")
	@Temporal(TemporalType.DATE)
	private Date dtPrevInstallment;

	@Column(name = "dNextInstallment")
	@Temporal(TemporalType.DATE)
	private Date dtNextInstallment;

	@Column(name = "dLastStatusUpdated")
	@Temporal(TemporalType.DATE)
	private Date dtLastStatusUpdated;

	@Column(name = "nPrevInstallmentAmount", length = 20)
	private Double prevInstallmentAmount;

	@Column(name = "nNextInstallmentAmount", length = 20)
	private Double nextInstallmentAmount = 0.0;

	@Column(name = "nNextDropAmount", length = 20)
	private Double nextDropAmount = 0.0;

	@Column(name = "nCurrentDroppedLimit", length = 20)
	private Double currentDroppedLimit = 0.0;

	@Column(name = "nTenor", length = 20)
	private Integer tenor;

	@Column(name = "nInterestRate", length = 20)
	private Float interestRate;

	@Column(name = "sRepayFreq", length = 20)
	private String repayFreq;

	@Column(name = "nCycleDay", length = 5)
	private Integer cycleDay;

	@Column(name = "dNextDrop")
	@Temporal(TemporalType.DATE)
	private Date dtNextDrop;

	// Supplier Finance Changes Start

	@Column(name = "sInvoiceNo", length = 20)
	private String invoiceNo;

	@Column(name = "sInvoiceDate")
	@Temporal(TemporalType.DATE)
	private Date dtInvoice;

	@Column(name = "sSupplierName", length = 200)
	private String supplierName;

	@Column(name = "nInvoiceSubTotalAmount", length = 20)
	private Double invoiceSubTotalAmount;

	@Column(name = "sInvoiceTaxAmount", length = 20)
	private Double invoiceTaxAmount;

	@Column(name = "sNetInvoiceAmount", length = 20)
	private Double netInvoiceAmount;

	@Column(name = "nCreditDays", length = 20)
	private Integer creditDays;
	
	// Supplier Finance Changes End

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	//Banking
	@Column(name = "szVirtualId", length = 20)
	private String virtualId;

	@Column(name = "szvpaid", length = 30)
	private String vpaId;

//	public String getMastAgrId() {
//		return mastAgrId;
//	}
//
//	public void setMastAgrId(String mastAgrId) {
//		this.mastAgrId = mastAgrId;
//	}
//
//	public String getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getFourFinAgrNo() {
//		return fourFinAgrNo;
//	}
//
//	public void setFourFinAgrNo(String fourFinAgrNo) {
//		this.fourFinAgrNo = fourFinAgrNo;
//	}
//
//	public String getOriginationApplnNo() {
//		return originationApplnNo;
//	}
//
//	public void setOriginationApplnNo(String originationApplnNo) {
//		this.originationApplnNo = originationApplnNo;
//	}
//
//	public String getPortfolioCode() {
//		return portfolioCode;
//	}
//
//	public void setPortfolioCode(String portfolioCode) {
//		this.portfolioCode = portfolioCode;
//	}
//
//	public String getProductCode() {
//		return productCode;
//	}
//
//	public void setProductCode(String productCode) {
//		this.productCode = productCode;
//	}
//
//	public String getHomeBranch() {
//		return homeBranch;
//	}
//
//	public void setHomeBranch(String homeBranch) {
//		this.homeBranch = homeBranch;
//	}
//
//	public String getServBranch() {
//		return servBranch;
//	}
//
//	public void setServBranch(String servBranch) {
//		this.servBranch = servBranch;
//	}
//
//	public String getHomeState() {
//		return homeState;
//	}
//
//	public void setHomeState(String homeState) {
//		this.homeState = homeState;
//	}
//
//	public String getServState() {
//		return servState;
//	}
//
//	public void setServState(String servState) {
//		this.servState = servState;
//	}
//
//	public String getGstExempted() {
//		return gstExempted;
//	}
//
//	public void setGstExempted(String gstExempted) {
//		this.gstExempted = gstExempted;
//	}
//
//	public String getAgreementStatus() {
//		return agreementStatus;
//	}
//
//	public void setAgreementStatus(String agreementStatus) {
//		this.agreementStatus = agreementStatus;
//	}
//
//	public String getPrefRepayMode() {
//		return prefRepayMode;
//	}
//
//	public void setPrefRepayMode(String prefRepayMode) {
//		this.prefRepayMode = prefRepayMode;
//	}
//
//	public String getTdsCode() {
//		return tdsCode;
//	}
//
//	public void setTdsCode(String tdsCode) {
//		this.tdsCode = tdsCode;
//	}
//
//	public Float getTdsRate() {
//		return tdsRate;
//	}
//
//	public void setTdsRate(Float tdsRate) {
//		this.tdsRate = tdsRate;
//	}
//
//	public String getAssetClass() {
//		return assetClass;
//	}
//
//	public void setAssetClass(String assetClass) {
//		this.assetClass = assetClass;
//	}
//
//	public String getNpaStatus() {
//		return npaStatus;
//	}
//
//	public void setNpaStatus(String npaStatus) {
//		this.npaStatus = npaStatus;
//	}
//
//	public Integer getDpd() {
//		return dpd;
//	}
//
//	public void setDpd(Integer dpd) {
//		this.dpd = dpd;
//	}
//
//	public Double getSanctionedAmount() {
//		return sanctionedAmount;
//	}
//
//	public void setSanctionedAmount(Double sanctionedAmount) {
//		this.sanctionedAmount = sanctionedAmount;
//	}
//
//	public Double getLoanAmount() {
//		return loanAmount;
//	}
//
//	public void setLoanAmount(Double loanAmount) {
//		this.loanAmount = loanAmount;
//	}
//
//	public Double getUnbilledPrincipal() {
//		return unbilledPrincipal;
//	}
//
//	public void setUnbilledPrincipal(Double unbilledPrincipal) {
//		this.unbilledPrincipal = unbilledPrincipal;
//	}
//
//	public Double getExcessAmount() {
//		return excessAmount;
//	}
//
//	public void setExcessAmount(Double excessAmount) {
//		this.excessAmount = excessAmount;
//	}
//
//	public Double getTdsExcessAmount() {
//		return tdsExcessAmount;
//	}
//
//	public void setTdsExcessAmount(Double tdsExcessAmount) {
//		this.tdsExcessAmount = tdsExcessAmount;
//	}
//
//	public Double getTotalDues() {
//		return totalDues;
//	}
//
//	public void setTotalDues(Double totalDues) {
//		this.totalDues = totalDues;
//	}
//
//	public Double getTaxDues() {
//		return taxDues;
//	}
//
//	public void setTaxDues(Double taxDues) {
//		this.taxDues = taxDues;
//	}
//
//	public Double getPrincipalDues() {
//		return principalDues;
//	}
//
//	public void setPrincipalDues(Double principalDues) {
//		this.principalDues = principalDues;
//	}
//
//	public Double getInterestDues() {
//		return interestDues;
//	}
//
//	public void setInterestDues(Double interestDues) {
//		this.interestDues = interestDues;
//	}
//
//	public Double getPenalDues() {
//		return penalDues;
//	}
//
//	public void setPenalDues(Double penalDues) {
//		this.penalDues = penalDues;
//	}
//
//	public Double getOtherChargesDues() {
//		return otherChargesDues;
//	}
//
//	public void setOtherChargesDues(Double otherChargesDues) {
//		this.otherChargesDues = otherChargesDues;
//	}
//
//	public Double getOtherTaxDues() {
//		return otherTaxDues;
//	}
//
//	public void setOtherTaxDues(Double otherTaxDues) {
//		this.otherTaxDues = otherTaxDues;
//	}
//
//	public Date getDtPrevInstallment() {
//		return dtPrevInstallment;
//	}
//
//	public void setDtPrevInstallment(Date dtPrevInstallment) {
//		this.dtPrevInstallment = dtPrevInstallment;
//	}
//
//	public Date getDtNextInstallment() {
//		return dtNextInstallment;
//	}
//
//	public void setDtNextInstallment(Date dtNextInstallment) {
//		this.dtNextInstallment = dtNextInstallment;
//	}
//
//	public Date getDtLastStatusUpdated() {
//		return dtLastStatusUpdated;
//	}
//
//	public void setDtLastStatusUpdated(Date dtLastStatusUpdated) {
//		this.dtLastStatusUpdated = dtLastStatusUpdated;
//	}
//
//	public Double getPrevInstallmentAmount() {
//		return prevInstallmentAmount;
//	}
//
//	public void setPrevInstallmentAmount(Double prevInstallmentAmount) {
//		this.prevInstallmentAmount = prevInstallmentAmount;
//	}
//
//	public Double getNextInstallmentAmount() {
//		return nextInstallmentAmount;
//	}
//
//	public void setNextInstallmentAmount(Double nextInstallmentAmount) {
//		this.nextInstallmentAmount = nextInstallmentAmount;
//	}
//
//	public Double getNextDropAmount() {
//		return nextDropAmount;
//	}
//
//	public void setNextDropAmount(Double nextDropAmount) {
//		this.nextDropAmount = nextDropAmount;
//	}
//
//	public Double getCurrentDroppedLimit() {
//		return currentDroppedLimit;
//	}
//
//	public void setCurrentDroppedLimit(Double currentDroppedLimit) {
//		this.currentDroppedLimit = currentDroppedLimit;
//	}
//
//	public Integer getTenor() {
//		return tenor;
//	}
//
//	public void setTenor(Integer tenor) {
//		this.tenor = tenor;
//	}
//
//	public Float getInterestRate() {
//		return interestRate;
//	}
//
//	public void setInterestRate(Float interestRate) {
//		this.interestRate = interestRate;
//	}
//
//	public String getRepayFreq() {
//		return repayFreq;
//	}
//
//	public void setRepayFreq(String repayFreq) {
//		this.repayFreq = repayFreq;
//	}
//
//	public Integer getCycleDay() {
//		return cycleDay;
//	}
//
//	public void setCycleDay(Integer cycleDay) {
//		this.cycleDay = cycleDay;
//	}
//
//	public Date getDtNextDrop() {
//		return dtNextDrop;
//	}
//
//	public void setDtNextDrop(Date dtNextDrop) {
//		this.dtNextDrop = dtNextDrop;
//	}
//
//	public String getInvoiceNo() {
//		return invoiceNo;
//	}
//
//	public void setInvoiceNo(String invoiceNo) {
//		this.invoiceNo = invoiceNo;
//	}
//
//	public Date getDtInvoice() {
//		return dtInvoice;
//	}
//
//	public void setDtInvoice(Date dtInvoice) {
//		this.dtInvoice = dtInvoice;
//	}
//
//	public String getSupplierName() {
//		return supplierName;
//	}
//
//	public void setSupplierName(String supplierName) {
//		this.supplierName = supplierName;
//	}
//
//	public Double getInvoiceSubTotalAmount() {
//		return invoiceSubTotalAmount;
//	}
//
//	public void setInvoiceSubTotalAmount(Double invoiceSubTotalAmount) {
//		this.invoiceSubTotalAmount = invoiceSubTotalAmount;
//	}
//
//	public Double getInvoiceTaxAmount() {
//		return invoiceTaxAmount;
//	}
//
//	public void setInvoiceTaxAmount(Double invoiceTaxAmount) {
//		this.invoiceTaxAmount = invoiceTaxAmount;
//	}
//
//	public Double getNetInvoiceAmount() {
//		return netInvoiceAmount;
//	}
//
//	public void setNetInvoiceAmount(Double netInvoiceAmount) {
//		this.netInvoiceAmount = netInvoiceAmount;
//	}
//
//	public Integer getCreditDays() {
//		return creditDays;
//	}
//
//	public void setCreditDays(Integer creditDays) {
//		this.creditDays = creditDays;
//	}
//
//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public LocalDate getDtLastUpdated() {
//		return dtLastUpdated;
//	}
//
//	public void setDtLastUpdated(LocalDate dtLastUpdated) {
//		this.dtLastUpdated = dtLastUpdated;
//	}
//
//	public LocalDate getDtUserDate() {
//		return dtUserDate;
//	}
//
//	public void setDtUserDate(LocalDate dtUserDate) {
//		this.dtUserDate = dtUserDate;
//	}
//
//	public String getVirtualId() {
//		return virtualId;
//	}
//
//	public void setVirtualId(String virtualId) {
//		this.virtualId = virtualId;
//	}
//
//	public String getVpaId() {
//		return vpaId;
//	}
//
//	public void setVpaId(String vpaId) {
//		this.vpaId = vpaId;
//	}

}
