package com.samsoft.lms.transaction.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
//@Table(name="v_agr_soa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Subselect("select * from v_mst_gl_config")
public class VMstGlConfig {

	@Id
	@Column(name = "nGLDtlId")
	private Integer glDtlId;

	@Column(name = "nGLId")
	private Integer glId;

	@Column(name = "sPortfolioCode")
	private String portfolioCode;

	@Column(name = "sGLEvent")
	private String glEvent;

	@Column(name = "headerActiveFlag")
	private String headerActiveFlag;

	@Column(name = "sTranCategory")
	private String tranCategory;

	@Column(name = "sTranHead")
	private String tranHead;

	@Column(name = "sCreditGLHead")
	private String creditGlHead;

	@Column(name = "sDebitGLHead")
	private String debitGlHead;

	@Column(name = "detailActiveFlag")
	private String detailActiveFlag;
	
	@Column(name = "sServBranch")
	private String servBranch;

	@Column(name = "sNBFC")
	private String nbfc;

}
