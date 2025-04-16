package com.samsoft.lms.core.entities;

import java.time.LocalDate;

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

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_mst_gl_config_dtl")
@Data
@NoArgsConstructor

public class TabMstGlConfigDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nGLDtlId", length = 20)
	private Integer glDtlId;

	@Column(name = "sTranCategory", length = 50)
	private String tranCategory;

	@Column(name = "sTranHead", length = 50)
	private String tranHead;

	@Column(name = "sDebitGLHead", length = 50)
	private String debitGlHead;

	@Column(name = "sCreditGLHead", length = 50)
	private String creditGlHead;

	@Column(name = "sActiveYN", length = 2)
	private String activeYn;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "nGlId", nullable = false)
	private TabMstGlConfig glConfig;

}
