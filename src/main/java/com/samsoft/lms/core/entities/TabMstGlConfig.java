package com.samsoft.lms.core.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tab_mst_gl_config")
@Data
@NoArgsConstructor

public class TabMstGlConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nGlId", length = 20)
	private Integer glId;

	@Column(name = "sPortfolioCode", length = 20)
	private String portfolioCode;

	@Column(name = "sGLEvent", length = 100)
	private String glEvent;

	@Column(name = "sActiveYN", length = 2)
	private String activeYn;

	@Column(name = "sUserID", length = 200)
	private String userId;

	@Column(name = "dLastUpdated")
	private LocalDate dtLastUpdated = LocalDate.now();

	@Column(name = "dUserDate")
	private LocalDate dtUserDate = LocalDate.now();

	@Column(name = "sServBranch")
	private String servBranch;

	@Column(name = "sNBFC")
	private String nbfc;

}
