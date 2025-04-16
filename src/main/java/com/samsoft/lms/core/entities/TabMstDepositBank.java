package com.samsoft.lms.core.entities;

import java.time.LocalDate;

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
@Table(name = "tab_mst_deposit_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstDepositBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sDepositBankId", length = 20)
	private Integer depositBankId;

	@Column(name = "sIfscCode", length = 20)
	private String ifscCode;

	@Column(name = "sBankName", length = 20)
	private String bankName;

	@Column(name = "sBranchName", length = 20)
	private String branchName;

	@Column(name = "sActive", columnDefinition = "varchar(255) default 'Y' ", length = 10)
	private String active;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

}
