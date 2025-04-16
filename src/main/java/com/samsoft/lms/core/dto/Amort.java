package com.samsoft.lms.core.dto;

public class Amort {

	private int installmentNo;
	private String dtInstallment;
	private float interestRate;
	private double installmentAmount;
	private double totalAdvanceEmiAmount;
	private double loanAmount;
	private String dtPreviousInstallment;
	private double bpiAmount;
	private double interestAmount;
	private double principalAmount;
	private double openingBalance;
	private double closingBalance;
	private double previousClosingBalance;
	private double installmentThreshold;
	private String dtNextInstallment;
	private double nextInstallmentInterest;
	private double nextInstallmentAmount;
	
	
	public int getInstallmentNo() {
		return installmentNo;
	}
	public void setInstallmentNo(int installmentNo) {
		this.installmentNo = installmentNo;
	}
	public String getDtInstallment() {
		return dtInstallment;
	}
	public void setDtInstallment(String dtInstallment) {
		this.dtInstallment = dtInstallment;
	}
	public float getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(float interestRate) {
		this.interestRate = interestRate;
	}
	public double getInstallmentAmount() {
		return installmentAmount;
	}
	public void setInstallmentAmount(double installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	public double getTotalAdvanceEmiAmount() {
		return totalAdvanceEmiAmount;
	}
	public void setTotalAdvanceEmiAmount(double totalAdvanceEmiAmount) {
		this.totalAdvanceEmiAmount = totalAdvanceEmiAmount;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getDtPreviousInstallment() {
		return dtPreviousInstallment;
	}
	public void setDtPreviousInstallment(String dtPreviousInstallment) {
		this.dtPreviousInstallment = dtPreviousInstallment;
	}
	public double getBpiAmount() {
		return bpiAmount;
	}
	public void setBpiAmount(double bpiAmount) {
		this.bpiAmount = bpiAmount;
	}
	public double getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}
	public double getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(double principalAmount) {
		this.principalAmount = principalAmount;
	}
	public double getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}
	public double getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}
	
	public double getPreviousClosingBalance() {
		return previousClosingBalance;
	}
	public void setPreviousClosingBalance(double previousClosingBalance) {
		this.previousClosingBalance = previousClosingBalance;
	}
	
	public double getInstallmentThreshold() {
		return installmentThreshold;
	}
	public void setInstallmentThreshold(double installmentThreshold) {
		this.installmentThreshold = installmentThreshold;
	}
	public String getDtNextInstallment() {
		return dtNextInstallment;
	}
	public void setDtNextInstallment(String dtNextInstallment) {
		this.dtNextInstallment = dtNextInstallment;
	}
	public double getNextInstallmentInterest() {
		return nextInstallmentInterest;
	}
	public void setNextInstallmentInterest(double nextInstallmentInterest) {
		this.nextInstallmentInterest = nextInstallmentInterest;
	}
	public double getNextInstallmentAmount() {
		return nextInstallmentAmount;
	}
	public void setNextInstallmentAmount(double nextInstallmentAmount) {
		this.nextInstallmentAmount = nextInstallmentAmount;
	}
	public Amort(int installmentNo, String dtInstallment, float interestRate, double installmentAmount,
			double totalAdvanceEmiAmount, double loanAmount, String dtPreviousInstallment, double bpiAmount,
			double interestAmount, double principalAmount, double openingBalance, double closingBalance,
			double previousClosingBalance, double installmentThreshold, String dtNextInstallment,
			double nextInstallmentInterest, double nextInstallmentAmount) {
		super();
		this.installmentNo = installmentNo;
		this.dtInstallment = dtInstallment;
		this.interestRate = interestRate;
		this.installmentAmount = installmentAmount;
		this.totalAdvanceEmiAmount = totalAdvanceEmiAmount;
		this.loanAmount = loanAmount;
		this.dtPreviousInstallment = dtPreviousInstallment;
		this.bpiAmount = bpiAmount;
		this.interestAmount = interestAmount;
		this.principalAmount = principalAmount;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.previousClosingBalance = previousClosingBalance;
		this.installmentThreshold = installmentThreshold;
		this.dtNextInstallment = dtNextInstallment;
		this.nextInstallmentInterest = nextInstallmentInterest;
		this.nextInstallmentAmount = nextInstallmentAmount;
	}
	
	public Amort() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "Amort [installmentNo=" + installmentNo + ", dtInstallment=" + dtInstallment + ", interestRate="
				+ interestRate + ", installmentAmount=" + installmentAmount + ", totalAdvanceEmiAmount="
				+ totalAdvanceEmiAmount + ", loanAmount=" + loanAmount + ", dtPreviousInstallment="
				+ dtPreviousInstallment + ", bpiAmount=" + bpiAmount + ", interestAmount=" + interestAmount
				+ ", principalAmount=" + principalAmount + ", openingBalance=" + openingBalance + ", closingBalance="
				+ closingBalance + ", previousClosingBalance=" + previousClosingBalance + ", installmentThreshold="
				+ installmentThreshold + ", dtNextInstallment=" + dtNextInstallment + ", nextInstallmentInterest="
				+ nextInstallmentInterest + ", nextInstallmentAmount=" + nextInstallmentAmount + "]";
	}


}
