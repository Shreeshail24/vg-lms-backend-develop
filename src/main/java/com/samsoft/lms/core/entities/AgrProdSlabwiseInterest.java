package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "agr_prod_slabwise_interest")
public class AgrProdSlabwiseInterest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "nIntSlabID", length = 20)
	private int intSlabId;

	@Column(name = "sMasterAgrId", length = 20)
	private String masterAgrId;

	@Column(name = "sSlabAdjusted", length = 2, columnDefinition = "varchar(2) default 'N'")
	private String slabAdjusted="N";

	@Column(name = "nTenorFrom", length = 3)
	private int tenorFrom;

	@Column(name = "nTenorTo", length = 3)
	private int tenorTo;

	@Column(name = "nInterestRate", length = 10)
	private float interestRate;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sProdDtlId", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private AgrProduct product;

}
