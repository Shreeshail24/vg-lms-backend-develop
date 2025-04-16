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
@Table(name = "tab_mst_pay_dec_key")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabMstPayDecKey {

	@Id
	@Column(name = "sDecKeyID", length = 20)
	private String decKeyId;

	@Column(name = "sDecKeyDesc", length = 20)
	private String decKeyDesc;

	@Column(name = "nSeqID", length = 10)
	private Integer seqId;

	@Column(name = "nDecValSource")
	private String decValSource;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	
	private LocalDate dtUserDate = LocalDate.now();

}
