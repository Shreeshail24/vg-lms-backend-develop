package com.samsoft.lms.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="agr_trn_amort")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Amort {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer srNo;
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
	
	
}

